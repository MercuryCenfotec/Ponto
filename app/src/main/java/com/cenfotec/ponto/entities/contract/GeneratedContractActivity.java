package com.cenfotec.ponto.entities.contract;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.cenfotec.ponto.R;
import com.cenfotec.ponto.data.model.Bidder;
import com.cenfotec.ponto.data.model.Contract;
import com.cenfotec.ponto.data.model.User;
import com.github.gcacace.signaturepad.views.SignaturePad;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import customfonts.MyTextView_SF_Pro_Display_Bold;
import customfonts.MyTextView_SF_Pro_Display_Medium;

public class GeneratedContractActivity extends AppCompatActivity {

    private static SharedPreferences sharedpreferences;
    private String activeUserId;
    MyTextView_SF_Pro_Display_Bold petitionerName;
    MyTextView_SF_Pro_Display_Bold petitionerIdentification;
    MyTextView_SF_Pro_Display_Bold bidderName;
    MyTextView_SF_Pro_Display_Bold bidderIdentification;
    ImageView petitionerSignature;
    ImageView bidderSignature;
    MyTextView_SF_Pro_Display_Medium btnSignature;
    String petitionerId;
    String bidderUserId;
    String contractId;
    User bidderUser;
    User petitionerUser;
    Bidder bidder;
    Contract contract;
    private SignaturePad mSignaturePad;
    private Button btnSignatureClear;
    private Button btnSignatureSave;
    String userSignatureType = "";
    Uri uri;
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {Manifest.permission.WRITE_EXTERNAL_STORAGE};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generated_contract);
        catchIntent();
        initContractActivityControls();
        getPetitionerById();
        getBidderByUserId();
        getContractById();
        //registerContractToDB();
    }

    /*private void registerContractToDB(){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Contracts");
        String contractId = databaseReference.push().getKey();
        Contract contract = new Contract(contractId, petitionerId, "-M2qxRy4WTlHbkuRoWfS", "", "");
        contract.setName("Contrato #2");
        contract.setDateCreated("20/3/2020");
        contract.setServicePetitionId("-M3DsvgWh6XMw3i_yf_J");
        contract.setOfferId("-M3SkK7pJjDo5T1ebpAJ");
        databaseReference.child(contractId).setValue(contract);
    }*/

    //Init statements start here
    private void catchIntent() {
        sharedpreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        activeUserId = sharedpreferences.getString("userId", "");
        if (getIntent().getExtras() != null) {
            petitionerId = getIntent().getStringExtra("petitionerId");
            bidderUserId = getIntent().getStringExtra("bidderUserId");
            contractId = getIntent().getStringExtra("contractId");
        }
    }

    private void initContractActivityControls() {
        petitionerName = findViewById(R.id.petitionerContractNameTV);
        petitionerIdentification = findViewById(R.id.petitionerContractIdentificationTV);
        bidderName = findViewById(R.id.bidderContractNameTV);
        bidderIdentification = findViewById(R.id.bidderContractIdentificationTV);
        petitionerSignature = findViewById(R.id.petitionerContractSignatureIV);
        bidderSignature = findViewById(R.id.bidderContractSignatureIV);
        btnSignature = findViewById(R.id.btnContractSignature);
    }

    //User information retrieval statements start here
    private void getPetitionerById() {
        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        Query getPetitionerByIdQuery =
                databaseReference.child("Users").orderByChild("id").equalTo(petitionerId);
        getPetitionerByIdQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot petitionerSnapshot : snapshot.getChildren()) {
                    petitionerUser = petitionerSnapshot.getValue(User.class);
                    fillPetitionerTextViews();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });
    }

    private void getBidderByUserId() {
        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        Query getBidderByUserIdQuery =
                databaseReference.child("Bidders").orderByChild("userId").equalTo(bidderUserId);
        getBidderByUserIdQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot bidderSnapshot : snapshot.getChildren()) {
                    bidder = bidderSnapshot.getValue(Bidder.class);
                    getUserByBidderUserId(bidder.getUserId());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });
    }

    private void getUserByBidderUserId(String userId) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        databaseReference.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                bidderUser = dataSnapshot.getValue(User.class);
                fillBidderTextViews();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void fillPetitionerTextViews() {
        petitionerName.setText(petitionerUser.getFullName());
        petitionerIdentification.setText(petitionerUser.getIdentificationNumber());
    }

    private void fillBidderTextViews() {
        bidderName.setText(bidderUser.getFullName());
        bidderIdentification.setText(bidderUser.getIdentificationNumber());
    }

    private void getContractById() {
        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        Query getContractByIdQuery =
                databaseReference.child("Contracts").orderByChild("id").equalTo(contractId);
        getContractByIdQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot contractSnapshot : snapshot.getChildren()) {
                    contract = contractSnapshot.getValue(Contract.class);
                    checkIfContractHasBothSignatures();
                    fillSignatures();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });
    }

    private void checkIfContractHasBothSignatures() {
        if (!contract.getPetitionerSignatureUrl().equals("") && !contract.getBidderSignatureUrl().equals("")) {
            //btnGeneratePdf.setEnabled(true);
        }
        if (!contract.getPetitionerSignatureUrl().equals("") && activeUserId.equals(petitionerId)) {
            btnSignature.setEnabled(false);
            btnSignature.setVisibility(View.INVISIBLE);
        }
        if (!contract.getBidderSignatureUrl().equals("") && activeUserId.equals(bidder.getUserId())) {
            btnSignature.setEnabled(false);
            btnSignature.setVisibility(View.INVISIBLE);
        }
    }

    private void fillSignatures() {
        if (!contract.getPetitionerSignatureUrl().equals("")) {
            Picasso.get().load(contract.getPetitionerSignatureUrl()).fit().into(petitionerSignature);
        }
        if (!contract.getBidderSignatureUrl().equals("")) {
            Picasso.get().load(contract.getBidderSignatureUrl()).fit().into(bidderSignature);
        }
    }

    //Signature statements start here
    public void openSignaturePad(View view) {
        verifyStoragePermissions(this);

        if (activeUserId.equals(petitionerUser.getId())) {
            userSignatureType = "petitioner";
        } else if (activeUserId.equals(bidderUser.getId())) {
            userSignatureType = "bidder";
        }

        setContentView(R.layout.view_signature);
        initSignaturePadControls();
        callSignatureEvent();
    }

    private void initSignaturePadControls() {
        mSignaturePad = findViewById(R.id.signaturePad);
        btnSignatureClear = findViewById(R.id.btnSignatureClear);
        btnSignatureSave = findViewById(R.id.btnSignatureSave);
    }

    private void callSignatureEvent() {
        mSignaturePad.setOnSignedListener(new SignaturePad.OnSignedListener() {
            @Override
            public void onStartSigning() {
            }

            @Override
            public void onSigned() {
                btnSignatureSave.setEnabled(true);
                btnSignatureSave.setVisibility(View.VISIBLE);
                btnSignatureClear.setEnabled(true);
                btnSignatureClear.setVisibility(View.VISIBLE);
            }

            @Override
            public void onClear() {
                btnSignatureSave.setEnabled(false);
                btnSignatureSave.setVisibility(View.INVISIBLE);
                btnSignatureClear.setEnabled(false);
                btnSignatureClear.setVisibility(View.INVISIBLE);
            }
        });
        initSignaturePadButtonEvents();
    }

    private void initSignaturePadButtonEvents() {
        btnSignatureClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSignaturePad.clear();
            }
        });

        btnSignatureSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bitmap signatureBitmap = mSignaturePad.getSignatureBitmap();
                addPngSignatureToGallery(signatureBitmap);
            }
        });
    }

    public void addPngSignatureToGallery(Bitmap signature) {
        uri = getImageUri(signature);
        uploadImageToFirebase();
    }

    public Uri getImageUri(Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.PNG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(this.getContentResolver(), inImage,
                "Title", null);
        return Uri.parse(path);
    }

    //Image upload statements start here
    private void uploadImageToFirebase() {
        StorageReference storageReference = FirebaseStorage.getInstance().getReference();
        final StorageReference imgReference = storageReference.child("signatures/" +
                getPictureName());
        UploadTask uploadTask = imgReference.putFile(uri);

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                System.out.println(e.getMessage());
            }
        });

        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Task<Uri> downloadUrl = imgReference.getDownloadUrl();
                downloadUrl.addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        updateContract(uri);
                    }
                });
            }
        });
    }

    private String getPictureName() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
        Date now = new Date();
        String timestamp = sdf.format(now);
        return "Signature_" + timestamp + ".png";
    }

    private void updateContract(Uri uri) {
        if (userSignatureType.equals("petitioner")) {
            contract.setPetitionerSignatureUrl(uri.toString());
        } else {
            contract.setBidderSignatureUrl(uri.toString());
        }
        DatabaseReference updateContractReference = FirebaseDatabase.getInstance().
                getReference("Contracts").child(contract.getId());
        updateContractReference.setValue(contract);
        showToaster("Firma exitosa");
        finish();
        Intent intent = new Intent(this, GeneratedContractActivity.class);
        intent.putExtra("petitionerId", petitionerId);
        intent.putExtra("bidderUserId", bidderUserId);
        intent.putExtra("contractId", contract.getId());
        startActivity(intent);
    }

    //Other statements start here
    private void showToaster(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    public void goBackToContract(View view) {
        finish();
        Intent intent = new Intent(this, GeneratedContractActivity.class);
        intent.putExtra("petitionerId", petitionerId);
        intent.putExtra("bidderUserId", bidderUserId);
        intent.putExtra("contractId", contract.getId());
        startActivity(intent);
    }

    public void goBackToContractList(View view){
        finish();
    }

    //Permissions statements start here
    public static void verifyStoragePermissions(Activity activity) {
        int permission = ActivityCompat.checkSelfPermission(activity,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_EXTERNAL_STORAGE: {
                if (grantResults.length <= 0
                        || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    showToaster("Se necesitan permisos");
                }
            }
        }
    }

}
