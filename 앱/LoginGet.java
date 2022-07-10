package com.example.assertqr;

import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;

public class LoginGet {
    @SerializedName("success")
    private Boolean success;
    @SerializedName("message")
    private String message;
    @SerializedName("error")
    private String error;
    @SerializedName("data")
    private String token;

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setErrors(String error) {
        this.error = error;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Boolean getSuccess(){
        return success;
    }

    public String getMessage() {
        return message;
    }

    public String getErrors() {
        return error;
    }

    public String getToken() {
        return token;
    }
}
