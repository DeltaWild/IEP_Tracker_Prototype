package com.grey.niko.firebasetest.viewmodel;

import android.provider.ContactsContract;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.grey.niko.firebasetest.model.TeamMember;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TeamMemberViewModel extends ViewModel {

    // Log tag for debug
    private final String LOG_TAG = "MainViewModel";
    // Firebase Database references
    FirebaseDatabase db = FirebaseDatabase.getInstance();
    DatabaseReference dbTeamMembers = db.getReference("teamMembers");

    public void addTeamMember(String id, String name, String position, String school) {
        TeamMember newTeamMember = new TeamMember(id, name, position, school);
        Map<String, Object> updateTeamMembers = new HashMap<>();
        updateTeamMembers.put(id, newTeamMember);
        dbTeamMembers.updateChildren(updateTeamMembers);
    }

    public void removeTeamMember(String teamMemberKey) {
        DatabaseReference teamMemberToRemove = dbTeamMembers.child(teamMemberKey);
        teamMemberToRemove.removeValue();
    }

}