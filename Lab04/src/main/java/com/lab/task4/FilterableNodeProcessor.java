package com.lab.task4;

import com.lab.task3.NodeProcessor;

import java.util.Comparator;
import java.util.List;

public class FilterableNodeProcessor extends NodeProcessor {
    public void printFilteredDescriptors(List<String> keyWords) {
        wordsDescriptors.entrySet()
                .stream()
                .filter(e -> keyWords.contains(e.getKey()))
                .map(e -> e.getValue())
                .sorted(Comparator.comparing(d -> d.files().size()))
                .forEach(x -> {
                    if (x.files().size() > 1) {
                        System.out.println(x.word() + ": " + String.join(", ", x.files()));
                    }
                });
    }
}
