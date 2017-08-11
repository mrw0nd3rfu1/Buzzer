package com.tech.abhinav.buzzer.Profile;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.tech.abhinav.buzzer.R;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class PhoneAuthActivity extends AppCompatActivity {

    private EditText etxtPhone;
    private FirebaseAuth mAuth;
 //   private FirebaseAuth.AuthStateListener mAuthListener;
    private EditText etxtPhoneCode;
    private String mVerificationId;
    private String phoneNumber;
    private EditText code;
    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_auth);
        etxtPhone = (EditText)findViewById(R.id.etxtPhone);
        etxtPhoneCode = (EditText)findViewById(R.id.etxtPhoneCode);
        code = (EditText) findViewById(R.id.code);
        code.setText("+91");
        code.setEnabled(false);
        mAuth =FirebaseAuth.getInstance();
        mToolbar = (Toolbar)findViewById(R.id.toolbar);
        mToolbar.setTitle("Phone Authentication");

    }

    public void requestCode(View view){
        phoneNumber = etxtPhone.getText().toString();
        phoneNumber = "+91" + phoneNumber;
        if (TextUtils.isEmpty(phoneNumber))
            return;
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber , 60 , TimeUnit.SECONDS , PhoneAuthActivity.this, new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    @Override
                    public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                        //called if it is not needed to enter verification code
                        signInWithCredential(phoneAuthCredential);
                    }

                    @Override
                    public void onVerificationFailed(FirebaseException e) {
                        //incorrect phone number , verification code
                        Toast.makeText(PhoneAuthActivity.this , "Verification Failed" ,Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                        super.onCodeSent(s, forceResendingToken);
                        mVerificationId = s;
                    }

                    @Override
                    public void onCodeAutoRetrievalTimeOut(String s) {
                        super.onCodeAutoRetrievalTimeOut(s);
                        //Called if timeout or code isnt retrieved
                        Toast.makeText(PhoneAuthActivity.this , "Verification Timed Out" ,Toast.LENGTH_LONG).show();

                    }
                }
        );
    }

    private void signInWithCredential(PhoneAuthCredential phoneAuthCredential){

                Toast.makeText(PhoneAuthActivity.this , "Now you are verified..", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(PhoneAuthActivity.this , SetupActivity.class) ;
                intent.putExtra("phoneNo",phoneNumber);
                startActivity(intent);
                finish();

    }

    public void signIn (View view){
        String code = etxtPhoneCode.getText().toString();
        if (TextUtils.isEmpty(code))
            return;

        signInWithCredential(PhoneAuthProvider.getCredential(mVerificationId , code));
    }

}
