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

public class LoginActivity extends AppCompatActivity {

    private EditText usernameField;
    private EditText passwordField;
    private Button loginButton;
    private String username;
    private String password;
    private TextView registerLink;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_login);
        this.usernameField = (EditText) findViewById(R.id.usernameText);
        this.passwordField = (EditText) findViewById(R.id.passwordText);
        this.loginButton = (Button) findViewById(R.id.loginButton);
        this.registerLink = (TextView) findViewById(R.id.registerLink);

        this.registerLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(i);
                finish();
            }
        });

        this.loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username = usernameField.getText().toString();
                password = passwordField.getText().toString();

                if (username.length() == 0) {
                    usernameField.setError("This field cannot be empty");
                    return;
                }
                if (password.length() == 0) {
                    passwordField.setError("This field cannot be empty");
                    return;
                }
                LoginThread thread = new LoginThread();
                thread.start();

            }
        });

    }

    public void showProgressDialog() {
        progressDialog = new ProgressDialog(LoginActivity.this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Please wait...");
        progressDialog.show();
    }

    public void dissmissProgressDialog() {
        progressDialog.dismiss();
    }

    class LoginThread extends BaseThread {
        @Override
        public void run() {
            super.run();
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    showProgressDialog();
                }
            });

            User loginUser = new User(null, password, username);
            Message messageForServer = new Message(Type.LOGIN, JsonHelper.convertToJSON(loginUser), "", "", "");
            sendMessageToServer(messageForServer);
            try {
                String responseInJsonFormat = inputFromClient.readLine();
                Message responseMessage = JsonHelper.convertToObject(responseInJsonFormat, Message.class);
                boolean isUserAuthenticated = JsonHelper.convertToObject(responseMessage.getJsonData(), boolean.class);

                if (isUserAuthenticated) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            dissmissProgressDialog();
                        }
                    });
                    SharedPreferences sharedPref = getSharedPreferences("traveller", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putBoolean("isUserLogInned", true);
                    editor.putString("username", username);
                    editor.apply();
                    Intent i = new Intent(getApplicationContext(), DistrictActivity.class);
                    startActivity(i);
                    finish();
                } else {
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
