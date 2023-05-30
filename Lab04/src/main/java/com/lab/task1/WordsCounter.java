package com.lab.task1;

import java.util.List;
import java.util.Map;

public abstract class WordsCounter {
    abstract Result process(List<String> words);

    protected double getAverageLength(Map<String, Integer> wordCounts) {
        var wordsTotalLengths = wordCounts.values().stream().mapToDouble(wordLength -> wordLength).sum();
        return wordsTotalLengths / (long) wordCounts.values().size();
    }
}
