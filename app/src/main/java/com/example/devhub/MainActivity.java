package com.example.devhub;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.devhub.Activities.LoginPage;

public class MainActivity extends AppCompatActivity {

    ImageView backgroundIV;
    Button moveTologinPageBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        attachJavaObjectToXML();
        startAnimations();
        moveTologinPageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goTOLoginPage();
            }
        });
    }
    private void attachJavaObjectToXML()
    {
        try {
            backgroundIV=findViewById(R.id.mainActivity_BackgroundIV);
            moveTologinPageBtn=findViewById(R.id.mainActivity_MoveBtn);

        }
        catch (Exception e)
        {
            Toast.makeText(this,"MainAction:"+e.getMessage(),Toast.LENGTH_SHORT).show();
        }
    }
    private void startAnimations()
    {
        try{
            backgroundIV.animate().scaleX(2).scaleY(2).setDuration(30000).start();
        }
        catch (Exception e)
        {
            Toast.makeText(this,"MainActivity:"+e.getMessage(),Toast.LENGTH_SHORT).show();
        }
    }
    private void goTOLoginPage()
    {
        try {
            startActivity(new Intent(this, LoginPage.class));
        }
        catch (Exception e)
        {
         Toast.makeText(this, "MainActivity"+e.getMessage(),Toast.LENGTH_SHORT).show();
        }
    }
}