package org.example.trie;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Joiner;
import io.netty.util.internal.StringUtil;

import java.util.HashMap;
import java.util.Map;

public class Node {
    @JsonProperty
    private boolean isEnd;

    public char getValue() {
        return value;
    }

    public void setValue(char value) {
        this.value = value;
    }

    @JsonProperty
    private char value;
    @JsonProperty
    private Map<Character, Node> map;

    public Node(boolean isEnd, char value) {
        this.isEnd = isEnd;
        this.value = value;
        this.map = new HashMap<>();
    }

    public Map<Character, Node> getMap() {
        return map;
    }

    public void setMap(Map<Character, Node> map) {
        this.map = map;
    }


    public boolean isEnd() {
        return isEnd;
    }

    public void setEnd(boolean end) {
        isEnd = end;
    }

    @Override
    public String toString() {
        return "Node{" +
                "isEnd=" + isEnd +
                ", value=" + value +
                ", map=" + Joiner.on(", ").withKeyValueSeparator("=").join(map)+
                '}';
    }
}
