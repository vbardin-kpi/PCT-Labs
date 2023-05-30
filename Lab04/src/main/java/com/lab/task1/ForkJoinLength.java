package com.lab.task1;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ForkJoinPool;

public class ForkJoinLength extends WordsCounter {
    private final Map<String, Integer> wordsStat = Collections.synchronizedMap(new HashMap<>());

    @Override
    public Result process(List<String> words) {
        var pool = new ForkJoinPool(Runtime.getRuntime().availableProcessors());
        var task = new RecursivePreProcessor(words, wordsStat);

        pool.submit(task);

        return new Result(getAverageLength(wordsStat));
    }
}
