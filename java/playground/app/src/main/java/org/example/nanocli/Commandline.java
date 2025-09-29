package org.example.nanocli;

import com.google.common.base.Preconditions;

import java.io.InvalidClassException;
import java.security.InvalidParameterException;
import java.util.Arrays;

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
                    var command = findCmd(cmd);
            }


        }

        private Command findCmd(String cmd) {
            var command = rootCommand;
            if (!command.getClass().isAnnotationPresent(CommandSpec.class)) {
                // FIXME: how to represent a ocmmnad wwhich has no annotation ?
                throw new InvalidParameterException("Command should have CommandSpec annotation");
            }
            var specs = Arrays.stream(command.getClass().getAnnotations())
                    .filter(anno -> anno instanceof CommandSpec)
                    .map(anno -> (CommandSpec) anno).toList();
            // TODO: write command name in exception msg
            Preconditions.checkArgument(specs.size() == 1, "Command should only have 1 spec");

            var subCommands = specs.getFirst().subCommands();
            // FIXME: how to get names of command ? can we add name() method in interface ?
            return null;
        }
    }
}
