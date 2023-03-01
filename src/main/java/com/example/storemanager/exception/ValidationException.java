package com.example.storemanager.exception;

import lombok.Data;

@Data
public class ValidationException extends AppException {

    private String field;
    private String validationMessage;

    public ValidationException(String field, String validationMessage, String prettyErrorMessage) {
        super(prettyErrorMessage);
        this.field = field;
        this.validationMessage = validationMessage;
    }

    public ValidationException(String validationMessage, String prettyErrorMessage) {
        super(prettyErrorMessage);
        this.validationMessage = validationMessage;
    }

    public ValidationException(String prettyErrorMessage) {
        super(prettyErrorMessage);
    }

}
