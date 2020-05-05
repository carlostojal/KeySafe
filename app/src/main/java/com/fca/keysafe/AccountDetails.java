package com.fca.keysafe;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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
    private Button delete;
    private Button generate_password;

    private boolean creating_new = true;

    private Bundle extras;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_details);

        serviceName = findViewById(R.id.service);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        save = findViewById(R.id.save);
        delete = findViewById(R.id.delete);
        generate_password = findViewById(R.id.generate_password);

        save.setVisibility(View.GONE);

        extras = getIntent().getExtras();

        if(extras.containsKey("creating_new"))
            creating_new = extras.getBoolean("creating_new");

        if(extras.getBoolean("creating_new")) {
            setTitle("Add Account");
            delete.setVisibility(View.GONE);
        } else {
            generate_password.setVisibility(View.GONE);
            if (extras.containsKey("serviceName")) {
                serviceName.setText(extras.getString("serviceName"));
                setTitle(extras.getString("serviceName") + " Account");
            }
            if (extras.containsKey("username"))
                username.setText(extras.getString("username"));
            if (extras.containsKey("password"))
                password.setText(extras.getString("password"));
        }

        serviceName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                save.setVisibility(View.VISIBLE);
            }
        });

        username.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                save.setVisibility(View.VISIBLE);
            }
        });

        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                save.setVisibility(View.VISIBLE);
            }
        });
    }

    public void save(View view) {
        if(serviceName.getText().toString().length() > 0 && username.getText().toString().length() > 0 && password.getText().toString().length() > 0) {
            ArrayList<Account> accounts = new Helpers().readAccounts(this);

            String compare_to_service_name;
            String compare_to_username;

            if (extras.containsKey("serviceName"))
                compare_to_service_name = extras.getString("serviceName");
            else
                compare_to_service_name = serviceName.getText().toString();

            if(extras.containsKey("username"))
                compare_to_username = extras.getString("username");
            else
                compare_to_username = username.getText().toString();

            boolean exists = false;
            boolean success = false;
            for (int i = 0; i < accounts.size() && !exists; i++) {
                if (accounts.get(i).getServiceName().equals(compare_to_service_name) && accounts.get(i).getUsername().equals(compare_to_username)) {
                    exists = true;
                    if (creating_new) {
                        Toast.makeText(this, "That service name and username are already registered.", Toast.LENGTH_SHORT).show();
                    } else {
                        accounts.get(i).setServiceName(serviceName.getText().toString());
                        accounts.get(i).setUsername(username.getText().toString());
                        accounts.get(i).setPassword(password.getText().toString());
                        success = true;
                    }
                }
            }

            if (!exists) {
                accounts.add(new Account(serviceName.getText().toString(), username.getText().toString(), password.getText().toString()));
                success = true;
            }

            if(success) {
                if (new Helpers().saveAccounts(this, accounts)) {
                    Toast.makeText(this, "Account saved successfully.", Toast.LENGTH_SHORT).show();
                    this.finish();
                } else {
                    Toast.makeText(this, "Error saving account.", Toast.LENGTH_SHORT).show();
                }
            }
        } else {
            Toast.makeText(this, "At least one field is empty.", Toast.LENGTH_SHORT).show();
        }
    }

    public void delete(View view) {
        ArrayList<Account> accounts = new Helpers().readAccounts(this);

        String compare_to;

        if (extras.containsKey("serviceName"))
            compare_to = extras.getString("serviceName");
        else
            compare_to = serviceName.getText().toString();

        for(int i = 0; i < accounts.size(); i++) {
            if(accounts.get(i).getServiceName().equals(compare_to)) {
                accounts.remove(accounts.get(i));
                break;
            }
        }

        if(new Helpers().saveAccounts(this, accounts))
            this.finish();
        else
            Toast.makeText(this, "Error deleting account.", Toast.LENGTH_SHORT).show();
    }

    public void generatePassword(View view) {
        password.setText(new Helpers().generatePassword(8));
    }
}
