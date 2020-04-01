package com.cenfotec.ponto.entities.petitioner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.cenfotec.ponto.R;
import com.cenfotec.ponto.data.model.User;
import com.cenfotec.ponto.entities.bidder.BidderUpdateActivity;
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

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

public class PetitionerUpdateActivity extends AppCompatActivity {

    DatabaseReference databaseReference;
    EditText identificationEditText;
    EditText fullNameEditText;
    EditText birthDateEditText;
    EditText emailEditText;
    EditText passwordEditText;
    User updatedPet;
    public static final int CAMERA_PERMISSION_REQUEST_CODE = 1996;
    public static final int CAMERA_REQUEST_CODE = 1995;
    public static final int GALLERY_REQUEST_CODE = 1994;
    Uri uri;
    ImageView imageView;
    Uri temporalUri;
    String userId;

    String fullname;
    String identification;
    String birthdate;
    String mail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_petitioner_update);

        findViews();
        getPetitionerByIntentToken();
    }

    private void findViews() {
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        fullNameEditText = findViewById(R.id.fullNameUpdPetEditText);
        birthDateEditText = findViewById(R.id.birthDateUpdPetEditText);
        emailEditText = findViewById(R.id.emailUpdPetEditText);
        identificationEditText = findViewById(R.id.idUpdPetEditText);
        imageView = findViewById(R.id.imgPetitionerProfile);

        if (getIntent().getExtras() != null) {
            userId = getIntent().getStringExtra("petitionerId");
        }
    }

    private void fillUpdateFields() {
        fullNameEditText.setText(updatedPet.getFullName());
        birthDateEditText.setText(updatedPet.getBirthDate());
        emailEditText.setText(updatedPet.getEmail());
        identificationEditText.setText(updatedPet.getIdentificationNumber());
        if (!updatedPet.getProfileImageUrl().equals("")) {
            Picasso.get().load(updatedPet.getProfileImageUrl()).into(imageView);
        }
    }

    private void getPetitionerByIntentToken() {
        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        Query getPetitionerByIdQuery = databaseReference.child("Users").orderByChild("id").equalTo(userId);
        getPetitionerByIdQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot petitionerSnapshot : dataSnapshot.getChildren()) {
                    updatedPet = petitionerSnapshot.getValue(User.class);
                    fillUpdateFields();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

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
        AlertDialog.Builder builderSingle = new AlertDialog.Builder(PetitionerUpdateActivity.this);
        builderSingle.setTitle("Selecciona una opción");

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                PetitionerUpdateActivity.this,
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
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                openGallery.setType("image/*");
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
                    try {
                        temporalUri = uri;
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(),
                                uri);
                        imageView.setImageBitmap(bitmap);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                case GALLERY_REQUEST_CODE:
                    Uri selectedImage = data.getData();
                    temporalUri = selectedImage;
                    imageView.setImageURI(selectedImage);
                    break;
                default:
                    break;
            }
        }
    }

    //Upload statements start here
    private void uploadImageToFirebase() {
        StorageReference storageReference = FirebaseStorage.getInstance().getReference();
        final StorageReference imgReference = storageReference.child("images/" +
                temporalUri.getLastPathSegment());
        UploadTask uploadTask = imgReference.putFile(temporalUri);

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
                        updatedPet.setProfileImageUrl(uri.toString());
                        updatePetitioner();
                    }
                });
            }
        });
    }

    public void checkPetitionerValidations(final View view) {
        if (view == findViewById(R.id.btnRegisterPetitioner)) {
            if (!showErrorOnBlankSpaces() && isValidEmail()) {
                if (isCurrentUserEmail()) {
                    prePetitionerModification();
                } else {
                    FirebaseDatabase.getInstance().getReference().child("Users")
                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot snapshot) {
                                    boolean userFound = false;
                                    for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                                        if (emailEditText.getText().toString().
                                                equals(userSnapshot.child("email").getValue().toString())) {
                                            userFound = true;
                                            showToaster("Email existente");
                                        }
                                    }
                                    if (!userFound) {
                                        prePetitionerModification();
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                    System.out.println("The read failed: " + databaseError.getCode());
                                }
                            });
                }
            } else {
                showToaster("Verificar campos");
            }
        }
    }

    public void updatePetitioner() {
        if (userId != null) {
            final DatabaseReference[] ref = {FirebaseDatabase.getInstance().getReference("Users")};
            ref[0].child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.getValue() != null) {
                        updatedPet.setFullName(fullNameEditText.getText().toString());
                        updatedPet.setEmail(emailEditText.getText().toString());

                        ref[0].child(userId).setValue(updatedPet);
                        showToaster("Actualización exitosa");
                        openPetitionerProfile(userId);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }

    }

    private void prePetitionerModification() {
        if (temporalUri != null) {
            uploadImageToFirebase();
        } else {
            updatePetitioner();
        }

    }

    private void showToaster(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    public void goBack(View view) {
        finish();
    }

    private void openPetitionerProfile(String id) {
        finish();
        Intent intent = new Intent(this, PetitionerHomeActivity.class);
        intent.putExtra("forceProfileAction", 4);
        startActivity(intent);
    }

    //Validation statements start here
    private boolean showErrorOnBlankSpaces() {
        boolean isEmpty = false;
        EditText[] editTextsList = new EditText[]{fullNameEditText, emailEditText};
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

    private boolean isValidEmail() {
        String email = emailEditText.getText().toString();
        if (android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailEditText.setBackgroundResource(R.drawable.rect_black);
            emailEditText.setHintTextColor(Color.parseColor("#000000"));
            return true;
        } else {
            emailEditText.setBackgroundResource(R.drawable.edittext_error);
            emailEditText.setHintTextColor(Color.parseColor("#c0392b"));
            return false;
        }
    }

    private boolean isCurrentUserEmail() {
        String email = emailEditText.getText().toString();
        return email.equals(updatedPet.getEmail());
    }
}
