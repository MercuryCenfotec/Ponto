package com.cenfotec.ponto.entities.membership;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.content.SharedPreferences;
import android.os.Bundle;

import com.cenfotec.ponto.R;
import com.cenfotec.ponto.data.model.Membership;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import adapter.MembershipCard_Adapter;

public class MembershipAcquisitionActivity extends AppCompatActivity {

    private MembershipCard_Adapter membershipCard_adapter;
    private RecyclerView recyclerView;
    private List<Membership> membershipList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        membershipList = new ArrayList<>();
        setContentView(R.layout.activity_membership_acquisition);
        final SharedPreferences myPrefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String currentMembership = myPrefs.getString("userMembership", "none");
        Query membershipsQuery = currentMembership.equals("none")
                ? FirebaseDatabase.getInstance().getReference("Memberships").orderByChild("id")
                : FirebaseDatabase.getInstance().getReference("Memberships").orderByChild("id").equalTo(currentMembership);
        recyclerView = findViewById(R.id.cardListView);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        membershipCard_adapter = new MembershipCard_Adapter(this,membershipList);

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

        membershipCard_adapter = new MembershipCard_Adapter(this,membershipList);
        recyclerView.setAdapter(membershipCard_adapter);
    }
}
