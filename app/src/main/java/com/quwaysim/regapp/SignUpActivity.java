package com.quwaysim.regapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.quwaysim.regapp.helpers.User;

import java.util.Objects;

public class SignUpActivity extends AppCompatActivity {
    EditText email, password, confirmPass;
    Button signUp;
    ProgressBar signUpProgressBar;
    TextView signIn;
//    FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        Objects.requireNonNull(getSupportActionBar()).hide();
        //widgets
        email = findViewById(R.id.email_editText);
        password = findViewById(R.id.password_editText);
        confirmPass = findViewById(R.id.confirmPass_editText);
        signUp = findViewById(R.id.signUp_btn);
        signUpProgressBar = findViewById(R.id.signUpProgressBar);
        signIn = findViewById(R.id.signIn_btn);

//        setUpFirebaseAuth();

        signIn.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignUpActivity.this, SignInActivity.class);
                startActivity(intent);
            }
        }));

        signUp.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!email.getText().toString().equals("") && !password.getText().toString().equals("") && !confirmPass.getText().toString().equals("")) {
                    if (password.getText().toString().equals(confirmPass.getText().toString())) {
                        registerNewUser(email.getText().toString(), password.getText().toString());
                        //redirectToSignIn(email.getText().toString(), password.getText().toString());
                    } else {
                        Toast.makeText(SignUpActivity.this, "Provided passwords do not match", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(SignUpActivity.this, "Please Fill all Fields!", Toast.LENGTH_SHORT).show();
                }
            }
        }));
    }


    private void registerNewUser(final String email, final String password) {
        showProgressBar();
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    User user = new User();
                    user.setEmail(email);
                    user.setPhone("+234");
                    user.setName(email.substring(0, email.indexOf("@")));
                    user.setUser_id(FirebaseAuth.getInstance().getCurrentUser().getUid());
                    user.setSecurityLevel(getIntent().getExtras().getString("Token_Value"));

                    FirebaseDatabase.getInstance().getReference()
                            .child(getString(R.string.users))
                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            FirebaseAuth.getInstance().signOut();
                            Toast.makeText(SignUpActivity.this, "Sign Up Successful", Toast.LENGTH_SHORT).show();
                            redirectToSignIn(email);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            FirebaseAuth.getInstance().signOut();
                            redirectToSignIn(email);
                            Toast.makeText(SignUpActivity.this, "Could not save details to DB", Toast.LENGTH_SHORT).show();
                        }
                    });
//                    redirectToSignIn(email, password);
                } else {
                    Toast.makeText(SignUpActivity.this, "Unable to Register, Something Went Wrong", Toast.LENGTH_LONG).show();
                }
                hideProgressBar();
            }
        });
    }

    private void redirectToSignIn(final String email) {
        Intent intent = new Intent(SignUpActivity.this, SignInActivity.class);
        intent.putExtra("Email", email);
        startActivity(intent);
        finish();
    }

    private void showProgressBar() {
        signUpProgressBar.setVisibility(View.VISIBLE);
    }

    private void hideProgressBar() {
        if (signUpProgressBar.getVisibility() == View.VISIBLE) {
            signUpProgressBar.setVisibility(View.INVISIBLE);
        }
    }
}
