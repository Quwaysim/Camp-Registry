package com.quwaysim.regapp;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.quwaysim.regapp.helpers.Participants;

public class RevenueActivity extends AppCompatActivity {
    TextView cashRevenue, bankRevenue;
    ProgressBar revenueProgressbar;
    private DatabaseReference dbRef;
    private String TAG = "RevenueActivity";
    private int revenue1;
    private int revenue2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_revenue);

        cashRevenue = findViewById(R.id.cashRevenue);
        bankRevenue = findViewById(R.id.bankRevenue);
        revenueProgressbar = findViewById(R.id.revenueProgressBar);

        dbRef = FirebaseDatabase.getInstance().getReference();

        updateCounts();

    }

    private void updateCounts() {
        showProgressBar();
        dbRef.child(getString(R.string.participants)).addValueEventListener((new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                hideProgressBar();

                revenue1 = 0;
                revenue2 = 0;

                for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
                    Participants participant = new Participants();
                    participant.setCashAmount(singleSnapshot.getValue(Participants.class).getCashAmount());
                    participant.setBankAmount(singleSnapshot.getValue(Participants.class).getBankAmount());
                    int bankMoney = Integer.parseInt(participant.getBankAmount());
                    int cashMoney = Integer.parseInt(participant.getCashAmount());

                    revenue1 += bankMoney;
                    revenue2 += cashMoney;

                }

                bankRevenue.setText("₦" + revenue1);
                cashRevenue.setText("₦" + revenue2);

                Log.d(TAG, "onDataChange:*** Status " + revenue1);
                Log.d(TAG, "onDataChange:*** Gender " + revenue2);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }

        }));

    }

    private void showProgressBar() {
        if(revenueProgressbar.getVisibility() == View.INVISIBLE){
            revenueProgressbar.setVisibility(View.VISIBLE);
        }
    }

    private void hideProgressBar(){
        revenueProgressbar.setVisibility(View.INVISIBLE);
    }
}
