package com.quwaysim.regapp.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.quwaysim.regapp.R;
import com.quwaysim.regapp.helpers.Participants;

import java.util.ArrayList;

public class DetailsAdapter extends RecyclerView.Adapter<DetailsAdapter.DetailsViewHolder> {

    private ArrayList<Participants> mDetails;

    public DetailsAdapter(ArrayList<Participants> details) {
        mDetails = details;
    }

    @NonNull
    @Override
    public DetailsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.participant_list_item, parent, false);
        return new DetailsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DetailsViewHolder holder, int position) {
        holder.bindDetails(mDetails.get(position));
    }

    @Override
    public int getItemCount() {
        return mDetails.size();
    }

    public class DetailsViewHolder extends RecyclerView.ViewHolder {

        public TextView mName, mPhone, mEmail, mSchool, mDept, mLevel, mGender, mStatus, mBankAmount, mCashAmount, mHouse;

        public DetailsViewHolder(@NonNull final View itemView){
            super(itemView);
            Log.d("TAG", "DetailsViewHolder: gotCalled");
            mName = itemView.findViewById(R.id.name);
            mPhone = itemView.findViewById(R.id.phone);
            mEmail = itemView.findViewById(R.id.email);
            mSchool = itemView.findViewById(R.id.school);
            mDept = itemView.findViewById(R.id.dept);
            mLevel = itemView.findViewById(R.id.level);
            mGender = itemView.findViewById(R.id.gender);
            mStatus = itemView.findViewById(R.id.status);
            mBankAmount = itemView.findViewById(R.id.bankAmount);
            mCashAmount = itemView.findViewById(R.id.cashAmount);
            mHouse = itemView.findViewById(R.id.house);
        }

        public void bindDetails(Participants details) {

            Log.d("TAG", "bindDetails: gotCalled");

            mName.setText(details.getName());
            mPhone.setText(details.getPhone());
            mEmail.setText(details.getEmail());
            mSchool.setText(details.getSchool());
            mDept.setText(details.getDept());
            mLevel.setText(details.getLevel());
            mGender.setText(details.getGender());
            mStatus.setText(details.getStatus());
            if (!details.getBankAmount().equals("0")) {
                mBankAmount.setText(details.getBankAmount());
            } else {
                mBankAmount.setText(" ");
            }
            if (!details.getCashAmount().equals("0")) {
                mCashAmount.setText(details.getCashAmount());
            } else {
                mCashAmount.setText(" ");
            }
            mHouse.setText(details.getHouse());
        }
    }
}
