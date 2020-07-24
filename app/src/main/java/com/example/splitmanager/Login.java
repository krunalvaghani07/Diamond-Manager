package com.example.splitmanager;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class Login extends AppCompatActivity {
    private EditText Name;
    private EditText Password;
    private Button Login;
    private Button register;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Name=(EditText)findViewById(R.id.editText2);
        register=(Button)findViewById(R.id.register);
        Password=(EditText)findViewById(R.id.editText3);
        Name.setText("Admin");
        Password.setText("1234");
        Login=(Button)findViewById(R.id.button);
        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validate(Name.getText().toString(),Password.getText().toString());
            }
        });


    }



    private void validate(String username, String userPassword)
    {
        if((username.equals("Admin")) && (userPassword.equals("1234")))
        {
            Intent intent = new Intent(this,SecondActivity.class);
            startActivity(intent);
        }
    }
}
