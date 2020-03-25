package com.cenfotec.ponto.entities.bidder;

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
import com.cenfotec.ponto.data.model.Bidder;
import com.cenfotec.ponto.data.model.User;
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

public class BidderUpdateActivity extends AppCompatActivity {

    DatabaseReference userDatabaseReference;
    DatabaseReference bidderDatabaseReference;
    EditText updateIdentificationEditText;
    EditText updateFullNameEditText;
    EditText updateBirthDateEditText;
    EditText updateEmailEditText;
    EditText updateBiographyEditText;
    User updatedUser;
    Bidder updatedBidder;
    String userId;
    public static final int CAMERA_PERMISSION_REQUEST_CODE = 1996;
    public static final int CAMERA_REQUEST_CODE = 1995;
    public static final int GALLERY_REQUEST_CODE = 1994;
    Uri uri;
    ImageView imageView;
    Uri temporalUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bidder_update);
        initViewControls();
        getUserByIntentToken();
    }

    //Initialization statements start here
    private void initViewControls() {
        userDatabaseReference = FirebaseDatabase.getInstance().getReference("Users");
        bidderDatabaseReference = FirebaseDatabase.getInstance().getReference("Bidders");
        updateFullNameEditText = findViewById(R.id.fullNameUpdBidEditText);
        updateBirthDateEditText = findViewById(R.id.birthDateUpdBidEditText);
        updateEmailEditText = findViewById(R.id.emailUpdBidEditText);
        updateIdentificationEditText = findViewById(R.id.idUpdBidEditText);
        updateBiographyEditText = findViewById(R.id.biographyUpdBidEditText);
        imageView = findViewById(R.id.imgBidderProfile);

        if (getIntent().getExtras() != null) {
            userId = getIntent().getStringExtra("bidderId");
        }
    }

    private void fillUpdateFields() {
        updateFullNameEditText.setText(updatedUser.getFullName());
        updateBirthDateEditText.setText(updatedUser.getBirthDate());
        updateEmailEditText.setText(updatedUser.getEmail());
        updateIdentificationEditText.setText(updatedUser.getIdentificationNumber());
        updateBiographyEditText.setText(updatedBidder.getBiography());
        if (!updatedUser.getProfileImageUrl().equals("")) {
            Picasso.get().load(updatedUser.getProfileImageUrl()).into(imageView);
        }
    }

    private void getUserByIntentToken() {
        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        Query getUserByIdQuery = databaseReference.child("Users").orderByChild("id").equalTo(userId);
        getUserByIdQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    updatedUser = userSnapshot.getValue(User.class);
                    getBidderByUserId();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void getBidderByUserId() {
        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        Query getBidderByUserIdQuery =
                databaseReference.child("Bidders").orderByChild("userId").equalTo(userId);
        getBidderByUserIdQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot bidderSnapshot : dataSnapshot.getChildren()) {
                    updatedBidder = bidderSnapshot.getValue(Bidder.class);
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
        AlertDialog.Builder builderSingle = new AlertDialog.Builder(BidderUpdateActivity.this);
        builderSingle.setTitle("Selecciona una opción");

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                BidderUpdateActivity.this,
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
                        updatedUser.setProfileImageUrl(uri.toString());
                        updateUserBidder();
                    }
                });
            }
        });
    }

    //Update statements start here
    public void checkBidderValidations(View view) {
        if (!showErrorOnBlankSpaces() && isValidEmail()) {
            if (isCurrentUserEmail()) {
                preBidderModification();
            } else {
                FirebaseDatabase.getInstance().getReference().child("Users")
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot snapshot) {
                                boolean userFound = false;
                                for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                                    if (updateEmailEditText.getText().toString().
                                            equals(userSnapshot.child("email").getValue().toString())) {
                                        userFound = true;
                                        showToaster("Email existente");
                                    }
                                }
                                if (!userFound) {
                                    preBidderModification();
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

    private void preBidderModification() {
        if (temporalUri != null) {
            uploadImageToFirebase();
        } else {
            updateUserBidder();
        }

    }

    private void updateUserBidder() {
        DatabaseReference updateUserReference = FirebaseDatabase.getInstance().
                getReference("Users").child(userId);
        DatabaseReference updateBidderReference = FirebaseDatabase.getInstance().
                getReference("Bidders").child(updatedBidder.getId());
        updatedUser.setFullName(updateFullNameEditText.getText().toString());
        updatedUser.setEmail(updateEmailEditText.getText().toString());
        updatedBidder.setBiography(updateBiographyEditText.getText().toString());
        updateUserReference.setValue(updatedUser);
        updateBidderReference.setValue(updatedBidder);
        showToaster("Actualización exitosa");
        openBidderProfile();
    }

    //Other method statements start here
    private void showToaster(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    public void goBack(View view) {
        finish();
    }

    private void openBidderProfile() {
        Intent intent = new Intent(BidderUpdateActivity.this,
                BidderProfileActivity.class);
        startActivity(intent);
    }

    //Validation statements start here
    private boolean showErrorOnBlankSpaces() {
        boolean isEmpty = false;
        EditText[] editTextsList = new EditText[]{updateFullNameEditText, updateEmailEditText,
                updateBiographyEditText};
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
        String email = updateEmailEditText.getText().toString();
        if (android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            updateEmailEditText.setBackgroundResource(R.drawable.rect_black);
            updateEmailEditText.setHintTextColor(Color.parseColor("#000000"));
            return true;
        } else {
            updateEmailEditText.setBackgroundResource(R.drawable.edittext_error);
            updateEmailEditText.setHintTextColor(Color.parseColor("#c0392b"));
            return false;
        }
    }

    private boolean isCurrentUserEmail() {
        String email = updateEmailEditText.getText().toString();
        return email.equals(updatedUser.getEmail());
    }
}
