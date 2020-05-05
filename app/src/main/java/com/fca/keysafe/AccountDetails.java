package com.fca.keysafe;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;

public class AccountDetails extends AppCompatActivity {

    Button save;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_details);

        boolean creating_new = getIntent().getExtras().getBoolean("creating_new");

        if(creating_new)
            setTitle("Add Account");

        save = findViewById(R.id.save);
        save.setClickable(false);
    }
}
