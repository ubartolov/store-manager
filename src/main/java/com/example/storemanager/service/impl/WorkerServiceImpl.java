package com.example.storemanager.service.impl;

import com.example.storemanager.dao.WorkerRepository;
import com.example.storemanager.dto.WorkerInfoDto;
import com.example.storemanager.exception.AppException;
import com.example.storemanager.model.Position;
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
    private PositionServiceImpl positionService;

    public WorkerServiceImpl(WorkerRepository workerRepository, StoreServiceImpl storeService, PositionServiceImpl positionService) {
        this.workerRepository = workerRepository;
        this.storeService = storeService;
        this.positionService = positionService;
    }

    @Override
    public List<Worker> findAll() {
        List<Worker> workersList = new ArrayList<>();
        workerRepository.findAll().forEach(workersList::add);
        return workersList;
    }

    @Override
    public Worker findById(Long id) {
        Optional<Worker> optionalWorker = workerRepository.findById(id);
        if (optionalWorker.isPresent()) {
            return optionalWorker.get();
        }
        throw new AppException(String.format("Worker with the given ID does not exist", id));
    }

    @Override
    public WorkerInfoDto findByIdToDto(Long id) {
        Worker worker = findById(id);
        return copyWorkerToWorkerInfoDto(worker);
    }

    private WorkerInfoDto copyWorkerToWorkerInfoDto(Worker worker) {
        WorkerInfoDto workerInfoDto = new WorkerInfoDto();
        workerInfoDto.setWorkerId(worker.getWorkerId());
        workerInfoDto.setFirstName(worker.getFirstName());
        workerInfoDto.setLastName(worker.getLastName());
        workerInfoDto.setEmail(worker.getEmail());
        workerInfoDto.setStoreId(worker.getStore().getStoreId());
        workerInfoDto.setPositionId(worker.getPosition().getPositionId());
        workerInfoDto.setPositionName(worker.getPosition().getPositionName());
        workerInfoDto.setSalary(worker.getPosition().getSalary());
        workerInfoDto.setStoreAddress(worker.getStore().getAddress());
        workerInfoDto.setHomeAddress(worker.getHomeAddress());
        return workerInfoDto;
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
    public void addOrUpdateNewWorker(WorkerInfoDto workerInfoDto) {
        Position position = positionService.findById(workerInfoDto.getPositionId());
        Store store = storeService.findById(workerInfoDto.getStoreId());
        Worker worker;
        if (workerInfoDto.getWorkerId() != null) {
            worker = findById(workerInfoDto.getWorkerId());
        } else {
            worker = new Worker();
        }
        worker.setFirstName(workerInfoDto.getFirstName());
        worker.setLastName(workerInfoDto.getLastName());
        worker.setEmail(workerInfoDto.getEmail());
        worker.setHomeAddress(workerInfoDto.getHomeAddress());
        worker.setPosition(position);
        worker.setStore(store);
        saveOrUpdate(worker);
    }

    @Override
    public void transferWorker(WorkerInfoDto workerInfoDto) {
        Store store = storeService.findById(workerInfoDto.getStoreId());
        Worker worker;
        if (workerInfoDto.getWorkerId() != null) {
            worker = findById(workerInfoDto.getWorkerId());
            worker.setStore(store);
            saveOrUpdate(worker);
        }
    }

    @Override
    public void delete(Worker worker) {
        workerRepository.delete(worker);
    }

    @Override
    public void deleteById(Long id) {
        Worker worker = findById(id);
        if (worker.getWorkerId() != null) {
            delete(worker);
        }
    }
    @Override
    public List<WorkerInfoDto> getAllWorkerDetails() {
        List<WorkerInfoDto> list = new ArrayList<>();
        for (Worker worker : findAll()) {
            WorkerInfoDto workerInfoDto = new WorkerInfoDto();
            workerInfoDto.setWorkerId(worker.getWorkerId());
            workerInfoDto.setFirstName(worker.getFirstName());
            workerInfoDto.setLastName(worker.getLastName());
            workerInfoDto.setHomeAddress(worker.getHomeAddress());
            workerInfoDto.setEmail(worker.getEmail());
            workerInfoDto.setPositionName(worker.getPosition().getPositionName());
            workerInfoDto.setSalary(worker.getPosition().getSalary());
            workerInfoDto.setStoreAddress(worker.getStore().getAddress());
            list.add(workerInfoDto);
        }
        return list;
    }

    @Override
    public List<WorkerInfoDto> getWorkerDetailsById(Long storeId) {
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
