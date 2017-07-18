package com.example.abhinav.buzzer.Profile;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.abhinav.buzzer.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

import id.zelory.compressor.Compressor;

public class ProfilePhotoSelector extends AppCompatActivity {

    private ImageButton mProfilePhoto;
    private Button mSubmit;
    private StorageReference mStorage;
    private DatabaseReference mDatabase;
    private static final int GALLERY_REQUEST = 1;
    private Uri mImageUri = null;
    private ProgressDialog mProgress;
    private DatabaseReference mDatabaseUser;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_college_photo_selector);


        mProfilePhoto = (ImageButton) findViewById(R.id.imageSelect);
        mSubmit = (Button) findViewById(R.id.submitPhoto);
        mStorage = FirebaseStorage.getInstance().getReference().child("Profile_images");
        mProgress = new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();
        mDatabaseUser = FirebaseDatabase.getInstance().getReference().child("Users").child(mAuth.getCurrentUser().getUid());


        mProfilePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent galleryIntent = new Intent();
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, GALLERY_REQUEST);
            }
        });

        mSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startSetupAccount();
            }
        });

    }

    private void startSetupAccount() {


  if (mImageUri != null ){

            mProgress.setMessage("Saving image");
            mProgress.show();

            final String user_ID = mAuth.getCurrentUser().getUid();

         final File thumb_filePath = new File(mImageUri.getPath());

          Bitmap thumb_bitmap = new Compressor(this)
              .setMaxHeight(200)
              .setMaxWidth(200)
              .setQuality(75)
              .compressToBitmap(thumb_filePath);

             ByteArrayOutputStream baos = new ByteArrayOutputStream();
              thumb_bitmap.compress(Bitmap.CompressFormat.JPEG , 100 ,baos);
             final byte[] thumb_byte = baos.toByteArray();


         StorageReference filePath = mStorage.child("Profile_images/"+user_ID);
         final StorageReference thumb_filepath = mStorage.child("Profile_images").child("thumb").child(user_ID+ ".jpg");

          filePath.putFile(mImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {


                    final String downloadUrl = taskSnapshot.getDownloadUrl().toString();


                    UploadTask uploadTask = thumb_filepath.putBytes(thumb_byte);
                    uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> thumb_task) {
                            @SuppressWarnings("VisibleForTests")
                            String  thumb_downloadUrl = thumb_task.getResult().getDownloadUrl().toString();
                            if (thumb_task.isSuccessful()){

                                Map update_HashMap = new HashMap<String, String>();
                                update_HashMap.put("profile_pic",downloadUrl);
                                update_HashMap.put("thumb_profile_pic",thumb_downloadUrl);

                                mDatabaseUser.updateChildren(update_HashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()){
                                            mProgress.dismiss();
                                            Intent mainIntent = new Intent(ProfilePhotoSelector.this, ProfileActivity.class);
                                            mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                            startActivity(mainIntent);
                                        }
                                        else {

                                        }
                                    }
                                });
                            }
                            else {

                            }
                        }
                    });




                }
            });
        }
        else
        {
            Toast.makeText(ProfilePhotoSelector.this , "Select the Photo" , Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY_REQUEST && resultCode == RESULT_OK) {

            Uri imageUri = data.getData();

            CropImage.activity(imageUri)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .start(this);

        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {

                mImageUri = result.getUri();

                mProfilePhoto.setImageURI(mImageUri);

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }

    }

}
