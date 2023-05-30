package com.lab.task1;

import java.util.HashMap;
import java.util.List;

public class LinearLength extends WordsCounter {
    @Override
    public Result process(List<String> words) {
        HashMap<String, Integer> wordsStat = new HashMap<>();

        for (String word : words) {
            wordsStat.putIfAbsent(word, word.length());
        }

        return new Result(
                getAverageLength(wordsStat),
                getVariance(wordsStat),
                getUniqueWords(wordsStat));
    }
}
