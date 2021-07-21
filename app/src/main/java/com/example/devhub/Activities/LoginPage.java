package com.example.devhub.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.devhub.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginPage extends AppCompatActivity {

    private RelativeLayout objectRelativeLayout;
    private ImageView logoLoginLogoIV;
    private AnimationDrawable objectAnimationDrawable;
    private Animation objectAnimation;
    private EditText loginPageEmailET, loginPagePasswordET;
    private TextView loginPageTagLineTV;
    private Button loginPageLoginBtn, loginPageGoToRegisterBtn;
    private FirebaseAuth objectFirebaseAuth;
    private Dialog objectDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);
        attachJavaObjectToXML();
        objectFirebaseAuth = FirebaseAuth.getInstance();
        objectDialog = new Dialog(this);
        objectDialog.setContentView(R.layout.please_wait_layout);


    }

    private void attachJavaObjectToXML() {
        try {
            objectRelativeLayout = findViewById(R.id.loginPage_RL);
            objectAnimationDrawable = (AnimationDrawable) objectRelativeLayout.getBackground();
            objectAnimationDrawable.setEnterFadeDuration(4500);
            objectAnimationDrawable.setExitFadeDuration(4500);
            objectAnimationDrawable.start();
            logoLoginLogoIV = findViewById(R.id.loginPage_logoIV);
            loginPageEmailET = findViewById(R.id.loginPage_EmailET);
            loginPagePasswordET = findViewById(R.id.loginPage_passwordET);
            loginPageTagLineTV = findViewById(R.id.loginPage_CreateAccount);
            loginPageLoginBtn = findViewById(R.id.loginPage_loginBtn);
            loginPageGoToRegisterBtn = findViewById(R.id.loginPage_moveToRegisterPage);
            objectAnimation = AnimationUtils.loadAnimation(this, R.anim.anim_login_enry);
            logoLoginLogoIV.startAnimation(objectAnimation);
            loginPageEmailET.startAnimation(objectAnimation);
            loginPagePasswordET.startAnimation(objectAnimation);
            loginPageLoginBtn.startAnimation(objectAnimation);
            loginPageGoToRegisterBtn.startAnimation(objectAnimation);
            loginPageTagLineTV.startAnimation(objectAnimation);
            loginPageGoToRegisterBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    moveToRegisterPage();
                }
            });
            loginPageLoginBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    signInUser();
                }
            });
        } catch (Exception e) {
            Toast.makeText(this, "LoginPage" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void signInUser() {
        try {
            if (!loginPageEmailET.getText().toString().isEmpty() && !loginPagePasswordET.getText().toString().isEmpty()) {
                objectDialog.show();
                if (objectFirebaseAuth.getCurrentUser() == null) {
                    objectFirebaseAuth.signInWithEmailAndPassword(loginPageEmailET.getText().toString(), loginPagePasswordET.getText().toString())
                            .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                @Override
                                public void onSuccess(AuthResult authResult) {
                                    objectDialog.dismiss();
                                    Toast.makeText(LoginPage.this, "Welcome " + loginPageEmailET.getText().toString(), Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(LoginPage.this, MainContentPage.class));
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            objectDialog.dismiss();
                            Toast.makeText(LoginPage.this, "LoginPage:" + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    objectDialog.dismiss();
                    objectFirebaseAuth.signOut();
                    Toast.makeText(this, "Previously signed in user logged out, sign in again please", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Fill both login and password fields", Toast.LENGTH_SHORT).show();
            }

        } catch (Exception e) {
            Toast.makeText(this, "LoginPage" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void moveToRegisterPage() {
        try {
            startActivity(new Intent(this, RegisterPage.class));
        } catch (Exception e) {
        }
    }
}