package org.example.nanocli;

import com.google.common.base.Preconditions;

import java.io.InvalidClassException;
import java.security.InvalidParameterException;
import java.util.Arrays;
import java.util.Objects;

public class Commandline {
    public static class Builder {
        private StringBuffer outputBuffer;
        private Command rootCommand;

        public Builder withCommand(Command root) {
            this.rootCommand = root;
            return this;
        }

        public Builder withOutputBuffer(StringBuffer buf) {
            this.outputBuffer = buf;
            return this;
        }

        public void execute(String[] args) {
            switch (args.length) {
                case 1:
                    // display help message
                    break;
                default:
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
