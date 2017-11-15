package com.imennmn.myprofileapp.controllers;

import android.content.Context;
import android.graphics.Bitmap;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.firebase.storage.UploadTask.TaskSnapshot;

import java.io.ByteArrayOutputStream;

/**
 * Created by imen_nmn on 15/11/17.
 */

public class UploadPhotoController {
    private final static  String GS_URL="gs://myprofileapp-a5e32.appspot.com" ;
    private final static  String PHOTO_NAME="user_profile.jpg" ;

    private FirebaseStorage storage ;

    public void init(Context context){
        FirebaseApp.initializeApp(context) ;
        storage = FirebaseStorage.getInstance(GS_URL);
    }

    public void uploadPhoto(Bitmap bitmap,
                            OnFailureListener onFailureListener,
                            OnSuccessListener<TaskSnapshot> onSuccessListener){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();
        // Create a storage reference from our app
        StorageReference storageRef = storage.getReference();
        // Create a reference to "mountains.jpg"
        StorageReference photoRef = storageRef.child(PHOTO_NAME);

        UploadTask uploadTask = photoRef.putBytes(data);
        uploadTask.addOnFailureListener(onFailureListener);
        uploadTask.addOnSuccessListener(onSuccessListener);
//        uploadTask.addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception exception) {
//                // Handle unsuccessful uploads
//                Log.i("UploadPhoto"," onFailure "+exception) ;
//            }
//        }).addOnSuccessListener(new OnSuccessListener<TaskSnapshot>() {
//            @Override
//            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
//                Uri downloadUrl = taskSnapshot.getDownloadUrl();
//
//                Log.i("UploadPhoto"," onSuccess "+downloadUrl) ;
//
//            }
//        });
    }
}
