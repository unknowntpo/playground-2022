package org.example.nanocli;

import com.google.common.base.Preconditions;

import java.io.InvalidClassException;
import java.lang.reflect.InvocationTargetException;
import java.security.InvalidParameterException;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;
import java.util.Queue;
import java.util.stream.Collectors;

public class Commandline {
    private Command rootCommand;
    private CommandTree commandTree;
    private StringBuffer outputBuffer;

    public Commandline(Command rootCommand, CommandTree commandTree, StringBuffer outputBuffer) {
        this.rootCommand = rootCommand;
        this.commandTree = commandTree;
        this.outputBuffer = outputBuffer;
    }

    public void execute(String[] args) {
        this.commandTree = CommandTree.from(this.rootCommand);

        try {
            parseArgs(args, this.commandTree);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("failed to parse arguments", e);
        }

        this.commandTree.execute(this.outputBuffer);

        /*
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
                // FIXME: Avoid hard-coding logic
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
        */
    }

                /*
docker -c orbstack run -p 8080:8080 -v $(pwd):/app/mnt

    docker
    -c run
orbstack -p -v
        xxx   yyy

cmda pos1 pos2 cmdb -o 1 -b 2


    pos1
    pos2
cmda
    cmdb

             */

    /**
     * Parse and inject args into commandTree.
     *
     * @param args
     * @param commandTree
     */
    static void parseArgs(String[] args, CommandTree commandTree) throws IllegalAccessException {
        // FIXME: should handle empty args, display help message
        if (args.length == 1) {
            // TODO: should display help message
            return;
        }
        var cliName = args[0];
        //
        // args = {"cli", "hello", "-c", "upper"};
        Queue<String> argsQueue = new ArrayDeque<>(List.of(args));
        // pop cli
        argsQueue.poll();
        CommandTree.Node curNode = commandTree.root();
        while (!argsQueue.isEmpty()) {
            String arg = argsQueue.poll();
            if (arg.contains("--") || arg.contains("-")) {
                // if is option, parse it, match with curNode
                var optionStr = arg;
                String curNodeName = curNode.getName();
                var option = curNode.getOptions()
                        .stream()
                        .filter(opt -> opt.name().equals(optionStr))
                        .findFirst().orElseThrow(
                                () -> new InvalidParameterException(String.format("option %s not found in command %s", arg, curNodeName))
                        );
                // FIXME: determine number of values in this option (0..n). Now only support single value.
                // now, just match single value option
                if (argsQueue.isEmpty()) {
                    throw new IllegalArgumentException(String.format("value of option %s not found in command %s", arg, curNode.getName()));
                }
                curNode.setOption(option, optionStr, argsQueue.poll());
            } else {
                // if is command, parse it , set curNode, continue
                curNode = curNode
                        .getSubCommands()
                        .stream()
                        .filter(command -> command.getName().equals(arg))
                        .findFirst()
                        .orElseThrow(() -> new IllegalArgumentException(String.format("%s: unknown command: %s %s", cliName, cliName, arg)));
                curNode.setShouldExecute(true);
            }
        }

    }

    static String removeDash(String arg) {
        while (arg.startsWith("-")) {
            arg = arg.substring(1);
        }

        return arg;
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

    private record CommandInfo(Object name, String description) {
    }


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

        public Commandline build() {
            return new Commandline(rootCommand, commandTree, outputBuffer);
        }


    }
}
