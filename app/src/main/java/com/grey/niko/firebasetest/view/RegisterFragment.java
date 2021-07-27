package com.grey.niko.firebasetest.view;

import androidx.annotation.IdRes;
import androidx.annotation.StringRes;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.grey.niko.firebasetest.R;
import com.grey.niko.firebasetest.databinding.RegisterFragmentBinding;
import com.grey.niko.firebasetest.viewmodel.RegisterViewModel;

public class RegisterFragment extends Fragment {

    // Log tag for debug
    private final String LOG_TAG = "RegisterFragment";
    // ViewModel variable declared globally for use across fragment
    private RegisterViewModel mViewModel;
    // Binding variable declared globally for use across fragment
    private RegisterFragmentBinding binding;
    // Firebase Auth reference
    FirebaseAuth auth = FirebaseAuth.getInstance();

    public static RegisterFragment newInstance() {
        return new RegisterFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Access XML file associated with this Java class via binding variable
        binding = RegisterFragmentBinding.inflate(inflater, container, false);
        // Hide the bottom navigation bar
        BaseActivity activity = (BaseActivity) getActivity();
        if (activity != null) {
            activity.hideBottomNav(true);
        }
        // Return the View to the parent container
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        // Load any saved instance
        super.onViewCreated(view, savedInstanceState);
        // Obtain an instance of the ViewModel
        mViewModel = new ViewModelProvider(this).get(RegisterViewModel.class);
        binding.setLivedata(mViewModel);
        binding.linkSignIn.setOnClickListener(v -> goTo(R.id.action_registerToAuth));

        setupForm();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // Release the binding when the view is destroyed
        binding = null;
    }

    private void setupForm() {
        mViewModel.startUp();
        // Setup the observer for the MutableLiveData field
        mViewModel.getBtnRegisterClick().observe(getViewLifecycleOwner(), registerFields -> {
            auth.createUserWithEmailAndPassword(registerFields.getEmail(), registerFields.getPassword())
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            // Registration successful
                            Log.i(LOG_TAG, "registerWithEmailAndPassword:success");
                            auth.getCurrentUser();
                            goTo(R.id.action_registerToMain);
                        }
                        else {
                            // Registration failed
                            Log.i(LOG_TAG, "registerWithEmailAndPassword:failed");
                            snack(R.string.register_failed);
                        }
                    });
            binding.progressBar.setVisibility(View.VISIBLE);
        });
    }

    // Convenience method for fragment navigation
    private void goTo(@IdRes int action) {
        Navigation.findNavController(binding.getRoot()).navigate(action);
    }

    // Convenience method to show messages on screen
    private void snack(@StringRes int msg) {
        Snackbar.make(binding.getRoot(), msg, Snackbar.LENGTH_LONG).show();
    }

}