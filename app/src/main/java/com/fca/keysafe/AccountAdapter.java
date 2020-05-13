package com.fca.keysafe;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class AccountAdapter extends ArrayAdapter<Account> {

    private final Context context;
    private final ArrayList<Account> accounts;

    public AccountAdapter(Context context, ArrayList<Account> accounts) {
        super(context, R.layout.account, accounts);
        this.context = context;
        this.accounts = accounts;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = layoutInflater.inflate(R.layout.account, parent, false);

        ImageView previewImage = (ImageView) rowView.findViewById(R.id.preview_image);
        TextView serviceName = (TextView) rowView.findViewById(R.id.service_name);
        TextView username = (TextView) rowView.findViewById(R.id.username_label);
        TextView password = (TextView) rowView.findViewById(R.id.password_label);

        StringBuilder passwordBuilder = new StringBuilder();
        for(int i = 0; i < accounts.get(position).getPassword().length(); i++)
            passwordBuilder.append("*");

        serviceName.setText(accounts.get(position).getServiceName());
        username.setText(accounts.get(position).getUsername());
        password.setText(passwordBuilder.toString());
        new ImageFromUrl(previewImage).execute(accounts.get(position).getPreviewImgUrl());

        return rowView;
    }
}
