package com.grey.niko.firebasetest.model;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * <h1>TeamMember</h1>
 * <p>Model class for a team member object to be mapped to the Firebase Realtime Database.</p>
 *
 * @version 1.0.0
 */
@IgnoreExtraProperties
public class TeamMember {

    /**
     * The identifier of the team member; used as a key in the database, must be unique.
     */
    public String id;
    /**
     * The full name of the team member.
     */
    public String name;
    /**
     * The position or role of the team member.
     */
    public String position;
    /**
     * The school the team member works at or out of.
     */
    public String school;

    /**
     * Empty constructor for use with Firebase API; avoid use of this directly, use parameterized
     * constructor instead.
     */
    public TeamMember() {}

    /**
     * Constructor specifying a team member's ID, full name, position, and school for mapping to the
     * Database.
     *
     * @param id the unique ID to be used as a key in the Database
     * @param name the full name of the team member
     * @param position the position/role of the team member
     * @param school the school the team member works at/out of
     */
    public TeamMember(String id, String name, String position, String school) {
        this.id = id;
        this.name = name;
        this.position = position;
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

    public String getPosition() {
        return position;
    }

    void setPosition(String position) {
        this.position = position;
    }

    public String getSchool() {
        return school;
    }

    void setSchool(String school) {
        this.school = school;
    }

}