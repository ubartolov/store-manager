package com.example.storemanager.constants;

public enum StoreType {

    STORE("store"),
    WAREHOUSE("warehouse");


    private String code;

    public String getCode() {
        return code;
    }

    StoreType(String code) {
        this.code = code;
    }

    public static StoreType getByCode(String code) {
        for (StoreType type : StoreType.values()) {
            if (type.getCode().equals(code)) {
                return type;
            }
        }
        return null;
    }
}
