package com.fca.keysafe;

public class User {

    private String pin;

    public User() {
        this.pin = "0000";
    }

    public User(String pin) {
        this.pin = pin;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }
}
