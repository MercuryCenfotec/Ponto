package com.cenfotec.ponto.entities.contract;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cenfotec.ponto.R;
import com.cenfotec.ponto.data.model.Bidder;
import com.cenfotec.ponto.data.model.Contract;
import com.cenfotec.ponto.data.model.User;
import com.cenfotec.ponto.entities.user.LoginActivity;
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
import com.itextpdf.text.BadElementException;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.PdfWriter;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class GeneratedContractActivity extends AppCompatActivity {

    private static SharedPreferences sharedpreferences;
    private String activeUserId;
    TextView petitionerName;
    TextView petitionerIdentification;
    TextView bidderName;
    TextView bidderIdentification;
    ImageView petitionerSignature;
    ImageView bidderSignature;
    Button btnGeneratePdf;
    Button btnSignature;
    String petitionerId = "-M2qHnZVxG6tWbLwMUWa";
    String bidderId = "-M2qtYcjMe1CM8CHrSaA";
    String contractId = "-M3SXlRAkuyWD0bEXFkj";
    User bidderUser;
    User petitionerUser;
    Bidder bidder;
    Contract contract;
    private SignaturePad mSignaturePad;
    private Button mClearButton;
    private Button mSaveButton;
    String userSignatureType = "";
    Uri uri;
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {Manifest.permission.WRITE_EXTERNAL_STORAGE};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generated_contract);
        getAllData();
        getPetitionerById();
        getBidderById();
        getContract();
        sharedpreferences = getSharedPreferences(LoginActivity.MY_PREFERENCES, Context.MODE_PRIVATE);
        activeUserId = sharedpreferences.getString("userId", "");
        //registerContractToDB();
    }

    /*private void registerContractToDB(){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Contracts");
        String contractId = databaseReference.push().getKey();
        Contract contract = new Contract(contractId, petitionerId, bidderId, "", "");
        databaseReference.child(contractId).setValue(contract);
    }*/

    private void getAllData(){
        petitionerName = findViewById(R.id.textView15);
        petitionerIdentification = findViewById(R.id.textView16);
        bidderName = findViewById(R.id.textView20);
        bidderIdentification = findViewById(R.id.textView21);
        petitionerSignature = findViewById(R.id.imageView);
        bidderSignature = findViewById(R.id.imageView2);
        btnGeneratePdf = findViewById(R.id.button3x);
        btnSignature = findViewById(R.id.btnSignatureUniqueUnique);
    }

    private void getPetitionerById(){
        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        Query getBidderByIdQuery =
                databaseReference.child("Users").orderByChild("id").equalTo(petitionerId);
        getBidderByIdQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot bidderSnapshot : snapshot.getChildren()) {
                    petitionerUser = bidderSnapshot.getValue(User.class);
                    fillPetitionerSpaces();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });
    }

    private void getBidderById(){
        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        Query getBidderByIdQuery =
                databaseReference.child("Bidders").orderByChild("id").equalTo(bidderId);
        getBidderByIdQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot bidderSnapshot : snapshot.getChildren()) {
                    bidder = bidderSnapshot.getValue(Bidder.class);
                    getUserByActiveUserId(bidder.getUserId());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });
    }

    private void getUserByActiveUserId(String userId) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
        ref.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                bidderUser = dataSnapshot.getValue(User.class);
                fillBidderSpaces();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void fillPetitionerSpaces() {
        petitionerName.setText(petitionerUser.getFullName());
        petitionerIdentification.setText(petitionerUser.getIdentificationNumber());
    }

    private void fillBidderSpaces(){
        bidderName.setText(bidderUser.getFullName());
        bidderIdentification.setText(bidderUser.getIdentificationNumber());
    }

    private void getContract(){
        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        Query getBidderByIdQuery =
                databaseReference.child("Contracts").orderByChild("id").equalTo(contractId);
        getBidderByIdQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot bidderSnapshot : snapshot.getChildren()) {
                    contract = bidderSnapshot.getValue(Contract.class);
                    checkIfContractHasBoth();
                    fillSignatures();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });
    }

    private void checkIfContractHasBoth(){
        if(!contract.getPetitionerSignatureUrl().equals("") && !contract.getBidderSignatureUrl().equals("")){
            btnGeneratePdf.setEnabled(true);
        }
        if(!contract.getPetitionerSignatureUrl().equals("") && activeUserId.equals(petitionerId)){
            btnSignature.setEnabled(false);
        }
        if(!contract.getBidderSignatureUrl().equals("") && activeUserId.equals(bidder.getUserId())){
            btnSignature.setEnabled(false);
        }
    }

    private void fillSignatures(){
        if(!contract.getPetitionerSignatureUrl().equals("")){
            Picasso.get().load(contract.getPetitionerSignatureUrl()).into(petitionerSignature);
        }
        if(!contract.getBidderSignatureUrl().equals("")){
            Picasso.get().load(contract.getBidderSignatureUrl()).into(bidderSignature);
        }
    }

    public void openSignature(View view) {
        verifyStoragePermissions(this);

        if(activeUserId.equals(petitionerUser.getId())){
            userSignatureType = "petitioner";
        }else if (activeUserId.equals(bidderUser.getId())){
            userSignatureType = "bidder";
        }

        setContentView(R.layout.view_signature);
        callSignatureEvent();

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_EXTERNAL_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length <= 0
                        || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(GeneratedContractActivity.this, "Cannot write images to external storage", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void generatePdf(View view){
        File pdfDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DOCUMENTS), "MyApp");
        if (!pdfDir.exists()){
            pdfDir.mkdir();
        }

        File pdfFile = new File(pdfDir, "myPdfFile.pdf");
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
        ConstraintLayout root2 = (ConstraintLayout) inflater.inflate(R.layout.activity_generated_contract, null);
        root2.setDrawingCacheEnabled(true);
        Bitmap screen = loadBitmapFromView(view);

        try {
            Document document = new Document();

            PdfWriter.getInstance(document, new FileOutputStream(pdfFile));
            document.open();
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            screen.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] byteArray = stream.toByteArray();
            addImage(document,byteArray);
            document.close();

            Intent intent = new Intent(Intent.ACTION_VIEW);
            Uri uri = Uri.fromFile(new File(pdfDir,  "myPdfFile.pdf"));
            intent.setDataAndType(uri, "application/pdf");
            intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            startActivity(intent);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }


    private static void addImage(Document document,byte[] byteArray)
    {
        Image image = null;
        try
        {
            image = Image.getInstance(byteArray);
        }
        catch (BadElementException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        catch (MalformedURLException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        // image.scaleAbsolute(150f, 150f);
        try
        {
            document.add(image);
        } catch (DocumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static Bitmap loadBitmapFromView(View v) {
        Bitmap bm = Bitmap.createBitmap( v.getLayoutParams().width, v.getLayoutParams().height, Bitmap.Config.ARGB_8888);

        if (v.getMeasuredHeight() <= 0) {
            v.measure(ConstraintLayout.LayoutParams.WRAP_CONTENT, ConstraintLayout.LayoutParams.WRAP_CONTENT);
            Bitmap b = Bitmap.createBitmap(v.getMeasuredWidth(), v.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
            Canvas c = new Canvas(b);
            v.layout(0, 0, v.getMeasuredWidth(), v.getMeasuredHeight());
            v.draw(c);
            bm = b;
        }
        return bm;
    }



    private void showToaster(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    private void callSignatureEvent(){

        mSignaturePad = findViewById(R.id.signature_pad);
        mSignaturePad.setOnSignedListener(new SignaturePad.OnSignedListener() {
            @Override
            public void onStartSigning() {
            }

            @Override
            public void onSigned() {
                mSaveButton.setEnabled(true);
                mClearButton.setEnabled(true);
            }

            @Override
            public void onClear() {
                mSaveButton.setEnabled(false);
                mClearButton.setEnabled(false);
            }
        });

        mClearButton = (Button) findViewById(R.id.clear_button);
        mSaveButton = (Button) findViewById(R.id.save_button);

        mClearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSignaturePad.clear();
            }
        });

        mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bitmap signatureBitmap = mSignaturePad.getSignatureBitmap();
                addJpgSignatureToGallery(signatureBitmap);
            }
        });
    }


    public void addJpgSignatureToGallery(Bitmap signature) {
        uri = getImageUri(signature);
        uploadImageToFirebase();
    }

    public Uri getImageUri(Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.PNG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(this.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    private String getPictureName() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
        Date now = new Date();
        String timestamp = sdf.format(now);
        return "Signature_" + timestamp + ".png";
    }

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

    private void updateContract(Uri uri){
        if(userSignatureType.equals("petitioner")){
            contract.setPetitionerSignatureUrl(uri.toString());
        }else{
            contract.setBidderSignatureUrl(uri.toString());
        }
        DatabaseReference updateUserReference = FirebaseDatabase.getInstance().
                getReference("Contracts").child(contract.getId());
        updateUserReference.setValue(contract);
        showToaster("Actualización exitosa");
        finish();
    }

    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }
}
