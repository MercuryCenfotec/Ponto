package com.cenfotec.ponto.entities.account;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.cenfotec.ponto.R;
import com.cenfotec.ponto.data.model.Account;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.security.SecureRandom;
import java.text.NumberFormat;
import java.util.Locale;

import customfonts.EditText__SF_Pro_Display_Medium;
import customfonts.MyTextView_SF_Pro_Display_Medium;

public class FetchBalanceFromAccountActivity extends AppCompatActivity {

    final String charList = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    static SecureRandom secureRandom;
    DatabaseReference databaseReference;
    String code;
    Float tempAmount;
    String balanceAccountId;
    String userId;
    EditText__SF_Pro_Display_Medium destinationAccount;
    EditText__SF_Pro_Display_Medium userIdentification;
    EditText__SF_Pro_Display_Medium balanceAccount;
    EditText__SF_Pro_Display_Medium firstCodeSection;
    EditText__SF_Pro_Display_Medium secondCodeSection;
    EditText__SF_Pro_Display_Medium thirdCodeSection;
    MyTextView_SF_Pro_Display_Medium generatedCode;
    Account account;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fetch_balance_from_account);
        initControls();
        catchIntent();
        getUserBalanceAccount();
        generateRandomCode();
    }

    // ## OnActivityCreation statements start here ##
    private void initControls() {
        databaseReference = FirebaseDatabase.getInstance().getReference("Accounts");
        secureRandom = new SecureRandom();
        destinationAccount = findViewById(R.id.fetchBalanceDestinationET);
        userIdentification = findViewById(R.id.fetchBalanceIdentificationET);
        balanceAccount = findViewById(R.id.fetchBalanceAmountET);
        generatedCode = findViewById(R.id.fetchBalanceGeneratedCodeTV);
        firstCodeSection = findViewById(R.id.fetchBalanceFirstCodeET);
        secondCodeSection = findViewById(R.id.fetchBalanceSecondCodeET);
        thirdCodeSection = findViewById(R.id.fetchBalanceThirdCodeET);
    }

    private void catchIntent() {
        if (getIntent().getExtras() != null) {
            balanceAccountId = getIntent().getStringExtra("balanceAccountId");
            userId = getIntent().getStringExtra("userId");
        }
    }
    // ## OnActivityCreation statements end ##

    // ## User balance account information statements start here ##
    private void getUserBalanceAccount() {
        Query getBalanceAccByIdQuery = databaseReference.orderByChild("accountNumber").equalTo(balanceAccountId);
        getBalanceAccByIdQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot balanceSnapshot : dataSnapshot.getChildren()) {
                    account = balanceSnapshot.getValue(Account.class);
                    NumberFormat formatter = NumberFormat.getCurrencyInstance(Locale.US);
                    String formattedNumber = formatter.format(account.getBalance());
                    String finalFormattedNumber = formattedNumber.substring(1);
                    String balance = "₡ " + finalFormattedNumber + " CRC";
                    tempAmount = account.getBalance();
                    balanceAccount.setText(balance);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    // ## User balance account information statements start here ##

    // ## Fetch confirmation statements start here ##
    public void confirmFetch(View view) {
        if (!showErrorOnBlankSpaces()) {
            if (isSameCode()) {
                updateUserBalanceAccount();
            } else {
                showToaster("Código incorrecto");
            }
        } else {
            showToaster("Verificar campos");
        }
    }
    // ## Fetch confirmation statements end ##

    // ## Other statements start here ##
    public void goBackFromFetchBalance(View view) {
        finish();
        Intent intent = new Intent(this, AccountActivity.class);
        intent.putExtra("userId", userId);
        startActivity(intent);
    }

    private void showToaster(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    private void generateRandomCode() {
        String randomCodeBuilder = random().toUpperCase() + " - " + random().toUpperCase() + " - "
                + random().toUpperCase();
        code = randomCodeBuilder;
        generatedCode.setText(randomCodeBuilder);
    }

    public String random() {
        StringBuilder sb = new StringBuilder(2);
        for (int codeLength = 0; codeLength < 2; codeLength++)
            sb.append(charList.charAt(secureRandom.nextInt(charList.length())));
        return sb.toString();
    }
    // ## Other statements end ##

    // ## Update user balance account statements start here ##
    private void updateUserBalanceAccount() {
        DatabaseReference updateBalanceReference = databaseReference.child(account.getAccountNumber());
        Float newBalance = account.getBalance() - tempAmount;
        account.setBalance(newBalance);
        updateBalanceReference.setValue(account);
        showToaster("Retiro exitoso");
        finish();
        Intent intent = new Intent(this, AccountActivity.class);
        intent.putExtra("userId", userId);
        startActivity(intent);
    }
    // ## Update user balance account statements end ##

    // ## Validation statements start here ##
    private boolean showErrorOnBlankSpaces() {
        boolean isEmpty = false;
        int color;
        EditText[] editTextsList = new EditText[]{destinationAccount, userIdentification,
                firstCodeSection, secondCodeSection, thirdCodeSection};
        for (EditText editText : editTextsList) {
            if (editText.equals(firstCodeSection) || editText.equals(secondCodeSection)
                    || editText.equals(thirdCodeSection)) {
                color = Color.parseColor("#ced0d2");
            } else {
                color = Color.parseColor("#000000");
            }
            if (editText.getText().toString().equals("")) {
                editText.setHintTextColor(Color.parseColor("#c0392b"));
                editText.setBackgroundResource(R.drawable.edittext_error);
                isEmpty = true;
            } else {
                editText.setBackgroundResource(R.drawable.rect_black);
                editText.setHintTextColor(color);
            }
        }
        return isEmpty;
    }

    private boolean isSameCode() {
        boolean isEqual = false;
        String finalString = firstCodeSection.getText().toString() + " - "
                + secondCodeSection.getText().toString() + " - " + thirdCodeSection.getText().toString();
        if (finalString.equals(code)) {
            isEqual = true;
        }
        return isEqual;
    }
    // ## Validation statements end ##
}
