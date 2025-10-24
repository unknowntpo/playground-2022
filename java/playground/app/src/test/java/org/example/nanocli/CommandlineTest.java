package org.example.nanocli;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

@CommandSpec(
        name = "hello",
        description = "Print greeting message"
)
class HelloCommand implements Command {
    @OptionSpec(name = "-c", description = "case of output (upper or lower), default is lower")
    String letterCase;

    @Override
    public void execute(StringBuffer buf) {
        String howAreYou = "how are you";

        if (letterCase != null) {
            if (letterCase.equalsIgnoreCase("lower")) {
            } else if (letterCase.equalsIgnoreCase("upper")) {
                howAreYou = howAreYou.toUpperCase();
            } else {
                throw new IllegalArgumentException("Invalid letter case");
            }
        }

        buf.append(howAreYou);
    }
}

@CommandSpec(
        name = "cli",
        description = "A simple CLI tool",
        subCommands = {HelloCommand.class}
)
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
        var cmd = new Commandline.Builder().withCommand(rootCommand).withOutputBuffer(buf).build();

        var args = new String[]{"cli", "hello"};
        cmd.execute(args);

        // if -w capital is specified, then return capitalized words
        assertEquals("how are you", buf.toString());
    }

    @Test
    void testOption() {
        {
            var buf = new StringBuffer();
            var rootCommand = new RootCommand();
            var cmd = new Commandline.Builder().withCommand(rootCommand).withOutputBuffer(buf).build();

            var args = new String[]{"cli", "hello", "-c", "upper"};
            cmd.execute(args);

            // if -w capital is specified, then return capitalized words
            assertEquals("HOW ARE YOU", buf.toString());
        }

        // lower
        {
            var buf = new StringBuffer();
            var rootCommand = new RootCommand();
            var cmd = new Commandline.Builder().withCommand(rootCommand).withOutputBuffer(buf).build();

            var args = new String[]{"cli", "hello", "-c", "lower"};
            cmd.execute(args);

            // if -w capital is specified, then return capitalized words
            assertEquals("HOW ARE YOU", buf.toString());
        }

        // invalid
        {
            var buf = new StringBuffer();
            var rootCommand = new RootCommand();
            var cmd = new Commandline.Builder().withCommand(rootCommand).withOutputBuffer(buf).build();

            var args = new String[]{"cli", "hello", "-c", "invalid"};
            assertThrows(IllegalArgumentException.class, () -> cmd.execute(args));
        }
    }

    @Test
    void testBasicHelpMessage() {
        var buf = new StringBuffer();
        var rootCommand = new RootCommand();
        var cmd = new Commandline.Builder().withCommand(rootCommand).withOutputBuffer(buf).build();
        // docker -p terx -c dfd run -x abc -b abc
//        Usage:  docker [OPTIONS] COMMAND [ARG...]


        var args = new String[]{"cli", "--help"};
        cmd.execute(args);

        assertEquals("""
                Usage:  cli [OPTIONS] COMMAND
                
                A simple CLI tool
                
                Commands:
                  hello       Print greeting message
                """, buf.toString());
    }

    @Test
    void testParseArgs() {
        var cmd = new RootCommand();
        var commandTree = CommandTree.from(cmd);
        var args = new String[]{"cli", "hello", "-c", "upper"};
        Commandline.parseArgs(args, commandTree);
        
        assertEquals(commandTree.root().subCommands().size(), 1);
        
        var helloCommandNode = commandTree.root().subCommands().getFirst();
        assertInstanceOf(HelloCommand.class, helloCommandNode.command());

        var helloCommand = ((HelloCommand) helloCommandNode.command());

        // should inject value of letterCase into helloCommand instance;
        Assertions.assertEquals("upper", helloCommand.letterCase);
    }
}