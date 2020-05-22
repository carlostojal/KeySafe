package com.fca.keysafe;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Login extends AppCompatActivity {

    private EditText pin;
    private TextView error;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        pin = findViewById(R.id.pin);
        error = findViewById(R.id.error);

        pin.setAutofillHints("pin", "password");
        pin.setHint("PIN");

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

    public void reset(){
        setContentView(R.layout.activity_create_pin);
    }

}
