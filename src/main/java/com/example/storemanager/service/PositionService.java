package com.example.storemanager.service;


import com.example.storemanager.model.Position;
import org.springframework.stereotype.Service;

import java.util.List;


public interface PositionService {

    List<Position> findAll();

    Position findById(Long id);

    Position saveOrUpdate(Position position);

    void delete(Position position);
}
