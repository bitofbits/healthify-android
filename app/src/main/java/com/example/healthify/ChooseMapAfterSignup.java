package com.example.healthify;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class ChooseMapAfterSignup extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_map_after_signup);

        ImageView placePicker = findViewById(R.id.placeMarkerActivity);
        ImageView autoComplete = findViewById(R.id.autoCompleteActivity);

        placePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChooseMapAfterSignup.this, PickPlace.class);
                intent.putExtra("user_email", getIntent().getStringExtra("user_email"));
                intent.putExtra("signupType", getIntent().getStringExtra("signupType"));
                System.out.println("Extra" + getIntent().getStringExtra("signupType"));
                startActivity(intent);
            }
        });

        autoComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChooseMapAfterSignup.this, Autocomplete.class);
                intent.putExtra("user_email", getIntent().getStringExtra("user_email"));
                intent.putExtra("signupType", getIntent().getStringExtra("signupType"));
                startActivity(intent);
            }
        });
    }
}
