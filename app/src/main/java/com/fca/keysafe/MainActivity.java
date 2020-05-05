package com.fca.keysafe;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ListView list;

    private ArrayList<Account> accounts;

    @Override
    public void onResume() {
        super.onResume();
        accounts = new Helpers().readAccounts(this);
        ArrayAdapter accountAdapter = new AccountAdapter(this, accounts);
        list.setAdapter(accountAdapter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        list = findViewById(R.id.list);

        // the user was not created or the pin is default
        if(new Helpers().getUser(this).getPin().equals("0000")) {
            Intent intent = new Intent(this, CreatePin.class);
            startActivity(intent);
        } else { // login
            Intent intent = new Intent(this, Login.class);
            startActivity(intent);
        }

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Account selectedAccount = (Account) parent.getItemAtPosition(position);

                Intent intent = new Intent(MainActivity.this, AccountDetails.class);
                intent.putExtra("creating_new", false);
                intent.putExtra("serviceName", selectedAccount.getServiceName());
                intent.putExtra("username", selectedAccount.getUsername());
                intent.putExtra("password", selectedAccount.getPassword());
                startActivity(intent);
            }
        });
    }

    public void addAccount(View view) {
        Intent intent = new Intent(this, AccountDetails.class);
        intent.putExtra("creating_new", true);
        startActivity(intent);
    }
}
