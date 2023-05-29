package com.lab.task3.test;

import com.lab.task3.Journal;
import com.lab.task3.Teacher;

public record PutAMarkContext(Journal journal, Teacher teacher, String studentName, String subject, float mark) {
}
