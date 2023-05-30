package com.lab.task2;

public class Mark {
    private final float score;
    private final String subject;
    private final Teacher teacher;

    public Mark(float score, String subject, Teacher teacher) {
        this.score = score;
        this.subject = subject;
        this.teacher = teacher;
    }

    @Override
    public String toString() {
        return "Mark{" +
                "score=" + score +
                ", subject='" + subject + '\'' +
                ", teacher=" + teacher +
                '}';
    }
}
