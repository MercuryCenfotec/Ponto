package com.cenfotec.ponto.entities.petitioner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.ClipData;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cenfotec.ponto.R;
import com.cenfotec.ponto.data.model.Account;
import com.cenfotec.ponto.data.model.BCrypt;
import com.cenfotec.ponto.data.model.CustomDatePickerDialog;
import com.cenfotec.ponto.data.model.IdVerification;
import com.cenfotec.ponto.data.model.Petitioner;
import com.cenfotec.ponto.data.model.User;
import com.cenfotec.ponto.entities.bidder.BidderRegistrationActivity;
import com.cenfotec.ponto.entities.servicePetition.ServicePetitionCreationActivity;
import com.cenfotec.ponto.entities.user.LoginActivity;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PetitionerRegistrationActivity extends AppCompatActivity {

    DatabaseReference databaseReference;
    DatabaseReference accountDBReference;
    EditText identificationEditText;
    EditText fullNameEditText;
    EditText birthDateEditText;
    EditText emailEditText;
    EditText passwordEditText;
    DatePickerDialog.OnDateSetListener birthDateSetListener;
    CustomDatePickerDialog customDatePickerDialog;

    // Camera related variables start here
    public static final int CAMERA_PERMISSION_REQUEST_CODE = 1996;
    public static final int CAMERA_REQUEST_CODE = 1995;
    public static final int GALLERY_REQUEST_CODE = 1994;
    Uri uri;
    List<Uri> filesToUpload = new ArrayList<>();
    List<String> realFilesToUpload = new ArrayList<>();
    TextView fileTextView;
    DatabaseReference idVerificationsDBReference;
    // Camera related variables end here

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_petitioner_registration);

        findViews();
        initBidderRegistrationControlsListener();
    }

    private void initBidderRegistrationControlsListener() {
        birthDateEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                customDatePickerDialog.openDateDialog(birthDateEditText,
                        PetitionerRegistrationActivity.this, birthDateSetListener);
            }
        });

        birthDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month += 1;
                String formatDate = dayOfMonth + "/" + month + "/" + year;

                birthDateEditText.setText(formatDate);
            }
        };
    }

    private void findViews() {
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        accountDBReference = FirebaseDatabase.getInstance().getReference("Accounts");
        idVerificationsDBReference = FirebaseDatabase.getInstance().getReference("IdVerifications");
        fullNameEditText = findViewById(R.id.fullNamePetEditText);
        birthDateEditText = findViewById(R.id.birthDatePetEditText);
        emailEditText = findViewById(R.id.emailPetEditText);
        identificationEditText = findViewById(R.id.identificationPetEditText);
        passwordEditText = findViewById(R.id.txtPassword);
        fileTextView = findViewById(R.id.filesTextView);
        customDatePickerDialog = new CustomDatePickerDialog();
    }

    public void prePetitionerRegistration(View view) {
        if (!showErrorOnBlankSpaces() && isValidEmail()) {
            if (hasAllPhotos()) {
                uploadImageToFirebase();
                FirebaseDatabase.getInstance().getReference().child("Users")
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot snapshot) {
                                boolean petitionerFound = false;
                                for (DataSnapshot bidderSnapshot : snapshot.getChildren()) {
                                    if (identificationEditText.getText().toString().equals(bidderSnapshot
                                            .child("identificationNumber").getValue().toString())) {
                                        petitionerFound = true;
                                        showToaster("Identificación existente");
                                    } else if (emailEditText.getText().toString().equals(bidderSnapshot
                                            .child("email").getValue().toString())) {
                                        petitionerFound = true;
                                        showToaster("Email existente");
                                    }
                                }

                                if (!petitionerFound) {
                                    registerPetitioner();
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                System.out.println("The read failed: " + databaseError.getCode());
                            }
                        });
            } else {
                showToaster("Debe seleccionar 3 archivos");
            }
        } else {
            showToaster("Verificar campos");
        }
    }

    public void registerPetitioner() {
        String password = passwordEditText.getText().toString();
        String generatedSecuredPasswordHash = BCrypt.hashpw(password, BCrypt.gensalt(12));

        // Account creation start
        String accountNumber = accountDBReference.push().getKey();
        Account userAccount = new Account(accountNumber, (float) 0);
        accountDBReference.child(accountNumber).setValue(userAccount);
        // Account creation end

        String id = databaseReference.push().getKey();
        User user = new User(id,
                fullNameEditText.getText().toString(),
                birthDateEditText.getText().toString(),
                emailEditText.getText().toString(),
                identificationEditText.getText().toString(),
                generatedSecuredPasswordHash,
                1,
                0,
                true,
                1,
                "",
                accountNumber);
        user.setVerified(false);
        user.setMembershipId("");

        if (id != null)
            databaseReference.child(id).setValue(user);

        // IdVerification creation
        String idVerificationKey = idVerificationsDBReference.push().getKey();
        IdVerification idVerification = new IdVerification(idVerificationKey,user.getFullName(),user.getIdentificationNumber(),user.getId(),realFilesToUpload);
        if (idVerificationKey != null) idVerificationsDBReference.child(idVerificationKey).setValue(idVerification);

        clearPetitionRegistrationInputs();
        showToaster("Registro exitoso");
        openPetitionerProfile();
    }

    private void showToaster(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    private void clearPetitionRegistrationInputs() {
        fullNameEditText.setText("");
        birthDateEditText.setText("");
        emailEditText.setText("");
        identificationEditText.setText("");
        passwordEditText.setText("");
    }

    public void showHidePass(View view) {
        if (view.getId() == R.id.imgViewPassword) {
            if(passwordEditText.getTransformationMethod().equals(PasswordTransformationMethod.getInstance())){
                ((ImageView)(view)).setImageResource(R.drawable.ic_hide);
                //Show Password
                passwordEditText.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            }
            else{
                ((ImageView)(view)).setImageResource(R.drawable.ic_eye);
                //Hide Password
                passwordEditText.setTransformationMethod(PasswordTransformationMethod.getInstance());
            }
        }
    }

    private boolean showErrorOnBlankSpaces() {
        boolean isEmpty = false;
        EditText[] editTextsList = new EditText[]{fullNameEditText, birthDateEditText,
                emailEditText, identificationEditText, passwordEditText};
        for (int matchKey = 0; matchKey < editTextsList.length; matchKey++) {
            if (editTextsList[matchKey].getText().toString().equals("")) {
//                editTextsList[matchKey].setHintTextColor(Color.parseColor("#c0392b"));
                editTextsList[matchKey].setBackgroundResource(R.drawable.edittext_error);
                isEmpty = true;
            } else {
                editTextsList[matchKey].setBackgroundResource(R.drawable.rect);
                editTextsList[matchKey].setHintTextColor(Color.parseColor("#ffffff"));
            }
        }
        return isEmpty;
    }

    private boolean isValidEmail() {
        String email = emailEditText.getText().toString();
        if (android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailEditText.setBackgroundResource(R.drawable.rect);
            emailEditText.setHintTextColor(Color.parseColor("#ffffff"));
            return true;
        } else {
            emailEditText.setBackgroundResource(R.drawable.edittext_error);
//            emailEditText.setHintTextColor(Color.parseColor("#c0392b"));
            return false;
        }
    }

    private boolean hasAllPhotos() {
        if (filesToUpload.size() != 3) {
            return false;
        }
        return true;
    }

    private void openPetitionerProfile() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
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
        AlertDialog.Builder builderSingle = new AlertDialog.Builder(PetitionerRegistrationActivity.this);
        builderSingle.setTitle("Selecciona una opción");

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                PetitionerRegistrationActivity.this,
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
                            registerPetitioner();
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

    // Upload statements end here

    // Camera controls statements end here
}
