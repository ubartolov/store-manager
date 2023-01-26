package com.example.storemanager.service;

import com.example.storemanager.dto.WorkerInfoDto;
import com.example.storemanager.model.Worker;
import org.springframework.stereotype.Service;

import java.util.List;


public interface WorkerService {

    List<Worker> findAll();

    Worker saveOrUpdate(Worker worker);

    void delete(Worker worker);

    List<WorkerInfoDto> getWorkerDetails(Long storeId);
}
