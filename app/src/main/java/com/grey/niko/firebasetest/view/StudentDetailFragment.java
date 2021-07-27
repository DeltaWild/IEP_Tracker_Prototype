package com.grey.niko.firebasetest.view;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.grey.niko.firebasetest.R;
import com.grey.niko.firebasetest.databinding.StudentDetailFragmentBinding;
import com.grey.niko.firebasetest.model.Goal;
import com.grey.niko.firebasetest.livedata.GoalsHolder;
import com.grey.niko.firebasetest.model.TeamMember;
import com.grey.niko.firebasetest.livedata.TeamMemberListHolder;
import com.grey.niko.firebasetest.viewmodel.StudentDetailViewModel;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 *
 * @see <a href="https://github.com/WillieCubed/FirebaseUI-Android-Addons/blob/master/library/src/main/java/com/craft/libraries/firebaseuiaddon/FirebaseSpinnerAdapter.java">
 *     FirebaseUI-Android-Addons by WillieCubed on GitHub</a>
 */
public class StudentDetailFragment extends Fragment {

    // Log tag for debug
    private final String LOG_TAG = "StudentDetailFragment";
    // Key to extract safearg
    private static final String ARG_STUDENT_ID = "studentId";
    // Value store for safearg
    private String paramStudentId;
    // ViewModel variable declared globally for use across fragment
    private StudentDetailViewModel mViewModel;
    // Binding variable declared globally for use across fragment
    private StudentDetailFragmentBinding binding;
    private String addGoal;
    private List<String> tmNamesList;
    // Firebase Database references
    FirebaseDatabase db = FirebaseDatabase.getInstance();
    Query dbTeamMembers = db.getReference("teamMembers");
    Query queryGoals;
    Query queryTeamMember;
    FirebaseRecyclerOptions<Goal> goalsOptions;
    FirebaseRecyclerOptions<TeamMember> teamMemberListOptions;
    FirebaseRecyclerAdapter<Goal, GoalsHolder> goalsAdapter;
    FirebaseRecyclerAdapter<TeamMember, TeamMemberListHolder> adapter;

