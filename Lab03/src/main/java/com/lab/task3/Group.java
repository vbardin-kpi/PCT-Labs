package com.lab.task3;

import java.util.*;

public class Group {
    private final String name;
    private final Set<Student> students;

    public Group(String name) {
        this.name = name;
        this.students = Collections.synchronizedSet(new HashSet<>());
    }

    public String getName() {
        return name;
    }

    public Set<Student> getStudents() {
        return students;
    }

    public void addStudent(Student student) {
        students.add(student);
    }

    @Override
    public String toString() {
        return "Group{" +
                "name='" + name + '\'' +
                ", students=" + students +
                '}';
    }
}
