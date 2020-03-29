package com.cenfotec.ponto.entities.servicePetition;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import com.cenfotec.ponto.R;
import com.cenfotec.ponto.data.model.ServicePetition;
import com.cenfotec.ponto.data.model.ServiceType;
import com.cenfotec.ponto.data.model.User;
import com.cenfotec.ponto.entities.bidder.BidderHomeActivity;
import com.cenfotec.ponto.entities.petitioner.PetitionerHomeActivity;
import com.cenfotec.ponto.entities.user.LoginActivity;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import adapter.Carousel_Adapter;
import adapter.TabLayoutAdapter_PetitionerList;
import adapter.TabLayoutAdapter_ServicePetitionDetailPetitioner;
import customfonts.MyTextView_SF_Pro_Display_Bold;
import customfonts.MyTextView_SF_Pro_Display_Semibold;
import customfonts.TextViewSFProDisplayRegular;
import de.hdodenhof.circleimageview.CircleImageView;
import me.relex.circleindicator.CircleIndicator;

public class ServicePetitionPetitionerDetailActivity extends AppCompatActivity {

    ImageView returnIcon;
    ViewPager petitionViewPager;
    TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_petition_petitioner_detail);
        initContent();
        setContent();
    }

    private void initContent() {
        returnIcon = findViewById(R.id.returnIcon);
        petitionViewPager = findViewById(R.id.petitionViewPager);
        tabLayout = findViewById(R.id.tabLayout);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
    }

    private void setContent() {
        returnIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToHomePetitioner();
            }
        });
        TabLayoutAdapter_ServicePetitionDetailPetitioner adapter = new TabLayoutAdapter_ServicePetitionDetailPetitioner(getSupportFragmentManager(), tabLayout.getTabCount());
        petitionViewPager.setAdapter(adapter);
        petitionViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
    }

    private void goToHomePetitioner() {
        Intent intent = new Intent(this, PetitionerHomeActivity.class);
        startActivity(intent);
    }
}
