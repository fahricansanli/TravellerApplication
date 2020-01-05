package com.example.istanbultravelapplication.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.istanbultravelapplication.R;
import com.example.istanbultravelapplication.models.BaseThread;
import com.example.istanbultravelapplication.models.Message;
import com.example.istanbultravelapplication.models.Type;
import com.example.istanbultravelapplication.models.User;
import com.example.istanbultravelapplication.util.JsonHelper;

import java.io.IOException;

public class RegisterActivity extends AppCompatActivity {

    private EditText usernameField;
    private EditText nameField;
    private EditText passwordField;
    private Button registerButton;
    private String username;
    private String name;
    private String password;
    private TextView loginLink;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_register);
        this.usernameField = (EditText) findViewById(R.id.usernameText);
        this.nameField = (EditText) findViewById(R.id.nameText);
        this.passwordField = (EditText) findViewById(R.id.passwordText);
        this.registerButton = (Button) findViewById(R.id.registerButton);
        this.loginLink = (TextView) findViewById(R.id.loginLink);

        this.loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),LoginActivity.class);
                startActivity(i);
                finish();
            }
        });

        this.registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username = usernameField.getText().toString();
                name = nameField.getText().toString();
                password = passwordField.getText().toString();

                if(username.length()==0){
                    usernameField.setError("This field cannot be empty");
                    return;
                }
                if(name.length()==0){
                    nameField.setError("This field cannot be empty");
                    return;
                }
                if(password.length()==0){
                    passwordField.setError("This field cannot be empty");
                    return;
                }
                final RegisterThread thread = new RegisterThread();
                thread.start();

            }
        });

    }
    public void showProgressDialog(){
        progressDialog = new ProgressDialog(RegisterActivity.this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Please wait...");
        progressDialog.show();
    }

    public void dissmissProgressDialog(){
        progressDialog.dismiss();
    }

    class RegisterThread extends BaseThread {
        @Override
        public void run() {
            super.run();
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    showProgressDialog();
                }
            });
            User registerUser = new User(name,password,username);
            Message messageForServer = new Message(Type.REGISTER, JsonHelper.convertToJSON(registerUser),"","","");
            sendMessageToServer(messageForServer);
            try {
                String responseInJsonFormat = inputFromClient.readLine();
                Message responseMessage = JsonHelper.convertToObject(responseInJsonFormat,Message.class);
                boolean isUserRegistered = JsonHelper.convertToObject(responseMessage.getJsonData(),boolean.class);

                if(isUserRegistered){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            dissmissProgressDialog();
                        }
                    });
                    SharedPreferences sharedPref = getSharedPreferences("traveller", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putBoolean("isUserLogInned",true);
                    editor.putString("username",username);
                    editor.apply();
                    Intent i = new Intent(getApplicationContext(),DistrictActivity.class);
                    startActivity(i);
                    finish();
                }
                else{
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            dissmissProgressDialog();
                        }
                    });
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
