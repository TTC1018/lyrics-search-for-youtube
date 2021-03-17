package com.example.splash_lyrics;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = new Intent(this, SplashActivity.class);
        startActivity(intent);

        Button yesButton = (Button) findViewById(R.id.yesBtn);
        Button noButton = (Button) findViewById(R.id.noBtn);
        yesButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent1 =  new Intent(MainActivity.this, DescriptionActivity.class);
                startActivity(intent1);
            }
        });
        noButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent1 =  new Intent(MainActivity.this, DescriptionActivity.class);
                startActivity(intent1);
            }
        });


    }
}