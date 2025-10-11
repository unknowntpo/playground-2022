package org.example.nanocli;

import com.google.common.base.Preconditions;

import java.io.InvalidClassException;
import java.lang.reflect.InvocationTargetException;
import java.security.InvalidParameterException;
import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;

public class Commandline {
    public static class Builder {
        private StringBuffer outputBuffer;
        private Command rootCommand;
        private CommandTree commandTree;

        public Builder withCommand(Command root) {
            if (root == null) {
                throw new IllegalArgumentException("root command cannot be null");
            }
            this.rootCommand = root;
            return this;
        }

        public Builder withOutputBuffer(StringBuffer buf) {
            this.outputBuffer = buf;
            return this;
        }

        public void execute(String[] args) {
            this.commandTree = CommandTree.from(this.rootCommand);
            switch (args.length) {
                case 1:
                    // display help message
                    displayHelpMessage(this.rootCommand, this.outputBuffer);
                    break;
                default:
                    // cli --help
                    if (args.length == 2 && args[1].equals("--help")) {
                        displayHelpMessage(this.rootCommand, this.outputBuffer);
                        break;
                    }
                    // now we have only
                    // cli hello
                    var cmd = args[1];
                    if (cmd.equals("hello")) {
                        var subCommands = Arrays.stream(getCommandSpec(this.rootCommand).subCommands())
                                .map(clazz -> {
                                    try {
                                        return clazz.getDeclaredConstructor().newInstance();
                                    } catch (Exception e) {
                                        throw new RuntimeException("Failed to instantiate: " + clazz, e);
                                    }
                                }).toList();
                        Command helloCommand = subCommands
                                .stream()
                                .filter(command -> Objects.equals(getCommandSpec(command).name(), "hello"))
                                .toList()
                                .getFirst();
                        helloCommand.execute(this.outputBuffer);
                    }
            }
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
        private static void displayHelpMessage(Command rootCommand, StringBuffer outputBuffer) {
            CommandSpec spec = getCommandSpec(rootCommand);

            var subCommands = Arrays.stream(spec.subCommands()).map(clazz -> {
                try {
                    return clazz.getDeclaredConstructor().newInstance();
                } catch (Exception e) {
                    throw new RuntimeException("Failed to instantiate: " + clazz, e);
                }
            }).toList();

            var subCommandInfos = subCommands.stream().map(command -> {
                var subSpec = getCommandSpec(command);
                return new CommandInfo(subSpec.name(), subSpec.description());
            });

            var subCommandInfoStrs = subCommandInfos
                .map(info -> String.format("  %-12s%s", info.name(), info.description()))
                    .collect(Collectors.joining("\n"));

            String usageStr = """
                    Usage:  %s [OPTIONS] COMMAND
                    
                    %s
                    
                    Commands:
                    %s
                    """.formatted(spec.name(), spec.description(), subCommandInfoStrs);

            outputBuffer.append(usageStr);
        }

        private record CommandInfo(Object name, String description) {
        }
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
}
