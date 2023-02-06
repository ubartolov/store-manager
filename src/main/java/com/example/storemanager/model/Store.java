package com.example.storemanager.model;


import com.example.storemanager.constants.StoreType;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
@Entity
@Table(name = "STORE")
@Data
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Store {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "store_id")
    private Long storeId;

    @Column(name = "address")
    private String address;

    @Column(name = "store_type")
    private StoreType storeType = StoreType.WAREHOUSE;

    @OneToMany(mappedBy = "store")
    private List<StoreStock> storeStock = new ArrayList<>();

    @OneToMany(mappedBy = "store")
    private List<Worker> workerList = new ArrayList<>();
}
