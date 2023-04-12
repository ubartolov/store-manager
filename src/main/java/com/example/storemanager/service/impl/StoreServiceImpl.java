package com.example.storemanager.service.impl;

import com.example.storemanager.constants.StoreType;
import com.example.storemanager.dao.StoreRepository;
import com.example.storemanager.dao.StoreStockRepository;
import com.example.storemanager.dao.WorkerRepository;
import com.example.storemanager.dto.StoreDto;
import com.example.storemanager.exception.AppException;
import com.example.storemanager.model.Store;
import com.example.storemanager.service.StoreService;
import com.example.storemanager.validation.ValidationService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
@Service
public class StoreServiceImpl implements StoreService {

    private StoreRepository storeRepository;
    private final WorkerRepository workerRepository;
    private final StoreStockRepository storeStockRepository;
    private final ValidationService validationService;


    public StoreServiceImpl(StoreRepository storeRepository,
                            WorkerRepository workerRepository,
                            StoreStockRepository storeStockRepository, ValidationService validationService) {
        this.storeRepository = storeRepository;
        this.workerRepository = workerRepository;
        this.storeStockRepository = storeStockRepository;
        this.validationService = validationService;
    }

    @Override
    public List<Store> findAll() {
        List<Store> storeList = new ArrayList<>();
        storeRepository.findAll().forEach(storeList::add);
        return storeList;
    }

    @Override
    public Store saveOrUpdate(Store store) {
//        if (store.getStoreId() != null) {
//            Optional<Store> optionalStore = storeRepository.findById(store.getStoreId());
//            if(optionalStore.isPresent()) {
//                Store existingStore = optionalStore.get();
//                existingStore.setAddress(store.getAddress());
//                existingStore.setStoreType(store.getStoreType());
//                existingStore.setStoreStock(store.getStoreStock());
//                existingStore.setWorkerList(store.getWorkerList());
//                return storeRepository.save(existingStore);
//            }
//        }
        return storeRepository.save(store);
    }

    @Override
    public Store addOrUpdateStore(StoreDto storeDto) {

        if (storeDto.getAddress() == null) {
            throw new AppException("Address must not be empty");
        }

        Store store;
        if(storeDto.getStoreId() == null) {
            store = new Store();
        }else {
            store = findById(storeDto.getStoreId());
        }
        store.setAddress(storeDto.getAddress());
        String storeTypeCode = storeDto.getStoreType();
        if (!(storeTypeCode.equalsIgnoreCase("store") || storeTypeCode.equalsIgnoreCase("warehouse"))) {
            throw new AppException("New/Updated Store must be either Store or Warehouse type");
        }
        store.setStoreType(StoreType.getByCode(storeTypeCode.toLowerCase()));
        return saveOrUpdate(store);

    }

    @Override
    public void delete(Store store) {
        storeRepository.delete(store);
    }
    @Override
    public void deleteById (Long id) {
        Store store = findById(id);
        validationService.validateStoreDelete(store);
        delete(store);
    }
    @Override
    public Store findById(Long id) {
        Optional<Store> optionalStore = storeRepository.findById(id);
        if (optionalStore.isPresent()) {
            return optionalStore.get();
        }
        throw new AppException(String.format("Store with the ID number '%d' cannot be found", id));
    }

    @Override
    public StoreDto findByIdDto (Long id) {
        Store store = findById(id);
        return copyStoreToStoreDto(store);
    }


    @Override
    public StoreDto copyStoreToStoreDto (Store store) {
        StoreDto storeDto = new StoreDto();
        storeDto.setStoreId(store.getStoreId());
        storeDto.setAddress(store.getAddress());
        storeDto.setStoreType(store.getStoreType().getCode());
        return storeDto;
    }
    @Override
    public List<Store> findAllStores() throws AppException {
        Optional<List<Store>> optionalStores = storeRepository.findByStoreType(StoreType.STORE);
        if (optionalStores.isPresent()) {
            return optionalStores.get();
        }
        throw new AppException("No Stores found.");
    }

    @Override
    public List<Store> findAllWarehouses() {
        Optional<List<Store>> optionalStores = storeRepository.findByStoreType(StoreType.WAREHOUSE);
        if (optionalStores.isPresent()) {
            return optionalStores.get();
        }
        throw new AppException("No Warehouses Found");
    }



}
