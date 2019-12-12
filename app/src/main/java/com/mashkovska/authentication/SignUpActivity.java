package com.mashkovska.authentication;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class SignUpActivity extends AppCompatActivity {
    private EditText emailId;
    private EditText password;
    private EditText name;
    private EditText phoneNumber;
    private Button signUp;
    private TextView signIn;
    private FirebaseAuth mFirebaseAuth;
    private static Integer minLength = 6;
    private boolean successValidation = true;

    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mFirebaseAuth = FirebaseAuth.getInstance();
        emailId = findViewById(R.id.sign_up_screen_email);
        password = findViewById(R.id.sign_up_screen_password);
        name = findViewById(R.id.sign_up_screen_name);
        phoneNumber = findViewById(R.id.sign_up_screen_phone);
        signUp = findViewById(R.id.sign_up_screen_button);
        signIn = findViewById(R.id.sign_up_screen_login);


        signUp.setOnClickListener(view -> {
            final String yourName = name.getText().toString().trim();
            final String phone = phoneNumber.getText().toString().trim();
            final String email = emailId.getText().toString().trim();
            final String pwd = password.getText().toString().trim();


            if (Validation(yourName, phone, email, pwd)) {
                mFirebaseAuth.createUserWithEmailAndPassword(email, pwd)
                        .addOnCompleteListener(SignUpActivity.this, task -> {
                            if (task.isSuccessful()) {
                                FirebaseUser user = mFirebaseAuth.getCurrentUser();
                                UserProfileChangeRequest userUpdateProfile = new UserProfileChangeRequest
                                        .Builder().setDisplayName(yourName).build();
                                user.updateProfile(userUpdateProfile)
                                        .addOnCompleteListener(task1 -> {
                                            if (task1.isSuccessful()) {
                                                Intent intToHome = new Intent(this,
                                                        WelcomeActivity.class);
                                                intToHome.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                                                        | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                                startActivity(intToHome);
                                                }
                                            });
                                        } else {
                                Toast.makeText(SignUpActivity.this, "SignUp Unsuccessful",
                                        Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });

        signIn.setOnClickListener(view -> {
            Intent i = new Intent(SignUpActivity.this, LoginActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(i);
        });
    }

    public boolean Validation(String yourName, String phone, String email, String pwd) {
        if (yourName.isEmpty()) {
            name.setError(getString(R.string.enter_name));
            name.requestFocus();
            return false;
        } else if (phone.isEmpty()) {
            phoneNumber.setError(getString(R.string.enter_phone));
            phoneNumber.requestFocus();
            return false;

        } else if (phone.length() != 10) {
            phoneNumber.setError(getString(R.string.input_error_phone_invalid));
            phoneNumber.requestFocus();
            return false;
        } else if (email.isEmpty()) {
            emailId.setError(getString(R.string.enter_email));
            emailId.requestFocus();
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailId.setError(getString(R.string.input_error_email_invalid));
            emailId.requestFocus();
            return false;
        } else if (pwd.isEmpty()) {
            password.setError(getString(R.string.enter_pwd));
            password.requestFocus();
            return false;
        } else if (password.length() < minLength) {
            password.setError(getString(R.string.minimum_length));
            password.requestFocus();
            return false;
        }

        return successValidation;
    }


    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }
}