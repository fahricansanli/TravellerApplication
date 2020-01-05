package com.example.istanbultravelapplication.models;

import androidx.annotation.Nullable;

public class User {
    private String name;
    private String password;
    private String username;

    public User(String name, String password,String username) {
        this.name = name;
        this.password = password;
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public String getUsername() {
        return username;
    }
}
