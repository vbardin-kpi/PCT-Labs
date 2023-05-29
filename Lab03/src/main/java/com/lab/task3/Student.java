package com.lab.task3;

import java.util.*;

public final class Student {
    private final String name;
    private final Map<String, List<Mark>> marks;

    public Student(String name) {
        this.name = name;
        this.marks = Collections.synchronizedMap(new HashMap<>());
    }

    public String getName() {
        return name;
    }

    public void addMark(String subject, Mark mark) {
        if (marks.containsKey(subject)) {
            var marksForSubject = marks.get(subject);
            marksForSubject.add(mark);
        }
        else {
            var marksForSubject = Collections.synchronizedList(new ArrayList<Mark>());
            marksForSubject.add(mark);
            marks.put(subject, marksForSubject);
        }
    }

    public List<Mark> getMarks(String subject) {
        return marks.containsKey(subject) ? marks.get(subject) : new ArrayList<>();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (Student) obj;
        return Objects.equals(this.name, that.name) &&
                Objects.equals(this.marks, that.marks);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, marks);
    }

    @Override
    public String toString() {
        return "Student[" +
                "name=" + name + ", " +
                "Marks=" + marks + ']';
    }
}
