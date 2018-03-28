package com.manju.alex.flylight.ui;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.manju.alex.flylight.R;

public class FlyLightMainActivity extends AppCompatActivity {

    FirebaseAuth mAuth = null;

    Button mSignOut = null;

    FloatingActionButton mAddNewDeviceBtn = null;

    DatabaseReference mRoomsRef;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fly_light_main);

        mAuth = FirebaseAuth.getInstance();

        mSignOut = findViewById(R.id.btn_signout);

        mAddNewDeviceBtn = findViewById(R.id.fab_btn_add_new_device);
        mAddNewDeviceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addNewDevicePage = new Intent(getApplicationContext(), AddNewDeviceActivity.class);
                startActivity(addNewDevicePage);
            }
        });
        mSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.signOut();
                Intent signInActivity = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(signInActivity);
                finish();

            }
        });

        /*
        mRoomsRef = FirebaseDatabase.getInstance().getReference();
        Log.d("data", mRoomsRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("areas").toString());

               Log.d("User ID", FirebaseAuth.getInstance().getCurrentUser().getUid());

        Query ref = mRoomsRef.child("oszfd1JNxNcQfEzR9xFBEwNN99r1").child("areas");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("data2", dataSnapshot.toString());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });*/




    }
}
