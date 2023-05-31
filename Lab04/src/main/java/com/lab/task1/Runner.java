package com.lab.task1;

import com.lab.TextLoader;

import java.util.List;

public class Runner {
    public void run() {
        List<String> words = TextLoader.getWordsFromFile("C:\\Users\\vbardin\\Desktop\\PCT\\Labs\\Lab04\\src\\main\\resources\\task-1.txt");

        System.out.printf("Number of words: %d\n\n", words.size());

        var currTime = System.currentTimeMillis();
        var res = new ForkJoinLength().process(words);
        long currTimeForkJoin = System.currentTimeMillis() - currTime;

        System.out.printf("Execution time (ForkJoin): %d ms\n", currTimeForkJoin);

        System.out.printf("Mean: %f\n", res.mean());
        System.out.printf("Variance: %f\n", res.variance());
        System.out.printf("Unique words: %d\n", res.uniqueWords());
        System.out.println();

        var linearCounter = new LinearLength();
        currTime = System.currentTimeMillis();
        var linearResult = linearCounter.process(words);
        var currTimeLinear = System.currentTimeMillis() - currTime;

        System.out.printf("Mean: %f\n", linearResult.mean());
        System.out.printf("Variance: %f\n", linearResult.variance());
        System.out.printf("Unique words: %d\n", linearResult.uniqueWords());

        System.out.printf("Execution time (linear): %d ms\n", currTimeLinear);
    }
}
