package org.example.trie;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TrieTest {

    @Test
    void testSearch() throws JsonProcessingException {
        var trie = new Trie();
        trie.insert("app");
        trie.insert("apple");
        trie.insert("able");

        var mapper = new ObjectMapper();

        System.out.println("trie" + mapper.writerWithDefaultPrettyPrinter().writeValueAsString(trie));

        assertTrue(trie.search("apple"));
        assertFalse(trie.search("banana"));
    }

    @Test
    void testAutocomplete() {
        var trie = new Trie();
        trie.insert("app");
        trie.insert("apple");
        trie.insert("able");

        assertEquals(List.of("app", "apple"), trie.suggest("ap"));
        assertEquals(List.of("able"), trie.suggest("ab"));
    }
}
