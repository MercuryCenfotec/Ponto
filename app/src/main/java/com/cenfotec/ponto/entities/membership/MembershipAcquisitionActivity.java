package com.cenfotec.ponto.entities.membership;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.cenfotec.ponto.R;
import com.cenfotec.ponto.data.model.Membership;
import com.cenfotec.ponto.entities.account.AccountActivity;
import com.cenfotec.ponto.entities.account.NotEnoughFundsDialog;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import adapter.MembershipCard_Adapter;

public class MembershipAcquisitionActivity extends AppCompatActivity implements MembershipCard_Adapter.ItemClickListener, MembershipAcquisitionConfirmDialog.MembershipAcquireDialogListener, NotEnoughFundsDialog.GoToAccountNoFundsDialogListener {

    private MembershipCard_Adapter membershipCard_adapter;
    private RecyclerView recyclerView;
    private List<Membership> membershipList;
    private TextView activityTitle;
    private String membershipClicked = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        membershipList = new ArrayList<>();
        setContentView(R.layout.activity_membership_acquisition);
        activityTitle = findViewById(R.id.membershipActivityTitle);
        final SharedPreferences myPrefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String currentMembership = myPrefs.getString("userMembershipId", "none");
        Query membershipsQuery;
        if (currentMembership.equals("none")) {
            activityTitle.setText("Membresías");
            membershipsQuery = FirebaseDatabase.getInstance().getReference("Memberships").orderByChild("id");
        } else {
            activityTitle.setText("Mi membresía");
            membershipsQuery = FirebaseDatabase.getInstance().getReference("Memberships").orderByChild("id").equalTo(currentMembership);
        }
        recyclerView = findViewById(R.id.cardListView);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        membershipCard_adapter = new MembershipCard_Adapter(this, membershipList, this);

        membershipsQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                membershipList.clear();
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    membershipList.add(data.getValue(Membership.class));
                }
                membershipCard_adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                System.out.println("The memberships read failed: " + databaseError.getCode());
            }
        });

        membershipCard_adapter = new MembershipCard_Adapter(this,membershipList,this);
        recyclerView.setAdapter(membershipCard_adapter);
    }

    public void goBackToProfile(View view) {
        finish();
    }

    @Override
    public void onItemClicked(String itemId) {
        MembershipAcquisitionConfirmDialog confirmDialog = new MembershipAcquisitionConfirmDialog();
        confirmDialog.show(getSupportFragmentManager(), "membership acquisition dialog");
        membershipClicked = itemId;
    }

    @Override
    public void dialogConfirmAcquireMembership() {

        if (1000 >= 777) {
            NotEnoughFundsDialog notEnoughFundsDialog = new NotEnoughFundsDialog();
            notEnoughFundsDialog.show(getSupportFragmentManager(), "not enough funds dialog");
        }

    }

    @Override
    public void dialogNoFundsGoToAccount() {
        Intent intent = new Intent(this, AccountActivity.class);
//        intent.putExtra("userId", userId);
        startActivity(intent);
    }
}
