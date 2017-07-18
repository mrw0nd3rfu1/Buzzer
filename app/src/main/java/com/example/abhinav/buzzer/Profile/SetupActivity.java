package com.example.abhinav.buzzer.Profile;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.abhinav.buzzer.College.CollegeListActivity2;
import com.example.abhinav.buzzer.R;
import com.example.abhinav.buzzer.Timeline.MainActivity;
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

public class SetupActivity extends AppCompatActivity{

    private static final int GALLERY_REQUEST = 1;
    private static final int COLLEGE_REQUEST=111;
    private static final String Tag="Buzer";
    private static  String clgId="";
    private ImageButton mSetupImageButton;
    private EditText mNameField;
    private EditText mCollegeField;
    private EditText mLocationField;
    private Button mSubmitButton;
    private DatabaseReference mDatabaseUsers;
    private FirebaseAuth mAuth;
    private StorageReference mStorageImage;
    private Uri mImageUri = null;
    private ProgressDialog mProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);

        mDatabaseUsers = FirebaseDatabase.getInstance().getReference().child("Users");
        mAuth = FirebaseAuth.getInstance();
        mStorageImage = FirebaseStorage.getInstance().getReference().child("Profile_images");

        mProgress = new ProgressDialog(this);

        mSetupImageButton = (ImageButton) findViewById(R.id.profileImageButton);
        mNameField = (EditText) findViewById(R.id.setupNamefield);
        mCollegeField = (EditText) findViewById(R.id.setupCollegefield);
        mCollegeField.setEnabled(false);
        Button mCollegeChange = (Button) findViewById(R.id.changeCollege);
        mLocationField = (EditText) findViewById(R.id.setupLocationfield);
        mSubmitButton = (Button) findViewById(R.id.setupSubmitButton);

        mSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startSetupAccount();
            }
        });
        mCollegeChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent collegeIntent=new Intent(SetupActivity.this,CollegeListActivity2.class);
                collegeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivityForResult(collegeIntent,COLLEGE_REQUEST);

            }
        });
        mSetupImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent galleryIntent = new Intent();
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, GALLERY_REQUEST);

            }
        });

    }

    private void startSetupAccount() {


        final String name = mNameField.getText().toString().trim();
        final String college_name=mCollegeField.getText().toString().trim();


        final String location = mLocationField.getText().toString().trim();

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

        if (!TextUtils.isEmpty(name) && mImageUri != null && !college_name.equals("None")){

            mProgress.setMessage("Saving the Profile");
            mProgress.show();

            StorageReference filePath = mStorageImage.child("Profile_images/"+user_ID);
            final StorageReference thumb_filepath = mStorageImage.child("Profile_images").child("thumb").child(user_ID+ ".jpg");


            filePath.putFile(mImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    mProgress.dismiss();
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
                                update_HashMap.put("name" ,name);
                                update_HashMap.put("college_name" , college_name);
                                update_HashMap.put("location" , location);
                                update_HashMap.put("CollegeId" ,clgId );

                                mDatabaseUsers.child(user_ID).updateChildren(update_HashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()){

                                            Intent mainIntent = new Intent(SetupActivity.this, MainActivity.class);
                                            mainIntent.putExtra("colgId",clgId);
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
            Toast.makeText(SetupActivity.this , "Check name photo and college" , Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY_REQUEST && resultCode == RESULT_OK) {

            Uri imageUri = data.getData();

            CropImage.activity(imageUri)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(1, 1)
                    .start(this);

        }
        else  if (requestCode ==COLLEGE_REQUEST)
        {
            mCollegeField.setText(data.getStringExtra("CollegeName"));
            clgId=data.getStringExtra("CollegeId");
        }

      else  if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {

                mImageUri = result.getUri();

                mSetupImageButton.setImageURI(mImageUri);

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }

        }

    }


}
