package com.quwaysim.regapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.quwaysim.regapp.helpers.Participants;

public class DashboardActivity extends AppCompatActivity {
    ProgressBar mProgressBar;
    CardView revenueCard;
    private FloatingActionButton fab;
    private TextView revenueTxt, total, paid, partiallyPaid, unPaid, males, females, forFun, show, cover;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference dbRef;
    private DatabaseReference dbRef2;
    private String TAG = "DashboardActivity";
    private int paidNo;
    private int partiallyPaidNo;
    private int unpaidNo;
    private int maleNo;
    private int femaleNo;
    private int revenue;
    private int revenue2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        //widgets
        fab = findViewById(R.id.fab);
        total = findViewById(R.id.total_textView);
        paid = findViewById(R.id.no_of_paid);
        partiallyPaid = findViewById(R.id.no_of_partiallyPaid);
        unPaid = findViewById(R.id.no_of_unpaid);
        males = findViewById(R.id.no_of_males);
        females = findViewById(R.id.no_of_females);
        forFun = findViewById(R.id.for_fun);
        mProgressBar = findViewById(R.id.progressBar);
        revenueTxt = findViewById(R.id.revenue);
        show = findViewById(R.id.show_textView);
        cover = findViewById(R.id.revenue_cover);
        revenueCard = findViewById(R.id.revenue_cardView);

        revenueTxt.setVisibility(View.INVISIBLE);
        cover.setVisibility(View.VISIBLE);
//        revenueTxt.setText("NGN XX,XXX");

        //Refs
        dbRef = FirebaseDatabase.getInstance().getReference();
        dbRef2 = FirebaseDatabase.getInstance().getReference();

        setUpFirebaseAuth();
        updateCounts();
        queryDB();
        fun();

        revenueCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DashboardActivity.this, RevenueActivity.class));
            }
        });

        fab.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DashboardActivity.this, AddParticipant.class));
                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
            }
        }));

        total.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DashboardActivity.this, ParticipantsDetails.class));
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        }));

        show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (revenueTxt.getVisibility() == View.INVISIBLE) {
                    revenueTxt.setVisibility(View.VISIBLE);
                    cover.setVisibility(View.INVISIBLE);
                    show.setText("HIDE");

                } else {
                    revenueTxt.setVisibility(View.INVISIBLE);
                    cover.setVisibility(View.VISIBLE);
                    show.setText("SHOW");
                }
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.sign_out) {
            Toast.makeText(DashboardActivity.this, "Signed out", Toast.LENGTH_SHORT).show();
            FirebaseAuth.getInstance().signOut();
        }
        return super.onOptionsItemSelected(item);

    }

    private void setUpFirebaseAuth() {
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user == null) {
                    startActivity(new Intent(DashboardActivity.this, TokenActivity.class));
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

    private void updateCounts() {
        showProgressBar();
        dbRef.child(getString(R.string.participants)).addValueEventListener((new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int totalCount = (int) dataSnapshot.getChildrenCount();
                DashboardActivity.this.total.setText(totalCount + "");
                hideProgressBar();

                paidNo = 0;
                partiallyPaidNo = 0;
                unpaidNo = 0;
                femaleNo = 0;
                maleNo = 0;
                revenue = 0;
                revenue2 = 0;

                for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
                    Participants participant = new Participants();
                    participant.setStatus(singleSnapshot.getValue(Participants.class).getStatus());
                    participant.setGender(singleSnapshot.getValue(Participants.class).getGender());
                    participant.setBankAmount(singleSnapshot.getValue(Participants.class).getBankAmount());
                    participant.setCashAmount(singleSnapshot.getValue(Participants.class).getCashAmount());

                    int money = Integer.parseInt(participant.getBankAmount());
                    int money2 = Integer.parseInt(participant.getCashAmount());

                    Log.d(TAG, "onDataChange:*** " + money);

                    if (participant.getStatus().equals("Paid: Cash") || participant.getStatus().equals("Paid: Bank")) {
                        paidNo += 1;
                    } else if (participant.getStatus().equals("Not Paid")) {
                        unpaidNo += 1;
                    } else if (participant.getStatus().equals("Partially Paid: Cash") || participant.getStatus().equals("Partially Paid: Bank")) {
                        partiallyPaidNo += 1;
                    }

                    if (participant.getGender().equals("Male")) {
                        maleNo += 1;
                    } else {
                        femaleNo += 1;
                    }

                    revenue += money;
                    revenue2 += money2;

                }
                int totalRevenue = revenue + revenue2;
                revenueTxt.setText("₦" + totalRevenue);
                males.setText(maleNo + "");
                females.setText(femaleNo + "");
                paid.setText(paidNo + "");
                partiallyPaid.setText(partiallyPaidNo + "");
                unPaid.setText(unpaidNo + "");

                Log.d(TAG, "onDataChange:*** Status " + paidNo + " && " + unpaidNo);
                Log.d(TAG, "onDataChange:*** Gender " + maleNo + " && " + femaleNo);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        }));

    }

    private void fun() {
        dbRef2.child("Fun").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String value = dataSnapshot.getValue(String.class);
                forFun.setText(value);

                Log.d(TAG, "onDataChange:--* " + value);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void queryDB() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        Query query = reference.child(getString(R.string.users))
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("securityLevel");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String secLevel = dataSnapshot.getValue(String.class);
                if (secLevel.equals("2")) {
                    fab.show();
                }
                Log.d(TAG, "onDataChange: +-+ " + secLevel);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

    }

    private void showProgressBar() {
        mProgressBar.setVisibility(View.VISIBLE);
    }

    private void hideProgressBar() {
        if (mProgressBar.getVisibility() == View.VISIBLE) {
            mProgressBar.setVisibility(View.INVISIBLE);
        }
    }
}