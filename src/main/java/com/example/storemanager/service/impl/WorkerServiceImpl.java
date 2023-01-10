package com.example.storemanager.service.impl;

import com.example.storemanager.dao.WorkerRepository;
import com.example.storemanager.model.Worker;
import com.example.storemanager.service.WorkerService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
@Service
public class WorkerServiceImpl implements WorkerService {

    private WorkerRepository workerRepository;

    public WorkerServiceImpl (WorkerRepository workerRepository) {
        this.workerRepository = workerRepository;
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
}
