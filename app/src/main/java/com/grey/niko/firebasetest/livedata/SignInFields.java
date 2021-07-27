package com.grey.niko.firebasetest.livedata;

import android.util.Patterns;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.ObservableField;

import com.grey.niko.firebasetest.BR;

/**
 * <h1>SignInFields</h1>
 * <p>Uses Observable data to update the UI to changes in the sign-in form. Utility methods to
 * validate form data make use of ObservableFields to expose logic results.</p>
 *
 * @version 1.0.0
 */
public class SignInFields extends BaseObservable {

    private String email;
    private String password;
    public ObservableField<Integer> emailError = new ObservableField<>();
    public ObservableField<Integer> passwordError = new ObservableField<>();

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
        notifyPropertyChanged(BR.valid);
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
        notifyPropertyChanged(BR.valid);
    }

    @Bindable
    public boolean isValid() {
        boolean valid = isEmailValid(false);
        valid = isPasswordValid(false) && valid;
        return valid;
    }

    /**
     * Tests the validity of the email form field. Uses Patterns matcher as the validation source.
     *
     * @return boolean for the validity of the email form field
     */
    public boolean isEmailValid(boolean setErrorMsg) {
        if (email == null) {
            // Nothing input, no error to display
            emailError.set(null);
            return false;
        }
        else if (Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            // No error to display
            emailError.set(null);
            return true;
        }
        else {
            // Does not meet requirements
            return false;
        }
    }

    /**
     * Tests the validity of the password form field. Uses the minimum password length as the
     * validation source.
     *
     * <p><b>Minimum password length:</b> 8 characters.</p>
     *
     * @return boolean for the validity of the password form field
     */
    public boolean isPasswordValid(boolean setErrorMsg) {
        if (password == null) {
            // Nothing input, no error to display
            passwordError.set(null);
            return false;
        }
        else if (password.length() >= 8) {
            // No error to display
            passwordError.set(null);
            return true;
        }
        else {
            // Does not meet requirements
            return false;
        }
    }
}
