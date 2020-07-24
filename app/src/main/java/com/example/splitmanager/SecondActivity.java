package com.example.splitmanager;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class SecondActivity extends AppCompatActivity {

     TextView adduser;
     TextView seeuser;
    ImageView addimage;
    ImageView seeimage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second3);
         adduser=(TextView) findViewById(R.id.editText2);
        seeuser=(TextView) findViewById(R.id.editText3);
        addimage=(ImageView)findViewById(R.id.imageView3);
        seeimage=(ImageView)findViewById(R.id.imageView5);
        addimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SecondActivity.this, AddUser.class));
                finish();
            }
        });
        seeimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SecondActivity.this, ShowUser.class));
                finish();
            }
        });
    }

    }

