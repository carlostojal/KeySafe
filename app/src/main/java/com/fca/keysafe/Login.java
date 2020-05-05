package com.fca.keysafe;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Login extends AppCompatActivity {

    private EditText pin;
    private TextView error;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        pin = findViewById(R.id.pin);
        error = findViewById(R.id.error);
    }

    @Override
    public void onBackPressed() {
    }

    public void login(View view) {
        if(new Helpers().getUser(this).getPin().equals(pin.getText().toString()))
            this.finish();
        else
            Toast.makeText(this, "Wrong PIN.", Toast.LENGTH_SHORT).show();
    }
}