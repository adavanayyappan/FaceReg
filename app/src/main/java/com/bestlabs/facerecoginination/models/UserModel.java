package com.bestlabs.facerecoginination.models;

import com.google.gson.annotations.SerializedName;



public class UserModel {
    @SerializedName("name")
    private String name;

    @SerializedName("email")
    private String email;

    // Add other fields as needed

    // Constructors
    public UserModel(String name, String email) {
        this.name = name;
        this.email = email;
        // Initialize other fields as needed
    }

    // Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    // Add getters and setters for other fields

    // You can override toString() for debugging purposes
    @Override
    public String toString() {
        return "UserProfile{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                // Add other fields to the string representation
                '}';
    }
}

