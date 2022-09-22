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
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class SignupActivity extends AppCompatActivity {

    EditText Email;
    EditText Password;
    Button login;
    Button signup;
    FirebaseAuth mAuth;
    FirebaseFirestore fstore;
    EditText txtname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        // Try to remove
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mAuth = FirebaseAuth.getInstance();
        fstore = FirebaseFirestore.getInstance();


        Email = findViewById(R.id.email_text);
        Password = findViewById(R.id.password_text);
        login = findViewById(R.id.login_btn);
        signup = findViewById(R.id.signup_btn);
        txtname = findViewById(R.id.name_text);


        //if already signup go to login
        login.setOnClickListener((View v) -> {
            startActivity(new Intent(SignupActivity.this, LoginActivity.class));
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String emailID = Email.getText().toString();
                String password = Password.getText().toString();
                String name_string = txtname.getText().toString();

                //to kept login if once logged
                SharedPreferences preferences = getSharedPreferences("checkbox", MODE_PRIVATE);
                String checkbox = preferences.getString("remember","");
                if(checkbox.equals("true")){
                    Intent intent = new Intent(SignupActivity.this,CustomerMainActivity.class);
                    startActivity(intent);
                }else if(checkbox.equals("false")) {
                    Toast.makeText(SignupActivity.this, "Please sign in", Toast.LENGTH_SHORT).show();
                }

                //conditions to login
                if (name_string == null) {
                    txtname.setError("Enter your Name");
                    txtname.requestFocus();
                } else if (emailID == null) {
                    Email.setError("Email ID cannot be Empty");
                    Email.requestFocus();
                } else if (password == null) {
                    Password.setError("Password cannot be Empty");
                    Password.requestFocus();
                } else if (emailID != null && password != null && name_string != null) {
                    mAuth.createUserWithEmailAndPassword(emailID, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(SignupActivity.this, "SignUp Successful!!!", Toast.LENGTH_SHORT).show();
                                SharedPreferences pref = getSharedPreferences("ActivityPREF", Context.MODE_PRIVATE);
                                SharedPreferences.Editor edt = pref.edit();
                                edt.putBoolean("activity_executed", true);
                                edt.commit();
                                SharedPreferences preferences = getSharedPreferences("checkbox",MODE_PRIVATE);
                                SharedPreferences.Editor editor = preferences.edit();
                                editor.putString("remember","true");
                                editor.apply();
                                startActivity(new Intent(SignupActivity.this, CustomerMainActivity.class));
                                finish();
                            }
                            else {
                                Toast.makeText(SignupActivity.this, "It Failed!!!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                }


            }
        });
    }

}
