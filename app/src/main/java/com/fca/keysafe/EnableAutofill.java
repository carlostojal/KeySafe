package com.fca.keysafe;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;

public class EnableAutofill extends AppCompatActivity {

    Button goToSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enable_autofill);

        goToSettings = findViewById(R.id.go_to_settings);

        setTitle("Enable autofill functionality");
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void enableAutofill(View view) {
        startActivity(new Intent(Settings.ACTION_SETTINGS));
        goToSettings.setText(R.string.done);
        goToSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishActivity(v);
            }
        });
    }

    public void finishActivity(View view) {
        this.finish();
    }
}
