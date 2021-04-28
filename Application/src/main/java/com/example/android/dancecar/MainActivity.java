package com.example.android.dancecar;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.example.android.darktheme.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_firstpage);

        ImageButton nextPageButton = findViewById(R.id.driveButton);
        nextPageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeActivity();
            }
        });
    }

    //TODO: CODE TO CHANGE ACTIVITY -- MEIS
    private void changeActivity() {
        Intent intent = new Intent(this, SecondActivity.class);
        startActivity(intent);
    }

}

