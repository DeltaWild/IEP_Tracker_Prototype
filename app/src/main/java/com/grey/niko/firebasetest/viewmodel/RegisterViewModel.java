package com.grey.niko.firebasetest.viewmodel;

import android.view.View;
import android.widget.EditText;

import androidx.databinding.BindingAdapter;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.grey.niko.firebasetest.R;
import com.grey.niko.firebasetest.livedata.RegisterFields;

public class RegisterViewModel extends ViewModel {

    // Log tag for debug
    private final String LOG_TAG = "RegisterViewModel";
    // Observable live data fields for register
    public RegisterFields register;
    // Focus change listeners for registration fields for live error reporting
    private View.OnFocusChangeListener onFocusEmail;
    private View.OnFocusChangeListener onFocusPassword;
    private View.OnFocusChangeListener onFocusPasswordConfirm;
    // MutableLiveData for user event btnRegister click
    private MutableLiveData<RegisterFields> btnRegisterClick = new MutableLiveData<>();

    // Initialize the registration fields and their listeners
    public void startUp() {
        register = new RegisterFields();

        // Define the focus changed listeners
        onFocusEmail = (v, haveFocus) -> {
            EditText etEmail = (EditText) v;
            if (etEmail.getText().length() > 0 && !haveFocus) {
                register.isEmailValid();
                if (!register.isEmailValid()) {
                    register.emailError.set(R.string.invalid_email);
                }
            }
        };
        onFocusPassword = (v, haveFocus) -> {
            EditText etPassword = (EditText) v;
            if (etPassword.getText().length() > 0 && !haveFocus) {
                register.isPasswordValid();
            }
        };
        onFocusPasswordConfirm = (v, haveFocus) -> {
            EditText etPasswordConfirm = (EditText) v;
            if (etPasswordConfirm.getText().length() > 0 && !haveFocus) {
                register.isPasswordValid();
            }
        };
    }

    // Getters
    public RegisterFields getRegisterFields() {
        return register;
    }

    public View.OnFocusChangeListener getEmailOnFocusChangeListener() {
        return onFocusEmail;
    }

    public View.OnFocusChangeListener getPasswordOnFocusChangeListener() {
        return onFocusPassword;
    }

    public View.OnFocusChangeListener getPasswordConfirmOnFocusChangeListener() {
        return onFocusPasswordConfirm;
    }

    // Assignment of validated form data to MutableLiveData
    public void onBtnRegisterClick() {
        if (register.isValid()) {
            btnRegisterClick.setValue(register);
        }
    }

    // Observer-exposed component of LiveData for register form
    public MutableLiveData<RegisterFields> getBtnRegisterClick() {
        return btnRegisterClick;
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