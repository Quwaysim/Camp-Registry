package com.quwaysim.regapp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.quwaysim.regapp.adapters.DetailsAdapter;
import com.quwaysim.regapp.helpers.Participants;

import java.util.ArrayList;

public class ParticipantsDetails extends AppCompatActivity {
    ArrayList<Participants> list;
    DetailsAdapter adapter;
    private DatabaseReference dbRef;
    private String TAG = "Participants' Details";
    private RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_participants_details);

        mRecyclerView = findViewById(R.id.rv_row);
        dbRef = FirebaseDatabase.getInstance().getReference();

        //TODO order by key
        dbRef.child(getString(R.string.participants)).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list = new ArrayList<>();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    Participants participants = dataSnapshot1.getValue(Participants.class);
                    list.add(participants);
                }
                adapter = new DetailsAdapter(list);
                mRecyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        mRecyclerView = findViewById(R.id.rv_row);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

}
