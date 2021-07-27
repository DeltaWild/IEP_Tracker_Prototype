package com.grey.niko.firebasetest.view;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.grey.niko.firebasetest.R;
import com.grey.niko.firebasetest.databinding.TeamMemberFragmentBinding;
import com.grey.niko.firebasetest.model.TeamMember;
import com.grey.niko.firebasetest.livedata.TeamMemberListHolder;
import com.grey.niko.firebasetest.viewmodel.TeamMemberViewModel;

public class TeamMemberFragment extends Fragment {

    // Log tag for debug
    private final String LOG_TAG = "MainFragment";
    // ViewModel variable declared globally for use across fragment
    private TeamMemberViewModel mViewModel;
    // Binding variable declared globally for use across fragment
    private TeamMemberFragmentBinding binding;
    // Firebase Database references
    FirebaseDatabase db = FirebaseDatabase.getInstance();
    Query queryTeamMember = db.getReference("teamMembers");
    FirebaseRecyclerOptions<TeamMember> teamMemberListOptions;
    FirebaseRecyclerAdapter adapter;

    public static TeamMemberFragment newInstance() {
        return new TeamMemberFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Access XML file associated with this Java class via binding variable
        binding = TeamMemberFragmentBinding.inflate(inflater, container, false);
        binding.setLivedata(mViewModel);
        updateProgressBar(false);
        // Return the View to the parent container
        return binding.getRoot();
    }

    // ViewModel interaction happens primarily in onViewCreated
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        // Load any saved instance
        super.onViewCreated(view, savedInstanceState);
        // Obtain an instance of the ViewModel
        mViewModel = new ViewModelProvider(this).get(TeamMemberViewModel.class);
        // Click listener for FAB
        binding.fabAddTeamMember.setOnClickListener(v -> showAddTeamMemberDialog(v));
        loadTeamMemberList();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // Release the binding when the view is destroyed
        binding = null;
    }

    private void loadTeamMemberList() {
        // Register observer
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
                String key = adapter.getRef(position).getKey();
                holder.setText(teamMember.getName(), teamMember.getPosition(), teamMember.getSchool());

                holder.itemView.setOnClickListener(v -> {
                    Log.i(LOG_TAG, "Click detected on team member list RecyclerView: position: " + position);
                });
                holder.itemView.setOnLongClickListener(v -> {
                    Log.i(LOG_TAG, "Long click detected on team member list RecyclerView: position: " + position);
                    deleteTeamMemberSnack(key);
                    return true;
                });
            }

            @Override
            public void onDataChanged() {
                super.onDataChanged();
                updateProgressBar(true);
            }
        };
        binding.recyclerTeamMemberList.setAdapter(adapter);
        binding.recyclerTeamMemberList.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    private void showAddTeamMemberDialog(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Add Team Member");
        final View dialogLayout = getLayoutInflater()
                .inflate(R.layout.add_team_member_dialog, null);
        builder.setView(dialogLayout);
        builder.setPositiveButton("ADD", (dialog, which) -> {
            EditText edtAddTeamMemberId = dialogLayout.findViewById(R.id.edtAddTeamMemberId);
            EditText edtAddTeamMemberName = dialogLayout.findViewById(R.id.edtAddTeamMemberName);
            EditText edtAddTeamMemberPosition = dialogLayout.findViewById(R.id.edtAddTeamMemberPosition);
            EditText edtAddTeamMemberSchool = dialogLayout.findViewById(R.id.edtAddTeamMemberSchool);
            if (edtAddTeamMemberId != null && !edtAddTeamMemberId.getText().toString().isEmpty()) {
                mViewModel.addTeamMember(edtAddTeamMemberId.getText().toString(),
                        edtAddTeamMemberName.getText().toString(),
                        edtAddTeamMemberPosition.getText().toString(),
                        edtAddTeamMemberSchool.getText().toString());
            }
            else {
                snack(R.string.teamMember_id_req);
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void updateProgressBar(boolean isHidden) {
        binding.progbarTeamMemberList.setVisibility(isHidden ? View.GONE : View.VISIBLE);
        binding.fabAddTeamMember.setVisibility(isHidden ? View.VISIBLE : View.GONE);
    }

    // Convenience method for fragment navigation
    private void goTo(@IdRes int action) {
        Navigation.findNavController(binding.getRoot()).navigate(action);
    }

    // Convenience method to show messages on screen
    private void snack(@StringRes int msg) {
        Snackbar.make(binding.getRoot(), msg, Snackbar.LENGTH_LONG)
                .setAnchorView(R.id.bottomNav)
                .show();
    }

    // Interactive message
    private void deleteTeamMemberSnack(String teamMemberKey) {
        Snackbar.make(requireView(), R.string.delete_team_member, Snackbar.LENGTH_LONG)
                .setAction(R.string.delete, v -> mViewModel.removeTeamMember(teamMemberKey))
                .setAnchorView(R.id.bottomNav)
                .show();
    }

}