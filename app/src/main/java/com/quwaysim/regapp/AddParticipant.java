package com.quwaysim.regapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.quwaysim.regapp.helpers.Participants;

import java.util.Random;

public class AddParticipant extends AppCompatActivity {
    EditText name, email, phone, dept;
    AutoCompleteTextView school, amountDropdown;
    Spinner level, house, paymentType, paymentMode;
    RadioButton male, female, paid, partial, notPaid;
    Button save, back;
    RadioGroup paymentModes, gender;

    String selectType, selectLevel, selectHouse;
    private DatabaseReference dbRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_participant);

        //referencing the widgets
        name = findViewById(R.id.name_editText);
        email = findViewById(R.id.email_editText);
        phone = findViewById(R.id.number_editText);
        school = findViewById(R.id.school_editText);
        dept = findViewById(R.id.dept_editText);
        level = findViewById(R.id.spinner);
        house = findViewById(R.id.houses_spinner);
        male = findViewById(R.id.male);
        female = findViewById(R.id.female);
        paid = findViewById(R.id.paid_btn);
        partial = findViewById(R.id.partial_btn);
        notPaid = findViewById(R.id.not_paid_btn);
        amountDropdown = findViewById(R.id.amount);
        paymentType = findViewById(R.id.payment_type_spinner);
        paymentModes = findViewById(R.id.payment_modes_radioGroup);
        paymentMode = findViewById(R.id.payment_mode);
        gender = findViewById(R.id.gender_radioGroup);
        save = findViewById(R.id.save);
        back = findViewById(R.id.back);

        selectType = "Select Payment Mode";
        selectLevel = "Select Level";
        selectHouse = "Select House";

        autoSuggest();
        paymentLogic();

        dbRef = FirebaseDatabase.getInstance().getReference();

        save.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendDetailsToDB();
            }
        }));

        back.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AddParticipant.this, DashboardActivity.class));
            }
        }));
    }

    private void sendDetailsToDB() {
        if (!name.getText().toString().equals("") && !email.getText().toString().equals("")
                && !phone.getText().toString().equals("") && !school.getText().toString().equals("")
                && !dept.getText().toString().equals("") && !level.getSelectedItem().toString().equals(selectLevel)
                && (!amountDropdown.getText().toString().equals(""))
                && (paid.isChecked() || notPaid.isChecked() || partial.isChecked())
                && (male.isChecked() || female.isChecked())
                && !paymentMode.getSelectedItem().equals(selectType)
                && !house.getSelectedItem().toString().equals(selectHouse)) {

            Random r = new java.util.Random();
            String s = Long.toString(r.nextLong() & Long.MAX_VALUE, 36);
//          FirebaseUser user = firebaseAuth.getCurrentUser();

            Participants participant = new Participants();

            participant.setName(name.getText().toString());
            participant.setEmail(email.getText().toString());
            participant.setPhone(phone.getText().toString());
            participant.setSchool(school.getText().toString());
            participant.setDept(dept.getText().toString());
            participant.setLevel(level.getSelectedItem().toString());
            participant.setHouse(house.getSelectedItem().toString());

            if (male.isChecked()) {
                participant.setGender("Male");
            } else {
                participant.setGender("Female");
            }

            if (paid.isChecked() && paymentMode.getSelectedItem().toString().equals("Bank")) {
                participant.setStatus("Paid: Bank");
                participant.setBankAmount(amountDropdown.getText().toString());
                participant.setCashAmount("0");
            } else if (paid.isChecked() && paymentMode.getSelectedItem().toString().equals("Cash")) {
                participant.setStatus("Paid: Cash");
                participant.setCashAmount(amountDropdown.getText().toString());
                participant.setBankAmount("0");
            } else if (partial.isChecked() && paymentMode.getSelectedItem().toString().equals("Bank")) {
                participant.setStatus("Partially Paid: Bank");
                participant.setBankAmount(amountDropdown.getText().toString());
                participant.setCashAmount("0");
            } else if (partial.isChecked() && paymentMode.getSelectedItem().toString().equals("Cash")) {
                participant.setStatus("Partially Paid: Cash");
                participant.setCashAmount(amountDropdown.getText().toString());
                participant.setBankAmount("0");
            } else if (notPaid.isChecked()) {
                participant.setStatus("Not Paid");
                participant.setCashAmount(amountDropdown.getText().toString());
                participant.setBankAmount(amountDropdown.getText().toString());
            }

//          participant.setBankAmount(amountDropdown.getText().toString());

            participant.setReg_by(FirebaseAuth.getInstance().getCurrentUser().getEmail());

            dbRef.child(getString(R.string.participants)).child(s).setValue(participant)

                    .addOnSuccessListener((new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(AddParticipant.this, "Successfully Uploaded", Toast.LENGTH_SHORT).show();

                            resetForm();

                        }
                    }))
                    .addOnFailureListener((new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(AddParticipant.this, "Failed! Something went wrong", Toast.LENGTH_SHORT).show();
                        }
                    }));
        } else {
            Toast.makeText(AddParticipant.this, "Please, fill all fields!", Toast.LENGTH_SHORT).show();
        }
    }

    public void resetForm() {
        name.setText("");
        email.setText(getString(R.string.gmail_com));
        phone.setText("");
        school.setText("");
        dept.setText("");
        paymentModes.clearCheck();
        gender.clearCheck();
        paymentType.setSelection(0);
        level.setSelection(0);
        paymentMode.setSelection(0);
        house.setSelection(0);

        name.setFocusable(true);
        name.requestFocus();
    }

    private void paymentLogic() {

        paymentModes.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (i == R.id.paid_btn) {
                    paymentType.setEnabled(true);
                    amountDropdown.setEnabled(false);
                    if (paymentType.getSelectedItem().toString().equals("Early Bird")) {
                        amountDropdown.setText(R.string.early_bird);
                    } else if (paymentType.getSelectedItem().toString().equals("Normal")) {
                        amountDropdown.setText(R.string.normal);
                    }
                } else if (i == R.id.partial_btn) {
                    amountDropdown.setText("");
                    amountDropdown.setEnabled(true);
                    paymentType.setEnabled(false);
                } else {
                    amountDropdown.setText("0");
                    amountDropdown.setEnabled(false);
                    paymentType.setEnabled(false);
                }
            }
        });

        paymentType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {
                    amountDropdown.setText(R.string.early_bird);
                } else {
                    amountDropdown.setText(R.string.normal);
                }
            }

            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        paymentMode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 1) {
                    paid.setChecked(true);
                    notPaid.setEnabled(false);
                    partial.setEnabled(false);
                } else if (i == 2) {
                    paid.setEnabled(true);
                    notPaid.setEnabled(true);
                    partial.setEnabled(true);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    private void autoSuggest() {
        //schools
        String[] schools = getResources().getStringArray(R.array.schools);
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, schools);
        school.setAdapter(adapter);

        //mAmount
        String[] amount = getResources().getStringArray(R.array.amount);
        ArrayAdapter<String> adapter1 = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, amount);
        amountDropdown.setAdapter(adapter1);
    }
}