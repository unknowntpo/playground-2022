package org.example.nanocli;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import static org.junit.jupiter.api.Assertions.*;

@CommandSpec(name = "hello")
class HelloCommand implements Command {
    @Override
    public void execute(StringBuffer buf) {
        buf.append("how are you");
    }
}

@CommandSpec(subCommands = {HelloCommand.class})
class RootCommand implements Command {
    @Override
    public void execute(StringBuffer buf) {
    }
}

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CommandlineTest {
    @Test
    void testBasic() {
        var buf = new StringBuffer();
        var rootCommand = new RootCommand();
        var cmd = new Commandline.Builder().withCommand(rootCommand).withOutputBuffer(buf);

        var args = new String[]{"cli", "hello"};
        cmd.execute(args);

        assertEquals("how are you", buf.toString());
    }
}