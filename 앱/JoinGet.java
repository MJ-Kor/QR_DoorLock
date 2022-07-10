package com.example.assertqr;

import com.google.gson.annotations.SerializedName;

public class JoinGet {
    @SerializedName("success")
    private Boolean success;
    @SerializedName("message")
    private String message;
    @SerializedName("error")
    private String error;
    @SerializedName("data")
    private String data;

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setErrors(String error) {
        this.error = error;
    }

    public void setData(String data) {
        this.data = data;
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

    public String getData() {
        return data;
    }


}
