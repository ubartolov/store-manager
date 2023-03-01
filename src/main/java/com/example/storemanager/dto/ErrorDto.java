package com.example.storemanager.dto;

import lombok.Data;

@Data
public class ErrorDto {

    private String code;
    private String exceptionMessage;
    private String prettyErrorMessage;

    public ErrorDto(String code, String exceptionMessage, String prettyErrorMessage) {
        this.code = code;
        this.exceptionMessage = exceptionMessage;
        this.prettyErrorMessage = prettyErrorMessage;
    }
    public ErrorDto(String prettyErrorMessage) {
        this.prettyErrorMessage = prettyErrorMessage;
    }
}
