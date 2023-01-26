package com.example.storemanager.dto;

import lombok.Data;

@Data
public class WorkerInfoDto {

    private String firstName;
    private String lastName;
    private String email;
    private String homeAddress;
    private String positionName;
    private Integer salary;
    private Long storeId;

}
