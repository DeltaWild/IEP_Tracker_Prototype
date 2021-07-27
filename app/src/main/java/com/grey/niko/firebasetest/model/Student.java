package com.grey.niko.firebasetest.model;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * <h1>Student</h1>
 * <p>Model class for a student object to be mapped to the Firebase Realtime Database.</p>
 *
 * @version 1.0.0
 */
@IgnoreExtraProperties
public class Student {

    /**
     * The identifier of the student; used as a key in the database, must be unique.
     */
    public String id;
    /**
     * The full name of the student.
     */
    public String name;
    /**
     * The current grade level of the student.
     */
    public String gradeLevel;
    /**
     * The school the student attends.
     */
    public String school;

    /**
     * Empty constructor for use with Firebase API; avoid use of this directly, use parameterized
     * constructor instead.
     */
    public Student() {}

    /**
     * Constructor specifying a student's ID, full name, grade level, and school for mapping to the
     * Database.
     *
     * @param id the unique ID to be used as a key in the Database
     * @param name the full name of the student
     * @param gradeLevel the current grade level of the student
     * @param school the school the student attends
     */
    public Student(String id, String name, String gradeLevel, String school) {
        this.id = id;
        this.name = name;
        this.gradeLevel = gradeLevel;
        this.school = school;
    }

    // Standard getters and setters
    public String getId() {
        return id;
    }

    void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    void setName(String name) {
        this.name = name;
    }

    public String getGradeLevel() {
        return gradeLevel;
    }

    void setGradeLevel(String gradeLevel) {
        this.gradeLevel = gradeLevel;
    }

    public String getSchool() {
        return school;
    }

    void setSchool(String school) {
        this.school = school;
    }

}