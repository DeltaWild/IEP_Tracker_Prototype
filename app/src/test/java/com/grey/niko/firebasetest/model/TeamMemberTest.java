package com.grey.niko.firebasetest.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TeamMemberTest {
    
    TeamMember teamMember;

    @BeforeEach
    void setUp() {
        teamMember = new TeamMember("id", "name", "position", "school");
    }

    @Test
    void getId() {
        String retrievedId = teamMember.getId();
        assertEquals("id", retrievedId);
    }

    @Test
    void setId() {
        teamMember.setId("newId");
        assertEquals("newId", teamMember.getId());
    }

    @Test
    void getName() {
        String retrievedName = teamMember.getName();
        assertEquals("name", retrievedName);
    }

    @Test
    void setName() {
        teamMember.setName("newName");
        assertEquals("newName", teamMember.getName());
    }

    @Test
    void getPosition() {
        String retrievedGrade = teamMember.getPosition();
        assertEquals("position", retrievedGrade);
    }

    @Test
    void setPosition() {
        teamMember.setPosition("newPosition");
        assertEquals("newPosition", teamMember.getPosition());
    }

    @Test
    void getSchool() {
        String retrievedSchool = teamMember.getSchool();
        assertEquals("school", retrievedSchool);
    }

    @Test
    void setSchool() {
        teamMember.setSchool("newSchool");
        assertEquals("newSchool", teamMember.getSchool());
    }
}