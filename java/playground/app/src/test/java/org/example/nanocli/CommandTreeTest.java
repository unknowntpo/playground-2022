package org.example.nanocli;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@CommandSpec(
        name = "run",
        description = "Create and run a new container from an image"
)
class RunCommand implements Command {
    @Override
    public void execute(StringBuffer buf) {
        buf.append("executing run command");
    }
}

@CommandSpec(
        name = "exec",
        description = "Execute a command in a running container"
)
class ExecCommand implements Command {
    @Override
    public void execute(StringBuffer buf) {
        buf.append("executing exec command");
    }
}

@CommandSpec(
        name = "docker",
        description = "A self-sufficient runtime for containers",
        subCommands = {RunCommand.class, ExecCommand.class}
)
class DockerCommand implements Command {
    @Override
    public void execute(StringBuffer buf) {
    }
}

class CommandTreeTest {

    @Test
    void testBuildCommandTree() {
        var rootCommand = new DockerCommand();
        var commandTree = CommandTree.from(rootCommand);
        var root = commandTree.root();
        assertEquals("docker", root.name());
        assertEquals("A self-sufficient runtime for containers", root.description());
        assertEquals(List.of(
                new CommandTree.Node(
                        "run",
                        "Create and run a new container from an image",
                        List.of()
                ),
                new CommandTree.Node(
                        "exec",
                        "Execute a command in a running container",
                        List.of()
                )
        ), root.subCommands());
    }
}