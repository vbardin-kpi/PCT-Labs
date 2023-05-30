package com.lab.task2;

public class PutAMarkTask extends Thread {
    private final PutAMarkContext context;

    public PutAMarkTask(PutAMarkContext context) {
        this.context = context;
    }

    @Override
    public void run() {
        context.journal().addMarkForStudent(
                context.studentName(),
                context.subject(),
                context.teacher(),
                context.mark());

        // System.out.println(currentThread().getName() + " // " +
        //         "Teacher " + context.teacher().name() +
        //         " with role " + context.teacher().teacherType() +
        //         " put " + context.mark() +
        //         " to student " + context.studentName() +
        //         " for " + context.subject());
    }
}
