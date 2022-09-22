package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends MainActivity {


    EditText emailid;
    EditText password;
    Button login_l;
    Button signup_l;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailid = findViewById(R.id.email_text_logino);
        password = findViewById(R.id.password_text_logino);
        login_l = findViewById(R.id.login_logino);
        signup_l = findViewById(R.id.signup_logino);

        //to kept logged in
        SharedPreferences preferences = getSharedPreferences("checkbox", MODE_PRIVATE);
        String checkbox = preferences.getString("remember","");
        if(checkbox.equals("true")){
            Intent intent = new Intent(LoginActivity.this,CustomerMainActivity.class);
            startActivity(intent);
        }else if(checkbox.equals("false")) {
            Toast.makeText(this, "Please sign in", Toast.LENGTH_SHORT).show();
        }

        //to go to signup activity
        signup_l.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this,SignupActivity.class));
            }
        });

        login_l.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email_string = emailid.getText().toString();
                String password_string = password.getText().toString();
                firebaseAuth = FirebaseAuth.getInstance();

                if(email_string.isEmpty())
                {
                    emailid.setError("Email ID cannot be Empty");
                    emailid.requestFocus();
                }
                else if (password_string.isEmpty())
                {
                    password.setError("Password cannot be Empty");
                    password.requestFocus();
                }
                else if(!email_string.isEmpty() && !password_string.isEmpty())
                {
                    firebaseAuth.signInWithEmailAndPassword(email_string,password_string).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful())
                            {
                                Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(LoginActivity.this,CustomerMainActivity.class));
                                finish();
                                //pass an activity
                                SharedPreferences pref = getSharedPreferences("ActivityPREF", Context.MODE_PRIVATE);
                                SharedPreferences.Editor edt = pref.edit();
                                edt.putBoolean("activity_executed", true);
                                edt.commit();
                                SharedPreferences preferences = getSharedPreferences("checkbox",MODE_PRIVATE);
                                SharedPreferences.Editor editor = preferences.edit();
                                editor.putString("remember","true");
                                editor.apply();
                            }
                            else
                            {
                                Toast.makeText(LoginActivity.this, "Login Failed - Invalid Details", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
    }
}
