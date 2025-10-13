package org.example.nanocli;

import com.google.common.base.Preconditions;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.List;

public record CommandTree(Node root) {
    public static CommandTree from(Command rootCommand) {
        // parse Command, get spec, and build CommandTree
        if (rootCommand == null) {
            throw new IllegalArgumentException("rootCommand cannot be null");
        }
        var root = Node.from(rootCommand);
//        var root = Node.from();
        return new CommandTree(root);
    }

    record Node(String name, String description, List<Node> subCommands, Command command) {
        public static Node from(Command rootCommand) {
            var spec = getCommandSpec(rootCommand);
            var subCommands = Arrays.stream(spec.subCommands()).map(
                    clazz -> {
                        try {
                            // init new instance of command from clazz, and build a Node.
                            Command command = clazz.getDeclaredConstructor().newInstance();
                            return Node.from(command);
                        } catch (Exception e) {
                            throw new RuntimeException("Failed to instantiate: " + clazz, e);
                        }
                    }
            ).toList();

            // build Node from subCommands
            return new Node(spec.name(), spec.description(), subCommands, rootCommand);
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
}
