package com.cenfotec.ponto.entities.bidder;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.cenfotec.ponto.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class BidderVerification extends AppCompatActivity {

    public static final int CAMERA_PERMISSION_REQUEST_CODE = 1996;
    public static final int CAMERA_REQUEST_CODE = 1995;
    private Uri uri;
    Button btnUpload;
    Button btnSave;
    ImageView imgPreview;
    StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bidder_verification);
        btnUpload = findViewById(R.id.btnUpload);
        btnSave = findViewById(R.id.button2);
        imgPreview = findViewById(R.id.imgPreview);

        storageReference = FirebaseStorage.getInstance().getReference();

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                askCameraPermissions();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                call();
            }
        });
    }

    private void askCameraPermissions() {

        // permissions check.
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            invokeCamera();
        } else {
            String[] permissionRequest = {Manifest.permission.CAMERA};
            ActivityCompat.requestPermissions(this, permissionRequest, CAMERA_PERMISSION_REQUEST_CODE);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE) {
            // we are hearing back from the camera.
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                invokeCamera();
            } else {
                Toast.makeText(this, "No permisos", Toast.LENGTH_LONG).show();
            }
        }
    }


    private void invokeCamera() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // path to th eimages directory.
        File imagePath = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        // create an image file at this path.
        File picFile = new File(imagePath, getPictureName());

        // convert file to URI
        uri = FileProvider.getUriForFile(this, getApplicationContext().getPackageName() + ".provider", picFile);
        // where do I want to save the image?
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        // pass permission to the camera
        cameraIntent.setFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);

        startActivityForResult(cameraIntent, CAMERA_REQUEST_CODE);

    }

    private String getPictureName() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
        Date now = new Date();
        String timestamp = sdf.format(now);
        // assemble a picture name
        String pictureName = "image" + timestamp + ".jpg";
        return pictureName;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            // if we are here , the user selected OK
            if (requestCode == CAMERA_REQUEST_CODE) {

                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
                    imgPreview.setImageBitmap(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

        }

    }

    private void call() {
        StorageReference storageReference = FirebaseStorage.getInstance().getReference();
        final StorageReference imgReference = storageReference.child("images/" + uri.getLastPathSegment());
        UploadTask uploadTask = imgReference.putFile(uri);

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                int i = 1 + 1;
                //Handle the error
            }
        });

        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                StorageMetadata storageMetadata = taskSnapshot.getMetadata();
                Task<Uri> downloadUrl = imgReference.getDownloadUrl();
                downloadUrl.addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        String string = uri.toString();
                        String string1 = string;
                        //Update DTO with image location
                    }
                });
            }
        });
    }
}

