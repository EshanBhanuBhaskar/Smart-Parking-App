package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class CustomerMainActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    FirebaseFirestore fstore;
    // map to add data in database
    Map<String, Object> parking_value_list = new HashMap<>();
    Map<String, Object> parking_value_list_2 = new HashMap<>();
    String userID; // to get user id of user
    int Bparking; //booked parking
    int tparking; //total parking

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer);

        mAuth = FirebaseAuth.getInstance();
        fstore = FirebaseFirestore.getInstance();
        userID = mAuth.getCurrentUser().getUid();


        TextView totalpark = (TextView) findViewById(R.id.totalpark_id);
        TextView cNumber_id = (TextView) findViewById(R.id.upcomingqueue_id);
        TextView tavaiable = (TextView) findViewById(R.id.walletmoney_id);


        // To add the car number
        ImageButton setting_button = (ImageButton) findViewById(R.id.wallet_id);
        setting_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), SettingActivity.class);
                view.getContext().startActivity(intent);
            }

        });

        //To open history activity
        ImageButton history_button = (ImageButton) findViewById(R.id.history_id);
        history_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(CustomerMainActivity.this, "History will be available soon", Toast.LENGTH_SHORT).show();
            }

        });

        //to refresh page
        ImageButton refresh_button = (ImageButton) findViewById(R.id.noti_id);
        refresh_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), CustomerMainActivity.class);
                view.getContext().startActivity(intent);
            }

        });

        // to update total booked parking
        DocumentReference docRef = fstore.collection("Buy_parking").document(userID);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Bparking= Integer.parseInt(document.getString("Bparking"));
                    } else {

                    }
                } else {
                        Bparking = 0;
                }
            }
        });

        // to get total parking from database
        DocumentReference docRef_3 = fstore.collection("Total_Parking").document("t_b_parking");
        docRef_3.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        tparking  = Integer.parseInt(document.getString("Tparking"));
                        tavaiable.setText(document.getString("Tparking")); // total parking seen by user
                    } else {

                    }
                } else {

                }
            }
        });


        // this button buy parking
        ImageButton queue_button = (ImageButton) findViewById(R.id.que_id);
        queue_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userID = mAuth.getCurrentUser().getUid();
                if(tparking>0){
                    String Bp = String.valueOf(Bparking + 1);
                    DocumentReference documentReference = fstore.collection("Buy_parking").document(userID);
                    parking_value_list.put("Bparking", Bp);
                    documentReference.set(parking_value_list);

                    DocumentReference documentReference_2 = fstore.collection("Total_Parking").document("t_b_parking");
                    parking_value_list_2.put("Tparking", String.valueOf(tparking-1));
                    documentReference_2.set(parking_value_list_2);
                }
                else{
                    Toast.makeText(CustomerMainActivity.this, "No more parking available", Toast.LENGTH_SHORT).show();
                }

                Intent intent = new Intent(view.getContext(), CustomerMainActivity.class);
                view.getContext().startActivity(intent);
            }

        });


        // User add car number and we retrieve it
        DocumentReference docRef_2 = fstore.collection("Car_number").document(userID);
        docRef_2.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        String cNumber= document.getString("Cnumber");
                        cNumber_id.setText(cNumber);
                    } else {
                      //  Toast.makeText(CustomerMainActivity.this, "Error!!!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                   // Toast.makeText(CustomerMainActivity.this, "Error!!!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // to set how much parking is booked
        DocumentReference docRef_4 = fstore.collection("Buy_parking").document(userID);
        docRef_4.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        String cNumber= document.getString("Bparking");
                        totalpark.setText(cNumber);
                    } else {
                      //  Toast.makeText(CustomerMainActivity.this, "Error!!!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                 //   Toast.makeText(CustomerMainActivity.this, "Error!!!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    @Override
    public void onBackPressed() {
        finish();
        Intent intent = new Intent(CustomerMainActivity.this, CustomerMainActivity.class);
        startActivity(intent);
    }
}
