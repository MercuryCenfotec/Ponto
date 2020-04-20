package com.cenfotec.ponto.entities.account;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.cenfotec.ponto.R;
import com.cenfotec.ponto.data.model.Account;
import com.cenfotec.ponto.data.model.Bidder;
import com.cenfotec.ponto.data.model.User;
import com.cenfotec.ponto.entities.user.LoginActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;

public class AccountActivity extends AppCompatActivity {

    private static SharedPreferences sharedpreferences;
    private String userId;
    private User activeUser;
    private Account userAccount;
    private TextView accountBalanceText;
    private TextView addMoneyText;
    private TextView withdrawMoneyText;
    final DecimalFormat costFormat = new DecimalFormat("###,###.00");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        initViewControls();
        getUserByIntentToken();
    }

    //Initialization statements start here
    private void initViewControls() {
        accountBalanceText = findViewById(R.id.accountBalance);
        addMoneyText = findViewById(R.id.addMoneyDescription);
        withdrawMoneyText = findViewById(R.id.withdrawMoneyDescription);

        if (getIntent().getExtras() != null) {
            userId = getIntent().getStringExtra("userId");
        }
    }

    public void goBackToProfile(View view) {
        finish();
    }

    private void getUserByIntentToken() {
        final DatabaseReference userDBReference = FirebaseDatabase.getInstance().getReference();
        Query getUserByIdQuery = userDBReference.child("Users").orderByChild("id").equalTo(userId);
        getUserByIdQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    activeUser = snapshot.getValue(User.class);

                    if (activeUser.getUserType() == 1) { // Solicitante
                        addMoneyText.setText("Ingresar dinero a mi cuenta para hacer el pago de los servicios");
                        withdrawMoneyText.setText("Retirar el dinero que he ingresado a mi cuenta interna, en caso de error");

                    } else {
                        addMoneyText.setText("Ingresar dinero a mi cuenta para hacer el pago de las membresías");
                        withdrawMoneyText.setText("Retirar el dinero que se me ha pagado por los trabajos que he completado");
                    }

                    getUserAccount();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void getUserAccount() {
        final DatabaseReference accountDBReference = FirebaseDatabase.getInstance().getReference();
        Query getUserAccountQuery = accountDBReference.child("Accounts").orderByChild("accountNumber").equalTo(activeUser.getUserAccount());
        getUserAccountQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot accountSnapshot : dataSnapshot.getChildren()) {
                    userAccount = accountSnapshot.getValue(Account.class);
                    fillBalance();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void fillBalance() {
        String accountBalance = "₡" + costFormat.format(Double.parseDouble(userAccount.getBalance().toString())) + " CRC";
        accountBalanceText.setText(accountBalance);
    }
}
