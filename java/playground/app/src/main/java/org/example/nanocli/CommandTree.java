package org.example.nanocli;

import com.google.common.base.Preconditions;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

    record Node(String name, String description, List<Option> options, List<Node> subCommands, Command command) {
        public static Node of(Command rootCommand) {
            var spec = getCommandSpec(rootCommand);
            var optionSpecs = getOptionSpecsFromCommand(rootCommand);
            // inject option into rootCommand
            var options = optionSpecs.stream().map(Option::of).toList();
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
            return new Node(spec.name(), spec.description(), options, subCommands, rootCommand);
        }

        private static List<OptionSpec> getOptionSpecsFromCommand(Command command) {
            // FIXME: get option value type using reflect, and add to Option
            List<OptionSpec> specs = new ArrayList<>();
            var fields = command.getClass().getDeclaredFields();
            for (final var field : fields) {
                System.out.println("field: " + field.getName());
                System.out.println("field type: " + field.getType());
                System.out.println("field anno: " + Arrays.stream(field.getAnnotations()).map(Annotation::toString));
                var annotations = field.getDeclaredAnnotations();
                List<OptionSpec> annos = Arrays.stream(annotations)
                        .filter(anno -> anno.annotationType().equals(OptionSpec.class))
                        .map(anno -> (OptionSpec) anno)
                        .toList();
                specs.addAll(annos);
            }

            return specs;
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

        public void setOption(String optionStr, String poll) {
            // we have checked that argStr is in command
            var optionField = Arrays.stream(command.getClass().getDeclaredFields())
                    .filter(field -> field.getName().equals(optionStr))
                    .findFirst()
                    .orElseThrow(
                            () -> new IllegalArgumentException(String.format("option not found in Command %s", this.name()))
                    );
            optionField.setAccessible(true);
            switch (optionField.getType()) {
                case Class<String>:
            }



        }
    }

    // FIXME: add value as any
    record Option(String name, String description) {
        public static Option of(OptionSpec spec) {
            return new Option(spec.name(), spec.description());
        }
    }
}
