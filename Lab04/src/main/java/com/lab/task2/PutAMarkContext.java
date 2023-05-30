package com.lab.task2;

public record PutAMarkContext(Journal journal, Teacher teacher, String studentName, String subject, float mark, int week) {
}
