package org.example.nanocli;

import com.google.common.base.Preconditions;

import java.io.InvalidClassException;
import java.lang.reflect.InvocationTargetException;
import java.security.InvalidParameterException;
import java.util.Arrays;
import java.util.NoSuchElementException;
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
                    displayHelpMessage(this.commandTree.root(), this.outputBuffer);
                    break;
                default:
                    // cli --help
                    if (args.length == 2 && args[1].equals("--help")) {
                        displayHelpMessage(this.commandTree.root(), this.outputBuffer);
                        break;
                    }
                    // now we have only
                    // cli hello
                    var cmd = args[1];
                    if (cmd.equals("hello")) {
                        CommandTree.Node helloNode = this.commandTree
                                .root()
                                .subCommands()
                                .stream()
                                .filter(command -> command.name().equals("hello"))
                                .findFirst()
                                .orElseThrow(() -> new NoSuchElementException("hello command should be present"));
                        helloNode.command().execute(this.outputBuffer);
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
        private static void displayHelpMessage(CommandTree.Node node, StringBuffer outputBuffer) {
            var subCommandInfos = node.subCommands().stream().map(subNode -> new CommandInfo(subNode.name(), subNode.description()));

            var subCommandInfoStrs = subCommandInfos
                    .map(info -> String.format("  %-12s%s", info.name(), info.description()))
                    .collect(Collectors.joining("\n"));

            String usageStr = """
                    Usage:  %s [OPTIONS] COMMAND
                    
                    %s
                    
                    Commands:
                    %s
                    """.formatted(node.name(), node.description(), subCommandInfoStrs);

            outputBuffer.append(usageStr);
        }

        private record CommandInfo(Object name, String description) {
        }
    }
}
