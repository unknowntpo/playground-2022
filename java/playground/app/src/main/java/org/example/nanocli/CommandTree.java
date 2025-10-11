package org.example.nanocli;

import java.util.List;

public class CommandTree {
    public static CommandTree from(Command rootCommand) {
        // parse Command, get spec, and build CommandTree
//        var root = Node.from();
        return new CommandTree();
    }

    static class Node {
        private final String name;
        private final List<Node> subCommands;
        private final String description;

        public Node(String name, List<Node> subCommands, String description) {
            this.name = name;
            this.subCommands = subCommands;
            this.description  = description;
        }
    }
}
