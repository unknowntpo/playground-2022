package org.example.nanocli;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

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

        assertThat(root)
                .extracting("name", "description")
                .containsExactly("docker", "A self-sufficient runtime for containers");

        assertThat(root.getSubCommands())
                .hasSize(2)
                .satisfiesExactly(
                        runNode -> {
                            assertThat(runNode.getName()).isEqualTo("run");
                            assertThat(runNode.getDescription()).isEqualTo("Create and run a new container from an image");
                            assertThat(runNode.getCommand()).isInstanceOf(RunCommand.class);
                            assertThat(runNode.getSubCommands()).isEmpty();
                        },
                        execNode -> {
                            assertThat(execNode.getName()).isEqualTo("exec");
                            assertThat(execNode.getDescription()).isEqualTo("Execute a command in a running container");
                            assertThat(execNode.getCommand()).isInstanceOf(ExecCommand.class);
                            assertThat(execNode.getSubCommands()).isEmpty();
                        }
                );

    }
}