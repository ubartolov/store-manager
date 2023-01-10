package com.example.storemanager.model;


import com.example.storemanager.constants.StoreType;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
@Entity
@Table(name = "STORE")
@Data
public class Store {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "store_id")
    private Long storeId;

    @Column(name = "address")
    private String address;

    @Column(name = "store_type")
    private StoreType storeType = StoreType.WAREHOUSE;

    @JsonManagedReference
    @OneToMany(mappedBy = "store")
    private List<StoreStock> storeStock = new ArrayList<>();

    @JsonManagedReference
    @OneToMany(mappedBy = "store")
    private List<Worker> workerList = new ArrayList<>();
}
