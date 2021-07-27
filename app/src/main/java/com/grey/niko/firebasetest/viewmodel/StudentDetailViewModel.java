package com.grey.niko.firebasetest.viewmodel;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.grey.niko.firebasetest.livedata.FirebaseQueryLiveData;
import com.grey.niko.firebasetest.model.Goal;
import com.grey.niko.firebasetest.model.TeamMember;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StudentDetailViewModel extends ViewModel {

    // Log tag for debug
    private final String LOG_TAG = "StudentDetailViewModel";
    // Student ID for use in database query
    private String studentID;
    // Firebase Database references
    FirebaseDatabase db = FirebaseDatabase.getInstance();
    // Fetch data from database and assign it to observable livedata
    DatabaseReference dbStudent;
    FirebaseQueryLiveData studentLiveData;

    public void setStudentID(String studentID) {
        this.studentID = studentID;
        dbStudent = db.getReference("students").child(studentID);
        studentLiveData = new FirebaseQueryLiveData(dbStudent);
    }

    public void removeGoal(String goalKey) {
        DatabaseReference goalToRemove = dbStudent.child("goals").child(goalKey);
        goalToRemove.removeValue();
    }

    public void addGoal(String id, String desc) {
        DatabaseReference dbGoals = dbStudent.child("goals");
        Goal newGoal = new Goal(id, desc);
        Map<String, Object> updateGoals = new HashMap<>();
        updateGoals.put(id, newGoal);
        dbGoals.updateChildren(updateGoals);
    }

    public List<String> getTmNames() {
        List<String> tmNamesList = new ArrayList<>();
        Query findNames = db.getReference().child("teamMembers");

        findNames.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    TeamMember tm = postSnapshot.getValue(TeamMember.class);
                    if (tm != null) {
                        String name = tm.getName();
                        tmNamesList.add(name);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {
                Log.i(LOG_TAG, "Database call encountered an error: " + error);
            }
        });
        return tmNamesList;
    }

    public void addTmToStudent(String tmToAdd) {
        Query findTm = db.getReference().child("teamMembers");

        findTm.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    TeamMember tm = postSnapshot.getValue(TeamMember.class);
                    if (tm != null && tm.getName().equals(tmToAdd)) {

                        Map<String, Object> tmMap = new HashMap<>();
                        tmMap.put(tm.getId(), tm);
                        dbStudent.child("teamMembers").updateChildren(tmMap);

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {
                Log.i(LOG_TAG, "Database call encountered an error: " + error);
            }
        });
    }

    public void removeTmFromStudent(String teamMemberKey) {
        DatabaseReference teamMemberToRemove = dbStudent.child("teamMembers").child(teamMemberKey);
        teamMemberToRemove.removeValue();
    }

    @NonNull
    public LiveData<DataSnapshot> getStudentLiveData() {
        return studentLiveData;
    }

}