package com.example.storemanager.exception;

import lombok.Data;

@Data
public class ValidationException extends RuntimeException{

    private String field;
    private String validationMessage;
    private String prettyErrorMessage;

    public ValidationException(String field, String validationMessage, String prettyErrorMessage) {
        super();
        this.field = field;
        this.validationMessage = validationMessage;
        this.prettyErrorMessage = prettyErrorMessage;
    }

}
