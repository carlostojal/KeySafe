package com.fca.keysafe;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.autofill.AutofillManager;
import android.widget.EditText;
import android.widget.Toast;

public class CreatePin extends AppCompatActivity {

    private EditText pin;
    private EditText confirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_pin);
        setTitle("Create PIN");

        pin = findViewById(R.id.pin);
        confirm = findViewById(R.id.confirm);
    }

    // can't go back to home screen
    @Override
    public void onBackPressed() {
    }

    public void createPin(View view) {
        if(pin.getText().length() == 4) {
            if (pin.getText().toString().equals(confirm.getText().toString())) {
                if (true /*new Helpers().createUser(this, new User(pin.getText().toString()))*/) {
                    Toast.makeText(this, "PIN created successfully.", Toast.LENGTH_SHORT).show();
                    this.finish();
                    startActivity(new Intent(this, EnableAutofill.class));
                } else {
                    Toast.makeText(this, "Error creating PIN.", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "The PINs are not equal.", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "The PIN must be 4 digits long.", Toast.LENGTH_SHORT).show();
        }
    }
}
