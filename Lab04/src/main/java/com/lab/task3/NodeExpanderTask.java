package com.lab.task3;


import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Objects.isNull;

public class NodeExpanderTask extends RecursiveTask {
    private final Map<String, WordDescriptor> wordsDescriptors;
    private final File file;

    public NodeExpanderTask(Map<String, WordDescriptor> wordsDescriptors, File file) {
        this.wordsDescriptors = wordsDescriptors;
        this.file = file;
    }

    @Override
    protected Object compute() {
        if (file.isDirectory() && !isNull(file.listFiles())) {
            var tasks = new ArrayList<NodeExpanderTask>();

            for (var fileToExpand : Objects.requireNonNull(file.listFiles())) {
                var expandTask = new NodeExpanderTask(wordsDescriptors, fileToExpand);
                tasks.add(expandTask);
            }

            tasks.forEach(ForkJoinTask::fork);
            tasks.forEach(ForkJoinTask::join);
        } else {
            var words = getWordsFromFile(file);
            words.forEach(w -> {
                WordDescriptor descriptor;
                if (!wordsDescriptors.containsKey(w)) {
                    descriptor = new WordDescriptor(w, Collections.synchronizedList(new ArrayList<>()));
                    wordsDescriptors.put(w, descriptor);
                } else {
                    descriptor = wordsDescriptors.get(w);
                }
                descriptor.files().add(file.getName());
            });
        }

        return new Object();
    }

    private static Set<String> getWordsFromFile(File file) {
        Stream<String> lines;
        try {
            lines = Files.lines(Path.of(file.getPath()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Set<String> words = lines.parallel()
                .flatMap(line -> Arrays.stream(line.split("\\W+")))
                .collect(Collectors.toSet());

        lines.close();
        return words;
    }
}
