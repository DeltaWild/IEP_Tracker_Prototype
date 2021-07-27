package com.grey.niko.firebasetest.viewmodel;

import android.view.View;
import android.widget.EditText;

import androidx.databinding.BindingAdapter;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseAuth;
import com.grey.niko.firebasetest.R;
import com.grey.niko.firebasetest.livedata.SignInFields;

public class AuthViewModel extends ViewModel {

    // Log tag for debug
    private final String LOG_TAG = "AuthViewModel";
    // Firebase Auth reference
    FirebaseAuth auth = FirebaseAuth.getInstance();
    // Observable live data fields for sign in
    public SignInFields signIn;
    // Focus change listeners for sign in fields for live error reporting
    private View.OnFocusChangeListener onFocusEmail;
    private View.OnFocusChangeListener onFocusPassword;
    // MutableLiveData for user event btnSignIn click
    private MutableLiveData<SignInFields> btnSignInClick = new MutableLiveData<>();

    // Initialize the sign in fields and their listeners
    public void startUp() {
        signIn = new SignInFields();

        // Define the focus changed listeners to only call the validation methods
        // if the user has input text and removed focus from the field
        onFocusEmail = (v, haveFocus) -> {
            EditText etEmail = (EditText) v;
            if (etEmail.getText().length() > 0 && !haveFocus) {
                signIn.isEmailValid(true);
                if (!signIn.isEmailValid(true)) {
                    signIn.emailError.set(R.string.invalid_email);
                }
            }
        };
        onFocusPassword = (v, haveFocus) -> {
            EditText etPassword = (EditText) v;
            if (etPassword.getText().length() > 0 && !haveFocus) {
                signIn.isPasswordValid(true);
                if (!signIn.isPasswordValid(true)) {
                    signIn.passwordError.set(R.string.password_char_len);
                }
            }
        };
    }

    // Getters
    public SignInFields getSignIn() {
        return signIn;
    }

    public View.OnFocusChangeListener getEmailOnFocusChangeListener() {
        return onFocusEmail;
    }

    public View.OnFocusChangeListener getPasswordOnFocusChangeListener() {
        return onFocusPassword;
    }

    // Assignment of validated form data to MutableLiveData
    public void onBtnSignInClick() {
        if (signIn.isValid()) {
            btnSignInClick.setValue(signIn);
        }
    }

    // Observer-exposed component of LiveData for sign in form
    public MutableLiveData<SignInFields> getBtnSignInClick() {
        return btnSignInClick;
    }

    // Use binding adapters to allow XML properties to observe LiveData changes
    @BindingAdapter("error")
    public static void setError(EditText et, Object stringOrResId) {
        if (stringOrResId instanceof Integer) {
            et.setError(et.getContext().getString((Integer) stringOrResId));
        }
        else {
            et.setError((String) stringOrResId);
        }
    }

    @BindingAdapter("onFocus")
    public static void bindFocusChange(EditText et, View.OnFocusChangeListener fcl) {
        if (et.getOnFocusChangeListener() == null) {
            et.setOnFocusChangeListener(fcl);
        }
    }
}