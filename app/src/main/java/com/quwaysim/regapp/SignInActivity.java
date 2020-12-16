package com.quwaysim.regapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class SignInActivity extends AppCompatActivity {
    EditText email, password;
    Button signIn;
    ProgressBar progressBar;
//    FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        Objects.requireNonNull(getSupportActionBar()).hide();
        //widgets
        email = findViewById(R.id.email_editText);
        password = findViewById(R.id.password_editText);
        signIn = findViewById(R.id.signUp_btn);
        progressBar = findViewById(R.id.LoginProgressBar);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String result = extras.getString("Email");
            email.setText(result);
            Log.d("TAG", "onCreate: ++- it works" + result );
        }

        signIn.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!email.getText().toString().equals("") && !password.getText().toString().equals("")) {
                    showProgressBar();
                    FirebaseAuth.getInstance().signInWithEmailAndPassword(email.getText().toString(), password.getText().toString()).addOnCompleteListener((new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
//                            if (task.isSuccessful() && task.getResult() != null) {
//                            }
                        }
                    })).addOnFailureListener((new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            hideProgressBar();
                            Toast.makeText(SignInActivity.this, "Failed to Sign In", Toast.LENGTH_SHORT).show();
                        }
                    })).addOnSuccessListener((new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            hideProgressBar();
                            Toast.makeText(SignInActivity.this, "Signed In", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(SignInActivity.this, DashboardActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }));
                } else {
                    Toast.makeText(SignInActivity.this, "please fill all fields", Toast.LENGTH_SHORT).show();
                }
            }
        }));
    }

    private void showProgressBar() {
        progressBar.setVisibility(View.VISIBLE);
    }

    private void hideProgressBar() {
        if (progressBar.getVisibility() == View.VISIBLE) {
            progressBar.setVisibility(View.INVISIBLE);
        }
    }
}
