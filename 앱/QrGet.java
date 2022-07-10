package com.example.assertqr;

import com.google.gson.annotations.SerializedName;

public class QrGet{
    @SerializedName("success")
    private Boolean success;
    @SerializedName("message")
    private String message;
    @SerializedName("error")
    private String error;
    @SerializedName("data")
    private String QRcode;

    public String getQRcode() {
        return QRcode;
    }

    public Boolean getSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public String getError() {
        return error;
    }


}
