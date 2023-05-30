package com.lab.task2;

import java.util.List;
import java.util.concurrent.RecursiveTask;

import static java.lang.Thread.currentThread;

public class PutAMarkTaskV2 extends RecursiveTask {
    private final List<PutAMarkContext> contexts;
    private static final int THRESHOLD = 20;


    public PutAMarkTaskV2(List<PutAMarkContext> contexts) {
        this.contexts = contexts;
    }

    @Override
    protected Object compute() {
        if (contexts.size() > THRESHOLD) {
            var pt1 = new PutAMarkTaskV2(contexts.subList(0, contexts.size() / 2));
            var pt2 = new PutAMarkTaskV2(contexts.subList(contexts.size() / 2, contexts.size()));

            pt1.fork();
            pt2.fork();

            pt1.join();
            pt2.join();
        }
        else {
            for (var context : contexts) {
                context.journal().addMarkForStudent(
                        context.studentName(),
                        context.subject(),
                        context.teacher(),
                        context.mark());

                System.out.println(currentThread().getName() + " //" +
                        " Week: " + context.week() +
                        " Teacher " + context.teacher().name() +
                        " with role " + context.teacher().teacherType() +
                        " put " + context.mark() +
                        " to student " + context.studentName() +
                        " for " + context.subject());
            }
        }

        return new Object();
    }
}
