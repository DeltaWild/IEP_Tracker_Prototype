package com.grey.niko.firebasetest.view;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.grey.niko.firebasetest.R;
import com.grey.niko.firebasetest.databinding.AuthFragmentBinding;
import com.grey.niko.firebasetest.viewmodel.AuthViewModel;

/**
 * <h1>AuthFragment</h1>
 * <p></p>
 *
 * @version 1.0.0
 */
public class AuthFragment extends Fragment {

    // Log tag for debug
    private final String LOG_TAG = "AuthFragment";
    /**
     * Global ViewModel variable
     */
    private AuthViewModel mViewModel;
    /**
     * Databinding reference to access XML elements
     */
    private AuthFragmentBinding binding;
    /**
     * Firebase Auth reference for user verification
     */
    FirebaseAuth auth = FirebaseAuth.getInstance();

    /**
     * Constructor to create a new instance of the AuthFragment.
     *
     * @return a new instance of the Fragment
     */
    public static AuthFragment newInstance() {
        return new AuthFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Access XML file associated with this Java class via binding variable
        binding = AuthFragmentBinding.inflate(inflater, container, false);
        // Hide the bottom navigation bar
        BaseActivity activity = (BaseActivity) getActivity();
        if (activity != null) {
            activity.hideBottomNav(true);
        }
        // Return the View to the parent container
        return binding.getRoot();
    }

    /**
     * Obtains an instance of the ViewModel associated with this Fragment, sets the binding, and
     * registers click listeners for any buttons or links in the Fragment view.
     *
     * @param view the rectangular area of the screen which this Fragment occupies
     * @param savedInstanceState a Map of saved data with which to recreate a previously saved
     *                           instance of this Fragment
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        // Load any saved instance
        super.onViewCreated(view, savedInstanceState);
        // Obtain an instance of the ViewModel
        mViewModel = new ViewModelProvider(this).get(AuthViewModel.class);
        binding.setLivedata(mViewModel);
        binding.linkRegister.setOnClickListener(v -> goTo(R.id.action_authToRegister));

        setupForm();
    }

    /**
     * Checks to see if a user is already signed in; if so, redirects them automatically to bypass
     * unnecessary login screen.
     */
    @Override
    public void onStart() {
        super.onStart();

        // If a user is already signed in, redirect them to MainFragment
        if (auth.getCurrentUser() != null) goTo(R.id.action_authToMain);
    }

    /**
     * Releases the binding when the view is destroyed.
     */
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        //
        binding = null;
    }

    /**
     * Initializes the observer for the sign-in form and adds an OnCompleteListener which handles
     * navigation on successful completion or alerts the user on failure.
     */
    private void setupForm() {
        mViewModel.startUp();
        // Setup the observer for the MutableLiveData field
        mViewModel.getBtnSignInClick().observe(getViewLifecycleOwner(), signInFields -> {
            auth.signInWithEmailAndPassword(signInFields.getEmail(), signInFields.getPassword())
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            // Successful sign in
                            Log.i(LOG_TAG, "sighInWithEmailAndPassword:success");
                            auth.getCurrentUser();
                            goTo(R.id.action_authToMain);
                        } else {
                            // Sign in failed
                            Log.i(LOG_TAG, "signInWithEmailAndPassword:fail");
                            snack(R.string.auth_fail);
                        }
                    });
            binding.progressBar.setVisibility(View.VISIBLE);
        });
    }

    /**
     * Convenience method for fragment based navigation.
     *
     * @param action the IdRes of the Navigation action (e.g., R.id.FirstFragmentToSecondFragment)
     */
    private void goTo(@IdRes int action) {
        Navigation.findNavController(binding.getRoot()).navigate(action);
    }

    // Convenience method to show messages on screen

    /**
     * Convenience method to show messages on screen.
     *
     * @param msg the StringRes of the message to show (e.g., R.string.message_to_show)
     */
    private void snack(@StringRes int msg) {
        Snackbar.make(binding.getRoot(), msg, Snackbar.LENGTH_LONG).show();
    }

}