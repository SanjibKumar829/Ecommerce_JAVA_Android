package com.sanjib.koala.networking.model;

import com.google.gson.annotations.SerializedName;

public class RegisterRequest {
    public String name;
    public String email;
    public String password;
    @SerializedName("c_password")
    public String confirmPassword;
}
