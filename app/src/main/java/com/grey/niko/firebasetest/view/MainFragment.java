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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.grey.niko.firebasetest.R;
import com.grey.niko.firebasetest.databinding.MainFragmentBinding;
import com.grey.niko.firebasetest.model.Student;
import com.grey.niko.firebasetest.livedata.StudentListHolder;
import com.grey.niko.firebasetest.viewmodel.MainViewModel;

public class MainFragment extends Fragment {

    // Log tag for debug
    private final String LOG_TAG = "MainFragment";
    // ViewModel variable declared globally for use across fragment
    private MainViewModel mViewModel;
    // Binding variable declared globally for use across fragment
    private MainFragmentBinding binding;
    // Firebase Auth reference
    FirebaseAuth auth = FirebaseAuth.getInstance();
    // Firebase Database references
    FirebaseDatabase db = FirebaseDatabase.getInstance();
    Query queryStudent = db.getReference("students");
    FirebaseRecyclerOptions<Student> studentListOptions;
    FirebaseRecyclerAdapter adapter;

    public static MainFragment newInstance() {
        return new MainFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Access XML file associated with this Java class via binding variable
        binding = MainFragmentBinding.inflate(inflater, container, false);
        binding.setLivedata(mViewModel);
        // Hide the bottom navigation bar
        BaseActivity activity = (BaseActivity) getActivity();
        if (activity != null) {
            activity.hideBottomNav(false);
        }
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
        mViewModel = new ViewModelProvider(this).get(MainViewModel.class);
        mViewModel.startUp();
        // Click listener for FAB
        binding.fabAddStudent.setOnClickListener(v -> showAddStudentDialog(v));
        loadStudentList();
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check to see if the student is logged in; if not, redirect to auth fragment
        if (auth.getCurrentUser() == null) Navigation.findNavController(binding.getRoot()).navigate(R.id.action_mainToAuth);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // Release the binding when the view is destroyed
        binding = null;
    }

    private void loadStudentList() {
        // Register observer
      studentListOptions =
                new FirebaseRecyclerOptions.Builder<Student>()
                        .setQuery(queryStudent, Student.class)
                        .setLifecycleOwner(this)
                        .build();

        adapter =
                new FirebaseRecyclerAdapter<Student, StudentListHolder>(studentListOptions) {
            @NonNull
            @Override
            public StudentListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View v = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.student_list_card_layout, parent, false);
                return new StudentListHolder(v);
            }

            @Override
            protected void onBindViewHolder(@NonNull StudentListHolder holder, int position, @NonNull Student student) {
                String key = adapter.getRef(position).getKey();
                holder.setText(student.getName(), student.getId());

                holder.itemView.setOnClickListener(v -> {
                    Log.i(LOG_TAG, "Click detected on student list RecyclerView: position: " + position);
                    MainFragmentDirections.ActionMainToStudentDetail action = MainFragmentDirections.actionMainToStudentDetail();
                    action.setStudentId(student.getId());
                    Navigation.findNavController(binding.getRoot()).navigate(action);
                });
                holder.itemView.setOnLongClickListener(v -> {
                    Log.i(LOG_TAG, "Long click detected on student list RecyclerView: position: " + position);
                    deleteStudentSnack(key);
                    return true;
                });
            }

            @Override
            public void onDataChanged() {
                super.onDataChanged();
                updateProgressBar(true);
            }
        };
        binding.recyclerStudentList.setAdapter(adapter);
        binding.recyclerStudentList.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    private void showAddStudentDialog(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Add Student");
        final View dialogLayout = getLayoutInflater()
                .inflate(R.layout.add_student_dialog, null);
        builder.setView(dialogLayout);
        builder.setPositiveButton("ADD", (dialog, which) -> {
            EditText edtAddStudentId = dialogLayout.findViewById(R.id.edtAddStudentId);
            EditText edtAddStudentName = dialogLayout.findViewById(R.id.edtAddStudentName);
            EditText edtAddStudentGradeLvl = dialogLayout.findViewById(R.id.edtAddStudentGradeLvl);
            EditText edtAddStudentSchool = dialogLayout.findViewById(R.id.edtAddStudentSchool);
            if (edtAddStudentId != null && !edtAddStudentId.getText().toString().isEmpty()) {
                mViewModel.addStudent(edtAddStudentId.getText().toString(),
                        edtAddStudentName.getText().toString(),
                        edtAddStudentGradeLvl.getText().toString(),
                        edtAddStudentSchool.getText().toString());
            }
            else {
                snack(R.string.student_id_req);
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void updateProgressBar(boolean isHidden) {
        binding.progbarStudentList.setVisibility(isHidden ? View.GONE : View.VISIBLE);
        binding.fabAddStudent.setVisibility(isHidden ? View.VISIBLE : View.GONE);
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
    private void deleteStudentSnack(String studentKey) {
        Snackbar.make(requireView(), R.string.delete_student, Snackbar.LENGTH_LONG)
                .setAction(R.string.delete, v -> mViewModel.removeStudent(studentKey))
                .setAnchorView(R.id.bottomNav)
                .show();
    }

}