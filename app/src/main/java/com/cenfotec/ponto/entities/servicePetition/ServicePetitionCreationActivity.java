package com.cenfotec.ponto.entities.servicePetition;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.cenfotec.ponto.R;
import com.cenfotec.ponto.data.model.BCrypt;
import com.cenfotec.ponto.data.model.Bidder;
import com.cenfotec.ponto.data.model.CustomDatePickerDialog;
import com.cenfotec.ponto.data.model.ServicePetition;
import com.cenfotec.ponto.data.model.SpinnerItem;
import com.cenfotec.ponto.entities.bidder.BidderHomeActivity;
import com.cenfotec.ponto.entities.bidder.BidderProfileActivity;
import com.cenfotec.ponto.entities.bidder.BidderRegistrationActivity;
import com.cenfotec.ponto.entities.petitioner.PetitionerHomeActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import customfonts.MyTextView_SF_Pro_Display_Medium;

public class ServicePetitionCreationActivity extends AppCompatActivity {

    public static final String MY_PREFERENCES = "MyPrefs";
    public static final int CAMERA_PERMISSION_REQUEST_CODE = 1996;
    public static final int CAMERA_REQUEST_CODE = 1995;
    public static final int GALLERY_REQUEST_CODE = 1994;
    Uri uri;
    private String activeUserId;
    SharedPreferences sharedPreferences;
    DatabaseReference databaseReference;
    EditText titleEditText;
    EditText descriptionEditText;
    EditText serviceTypeEditText;
    TextView fileTextView;
    MyTextView_SF_Pro_Display_Medium btnPostPetition;
    List<Uri> filesToUpload = new ArrayList<>();
    List<String> realFilesToUpload = new ArrayList<>();
    DatabaseReference serviceTypesRef;
    ArrayList<String> spinnerKeys;
    ArrayList<String> spinnerValues;
    SpinnerDialog spinnerDialog;
    String serviceTypeId;
    ImageView returnIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_petition_creation);
        initFormControls();
        getUserId();
        initSpinnerData();
        initServicePetitionCreationControlsListener();
        Locale spanish = new Locale("es", "ES");
        Locale.setDefault(spanish);
    }

    private void getUserId() {
        activeUserId = sharedPreferences.getString("userId", "");
    }



    private void initFormControls() {
        returnIcon = findViewById(R.id.returnIcon);
        sharedPreferences = getSharedPreferences(MY_PREFERENCES, Context.MODE_PRIVATE);
        databaseReference = FirebaseDatabase.getInstance().getReference("ServicePetitions");
        titleEditText = findViewById(R.id.petitionTitleEditText);
        descriptionEditText = findViewById(R.id.descriptionEditText);
        serviceTypeEditText = findViewById(R.id.serviceTypeEditText);
        fileTextView = findViewById(R.id.filesTextView);
        btnPostPetition = findViewById(R.id.btnPetitionCreation);
        serviceTypesRef = FirebaseDatabase.getInstance().getReference("ServiceTypes");

        returnIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToHomePetitioner();
            }
        });
    }

    private void initSpinnerData() {
        serviceTypesRef = FirebaseDatabase.getInstance().getReference("ServiceTypes");
        spinnerKeys = new ArrayList<>();
        spinnerValues = new ArrayList<>();

        serviceTypesRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot serviceType : dataSnapshot.getChildren()) {
                    spinnerValues.add(serviceType.child("id").getValue().toString());
                    spinnerKeys.add(serviceType.child("serviceType").getValue().toString());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        spinnerDialog = new SpinnerDialog(this, spinnerKeys,"Buscar","Cancelar");


        spinnerDialog.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String item, int position) {
                serviceTypeEditText.setText(item);
                serviceTypeId = spinnerValues.get(position);
            }
        });
    }

    public void showSpinnerDialog(View view) {
        spinnerDialog.showSpinerDialog();
    }

    private void initServicePetitionCreationControlsListener() {
        btnPostPetition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                prePetitionCreation();
            }
        });
    }

    //Camera controls statements start here
    public void askCameraPermissions(View view) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) ==
                PackageManager.PERMISSION_GRANTED) {
            preInvokeCameraAndGallery();
        } else {
            String[] permissionRequest = {Manifest.permission.CAMERA};
            ActivityCompat.requestPermissions(this, permissionRequest,
                    CAMERA_PERMISSION_REQUEST_CODE);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                preInvokeCameraAndGallery();
            } else {
                showToaster("Permisos requeridos");
            }
        }
    }

    public void preInvokeCameraAndGallery() {
        File imagePath = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File picFile = new File(imagePath, getPictureName());
        uri = FileProvider.getUriForFile(this,
                getApplicationContext().getPackageName() + ".provider", picFile);
        openCameraAndGalleryDialog();
    }

    private void openCameraAndGalleryDialog() {
        AlertDialog.Builder builderSingle = new AlertDialog.Builder(ServicePetitionCreationActivity.this);
        builderSingle.setTitle("Selecciona una opción");

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                ServicePetitionCreationActivity.this,
                android.R.layout.select_dialog_singlechoice);
        arrayAdapter.add("Galería");
        arrayAdapter.add("Cámara");

        builderSingle.setNegativeButton(
                "Cancelar",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        builderSingle.setAdapter(
                arrayAdapter,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int picked) {
                        pickedOptionAction(picked);
                    }
                });
        builderSingle.show();
    }


    private void pickedOptionAction(int picked) {
        switch (picked) {
            case 0:
                Intent openGallery = new Intent(Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                openGallery.putExtra(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
                openGallery.setType("*/*");
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                    openGallery.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                }

                startActivityForResult(openGallery, GALLERY_REQUEST_CODE);
                break;
            case 1:
                Intent openCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                openCamera.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                startActivityForResult(openCamera, CAMERA_REQUEST_CODE);
                break;
            default:
                break;
        }
    }

    private String getPictureName() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
        Date now = new Date();
        String timestamp = sdf.format(now);
        return "image" + timestamp + ".jpg";
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case CAMERA_REQUEST_CODE:
                    filesToUpload.add(uri);
                    break;
                case GALLERY_REQUEST_CODE:
                    ClipData mClipData = data.getClipData();
                    ArrayList<Uri> mArrayUri = new ArrayList<Uri>();
                    for (int i = 0; i < mClipData.getItemCount(); i++) {
                        ClipData.Item item = mClipData.getItemAt(i);
                        Uri uri = item.getUri();
                        mArrayUri.add(uri);
                    }

                    filesToUpload.addAll(mArrayUri);

                    break;
                default:
                    break;
            }
            changeNumberOfFilesAdded(filesToUpload.size());

        }
    }

    //Upload statements start here
    private void uploadImageToFirebase() {
        for (Uri uri : filesToUpload) {
            String fileType = "";
            String asd = getContentResolver().getType(uri);
            if (asd.contains("image/")) {
                fileType = "images/";
            } else if (asd.contains("video/")) {
                fileType = "videos/";
            }
            tryUpload(fileType, uri);
        }

    }

    private void tryUpload(String fileType, Uri uri) {
        StorageReference storageReference = FirebaseStorage.getInstance().getReference();

        final StorageReference imgReference = storageReference.child(fileType +
                uri.getLastPathSegment());

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
                        realFilesToUpload.add(uri.toString());
                        if (realFilesToUpload.size() == filesToUpload.size()) {
                            registerServicePetitionToDB();
                        }
                    }
                });
            }
        });
    }

    private void changeNumberOfFilesAdded(int newAmount) {
        String textToDisplay = newAmount + " Archivos adjuntos";
        fileTextView.setText(textToDisplay);
    }


    //create statements start here
    private void prePetitionCreation() {
        if (filesToUpload.size() != 0) {
            uploadImageToFirebase();
        } else {
            if (!showErrorOnBlankSpaces()) {
                FirebaseDatabase.getInstance().getReference().child("ServicePetitions")
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot snapshot) {
                                registerServicePetitionToDB();
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                System.out.println("The read failed: " + databaseError.getCode());
                            }
                        });

            } else {
                showToaster("Error");
            }
        }
    }

    private void registerServicePetitionToDB() {

        String id = databaseReference.push().getKey();
        ServicePetition servicePetition = new ServicePetition(
                id,
                activeUserId,
                titleEditText.getText().toString(),
                descriptionEditText.getText().toString(),
                serviceTypeId,
                false
        );
        servicePetition.setFiles(realFilesToUpload);
        servicePetition.setAcceptedOfferId("");
        databaseReference.child(id).setValue(servicePetition);

        clearServicePetitionInputs();
        showToaster("Solicitud de servicio creada!");
    }

    private void showToaster(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    //validation statements start here
    private void clearServicePetitionInputs() {
        titleEditText.setText("");
        descriptionEditText.setText("");
        serviceTypeEditText.setText("");
        changeNumberOfFilesAdded(0);
        goToHome();
    }

    public void goToHome() {
        Intent intent = new Intent(this, PetitionerHomeActivity.class);
        startActivity(intent);
    }

    private boolean showErrorOnBlankSpaces() {
        boolean isEmpty = false;
        EditText[] editTextsList = new EditText[]{titleEditText, descriptionEditText,
                serviceTypeEditText};
        for (EditText editText : editTextsList) {
            if (editText.getText().toString().equals("")) {
                editText.setHintTextColor(Color.parseColor("#c0392b"));
                editText.setBackgroundResource(R.drawable.edittext_error);
                isEmpty = true;
            } else {
                editText.setBackgroundResource(R.drawable.rect_black);
                editText.setHintTextColor(Color.parseColor("#000000"));
            }
        }
        return isEmpty;
    }

    private void goToHomePetitioner() {
        Intent intent = new Intent(this, PetitionerHomeActivity.class);
        startActivity(intent);
    }
}
