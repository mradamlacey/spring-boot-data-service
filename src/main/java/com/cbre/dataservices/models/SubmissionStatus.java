package com.cbre.dataservices.models;

public class SubmissionStatus {

    private int statusCode;
    private String message;

    public SubmissionStatus(int code, String message){

        this.statusCode = code;
        this.message = message;

    }

    public int getStatusCode() { return statusCode; }
    public String getMessage() { return message; }
}
