package com.example.storemanager.exception;

import com.example.storemanager.dto.ErrorDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class WebExceptionHandler {

    @ExceptionHandler({RuntimeException.class})
    public ResponseEntity<?> handleException(RuntimeException runtimeException) {
        try {
            ErrorDto errorDto;

            if (runtimeException instanceof ValidationException validationException) {
                errorDto = new ErrorDto(validationException.getField(), validationException.getValidationMessage(),
                        validationException.getPrettyErrorMessage());
                return new ResponseEntity<>(errorDto, HttpStatus.BAD_REQUEST);
            }
            if (runtimeException instanceof AppException appException) {
                errorDto = new ErrorDto(appException.getPrettyErrorMessage());
                return new ResponseEntity<>(errorDto, HttpStatus.BAD_REQUEST);
            }
            errorDto = new ErrorDto("unspecifiedError", "unspecifiedError",
                    "An unspecified error has occured");
            return new ResponseEntity<>(errorDto, HttpStatus.BAD_REQUEST);
        }catch (Exception e) {
            return new ResponseEntity<>(new ErrorDto("request.genericError",
                    "requestGenericError", "An unexpected error has occured"),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
