package com.lab.task1;

import com.lab.TextLoader;

import java.util.List;
import java.util.concurrent.ForkJoinPool;

public class Runner {
    public void run() {
        List<String> words = TextLoader.getWordsFromFile("C:\\Users\\vbardin\\Desktop\\PCT\\Labs\\Lab04\\src\\main\\resources\\task-1.txt");

        System.out.printf("Number of words: %d\n\n", words.size());

        var pool = new ForkJoinPool(Runtime.getRuntime().availableProcessors());
        var currTime = System.currentTimeMillis();
        var res = new ForkJoinLength().process(words);
        long currTimeForkJoin = System.currentTimeMillis() - currTime;

        System.out.printf("Execution time (ForkJoin): %d ms\n", currTimeForkJoin);

        System.out.printf("Average length: %f\n", res.AverageLength());
        System.out.println();

        var linearCounter = new LinearLength();
        currTime = System.currentTimeMillis();
        var linearResult = linearCounter.process(words);
        var currTimeLinear = System.currentTimeMillis() - currTime;

        System.out.printf("Execution time (linear): %d ms\n", currTimeLinear);
        System.out.printf("Average length: %f\n", linearResult.AverageLength());
    }
}