    public static StudentDetailFragment newInstance(String paramStudentId) {
        StudentDetailFragment fragment = new StudentDetailFragment();
        Bundle args = new Bundle();
        args.putString(ARG_STUDENT_ID, paramStudentId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            paramStudentId = getArguments().getString(ARG_STUDENT_ID);
        }
        queryGoals = db.getReference("students").child(paramStudentId).child("goals");
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Access XML file associated with this Java class via binding variable
        binding = StudentDetailFragmentBinding.inflate(inflater, container, false);
        // Associate the livedata variable in the XML file with the ViewModel
        binding.setLivedata(mViewModel);
        // Return the View to the parent container
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(StudentDetailViewModel.class);
        // Send student ID to view model for use in db reference
        mViewModel.setStudentID(paramStudentId);
        // Observe livedata and update UI
        mViewModel.getStudentLiveData().observe(getViewLifecycleOwner(), snapshot -> {
            if (snapshot != null) {
                String name = snapshot.child("name").getValue(String.class);
                binding.txtStudentDetailName.setText(name);
                String studentID = snapshot.child("id").getValue(String.class);
                binding.txtStudentDetailsId.setText(studentID);
                String grade = snapshot.child("gradeLevel").getValue(String.class);
                binding.txtStudentDetailsGradeLvl.setText(grade);
                String school = snapshot.child("school").getValue(String.class);
                binding.txtStudentDetailsSchool.setText(school);
            }
        });
        
        loadGoals();
        tmNamesList = mViewModel.getTmNames();

        binding.btnAddGoalToStudent.setOnClickListener(v -> showAddGoalDialog(v));
        binding.btnAddTmToStudent.setOnClickListener(v -> showAddTmToStudentDialog(v));
        binding.btnViewStudentTm.setOnClickListener(v -> showStudentTmListDialog(v));


        queryTeamMember = db.getReference("students").child(paramStudentId).child("teamMembers");
        queryTeamMember.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    TeamMember tm = postSnapshot.getValue(TeamMember.class);
                    if (tm != null) {
                        Log.i(LOG_TAG, tm.getName() + tm.getPosition() + tm.getSchool());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    /**
     * This method loads the goals and benchmarks information for a student record from Firebase
     * Realtime Database into nested RecyclerViews using FirebaseUI components.
     */
    private void loadGoals() {
        goalsOptions =
                new FirebaseRecyclerOptions.Builder<Goal>()
                        .setQuery(queryGoals, Goal.class)
                        .setLifecycleOwner(this)
                        .build();
        goalsAdapter =
                new FirebaseRecyclerAdapter<Goal, GoalsHolder>(goalsOptions) {
                    @NonNull
                    @Override
                    public GoalsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View v = LayoutInflater.from(parent.getContext())
                                .inflate(R.layout.goals_card_layout, parent, false);
                        return new GoalsHolder(v);
                    }

                    @Override
                    protected void onBindViewHolder(@NonNull GoalsHolder holder, int position, @NonNull Goal goal) {
                        String key = goalsAdapter.getRef(position).getKey();
                        holder.setText(goal.getDesc());
                        holder.itemView.setOnClickListener(v -> {
                            Log.i(LOG_TAG, "Click detected on goals RecyclerView: position: " + position);
                        });
                        holder.itemView.setOnLongClickListener(v -> {
                            Log.i(LOG_TAG, "Long click detected on goals RecyclerView: position: " + position);
                            actionSnack(key);
                            return true;
                        });
                    }
                };
        binding.recyclerGoals.setAdapter(goalsAdapter);
        binding.recyclerGoals.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    private void showAddGoalDialog(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Add Goal");
        final View dialogLayout = getLayoutInflater()
                                    .inflate(R.layout.add_goal_dialog, null);
        builder.setView(dialogLayout);
        builder.setPositiveButton("ADD", (dialog, which) -> {
            EditText edtDialogAddGoalId = dialogLayout.findViewById(R.id.edtDialogAddGoalId);
            EditText edtDialogAddGoalDesc = dialogLayout.findViewById(R.id.edtDialogAddGoalDesc);
            if (edtDialogAddGoalId != null && !edtDialogAddGoalId.getText().toString().isEmpty()) {
                mViewModel.addGoal(edtDialogAddGoalId.getText().toString(), edtDialogAddGoalDesc.getText().toString());
            }
            else {
                snack(R.string.goal_id_req);
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void showAddTmToStudentDialog(View v) {
        final String[] tmToAdd = {""};
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Add Team Member");
        final View dialogLayout = getLayoutInflater()
                .inflate(R.layout.add_tm_to_student_dialog, null);
        builder.setView(dialogLayout);

        Spinner spinnerTeamMembers = dialogLayout.findViewById(R.id.spinnerTeamMembers);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, tmNamesList);
        spinnerTeamMembers.setAdapter(adapter);

        spinnerTeamMembers.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                TextView textView = (TextView) parent.getSelectedView();
                tmToAdd[0] = textView.getText().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        builder.setPositiveButton("ADD", (dialog, which) -> { mViewModel.addTmToStudent(tmToAdd[0]); });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void showStudentTmListDialog(View v) {
        final String[] key = {""};
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Team Members");
        final View dialogLayout = getLayoutInflater()
                .inflate(R.layout.student_tm_list_dialog, null);
        builder.setView(dialogLayout);
        builder.setNeutralButton("CLOSE", (dialog, which) -> {dialog.dismiss();});
        builder.setPositiveButton("DELETE", ((dialog, which) -> {mViewModel.removeTmFromStudent(key[0]);}));
        AlertDialog dialog = builder.create();
        dialog.show();
        dialog.getButton(DialogInterface.BUTTON_POSITIVE).setVisibility(View.INVISIBLE);

        queryTeamMember = db.getReference("students").child(paramStudentId).child("teamMembers");
        teamMemberListOptions =
                new FirebaseRecyclerOptions.Builder<TeamMember>()
                        .setQuery(queryTeamMember, TeamMember.class)
                        .setLifecycleOwner(this)
                        .build();
        adapter =
                new FirebaseRecyclerAdapter<TeamMember, TeamMemberListHolder>(teamMemberListOptions) {
                    @NonNull
                    @Override
                    public TeamMemberListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View v = LayoutInflater.from(parent.getContext())
                                .inflate(R.layout.team_member_list_card_layout, parent, false);
                        return new TeamMemberListHolder(v);
                    }

                    @Override
                    protected void onBindViewHolder(@NonNull TeamMemberListHolder holder, int position, @NonNull TeamMember teamMember) {
                        holder.setText(teamMember.getName(), teamMember.getPosition(), teamMember.getSchool());
                        holder.itemView.setOnLongClickListener(v1 -> {
                            Log.i(LOG_TAG, "Long click detected on team member list RecyclerView: position: " + position);
                            key[0] = adapter.getRef(position).getKey();
                            dialog.getButton(DialogInterface.BUTTON_POSITIVE).setVisibility(View.VISIBLE);
                            return true;
                        });
                    }

                    @Override
                    public void onDataChanged() {
                        super.onDataChanged();
                    }
                };
        adapter.notifyDataSetChanged();

        RecyclerView recyclerStudentTm = dialogLayout.findViewById(R.id.recyclerStudentTm);
        recyclerStudentTm.setAdapter(adapter);
        recyclerStudentTm.setLayoutManager(new LinearLayoutManager(v.getContext()));
    }

    /**
     * This is a convenience method for navigation graph-based navigation actions.
     * It simplifies redundant code by providing the NavController and binding root of the fragment.
     *
     * @param action an IdRes value (such as R.id.ACTION_TO_USE)
     */
    // Convenience method for fragment navigation
    private void goTo(@IdRes int action) {
        Navigation.findNavController(binding.getRoot()).navigate(action);
    }

    // Convenience method to show messages on screen
    private void snack(@StringRes int msg) {
        Snackbar.make(requireView(), msg, Snackbar.LENGTH_LONG)
                .setAnchorView(R.id.bottomNav)
                .show();
    }

    // Interactive message
    private void actionSnack(String goalKey) {
        Snackbar.make(requireView(), R.string.delete_goal, Snackbar.LENGTH_LONG)
                .setAction(R.string.delete, v -> mViewModel.removeGoal(goalKey))
                .setAnchorView(R.id.bottomNav)
                .show();
    }

}