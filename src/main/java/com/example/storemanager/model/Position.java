package com.example.storemanager.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "POSITION")
@Data
public class Position {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "position_id")
    private Long positionId;
    @Column(name = "position_name")
    private String positionName;
    @Column(name = "salary")
    private Integer salary;
    @OneToMany(mappedBy = "position")
    private List<Worker> workers = new ArrayList<>();

}
