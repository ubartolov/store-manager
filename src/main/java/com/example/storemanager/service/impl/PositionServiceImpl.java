package com.example.storemanager.service.impl;

import com.example.storemanager.dao.PositionRepository;
import com.example.storemanager.model.Position;
import com.example.storemanager.service.PositionService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
@Service
public class PositionServiceImpl implements PositionService {

    private PositionRepository positionRepository;

    public PositionServiceImpl (PositionRepository positionRepository) {
        this.positionRepository = positionRepository;
    }


    @Override
    public List<Position> findAll() {
        List<Position> positionList = new ArrayList<>();
        positionRepository.findAll().forEach(positionList::add);
        return positionList;
    }

    @Override
    public Position saveOrUpdate(Position position) {
        if (position.getPositionId() != null) {
            Optional<Position> optionalPosition = positionRepository.findById(position.getPositionId());
            if (optionalPosition.isPresent()) {
                Position existingPosition = optionalPosition.get();
                existingPosition.setPositionName(position.getPositionName());
                existingPosition.setSalary(position.getSalary());
                existingPosition.setWorkers(position.getWorkers());
                return positionRepository.save(existingPosition);
            }
        }
        return positionRepository.save(position);
    }

    @Override
    public void delete(Position position) {
        positionRepository.delete(position);
    }
}
