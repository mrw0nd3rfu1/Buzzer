package com.example.abhinav.buzzer;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

public class SetupActivity extends AppCompatActivity{

    private static final int GALLERY_REQUEST = 1;
    private static final String Tag="Buzer";
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
        if(getIntent().hasExtra("CollegeName"))
        {
            mCollegeField.setText(getIntent().getStringExtra("CollegeName"));

        }
        else{
            mCollegeField.setText("None");
        }


        mCollegeChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent collegeIntent=new Intent(SetupActivity.this,CollegeListActivity2.class);
                collegeIntent.putExtra("User",mAuth.getCurrentUser().getUid());
                collegeIntent.putExtra("Caller","Setup");
                collegeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(collegeIntent);
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


        if (!TextUtils.isEmpty(name) && mImageUri != null && !college_name.equals("None")){

            mProgress.setMessage("Saving the Profile");
            mProgress.show();

            StorageReference filePath = mStorageImage.child("Profile_images/"+user_ID);
            filePath.putFile(mImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    mProgress.dismiss();
                    String downloadUrl = taskSnapshot.getDownloadUrl().toString();

                    mDatabaseUsers.child(user_ID).child("name").setValue(name);
                    mDatabaseUsers.child(user_ID).child("college_name").setValue(college_name);
                    mDatabaseUsers.child(user_ID).child("location").setValue(location);
                    mDatabaseUsers.child(user_ID).child("profile_pic").setValue(downloadUrl);
                    mDatabaseUsers.child(user_ID).child("CollegeId").setValue(getIntent().getStringExtra("CollegeId"));

                    Intent mainIntent = new Intent(SetupActivity.this, MainActivity.class);
                    mainIntent.putExtra("colgId",getIntent().getStringExtra("CollegeId"));
                    mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(mainIntent);
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

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
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
