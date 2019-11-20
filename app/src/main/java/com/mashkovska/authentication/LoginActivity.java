package com.mashkovska.authentication;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {
    private EditText emailId;
    private EditText password;
    private Button btnSignIn;
    private TextView tvSignUp;
    private FirebaseAuth auth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        auth = FirebaseAuth.getInstance();
        emailId = findViewById(R.id.login_screen_email);
        password = findViewById(R.id.login_screen_password);
        btnSignIn = findViewById(R.id.login_screen_login);
        tvSignUp = findViewById(R.id.login_screen_sign_up);

//        mAuthStateListener = firebaseAuth -> {
//            FirebaseUser mFirebaseUser = auth.getCurrentUser();
//            if (mFirebaseUser != null) {
////                Toast.makeText(LoginActivity.this, "You are logged in",
////                        Toast.LENGTH_SHORT).show();
//            } else {
//                Toast.makeText(LoginActivity.this, "Please login",
//                        Toast.LENGTH_SHORT).show();
//            }
//
//        };

        btnSignIn.setOnClickListener(view -> {
            String email = emailId.getText().toString();
            String pwd = password.getText().toString();
            if (validationLogginFields(email, pwd)) {
                auth.signInWithEmailAndPassword(email, pwd).addOnCompleteListener
                        (LoginActivity.this, task -> {
                            if (!task.isSuccessful()) {
                                Toast.makeText(LoginActivity.this,
                                        "Login Error, Please login Again!",
                                        Toast.LENGTH_SHORT).show();
                            } else {
                                Intent intToHome = new Intent(LoginActivity.this,
                                        WelcomeActivity.class);
                                intToHome.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                                        | Intent.FLAG_ACTIVITY_SINGLE_TOP
                                        | Intent.FLAG_ACTIVITY_NO_HISTORY);
                                startActivity(intToHome);
                            }
                        });
            } else {
                Toast.makeText(LoginActivity.this, "Error Occurred!",
                        Toast.LENGTH_SHORT).show();
            }
        });

        tvSignUp.setOnClickListener(view -> {
            Intent intSignUp = new Intent(LoginActivity.this, SignUpActivity.class);
            intSignUp.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP
                    | Intent.FLAG_ACTIVITY_NO_HISTORY);
            startActivity(intSignUp);
        });
    }

    private boolean validationLogginFields(String email, String pwd){
        if (email.isEmpty()) {
            emailId.setError("Please enter email id");
            emailId.requestFocus();
            return  false;
        } else if (pwd.isEmpty()) {
            password.setError("Please enter your password");
            password.requestFocus();
            return  false;
        } else if (email.isEmpty() && pwd.isEmpty()) {
            Toast.makeText(LoginActivity.this, "Field are empty",
                    Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }



//    @Override
//    protected void onStart() {
//        super.onStart();
//        auth.addAuthStateListener(mAuthStateListener);
//    }
}




