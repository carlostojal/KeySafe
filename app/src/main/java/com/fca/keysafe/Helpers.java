package com.fca.keysafe;

import android.os.Environment;

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

    private String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/keysafe";

    public String generatePassword(int len) {
        String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        SecureRandom rnd = new SecureRandom();

        StringBuilder sb = new StringBuilder(len);
        for(int i = 0; i < len; i++)
            sb.append(AB.charAt(rnd.nextInt(AB.length())));
        return sb.toString();
    }

    public User getUser() {
        User user = new User();
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

    public void createUser(User user) {
        try {
            File file = new File(path + "/user.csv");
            if(!file.exists())
                file.createNewFile();
            FileOutputStream fileOutputStream = new FileOutputStream(file, true);
            fileOutputStream.write((user.getPin()).getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Account> readAccounts() {
        ArrayList<Account> accounts = new ArrayList<>();
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

    public void saveAccount(Account account) {
        try {
            File file = new File(path + "/accounts.csv");
            if(!file.exists())
                file.createNewFile();
            FileOutputStream fileOutputStream = new FileOutputStream(file, true);
            fileOutputStream.write((account.getServiceName() + ";" + account.getUsername() + ";" + account.getPassword()).getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
