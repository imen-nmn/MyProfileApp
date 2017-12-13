package com.imennmn.myprofileapp.controllers;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.auth.PhoneAuthProvider.OnVerificationStateChangedCallbacks;

import java.util.concurrent.TimeUnit;

/**
 * Created by imen_nmn on 14/11/17.
 */

public class FirebaseRequestManager {

    private FirebaseAuth mAuth;
    private Activity mContext ;
    private String mVerificationId ;
    private PhoneAuthProvider.ForceResendingToken  token;

    private OnVerificationStateChangedCallbacks mcallback = new OnVerificationStateChangedCallbacks(){
        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
            Log.i("MyProfileTag", " onVerificationCompleted "+phoneAuthCredential.toString()) ;
//            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, "236555");
            signInWithPhoneAuthCredential(phoneAuthCredential) ;

        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            Log.i("MyProfileTag", " onVerificationFailed "+e.toString()) ;

        }
        @Override
        public void onCodeSent(String verificationId,
                               PhoneAuthProvider.ForceResendingToken token) {
            Log.i("MyProfileTag", " onCodeSent var1 = "+verificationId+", var2= "+token) ;
            mVerificationId = verificationId;
            FirebaseRequestManager.this.token = token;

            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, "236555");
            signInWithPhoneAuthCredential(credential) ;
        }
    } ;



    public void initFirebase(Activity context){
        FirebaseApp.initializeApp(context) ;
        mContext = context ;
        mAuth = FirebaseAuth.getInstance() ;
    }

    private void signOut() {
        mAuth.signOut();
    }

    public void validatePhoneNumber(String phoneNumber){

        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                mContext,               // Activity (for callback binding)
                mcallback);        // OnVerificationStateChangedCallbacks
    }

    public void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(mContext, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = task.getResult().getUser();

                            Log.i("MyProfileTag", "signInWithCredential:success "+user);

                            // ...
                        } else {
                            // Sign in failed, display a message and update the UI
                            Log.i("MyProfileTag", "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                                Exception e = task.getException() ;
                                Log.i("MyProfileTag", "signInWithCredential:failure "+e.getMessage());

                            }
                        }
                    }
                });
    }
}
