package com.lab.task1;

import java.util.*;
import java.util.concurrent.RecursiveTask;

public class RecursivePreProcessor extends RecursiveTask {
    private final List<String> words;
    private final Map<String, Integer> wordsStat;
    private static final int THRESHOLD = 200000;

    public RecursivePreProcessor(List<String> words, Map<String, Integer> wordsStat) {
        this.words = words;
        this.wordsStat = wordsStat;
    }

    @Override
    protected Object compute() {
        if (this.words.size() > THRESHOLD) {
            var partOne = new RecursivePreProcessor(words.subList(0, words.size() / 2), wordsStat);
            var partTwo = new RecursivePreProcessor(words.subList(words.size() / 2, words.size()), wordsStat);

            partOne.fork();
            partTwo.fork();

            partOne.join();
            partTwo.join();
        } else {
            processing();
        }

        return new Object();
    }

    private void processing() {
        for (String word : words) {
            wordsStat.putIfAbsent(word, word.length());
        }
    }
}