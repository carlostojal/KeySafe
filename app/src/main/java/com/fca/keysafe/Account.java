package com.fca.keysafe;

import java.util.Date;

public class Account {

    private String serviceName;
    private String username;
    private String password;
    private String lastChanged;

    public Account(String serviceName, String username, String password, String lastChanged) {
        this.serviceName = serviceName;
        this.username = username;
        this.password = password;
        this.lastChanged = lastChanged;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getLastChanged() {
        return lastChanged;
    }

    public void setLastChanged(String lastChanged) {
        this.lastChanged = lastChanged;
    }
}
