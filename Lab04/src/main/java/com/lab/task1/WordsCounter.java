package com.lab.task1;

import java.util.List;
import java.util.Map;

public abstract class WordsCounter {
    abstract Result process(List<String> words);

    protected double getAverageLength(Map<String, Integer> wordCounts) {
        var wordsTotalLengths = wordCounts.values().stream().mapToDouble(wordLength -> wordLength).sum();
        return wordsTotalLengths / (long) wordCounts.values().size();
    }

    protected double getVariance(Map<String, Integer> wordCounts) {
        return (wordCounts.values().stream().mapToDouble(i -> Math.pow(i, 2)).sum()
                / wordCounts.size()) - Math.pow(wordCounts.values().stream().mapToDouble(i -> i).sum()
                        / wordCounts.size(), 2);
    }

    protected int getUniqueWords(Map<String, Integer> wordCounts) {
        return wordCounts.size();
    }
}
