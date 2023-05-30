package com.lab.task1;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ForkJoinLength extends WordsCounter {
    private final Map<String, Integer> wordsStat = Collections.synchronizedMap(new HashMap<>());

    @Override
    public Result process(List<String> words) {
        var task = new RecursivePreProcessor(words, wordsStat);

        task.fork();
        task.join();

        return new Result(
                getAverageLength(wordsStat),
                getVariance(wordsStat),
                getUniqueWords(wordsStat));
    }
}
