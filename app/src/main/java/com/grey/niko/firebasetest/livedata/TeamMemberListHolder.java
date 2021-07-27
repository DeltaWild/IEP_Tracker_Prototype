package com.grey.niko.firebasetest.livedata;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.grey.niko.firebasetest.R;

/**
 * <h1>TeamMemberListHolder</h1>
 * <p>Custom implementation of the RecyclerView ViewHolder subclass. Describes the views to be
 * contained in the RecyclerView to which the associated adapter is bound.</p>
 * <p>This class is intended to be used with FirebaseRecyclerAdapter.</p>
 *
 * @version 1.0.0
 * @see com.firebase.ui.database.FirebaseRecyclerAdapter
 */
public class TeamMemberListHolder extends RecyclerView.ViewHolder {

    TextView txtTeamMemberName;
    TextView txtTeamMemberPosition;
    TextView txtTeamMemberSchool;

    /**
     * Constructor that takes the view item to be cached for display in the RecyclerView and
     * initializes UI elements to local variables.
     *
     * @param itemView view item to be cached for display in RecyclerView
     */
    public TeamMemberListHolder(@NonNull View itemView) {
        super(itemView);
        txtTeamMemberName = itemView.findViewById(R.id.txtTeamMemberName);
        txtTeamMemberPosition = itemView.findViewById(R.id.txtTeamMemberPosition);
        txtTeamMemberSchool = itemView.findViewById(R.id.txtTeamMemberSchool);
    }

    /**
     * Sets the text of the UI elements which have been initialized to the value of the local TextView
     * variables.
     *
     * @param name the team member's full name
     * @param position the team member's position/role
     * @param school the school the team member works at/out of
     */
    public void setText(String name, String position, String school) {
        txtTeamMemberName.setText(name);
        txtTeamMemberPosition.setText(position);
        txtTeamMemberSchool.setText(school);
    }
}