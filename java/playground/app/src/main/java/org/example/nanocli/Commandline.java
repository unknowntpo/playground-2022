package org.example.nanocli;

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

        }
    }
}
