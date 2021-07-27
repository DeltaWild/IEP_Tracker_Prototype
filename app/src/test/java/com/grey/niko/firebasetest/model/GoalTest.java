package com.grey.niko.firebasetest.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GoalTest {

    Goal goal;

    @BeforeEach
    void setUp() {
        goal = new Goal("id", "desc");
    }

    @Test
    void getId() {
        String retrievedId = goal.getId();
        assertEquals("id", retrievedId);
    }

    @Test
    void setId() {
        goal.setId("newId");
        assertEquals("newId", goal.getId());
    }

    @Test
    void getDesc() {
        String retrievedDesc = goal.getDesc();
        assertEquals("desc", retrievedDesc);
    }

    @Test
    void setDesc() {
        goal.setDesc("newDesc");
        assertEquals("newDesc", goal.getDesc());
    }
}