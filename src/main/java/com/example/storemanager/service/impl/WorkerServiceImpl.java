package com.example.storemanager.service.impl;

import com.example.storemanager.dao.WorkerRepository;
import com.example.storemanager.dto.WorkerInfoDto;
import com.example.storemanager.model.Store;
import com.example.storemanager.model.Worker;
import com.example.storemanager.service.WorkerService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
@Service
public class WorkerServiceImpl implements WorkerService {

    private WorkerRepository workerRepository;
    private StoreServiceImpl storeService;
    public WorkerServiceImpl (WorkerRepository workerRepository, StoreServiceImpl storeService) {
        this.workerRepository = workerRepository;
        this.storeService = storeService;
    }

    @Override
    public List<Worker> findAll() {
        List<Worker> workersList = new ArrayList<>();
        workerRepository.findAll().forEach(workersList::add);
        return workersList;
    }

    @Override
    public Worker saveOrUpdate(Worker worker) {
        if (worker.getWorkerId() != null) {
            Optional<Worker> optionalWorker = workerRepository.findById(worker.getWorkerId());
            if (optionalWorker.isPresent()) {
                Worker existingWorker = optionalWorker.get();
                existingWorker.setFirstName(worker.getFirstName());
                existingWorker.setLastName(worker.getLastName());
                existingWorker.setEmail(worker.getEmail());
                existingWorker.setHomeAddress(worker.getHomeAddress());
                existingWorker.setPosition(worker.getPosition());
                existingWorker.setStore(worker.getStore());
                return workerRepository.save(existingWorker);
            }
        }
        return workerRepository.save(worker);
    }

    @Override
    public void delete(Worker worker) {
        workerRepository.delete(worker);
    }

    @Override
    public List<WorkerInfoDto> getWorkerDetails(Long storeId) {
        List<WorkerInfoDto> list = new ArrayList<>();
        Store store = storeService.findById(storeId);
        for (Worker worker : store.getWorkerList()) {
            WorkerInfoDto workerInfoDto = new WorkerInfoDto();
            workerInfoDto.setFirstName(worker.getFirstName());
            workerInfoDto.setLastName(worker.getLastName());
            workerInfoDto.setHomeAddress(worker.getHomeAddress());
            workerInfoDto.setEmail(worker.getEmail());
            workerInfoDto.setPositionName(worker.getPosition().getPositionName());
            workerInfoDto.setSalary(worker.getPosition().getSalary());
            list.add(workerInfoDto);
        }
        return list;
    }
}
