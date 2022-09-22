package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class SettingActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    FirebaseFirestore fstore;
    EditText cNumber;
    String userID;
    Map<Object,String > number_value_list =  new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        cNumber = (EditText) findViewById(R.id.car_1_id);
        mAuth = FirebaseAuth.getInstance();
        fstore = FirebaseFirestore.getInstance();

        // To add car numebr
        Button ADD_button_2 = (Button) findViewById(R.id.ADD_button_2);
        ADD_button_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userID = mAuth.getCurrentUser().getUid();
                DocumentReference documentReference = fstore.collection("Car_number").document(userID);
                String data = cNumber.getText().toString();
                number_value_list.put("Cnumber",data);
                documentReference.set(number_value_list);
                Intent intent = new Intent(v.getContext() , CustomerMainActivity.class);
                v.getContext().startActivity(intent);
            }
        });

        // To get back to customer activity
        ImageButton backButton = (ImageButton) findViewById(R.id.back_button_1);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext() , CustomerMainActivity.class);
                v.getContext().startActivity(intent);

            }
        });

    }

    @Override
    public void onBackPressed() {
        finish();
        Intent intent = new Intent(SettingActivity.this, CustomerMainActivity.class);
        startActivity(intent);
    }
}
