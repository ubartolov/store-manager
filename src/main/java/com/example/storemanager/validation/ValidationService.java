package com.example.storemanager.validation;

import com.example.storemanager.exception.ValidationException;
import com.example.storemanager.model.Store;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ValidationService {

    public void validateInsertAmount(Integer requestAmount) {
        if (requestAmount <= 0) {
            throw new ValidationException("insertAmount", ValidationConstraints.MUST_BE_POSITIVE_NUMBER,
                    "Insert Amount Must Be Higher Than 0");
        }
    }

    public void validateStoreDelete(Store store) {
        if (store.getStoreStock().size() > 0) {
            throw new ValidationException(ValidationConstraints.STORE_STOCK_MUST_BE_EMPTY,
                    "Store must not contain any products");
        }
    }


}
