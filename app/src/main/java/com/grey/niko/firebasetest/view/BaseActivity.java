package com.grey.niko.firebasetest.view;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.navigation.Navigation;

import com.firebase.ui.auth.AuthUI;
import com.grey.niko.firebasetest.R;
import com.grey.niko.firebasetest.databinding.ActivityBaseBinding;

public class BaseActivity extends AppCompatActivity {

    // Log tag for debug
    private final String LOG_TAG = "BaseActivity";
    // Binding variable declared globally for use across activity
    private ActivityBaseBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Load any saved instances
        super.onCreate(savedInstanceState);
        // Access XML file associated with this Java class via binding variable
        binding = ActivityBaseBinding.inflate(getLayoutInflater());
        // Attach binding to root container to be displayed to user
        setContentView(binding.getRoot());
        // Bottom navigation displayed by default
        hideBottomNav(false);
    }

    @Override
    protected void onStart() {
        super.onStart();

        binding.bottomNav.setOnItemSelectedListener(item -> {
                    switch (item.getItemId()) {
                        case R.id.navStudentList:
                            Navigation.findNavController(this, R.id.nav_host_fragment).navigate(R.id.mainFragment);
                            return true;
                        case R.id.navTeamMembers:
                            Navigation.findNavController(this, R.id.nav_host_fragment).navigate(R.id.teamMemberFragment);
                            return true;
                        case R.id.navLogout:
                            AuthUI.getInstance()
                                    .signOut(this)
                                    .addOnCompleteListener(task ->
                                            Navigation.findNavController(this, R.id.nav_host_fragment).navigate(R.id.authFragment));
                            return true;
                        case R.id.toggleDarkMode:
                            toggleDarkMode();
                            break;
                    }
                    return true;
                }
        );
    }

    void toggleDarkMode() {
        int isDarkMode = AppCompatDelegate.getDefaultNightMode();
        if (isDarkMode != AppCompatDelegate.MODE_NIGHT_YES) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }
        else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
    }

    public void hideBottomNav(boolean isHidden) {
        binding.bottomNav.setVisibility(isHidden ? View.GONE : View.VISIBLE);
    }

}