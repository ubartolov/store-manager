package com.example.storemanager.validation;

import com.example.storemanager.exception.ValidationException;
import com.example.storemanager.model.Store;
import org.springframework.stereotype.Component;

@Component
public class ValidationService {

    public void validateInsertAmount(Integer requestAmount) {
        if(requestAmount <= 0) {
            throw new ValidationException("insertAmount", ValidationConstraints.MUST_BE_POSITIVE_NUMBER,
                    "Insert Amount Must Be Higher Than 0");
        }
    }

    public void validateWarehouseDelete(Store warehouse) {
        if (warehouse.getStoreStock().size() > 0) {
            //TODO throw exception
            System.out.println("STORESTOCK SIZE > 0");
        }
        if (warehouse.getWorkerList().size() > 0) {
            //TODO throw exception
            System.out.println("WORKERS SIZE > 0");
        }
    }
    
}
