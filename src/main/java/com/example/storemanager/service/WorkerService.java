package com.example.storemanager.service;

import com.example.storemanager.dto.WorkerInfoDto;
import com.example.storemanager.model.Worker;

import java.util.List;


public interface WorkerService {

    List<Worker> findAll();

    Worker findById(Long id);

    WorkerInfoDto findByIdToDto(Long id);

    Worker saveOrUpdate(Worker worker);


    Worker addOrUpdateNewWorker(WorkerInfoDto workerInfoDto);

    void transferWorker(WorkerInfoDto workerInfoDto);

    void delete(Worker worker);

    void deleteById(Long id);

    List<WorkerInfoDto> getAllWorkerDetails();

    List<WorkerInfoDto> getWorkerDetailsById(Long storeId);
}
