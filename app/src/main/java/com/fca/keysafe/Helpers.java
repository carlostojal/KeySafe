package com.fca.keysafe;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.SecureRandom;
import java.util.ArrayList;

public class Helpers {

    public String generatePassword(int len) {
        String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        SecureRandom rnd = new SecureRandom();

        StringBuilder sb = new StringBuilder(len);
        for(int i = 0; i < len; i++)
            sb.append(AB.charAt(rnd.nextInt(AB.length())));
        return sb.toString();
    }

    public User getUser(Context context) {
        User user = new User();
        String path = context.getFilesDir().toString();
        try {
            FileInputStream fileInputStream = new FileInputStream(new File(path + "/user.csv"));
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            String s = null;
            while((s = bufferedReader.readLine()) != null) {
                user.setPin(s);
            }
            fileInputStream.close();
            bufferedReader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return user;
    }

    public boolean createUser(Context context, User user) {
        String path = context.getFilesDir().toString();
        try {
            File file = new File(path + "/user.csv");
            file.createNewFile();
            FileOutputStream fileOutputStream = new FileOutputStream(file, true);
            fileOutputStream.write((user.getPin()).getBytes());
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public ArrayList<Account> readAccounts(Context context) {
        ArrayList<Account> accounts = new ArrayList<>();
        String path = context.getFilesDir().toString();
        try {
            FileInputStream fileInputStream = new FileInputStream(new File(path + "/accounts.csv"));
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            StringBuilder stringBuilder = new StringBuilder();

            String s = null;
            while((s = bufferedReader.readLine()) != null) {
                accounts.add(new Account(s.split(";")[0], s.split(";")[1], s.split(";")[2]));
            }
            fileInputStream.close();
            s = stringBuilder.toString();
            bufferedReader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return accounts;
    }

    public boolean saveAccounts(Context context, ArrayList<Account> accounts) {
        String path = context.getFilesDir().toString();
        try {
            File file = new File(path + "/accounts.csv");
            if(file.exists())
                file.delete();
            file.createNewFile();
            FileOutputStream fileOutputStream = new FileOutputStream(file, true);
            for(int i = 0; i < accounts.size(); i++)
                fileOutputStream.write((accounts.get(i).getServiceName() + ";" + accounts.get(i).getUsername() + ";" + accounts.get(i).getPassword() + "\n").getBytes());
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
