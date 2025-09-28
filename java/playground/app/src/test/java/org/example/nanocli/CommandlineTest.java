package org.example.nanocli;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CommandlineTest {
    @Test
    void testBasic() {
        class RootCommand implements Command {

        }
        var buf = new StringBuffer();
        var rootCommand = new RootCommand();
        var cmd = new Commandline.Builder().withCommand(rootCommand).withOutputBuffer(buf);

        var args = new String[]{"hello"};
        cmd.execute(args);

        assertEquals("how are you",buf.toString());
    }
}