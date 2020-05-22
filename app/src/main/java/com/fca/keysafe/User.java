package com.fca.keysafe;

public class User {

    private String pin;

    public User() {
        this.pin = "0000";
    }

    public User(String pin) {
        String enc = new String (encrypt(pin.getBytes()));
        this.pin = enc;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public String encrypt(byte[] frase){
        byte[] enc = new byte[frase.length];
        for(int i=0;i<frase.length;i++){
            enc[i] = (byte) ((i%2==0) ? frase[i]+1 : frase[i]-1);
        }
        return enc.toString();
    }

    public String decrypt(byte[] frase){
        byte[] enc = new byte[frase.length];
        for(int i=0;i<frase.length;i++){
            enc[i] = (byte) ((i%2==0) ? frase[i]-1 : frase[i]+1);
        }
        return enc.toString();
    }

}

