package org.example.nanocli;

import org.junit.jupiter.api.Test;

import java.lang.annotation.Annotation;

import static org.junit.jupiter.api.Assertions.*;

class CommandlineTest {
    @Test
    void testBasic() {
        @CommandSpec(name = "hello")
        class HelloCommand implements Command {
            public void execute() {}
        }
        @CommandSpec(subCommands = {HelloCommand.class})
        class RootCommand implements Command {
            public void execute() {}
        }
        var buf = new StringBuffer();
        var rootCommand = new RootCommand();
        var cmd = new Commandline.Builder().withCommand(rootCommand).withOutputBuffer(buf);

        var args = new String[]{"hello"};
        cmd.execute(args);

        assertEquals("how are you",buf.toString());
    }
}