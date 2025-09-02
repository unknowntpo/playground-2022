package org.example.trie;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

public class Trie {
    @JsonProperty
    private final Node root;

    @Override
    public String toString() {
        return "Trie{" +
                "root=" + root +
                '}';
    }

    public Trie() {
        this.root = new Node(false, '\0');
    }

    public boolean search(String s) {
        var cur = this.root;
        for (int i = 0; i < s.length(); i++) {
            var c = s.charAt(i);
            var map = cur.getMap();
            if (!map.containsKey(c)) {
                return false;
            }
            cur = map.get(c);
        }

        return cur.isEnd();
    }

    public void insert(String s) {
        var cur = this.root;
        for (int i = 0; i < s.length();i++) {
            char c = s.charAt(i);
            var map = cur.getMap();
            if (!map.containsKey(c)) {
                map.put(c, new Node(false, c));
            }
            cur = map.get(c);
        }

        // reach the end
        cur.setEnd(true);

        /*
        "app"

        cur = root
        for c in s:
            if (r["a"] == null)
              alloc new node, set a
              cur = r["a"]
        cur.isEnd = true



        c
        r -> a -> x

        app.
         */
    }

    public List<String> suggest(String prefix) {
        var cur = this.root;
        for (int i = 0; i < prefix.length(); i++) {
            char c = prefix.charAt(i);
            var m = cur.getMap();
            if (!m.containsKey(c)) {
                // not match, return empty list
                return List.of();
            }
            cur = m.get(c);
        }

        return dfs(cur, prefix);
    }

    private List<String> dfs(Node n, String prefix) {
        var out = new ArrayList<String>();

        if (n.isEnd()) {
            out.add(prefix);
        }

        n.getMap().values().forEach((child) -> out.addAll(dfs(child, prefix + child.getValue())));
        return out;
    }
}
