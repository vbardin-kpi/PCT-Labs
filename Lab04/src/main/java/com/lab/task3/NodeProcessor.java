package com.lab.task3;

import java.io.File;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.TimeUnit;

import static java.util.Objects.isNull;

public class NodeProcessor {
    protected Map<String, WordDescriptor> wordsDescriptors;

    public NodeProcessor() {
        this.wordsDescriptors = Collections.synchronizedMap(new HashMap<>());
    }

    public void createDescriptors(String folderPath) {
        var file = new File(folderPath);

        try (var pool = new ForkJoinPool(Runtime.getRuntime().availableProcessors())) {
            if (file.isDirectory() && !isNull(file.listFiles())) {
                pool.submit(new NodeExpanderTask(wordsDescriptors, file)).join();
            } else {
                System.out.println("Given path isn't a directory");
                throw new RuntimeException();
            }

            try {
                pool.shutdown();
                pool.awaitTermination(120L, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void printDescriptors() {
        wordsDescriptors.values().stream()
                .sorted(Comparator.comparing(d -> d.files().size()))
                .forEach(x -> {
                    if (x.files().size() > 2) {
                        System.out.println(x.word() + ": " + String.join(", ", x.files()));
                    }
                });
    }
}
