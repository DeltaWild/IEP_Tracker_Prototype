package com.grey.niko.firebasetest.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StudentTest {
    
    Student student;

    @BeforeEach
    void setUp() {
        student = new Student("id", "name", "grade", "school");
    }

    @Test
    void getId() {
        String retrievedId = student.getId();
        assertEquals("id", retrievedId);
    }

    @Test
    void setId() {
        student.setId("newId");
        assertEquals("newId", student.getId());
    }

    @Test
    void getName() {
        String retrievedName = student.getName();
        assertEquals("name", retrievedName);
    }

    @Test
    void setName() {
        student.setName("newName");
        assertEquals("newName", student.getName());
    }

    @Test
    void getGradeLevel() {
        String retrievedGrade = student.getGradeLevel();
        assertEquals("grade", retrievedGrade);
    }

    @Test
    void setGradeLevel() {
        student.setGradeLevel("newGrade");
        assertEquals("newGrade", student.getGradeLevel());
    }

    @Test
    void getSchool() {
        String retrievedSchool = student.getSchool();
        assertEquals("school", retrievedSchool);
    }

    @Test
    void setSchool() {
        student.setSchool("newSchool");
        assertEquals("newSchool", student.getSchool());
    }
}