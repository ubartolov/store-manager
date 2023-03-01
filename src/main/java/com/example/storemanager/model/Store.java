package com.example.storemanager.model;


import com.example.storemanager.constants.StoreType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.ToString;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import java.util.ArrayList;
import java.util.List;
@Entity
@Table(name = "STORE")
@Data
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@ToString
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
    @Cascade(CascadeType.ALL)
    private List<StoreStock> storeStock = new ArrayList<>();

    @OneToMany(mappedBy = "store")
    private List<Worker> workerList = new ArrayList<>();
}
