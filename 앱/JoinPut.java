package com.example.assertqr;

public class JoinPut {
    private final String name;
    private final String department;
    private final String username;
    private final String password;
    private final String passwordConfirmation;
    private static String access="F";

    public JoinPut(String name,String department, String studentID,String password,String password2) {
        this.name = name;
        this.department = department;
        this.username = studentID;
        this.password = password;
        this.passwordConfirmation = password2;
    }
}
