package com.example.storemanager.validation;

import com.example.storemanager.exception.ValidationException;
import org.springframework.stereotype.Component;

@Component
public class ValidationService {

    public void validateInsertAmount(Integer requestAmount) {
        if(requestAmount <= 0) {
            throw new ValidationException("insertAmount", ValidationConstraints.MUST_BE_POSITIVE_NUMBER,
                    "Insert Amount Must Be Higher Than 0");
        }
    }

    
}
