package com.lab.task3;


import java.util.*;

public class Journal {
    private final Set<Group> data = Collections.synchronizedSet(new HashSet<>());

    public void addGroup(Group group) {
        data.add(group);
    }

    public void addStudent(String studentName, String groupName)
    {
        var group = data
                .stream()
                .filter(g -> g.getName().equals(groupName))
                .findFirst();

        if (group.isEmpty()) {
            System.out.println("Group with name " + groupName + " not found");
            return;
        }

        group.get().addStudent(new Student(studentName));
    }

    public void addMarkForStudent(String studentName, String subject, Teacher teacher, float score) {
        Optional<Student> student = getStudent(studentName);
        if (student.isEmpty()) {
            return;
        }

        student.get().addMark(subject, new Mark(score, subject, teacher));
    }

    public List<Mark> getMarks(String studentName, String subject) {
        Optional<Student> student = getStudent(studentName);
        if (student.isEmpty()) {
            return new ArrayList<>();
        }

        return student.get().getMarks(subject);
    }

    private Optional<Student> getStudent(String studentName) {
        var student = data.stream()
                .map(Group::getStudents)
                .flatMap(Collection::stream)
                .filter(x -> x.getName().equals(studentName))
                .findFirst();

        if (student.isEmpty()) {
            System.out.println("Student with name " + studentName + " is not exists");
            return Optional.empty();
        }

        return student;
    }

    @Override
    public String toString() {
        return "Journal{" +
                "data=" + data +
                '}';
    }
}




