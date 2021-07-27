package com.grey.niko.firebasetest.livedata;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.grey.niko.firebasetest.R;

/**
 * <h1>GoalsViewHolder</h1>
 * <p>Custom implementation of the RecyclerView ViewHolder subclass. Describes the views to be
 * contained in the RecyclerView to which the associated adapter is bound.</p>
 * <p>This class is intended to be used with FirebaseRecyclerAdapter.</p>
 *
 * @version 1.0.0
 * @see com.firebase.ui.database.FirebaseRecyclerAdapter
 */
public class GoalsHolder extends RecyclerView.ViewHolder {

    TextView txtGoalDesc;

    /**
     * Constructor that takes the view item to be cached for display in the RecyclerView.
     *
     * @param itemView view item to be cached for display in RecyclerView
     */
    public GoalsHolder(@NonNull View itemView) {
        super(itemView);
        txtGoalDesc = itemView.findViewById(R.id.txtGoalDesc);
    }

    /**
     * Sets the text of the UI element which has been initialized to the value of the local TextView
     * variable.
     *
     * @param desc the text to assign to the UI element
     */
    public void setText(String desc) {
        txtGoalDesc.setText(desc);
    }

}
