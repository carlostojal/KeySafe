package com.fca.keysafe;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

public class AccountDetails extends AppCompatActivity {

    private EditText serviceName;
    private EditText username;
    private EditText password;
    private Button save;

    private boolean creating_new;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_details);

        creating_new = getIntent().getExtras().getBoolean("creating_new");

        if(creating_new)
            setTitle("Add Account");

        serviceName = findViewById(R.id.service);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        save = findViewById(R.id.save);
    }

    public void save(View view) {
        ArrayList<Account> accounts = new Helpers().readAccounts(this);

        boolean exists = false;
        for(int i = 0; i < accounts.size() && !exists; i++) {
            if(accounts.get(i).getServiceName().equals(serviceName.getText().toString())) {
                exists = true;
                if(creating_new) {
                    Toast.makeText(this, "That service name is already registered.", Toast.LENGTH_SHORT).show();
                } else {
                    accounts.get(i).setServiceName(serviceName.getText().toString());
                    accounts.get(i).setUsername(username.getText().toString());
                    accounts.get(i).setPassword(password.getText().toString());
                }
            }
        }

        if(!exists) {
            accounts.add(new Account(serviceName.getText().toString(), username.getText().toString(), password.getText().toString()));
        }

        if(new Helpers().saveAccounts(this, accounts)) {
            Toast.makeText(this, "Account saved successfully.", Toast.LENGTH_SHORT).show();
            this.finish();
        } else {
            Toast.makeText(this, "Error saving account.", Toast.LENGTH_SHORT).show();
        }
    }
}
