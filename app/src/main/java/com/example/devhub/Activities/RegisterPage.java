package com.example.devhub.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.devhub.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class RegisterPage extends AppCompatActivity {
    private CircleImageView profileIV;
    private EditText userNameET, userEmailET, userPasswordET, userConfirmPasswordET;
    private Button registerBtn;
    private RadioGroup objectRadioGroup;
    private TextView dateTV;
    private Uri profileImageURL;
    private static int REQUEST_CODE=1;
    private DatePickerDialog.OnDateSetListener objectOnDateSetListener;
    private FirebaseFirestore objectFirebaseFirestore;
    private FirebaseAuth objectFirebaseAuth;
    private StorageReference objectStorageReference;
    private String finalPassword;
    private int radioID;
    private RadioButton objectRadioButton;
    private Dialog objectDialog;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_page);
        AttachJavaToXMLObjects();
        objectDialog=new Dialog(this);
        objectDialog.setContentView(R.layout.please_wait_layout);
        objectFirebaseFirestore=FirebaseFirestore.getInstance();
        objectFirebaseAuth=FirebaseAuth.getInstance();
        objectStorageReference= FirebaseStorage.getInstance().getReference("ImageFolder");
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createUserAccount();
            }
        });
    }
    private void AttachJavaToXMLObjects(){
        try {
            profileIV=findViewById(R.id.RegisterPage_userProfileIV);
            userNameET=findViewById(R.id.RegisterPage_userNameET);
            userEmailET=findViewById(R.id.RegisterPage_userEmailET);
            userPasswordET=findViewById(R.id.RegisterPage_userPasswordET);
            userConfirmPasswordET=findViewById(R.id.RegisterPage_userConfirmPasswordET);
            dateTV=findViewById(R.id.RegisterPage_userDOBTV);
            registerBtn=findViewById(R.id.RegisterPage_RegisterBtn);
            objectRadioGroup=findViewById(R.id.RegisterPage_RadioGroup);
            profileIV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    chooseImageFromMobileGallery();
                }
            });

            dateTV.setOnClickListener(new View.OnClickListener(){
                public void onClick(View view){
                    chooseDOB();
                }

            });
        }
        catch (Exception e){
            Toast.makeText(this,"RegisterPage"+e.getMessage(),Toast.LENGTH_SHORT).show();
        }

    }
    private  void uploadUserDatatoFirebase(){
        try {
            if(profileImageURL!=null)
            {
                String imagename=userNameET.getText().toString()+"."+getExtension(profileImageURL);
                final StorageReference imageRef=objectStorageReference.child(imagename);
                //Toast.makeText(this, "Uploading User Profile Pic... ", Toast.LENGTH_SHORT).show();
                UploadTask objectUploadTask=imageRef.putFile(profileImageURL);
                objectUploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if(!task.isSuccessful()){
                            objectDialog.dismiss();
                            Toast.makeText(RegisterPage.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                            throw task.getException();
                        }
                        return imageRef.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(RegisterPage.this, "Uploading User Information...", Toast.LENGTH_SHORT).show();
                            Map<String,Object> objectMap=new HashMap<>();
                            objectMap.put("profileimageurl",task.getResult().toString());
                            objectMap.put("username",userNameET.getText().toString());
                            objectMap.put("useremail",userEmailET.getText().toString());
                            objectMap.put("dob",dateTV.getText().toString());
                            objectMap.put("userpassword",finalPassword);
                            radioID=objectRadioGroup.getCheckedRadioButtonId();
                            objectRadioButton=findViewById(radioID);
                            objectMap.put("noofemotions",0);
                            objectMap.put("gender",objectRadioButton.getText().toString());
                            objectMap.put("noofimagestatus",0);
                            objectMap.put("nooftextstatus",0);
                            objectMap.put("usercity","NA");
                            objectMap.put("usercountry","NA");
                            objectMap.put("userbio","NA");
                            objectFirebaseFirestore.collection("UserProfileData")
                                    .document(userEmailET.getText().toString())
                                    .set(objectMap)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            objectDialog.dismiss();
                                            Toast.makeText(RegisterPage.this, "User is created", Toast.LENGTH_SHORT).show();
                                            if(objectFirebaseAuth.getCurrentUser()!=null){
                                                objectFirebaseAuth.signOut();
                                            }
                                            startActivity(new Intent(RegisterPage.this,LoginPage.class));
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    objectDialog.dismiss();
                                    Toast.makeText(RegisterPage.this, "Failed to create and update user", Toast.LENGTH_SHORT).show();
                                }
                            });



                        }
                        else if (!task.isSuccessful()){
                            Toast.makeText(RegisterPage.this,task.getException().toString(),Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }

        }
        catch (Exception e){
            Toast.makeText(this,"RegisterPage"+e.getMessage(),Toast.LENGTH_SHORT).show();
        }

    }
    private void createUserAccount(){
        try{
            if(objectFirebaseAuth.getCurrentUser()!=null){
                objectFirebaseAuth.signOut();
            }
            if(objectFirebaseAuth.getCurrentUser()==null && !userNameET.getText().toString().isEmpty() && !userEmailET.getText().toString().isEmpty() && !userPasswordET.getText().toString().isEmpty() && !userConfirmPasswordET.getText().toString().isEmpty()){
                if(userPasswordET.getText().toString().equals(userConfirmPasswordET.getText().toString())){
                    objectDialog.show();
                    finalPassword=userPasswordET.getText().toString();
                    objectFirebaseAuth.createUserWithEmailAndPassword(userEmailET.getText().toString(),finalPassword).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            uploadUserDatatoFirebase();

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            objectDialog.dismiss();
                            Toast.makeText(RegisterPage.this,"Failed to create user "+e.getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                else{
                    Toast.makeText(this, "Password did not match", Toast.LENGTH_SHORT).show();
                }

            }
            else {
                objectDialog.dismiss();
                Toast.makeText(this, "Please Check user data fields or profile picture", Toast.LENGTH_SHORT).show();
            }


        }
        catch (Exception e){
            Toast.makeText(this,"RegisterPage"+e.getMessage(),Toast.LENGTH_SHORT).show();
        }
    }
    private String getExtension(Uri uri){
        try{
            ContentResolver objectContentResolver=getContentResolver();
            MimeTypeMap objectMimeTypeMap=MimeTypeMap.getSingleton();

            return objectMimeTypeMap.getExtensionFromMimeType(objectContentResolver.getType(uri));


        }
        catch (Exception e){
            Toast.makeText(this,"RegisterPage"+e.getMessage(),Toast.LENGTH_SHORT).show();
            return null;
        }

    }
    private void chooseDOB(){
        try{
            Calendar objectCalender=Calendar.getInstance();
            int year=objectCalender.get(Calendar.YEAR);
            int month=objectCalender.get(Calendar.MONTH);
            int day=objectCalender.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog objectDatePickerDialog= new DatePickerDialog(this, android.R.style.Theme_Holo_Light_Dialog_MinWidth,objectOnDateSetListener,year,month,day);
            objectDatePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            objectDatePickerDialog.show();
            objectOnDateSetListener=new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    month++;
                    dateTV.setText(dayOfMonth+"-"+month+"-"+year);
                }
            };
        }
        catch (Exception e){
            Toast.makeText(this,"RegisterPage"+e.getMessage(),Toast.LENGTH_SHORT).show();
        }
    }
    private void chooseImageFromMobileGallery(){
        try{
            openMobileGallery();
        }
        catch(Exception e){
            Toast.makeText(this,"RegisterPage"+e.getMessage(),Toast.LENGTH_SHORT).show();
        }
    }
    private void openMobileGallery(){
        try{
            Intent galleryIntent= new Intent(Intent.ACTION_GET_CONTENT);
            galleryIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,"image/*");
            startActivityForResult(galleryIntent, REQUEST_CODE);


        }
        catch (Exception e){
            Toast.makeText(this,"RegisterPage"+e.getMessage(),Toast.LENGTH_SHORT).show();

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data.getData()!=null && data!=null){
            profileImageURL=data.getData();
            profileIV.setImageURI(profileImageURL);
            
        }
        else
        {
            Toast.makeText(this, "No Project is selected", Toast.LENGTH_SHORT).show();

        }
    }
}