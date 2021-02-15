package com.example.lyricssearchforyoutube;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class MainActivity extends AppCompatActivity {
    private Intent intent;
    private final String packageName = "com.google.android.youtube";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        intent = this.getPackageManager().getLaunchIntentForPackage(packageName);

        ImageButton imagebtn = (ImageButton) findViewById(R.id.imagebtn1);
        imagebtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                MainActivity.this.startActivity(intent);

            }
        });
    }
}