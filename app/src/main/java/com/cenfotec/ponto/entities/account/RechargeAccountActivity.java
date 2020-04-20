package com.cenfotec.ponto.entities.account;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.cenfotec.ponto.R;
import com.cenfotec.ponto.data.model.Account;
import com.cenfotec.ponto.entities.notification.NotificationFactory;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

import config.Config;
import customfonts.MyTextView_SF_Pro_Display_Bold;
import customfonts.MyTextView_SF_Pro_Display_Medium;


public class RechargeAccountActivity extends AppCompatActivity {
    private static final int PAYPAL_REQUEST_CODE = 7777;
    private static PayPalConfiguration payPalConfiguration;
    MyTextView_SF_Pro_Display_Medium payPalPaymentCodeTV;
    MyTextView_SF_Pro_Display_Medium payPalPaymentAmountTV;
    MyTextView_SF_Pro_Display_Medium payPalPaymentStatusTV;
    MyTextView_SF_Pro_Display_Bold realBalanceTextView;
    ConstraintLayout paymentDetailContainer;
    View quantityContainerDivider;
    String tempAmount;
    String balanceAccountId;
    DatabaseReference databaseReference;
    Account account;
    String userId;

    @Override
    protected void onDestroy() {
        stopService(new Intent(this, PayPalService.class));
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recharge_account);
        initControls();
        catchIntent();
        startPayPalService();
        getUserBalanceAccount();
    }

    // ## OnActivityCreation statements start here ##
    private void initControls() {
        payPalConfiguration = new PayPalConfiguration().environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)
                .clientId(Config.PAYPAL_CLIENT_ID);
        databaseReference = FirebaseDatabase.getInstance().getReference("Accounts");
        realBalanceTextView = findViewById(R.id.realBalanceTextView);
        paymentDetailContainer = findViewById(R.id.paymentDetailContainer);
        quantityContainerDivider = findViewById(R.id.quantityContainerDivider);
        payPalPaymentCodeTV = findViewById(R.id.payPalPaymentCodeTV);
        payPalPaymentAmountTV = findViewById(R.id.payPalPaymentAmountTV);
        payPalPaymentStatusTV = findViewById(R.id.payPalPaymentStatusTV);
        tempAmount = "";
        balanceAccountId = "";
        userId = "";
    }

    private void catchIntent() {
        if (getIntent().getExtras() != null) {
            try {
                balanceAccountId = getIntent().getStringExtra("balanceAccountId");
                userId = getIntent().getStringExtra("userId");
                if (getIntent().getStringExtra("paymentDetails") != null
                        && getIntent().getStringExtra("paymentAmount") != null) {
                    JSONObject jsonObject = new JSONObject(getIntent().getStringExtra("paymentDetails"));
                    String paymentAmount = getIntent().getStringExtra("paymentAmount");
                    showDetails(jsonObject.getJSONObject("response"), paymentAmount);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
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
                    realBalanceTextView.setText(balance);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    // ## User balance account information statements start here ##

    // ## DetailContainer statements start here ##
    private void showDetails(JSONObject response, String paymentAmount) {
        try {
            String status = "";
            NumberFormat formatter = new DecimalFormat("#,###");
            double myNumber = Double.parseDouble(paymentAmount);
            String formattedNumber = "₡" + formatter.format(myNumber);
            paymentDetailContainer.setVisibility(View.VISIBLE);
            quantityContainerDivider.setVisibility(View.VISIBLE);
            if (response.getString("state").equals("approved")) {
                status = "Aprobado";
            } else {
                status = "Fallido";
            }
            payPalPaymentCodeTV.setText(response.getString("id"));
            payPalPaymentStatusTV.setText(status);
            payPalPaymentAmountTV.setText(formattedNumber);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    // ## DetailContainer statements end ##

    // ## Other statements start here ##
    public void goBackFromRechargeAccount(View view) {
        finish();
        Intent intent = new Intent(this, AccountActivity.class);
        intent.putExtra("userId", userId);
        startActivity(intent);
    }

    public void preProcessPayment(View view) {
        switch (view.getId()) {
            case R.id.btnAddFirstQuantity:
                processPayment("2500");
                break;
            case R.id.btnAddSecondQuantity:
                processPayment("5000");
                break;
            case R.id.btnAddThirdQuantity:
                processPayment("12500");
                break;
            case R.id.btnAddFourthQuantity:
                processPayment("25000");
                break;
            case R.id.btnAddFifthQuantity:
                processPayment("50000");
                break;
            default:
                break;
        }
    }
    // ## Other statements end ##

    // ## PayPalSDK statements start here ##
    private void startPayPalService() {
        Intent intent = new Intent(this, PayPalService.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, payPalConfiguration);
        startService(intent);
    }

    public void processPayment(String selectedAmount) {
        tempAmount = selectedAmount;
        Float colonToDollar = Float.parseFloat(selectedAmount) / Float.parseFloat(Config.USD_EXCHANGE_RATE);
        String obtainedDollar = String.valueOf(colonToDollar);
        PayPalPayment payPalPayment = new PayPalPayment(new BigDecimal(obtainedDollar), "USD",
                "Fondos", PayPalPayment.PAYMENT_INTENT_SALE);
        Intent intent = new Intent(this, PaymentActivity.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, payPalConfiguration);
        intent.putExtra(PaymentActivity.EXTRA_PAYMENT, payPalPayment);
        startActivityForResult(intent, PAYPAL_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PAYPAL_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                PaymentConfirmation confirmation = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
                if (confirmation != null) {
                    try {
                        updateBalanceAccount();
                        finish();
                        String paymentDetails = confirmation.toJSONObject().toString(4);
                        Intent intent = new Intent(this, RechargeAccountActivity.class);
                        intent.putExtra("balanceAccountId", balanceAccountId);
                        intent.putExtra("paymentDetails", paymentDetails);
                        intent.putExtra("paymentAmount", tempAmount);
                        intent.putExtra("userId", userId);
                        startActivity(intent);
                        notifyUserOfRecharge();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {

            }
        } else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID)
            Toast.makeText(this, "Credenciales inválidas", Toast.LENGTH_SHORT).show();
    }
    // ## PayPalSDK statements end ##

    // ## Update user balance account statements start here ##
    private void updateBalanceAccount() {
        DatabaseReference updateBalanceReference = databaseReference.child(account.getAccountNumber());
        Float parsedAmount = Float.parseFloat(tempAmount);
        Float newBalance = account.getBalance() + parsedAmount;
        account.setBalance(newBalance);
        updateBalanceReference.setValue(account);
    }
    // ## Update user balance account statements end ##

    // ## Notification example statements start here ##
    public void notifyUserOfRecharge(){
        //If the notification wont lead to another activity
        NotificationFactory.createNotificationWithoutExtras(this,"¡Recarga exitosa!",
                "Su recarga ha sido exitosa.");

        //If the notification will lead to another activity
        //Extras that the Intent need in order to work
        /*HashMap<String, String> extrasList = new HashMap<>();
        extrasList.put("userId",userId);
        NotificationFactory.createNotificationWithExtras(this, AccountActivity.class,
                "¡Recarga exitosa!", "Su recarga ha sido exitosa.", extrasList);*/
    }
    // ## Notification example statements end ##
}
