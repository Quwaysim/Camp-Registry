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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class TokenActivity extends AppCompatActivity {

    private EditText edit;
    private Button verify;
    private DatabaseReference dbRef;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private String TAG = "TokenActivity";
    private String dbTokenAdmin, dbTokenUser;
    private ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_token);
        Objects.requireNonNull(getSupportActionBar()).hide();

        edit = findViewById(R.id.token_editText);
        verify = findViewById(R.id.verify);
        mProgressBar = findViewById(R.id.TokenProgressBar);

        edit.setEnabled(false);
//        edit.setInputType(InputType.TYPE_NULL);

        //TODO I uncommented this ff line
//        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        dbRef = FirebaseDatabase.getInstance().getReference();

        Toast.makeText(TokenActivity.this, "Fetching Tokens, Please Wait...", Toast.LENGTH_LONG).show();

        retrieveTokens();
        setUpFirebaseAuth();

        verify.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (edit.getText().toString().equals(dbTokenAdmin)) {
                    Intent intent = new Intent(TokenActivity.this, SignUpActivity.class);
                    intent.putExtra("Token_Value", "2");
                    startActivity(intent);
                    finish();
                    Toast.makeText(TokenActivity.this, "ADMIN LOGIN CONFIRMED", Toast.LENGTH_SHORT).show();
                } else if (edit.getText().toString().equals(dbTokenUser)) {
                    Intent intent = new Intent(TokenActivity.this, SignUpActivity.class);
                    intent.putExtra("Token_Value", "1");
                    startActivity(intent);
                    finish();
                    Toast.makeText(TokenActivity.this, "USER LOGIN CONFIRMED", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(TokenActivity.this, "Wrong Token or No Internet Connection", Toast.LENGTH_SHORT).show();
                }
            }
        }));
    }

    private void setUpFirebaseAuth() {
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    Toast.makeText(TokenActivity.this, "Signed in as " + user.getEmail(), Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(TokenActivity.this, DashboardActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                    finish();
                }
            }
        };
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseAuth.getInstance().addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            FirebaseAuth.getInstance().removeAuthStateListener(mAuthListener);
        }
    }

    private void retrieveTokens() {

        showProgressbar();
        // Read Admin Token from the database
        dbRef.child("Tokens").child("Admin").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                dbTokenAdmin = dataSnapshot.getValue(String.class);
                Log.d(TAG, "Value is: " + dbTokenAdmin);
                hideProgressBar();
                edit.setEnabled(true);
//                edit.setInputType(InputType.TYPE_TEXT_VARIATION_WEB_PASSWORD);
                edit.setFocusable(true);
                edit.requestFocus();
                Toast.makeText(TokenActivity.this, "Tokens Fetched, Please Input the Token", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

        showProgressbar();
        //Read User Token from the Database
        dbRef.child("Tokens").child("User").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                dbTokenUser = dataSnapshot.getValue(String.class);
                Log.d(TAG, "Value is: " + dbTokenUser);
                hideProgressBar();
                edit.setEnabled(true);
//                edit.setInputType(InputType.TYPE_TEXT_VARIATION_WEB_PASSWORD);
                edit.setFocusable(true);
                edit.requestFocus();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

    }

    public void showProgressbar(){
        if (mProgressBar.getVisibility() == View.INVISIBLE){
            mProgressBar.setVisibility(View.VISIBLE);
        }
    }

    public void hideProgressBar(){
        mProgressBar.setVisibility(View.INVISIBLE);
    }

}
