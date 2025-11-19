package org.example.nanocli;

import com.google.common.base.Preconditions;
import lombok.Data;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public record CommandTree(Node root) {
    public static CommandTree from(Command rootCommand) {
        // parse Command, get spec, and build CommandTree
        if (rootCommand == null) {
            throw new IllegalArgumentException("rootCommand cannot be null");
        }
        var root = Node.of(rootCommand);
//        var root = Node.from();
        return new CommandTree(root);
    }

    public void execute(StringBuffer outputBuffer) {
        // execute command based on CommandTree.
        Node curNode = this.root;
        while (!curNode.getSubCommands().isEmpty()) {
            curNode = curNode
                    .getSubCommands()
                    .stream()
                    .filter(Node::getShouldExecute)
                    .findFirst()
                    .orElseThrow(() -> new IllegalStateException("should have one and only one subcommand"));
        }
        if (curNode.shouldDisplayHelpMessage) {
            displayHelpMessage(curNode, outputBuffer);
            return;
        }
        curNode.command.execute(outputBuffer);
    }

    @Data
    static class Node {
        private String name;
        private String description;
        private List<Option> options;
        private List<Node> subCommands;
        private Command command;
        private Boolean shouldExecute;
        private Boolean shouldDisplayHelpMessage;

        public Node(String name, String description, List<Option> options, List<Node> subCommands, Command command, Boolean shouldExecute, Boolean shouldDisplayHelpMessage) {
            this.name = name;
            this.description = description;
            this.options = options;
            this.subCommands = subCommands;
            this.command = command;
            this.shouldExecute = shouldExecute;
            this.shouldDisplayHelpMessage = shouldDisplayHelpMessage;
        }

        public static Node of(Command rootCommand) {
            var spec = getCommandSpec(rootCommand);
            var options = getOptionsFromCommand(rootCommand);
            // inject option into rootCommand
            // FIXME: should put optionField into Option, so we can set value in parse stage.
//            var options = optionSpecs.stream().map(Option::of).toList();
            var subCommands = Arrays.stream(spec.subCommands()).map(
                    // FIXME: make sure subCommands are unique
                    clazz -> {
                        try {
                            // init new instance of command from clazz, and build a Node.
                            Command command = clazz.getDeclaredConstructor().newInstance();
                            return Node.of(command);
                        } catch (Exception e) {
                            throw new RuntimeException("Failed to instantiate: " + clazz, e);
                        }
                    }
            ).toList();

            // build Node from subCommands
            return new Node(spec.name(), spec.description(), options, subCommands, rootCommand, false, false);
        }

        private static List<Option> getOptionsFromCommand(Command command) {
            // FIXME: get option value type using reflect, and add to Option
            List<Option> options = new ArrayList<>();
            var fields = command.getClass().getDeclaredFields();
            for (final var field : fields) {
                System.out.println("field: " + field.getName());
                System.out.println("field type: " + field.getType());
                System.out.println("field anno: " + Arrays.stream(field.getAnnotations()).map(Annotation::toString));
                var annotations = field.getDeclaredAnnotations();
                // FIXME: should ban multiple OptionSpec in same field
                var specInField = Arrays.stream(annotations)
                        .filter(anno -> anno.annotationType().equals(OptionSpec.class))
                        .map(anno -> (OptionSpec) anno)
                        .findFirst()
                        .orElseThrow(() -> new IllegalArgumentException("should have 1 OptionSpec"));
                options.add(Option.of(command, field, specInField));
            }

            return options;
        }

        private static CommandSpec getCommandSpec(Command command) {
            var specs = Arrays.stream(command.getClass().getAnnotations())
                    .filter(anno -> anno instanceof CommandSpec)
                    .map(anno -> (CommandSpec) anno).toList();
            // TODO: write command name in exception msg
            Preconditions.checkArgument(specs.size() == 1, "Command should only have 1 spec");
            var spec = specs.getFirst();
            return spec;
        }

        // optionKey = "-c", value: "hello"
        public void setOption(Option option, String optionKey, String value) throws IllegalAccessException {
//            // we have checked that argStr is in command
//            var option = getOptionsFromCommand(command)
//                    .stream()
//                    .filter(spec -> spec.name().equals(optionKey))
//                    .findFirst()
//                    .orElseThrow(
//                            () -> new IllegalArgumentException(String.format("option not found in Command %s", this.name()))
//                    );
            option.field.setAccessible(true);
            if (option.field.getType() == String.class) {
                option.field.set(option.command, value);
            }
        }
    }

    // FIXME: add value as any
    record Option(Command command, Field field, String name, String description) {
        public static Option of(Command command, Field field, OptionSpec spec) {
            return new Option(command, field, spec.name(), spec.description());
        }
    }

    private record CommandInfo(Object name, String description) {
    }

    /**
     * Displays the help message for the CLI tool.
     * <p>
     * Format:
     * <pre>
     * Usage:  cli [OPTIONS] COMMAND
     *
     * A simple CLI tool
     *
     * Commands:
     *   hello       Print greeting message
     * </pre>
     */
    private static void displayHelpMessage(Node node, StringBuffer outputBuffer) {
        var subCommandInfos = node.getSubCommands().stream().map(subNode -> new CommandInfo(subNode.getName(), subNode.getDescription()));

        var subCommandInfoStrs = subCommandInfos
                .map(info -> String.format("  %-12s%s", info.name(), info.description()))
                .collect(Collectors.joining("\n"));

        String usageStr = """
                Usage:  %s [OPTIONS] COMMAND
                
                %s
                
                Commands:
                %s
                """.formatted(node.getName(), node.getDescription(), subCommandInfoStrs);

        outputBuffer.append(usageStr);
    }
}
