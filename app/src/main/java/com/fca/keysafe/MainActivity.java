package com.fca.keysafe;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // the user was not created or the pin is default
        if(new Helpers().getUser(this).getPin().equals("0000")) {
            Intent intent = new Intent(this, CreatePin.class);
            startActivity(intent);
        } else { // login
            Intent intent = new Intent(this, Login.class);
            startActivity(intent);
        }
    }
}
