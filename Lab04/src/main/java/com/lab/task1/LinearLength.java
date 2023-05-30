package com.lab.task1;

import java.util.HashMap;
import java.util.List;

public class LinearLength extends WordsCounter {
    @Override
    public Result process(List<String> words) {
        HashMap<String, Integer> wordCounts = new HashMap<>();

        for (String word : words) {
            wordCounts.putIfAbsent(word, word.length());
        }

        double averageLength = getAverageLength(wordCounts);

        return new Result(averageLength);
    }
}
