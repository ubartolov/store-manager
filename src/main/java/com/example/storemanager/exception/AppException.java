package com.example.storemanager.exception;

import lombok.Data;

@Data
public class AppException extends RuntimeException {

    private String prettyErrorMessage;

    public AppException(String prettyErrorMessage) {
        super();
        this.prettyErrorMessage = prettyErrorMessage;
    }

}
