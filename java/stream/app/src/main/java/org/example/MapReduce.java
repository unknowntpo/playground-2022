package org.example;

import java.util.Arrays;
import java.util.List;

public class MapReduce {
    /**
     * @example
     * apple
     * banana
     * papaya
     */
    private String data;

    MapReduce(String data) {
        this.data = data;
    }

//    public List<WordCount> wordCount() {
//        Arrays.stream(data.split("\n")).map(line -> )
//    }

    public class WordCount {
        private String word;
        private Integer count;
    }
}
