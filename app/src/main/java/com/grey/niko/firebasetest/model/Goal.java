package com.grey.niko.firebasetest.model;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * <h1>Goal</h1>
 * <p>Model class for a goal object to be mapped to the Firebase Realtime Database.</p>
 *
 * @version 1.0.0
 */
@IgnoreExtraProperties
public class Goal {

    /**
     * The identifier of the goal; used as a key in the database, must be unique.
     */
    public String id;
    /**
     * The goal as it appears on the student's IEP or 504.
     */
    public String desc;

    /**
     * Empty constructor for use with Firebase API; avoid use of this directly, use parameterized
     * constructor instead.
     */
    public Goal() {}

    /**
     * Constructor specifying an ID and goal description for mapping to the Database.
     *
     * @param id the unique ID to be used as a key in the Database
     * @param desc the goal text to be used as the value for the associated ID in the Database
     */
    public Goal(String id, String desc) {
        this.id = id;
        this.desc = desc;
    }

    // Standard getters and setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

}
