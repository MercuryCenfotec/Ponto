package com.cenfotec.ponto;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    Button btnSwapToBidderRegistration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnSwapToBidderRegistration = findViewById(R.id.btnSwapToBidderRegistration);
        btnSwapToBidderRegistration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openBidderRegistrationView();
            }
        });
    }

    private void openBidderRegistrationView(){
        Intent intent = new Intent(this, BidderRegistrationActivity.class);
        startActivity(intent);
    }
}
