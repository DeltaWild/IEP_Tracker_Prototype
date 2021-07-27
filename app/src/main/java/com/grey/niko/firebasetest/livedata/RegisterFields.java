package com.grey.niko.firebasetest.livedata;

import android.util.Patterns;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.ObservableField;

import com.grey.niko.firebasetest.BR;
import com.grey.niko.firebasetest.R;

/**
 * <h1>RegisterFields</h1>
 * <p>Uses Observable data to update the UI to changes in the registration form. Utility methods to
 * validate form data make use of ObservableFields to expose logic results.</p>
 *
 * @version 1.0.0
 */
public class RegisterFields extends BaseObservable {

    private String email;
    private String password;
    private String passwordConfirm;
    public ObservableField<Integer> emailError = new ObservableField<>();
    public ObservableField<Integer> passwordError = new ObservableField<>();
    public ObservableField<Integer> passwordConfirmError = new ObservableField<>();

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

    public String getPasswordConfirm() {
        return passwordConfirm;
    }

    public void setPasswordConfirm(String passwordConfirm) {
        this.passwordConfirm = passwordConfirm;
        notifyPropertyChanged(BR.valid);
    }

    /**
     * Getter for the combined boolean value of isEmailValid and isPasswordValid as a bindable for UI
     * elements to test against.
     *
     * @return boolean for the validity of the form fields
     */
    @Bindable
    public boolean isValid() {
        boolean valid = isEmailValid();
        valid = isPasswordValid() && valid;
        return valid;
    }

    /**
     * Tests the validity of the email form field. Uses Patterns matcher as the validation source.
     *
     * @return boolean for the validity of the email form field
     */
    public boolean isEmailValid() {
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
     * <p>Tests whether the password form field and password confirmation form field hold identical
     * values and whether those fields are valid against declared password rules. Uses a RegEx
     * String as the validation source.</p>
     *
     * <p><b>Password rules:</b> 1+ digit, 1+ lowercase alpha, 1+ uppercase alpha, 1+ special char,
     * no whitespace, 8+ total chars.</p>
     *
     * @return boolean for the validity and conformity of the password and password confirmation
     * form fields
     */
    public boolean isPasswordValid() {
        String passRules = "(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{8,}";
        if (password == null && passwordConfirm == null) {
            // Nothing input, no error to display
            passwordError.set(null);
            passwordConfirmError.set(null);
            return false;
        }
        else if (password != null && !password.matches(passRules) && password.equals(passwordConfirm)) {
            // Passwords match but do not meet the security reqs
            passwordError.set(R.string.weak_password);
            passwordConfirmError.set(R.string.weak_password);
            return false;
        }
        else if (password != null && password.matches(passRules) && !password.equals(passwordConfirm)) {
            // Password matches rules but confirming password is wrong
            passwordError.set(null);
            passwordConfirmError.set(R.string.mismatched_passwords);
            return false;
        }
        else if (password != null && password.matches(passRules) && password.equals(passwordConfirm)) {
            // Password is valid and confirmed
            passwordError.set(null);
            passwordConfirmError.set(null);
            return true;
        }
        else {
            // Password does not meet security reqs and confirming password is wrong
            passwordError.set(R.string.weak_password);
            passwordConfirmError.set(R.string.mismatched_passwords);
            return false;
        }
    }
}
