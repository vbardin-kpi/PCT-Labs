package com.lab.task2;

import java.io.Console;
import java.util.ArrayList;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

public class Runner {
    private final Journal journal = new Journal();
    private final int studentsPerGroup = 1000;
    private final ArrayList<String> studentNames = new ArrayList<>();

    private final Teacher[] teachers;
    private final String[] subjects;


    public Runner() {
        teachers = new Teacher[]{
                new Teacher("lector", TeacherType.Lector),
                new Teacher("assistant-1", TeacherType.Assistant),
                new Teacher("assistant-2", TeacherType.Assistant),
                new Teacher("assistant-3", TeacherType.Assistant),
        };

        subjects = new String[]{
                "PCT"
        };

        init();
    }

    public void run() {
        int weeks = 18;

        var fjPool = new ForkJoinPool(Runtime.getRuntime().availableProcessors());
        for (int i = 0; i < weeks; i++) {
            int weekNumber = i + 1;
            var contexts = studentNames
                    .stream().map(studentName -> new PutAMarkContext(
                            journal,
                            getTeacher(teachers),
                            studentName,
                            getSubject(subjects),
                            ThreadLocalRandom.current().nextFloat(0, 101f),
                            weekNumber))
                    .toList();

            var task = new PutAMarkTaskV2(contexts);
            fjPool.submit(task);
        }

        try {
            fjPool.shutdown();
            fjPool.awaitTermination(30L, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private String getSubject(String[] items) {
        return items[new Random().nextInt(items.length)];
    }

    private Teacher getTeacher(Teacher[] items) {
        return items[new Random().nextInt(items.length)];
    }

    private void init() {
        var groups = new Group[]{
                new Group("G-1"),
                new Group("G-2"),
                new Group("G-3"),
        };

        for (var group : groups) {
            for (var i = 0; i < studentsPerGroup; i++) {
                var studentName = UUID.randomUUID().toString();
                studentNames.add(studentName);

                var student = new Student(studentName);
                group.addStudent(student);
            }

            journal.addGroup(group);
        }
    }
}
