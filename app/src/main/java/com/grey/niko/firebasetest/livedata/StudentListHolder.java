package com.grey.niko.firebasetest.livedata;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.grey.niko.firebasetest.R;

/**
 * <h1>StudentListHolder</h1>
 * <p>Custom implementation of the RecyclerView ViewHolder subclass. Describes the views to be
 * contained in the RecyclerView to which the associated adapter is bound.</p>
 * <p>This class is intended to be used with FirebaseRecyclerAdapter.</p>
 *
 * @version 1.0.0
 * @see com.firebase.ui.database.FirebaseRecyclerAdapter
 */
public class StudentListHolder extends RecyclerView.ViewHolder {

    TextView txtStudentName;
    TextView txtStudentId;

    /**
     * Constructor that takes the view item to be cached for display in the RecyclerView and
     * initializes UI elements to local variables.
     *
     * @param itemView view item to be cached for display in RecyclerView
     */
    public StudentListHolder(@NonNull View itemView) {
        super(itemView);
        txtStudentName = itemView.findViewById(R.id.txtStudentName);
        txtStudentId = itemView.findViewById(R.id.txtStudentId);
    }

    /**
     * Sets the text of the UI elements which have been initialized to the value of the local TextView
     * variables.
     *
     * @param name the student's full name
     * @param id the student's unique ID
     */
    public void setText(String name, String id) {
        txtStudentName.setText(name);
        txtStudentId.setText(id);
    }
}