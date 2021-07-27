package com.grey.niko.firebasetest.viewmodel;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.grey.niko.firebasetest.model.Goal;
import com.grey.niko.firebasetest.model.Student;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainViewModel extends ViewModel {

    // Log tag for debug
    private final String LOG_TAG = "MainViewModel";
    // Firebase Database references
    FirebaseDatabase db = FirebaseDatabase.getInstance();
    DatabaseReference dbStudents = db.getReference("students");
    // MutableLiveData value of students to communicate with Fragment
    private MutableLiveData<ArrayList<Student>> studentLiveData;
    private ArrayList<Student> studentArrayList = new ArrayList<>();


    public void startUp() {
        studentLiveData = new MutableLiveData<>();
        loadStudents();
        studentLiveData.setValue(studentArrayList);
    }

    public String getStudentId(int position) {
        return studentArrayList.get(position).getId();
    }

    public MutableLiveData<ArrayList<Student>> getStudentLiveData() {
        return studentLiveData;
    }

    private void loadStudents() {
        dbStudents.orderByChild("id").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    //Getting the data from snapshot
                    studentArrayList.add(postSnapshot.getValue(Student.class));
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.i(LOG_TAG, "Database call encountered an error: " + databaseError);
            }
        });
    }

    public void addStudent(String id, String name, String gradeLvl, String school) {
        Student newStudent = new Student(id, name, gradeLvl, school);
        Map<String, Object> updateStudents = new HashMap<>();
        updateStudents.put(id, newStudent);
        dbStudents.updateChildren(updateStudents);
    }

    public void removeStudent(String studentKey) {
        DatabaseReference studentToRemove = dbStudents.child(studentKey);
        studentToRemove.removeValue();
    }

}