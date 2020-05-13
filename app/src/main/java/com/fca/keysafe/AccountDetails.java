package com.fca.keysafe;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class AccountDetails extends AppCompatActivity {

    private TextView previewImageLabel;
    private ImageView previewImage;
    private EditText serviceName;
    private EditText username;
    private EditText password;
    private TextView last_edited;
    private Button save;
    private Button delete;
    private Button generate_password;

    private String last_service_name;
    private String preview_img_url;

    private String action = "creating_new";

    private Bundle extras;

    ArrayList<Account> accounts;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_details);

        previewImageLabel = findViewById(R.id.preview_image_label);
        previewImage = findViewById(R.id.preview_image);
        serviceName = findViewById(R.id.service);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        last_edited = findViewById(R.id.last_edited);
        save = findViewById(R.id.save);
        delete = findViewById(R.id.delete);
        generate_password = findViewById(R.id.generate_password);

        save.setVisibility(View.GONE);

        accounts = new Helpers().readAccounts(this);

        last_service_name = serviceName.getText().toString();

        extras = getIntent().getExtras();

        if (extras.containsKey("action"))
            action = extras.getString("action");

        if (action.equals("creating_new")) {
            setTitle("Add Account");
            delete.setVisibility(View.GONE);
            previewImageLabel.setVisibility(View.GONE);
            previewImage.setVisibility(View.GONE);
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
            if (extras.containsKey("last_edited"))
                last_edited.setText("Last edited on " + extras.getString("last_edited"));
            if(extras.containsKey("preview_image"))
                new ImageFromUrl(previewImage).execute(extras.getString("preview_image"));
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
                update();
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
                update();
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
                update();
            }
        });
    }

    public void save(View view) {
        boolean success = false;
        // if no field is empty
        if (serviceName.getText().toString().length() > 0 && username.getText().toString().length() > 0 && password.getText().toString().length() > 0) {
            ArrayList<Account> accounts = new Helpers().readAccounts(this);

            if (action.equals("creating_new")) {
                Account account = null;
                for (Account account1 : accounts) {
                    if (account1.getServiceName().equals(serviceName.getText().toString()) && account1.getUsername().equals(username.getText().toString()))
                        account = account1;
                }
                if (account != null) { // if the requested account already exists
                    Toast.makeText(this, "An account with this service name and username already exist.", Toast.LENGTH_SHORT).show();
                } else {
                    account = new Account();
                    account.setServiceName(serviceName.getText().toString());
                    account.setUsername(username.getText().toString());
                    account.setPassword(password.getText().toString());
                    account.setLastChanged(new Helpers().getStringDate());
                    account.setPreviewImgUrl(preview_img_url);
                    accounts.add(account);
                    success = true;
                }
            } else {
                boolean checked = true;
                for (Account account : accounts) {
                    Log.d(null, "service: " + account.getServiceName() + " " + serviceName.getText().toString() + "\nusername: " + account.getUsername() + " " + username.getText().toString());
                    if((account.getServiceName().equals(serviceName.getText().toString()) && account.getUsername().equals(username.getText().toString())) && (!extras.getString("serviceName").equals(serviceName.getText().toString()) || !extras.getString("username").equals(username.getText().toString()))) {
                        Toast.makeText(this, "An account with this service name and username already exist.", Toast.LENGTH_SHORT).show();
                        checked = false;
                        break;
                    }
                }

                if(checked) {
                    for(Account account : accounts) {
                        if(account.getServiceName().equals(extras.getString("serviceName")) && account.getUsername().equals(extras.getString("username"))) {
                            account.setServiceName(serviceName.getText().toString());
                            account.setUsername(username.getText().toString());
                            account.setPassword(password.getText().toString());
                            account.setLastChanged(new Helpers().getStringDate());
                            account.setPreviewImgUrl(preview_img_url);
                            success = true;
                            break;
                        }
                    }
                }
            }

            if (success) {
                String message_text = action.equals("creating_new") ? "Account created successfully." : "Account updated successfully.";
                if (new Helpers().saveAccounts(this, accounts))
                    this.finish();
                else // an error occurred saving the accounts to the file
                    message_text = "Error";
                Toast.makeText(this, message_text, Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "At least one field is empty.", Toast.LENGTH_SHORT).show();
        }
    }

    public void delete(View view) {

        String compare_to;

        if (extras.containsKey("serviceName"))
            compare_to = extras.getString("serviceName");
        else
            compare_to = serviceName.getText().toString();

        for (int i = 0; i < accounts.size(); i++) {
            if (accounts.get(i).getServiceName().equals(compare_to)) {
                accounts.remove(accounts.get(i));
                break;
            }
        }

        if (new Helpers().saveAccounts(this, accounts))
            this.finish();
        else
            Toast.makeText(this, "Error deleting account.", Toast.LENGTH_SHORT).show();
    }

    public void generatePassword(View view) {
        password.setText(new Helpers().generatePassword(8));
    }

    private void update() {
        if (!action.equals("creating_new")) {
            if (!serviceName.getText().toString().equals(extras.getString("serviceName")) || !username.getText().toString().equals(extras.getString("username")) || !password.getText().toString().equals(extras.getString("password")))
                save.setVisibility(View.VISIBLE);
            else
                save.setVisibility(View.GONE);
        } else {
            save.setVisibility(View.VISIBLE);
        }
        previewImageLabel.setVisibility(View.VISIBLE);
        previewImage.setVisibility(View.VISIBLE);
        if(!last_service_name.equals(serviceName.getText().toString()))
            updatePreviewImage();
        last_service_name = serviceName.getText().toString();
    }

    private void updatePreviewImage() {

        RequestQueue requestQueue = Volley.newRequestQueue(this);

        String url = "https://pixabay.com/api/?key=16532900-508eb48a9692e4da4b4116373&q=" + serviceName.getText().toString() + "+logo";

        Log.d(null, url);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    Log.d(null, response.toString());
                    String url = response.getJSONArray("hits").getJSONObject(0).getString("webformatURL");
                    new ImageFromUrl(previewImage).execute(url);
                    updateAccountPreviewImageUrl(url);
                    preview_img_url = url;
                } catch (JSONException e) {
                    Log.d(null, "Error parsing API response");
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(null, "Error requesting API");
            }
        });

        requestQueue.add(jsonObjectRequest);
    }

    private void updateAccountPreviewImageUrl(String url) {

        String compare_to;

        if (extras.containsKey("serviceName"))
            compare_to = extras.getString("serviceName");
        else
            compare_to = serviceName.getText().toString();

        for (int i = 0; i < accounts.size(); i++) {
            if (accounts.get(i).getServiceName().equals(compare_to)) {
                accounts.get(i).setPreviewImgUrl(url);
                break;
            }
        }
    }
}