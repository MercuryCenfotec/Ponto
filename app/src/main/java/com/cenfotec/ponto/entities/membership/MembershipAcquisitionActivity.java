package com.cenfotec.ponto.entities.membership;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
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
import com.cenfotec.ponto.data.model.Account;
import com.cenfotec.ponto.data.model.Membership;
import com.cenfotec.ponto.data.model.User;
import com.cenfotec.ponto.entities.account.AccountActivity;
import com.cenfotec.ponto.entities.account.NotEnoughFundsDialog;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
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
    public static final String MY_PREFERENCES = "MyPrefs";
    DatabaseReference userDBRef;
    DatabaseReference accountDBRef;
    User signedUser;
    Account userAccount;
    Membership selectedMembership;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userDBRef = FirebaseDatabase.getInstance().getReference("Users");
        accountDBRef = FirebaseDatabase.getInstance().getReference("Accounts");
        membershipList = new ArrayList<>();
        setContentView(R.layout.activity_membership_acquisition);
        activityTitle = findViewById(R.id.membershipActivityTitle);
        final SharedPreferences myPrefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String currentMembership = myPrefs.getString("userMembershipId", "none");
        Query membershipsQuery;
        if (currentMembership.equals("")) {
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
    public void onItemClicked(Membership item) {
        MembershipAcquisitionConfirmDialog confirmDialog = new MembershipAcquisitionConfirmDialog();
        confirmDialog.show(getSupportFragmentManager(), "membership acquisition dialog");
        selectedMembership = item;
    }

    @Override
    public void dialogConfirmAcquireMembership() {
        SharedPreferences myPrefs = this.getSharedPreferences(MY_PREFERENCES, MODE_PRIVATE);
        String userId = myPrefs.getString("userId","none");
        userDBRef.orderByChild("id").equalTo(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    signedUser = userSnapshot.getValue(User.class);
                    accountDBRef.orderByChild("accountNumber").equalTo(signedUser.getUserAccount()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot accountSnapshot : dataSnapshot.getChildren()) {
                                userAccount = accountSnapshot.getValue(Account.class);
                                acquireMembership();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @Override
    public void dialogNoFundsGoToAccount() {
        Intent intent = new Intent(this, AccountActivity.class);
        intent.putExtra("userId", signedUser.getId());
        startActivity(intent);
    }

    private void acquireMembership () {
        SharedPreferences myPrefs = this.getSharedPreferences(MY_PREFERENCES, MODE_PRIVATE);
        if (userAccount.getBalance() <= selectedMembership.getPrice()) {
            NotEnoughFundsDialog notEnoughFundsDialog = new NotEnoughFundsDialog();
            notEnoughFundsDialog.show(getSupportFragmentManager(), "Fondos insuficientes");
        } else {
            signedUser.setMembershipId(selectedMembership.getId());
            userAccount.setBalance(userAccount.getBalance() - selectedMembership.getPrice());
            userDBRef.child(signedUser.getId()).setValue(signedUser);
            accountDBRef.child(userAccount.getAccountNumber()).setValue(userAccount);
            myPrefs.edit().putString("userMembershipId",selectedMembership.getId()).commit();
            Toast.makeText(this, "¡Membresía adquirida con éxito!", Toast.LENGTH_LONG).show();
            finish();
            startActivity(getIntent());
        }
    }
}
