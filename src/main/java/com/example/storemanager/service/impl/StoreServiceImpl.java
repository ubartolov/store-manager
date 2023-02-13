package com.example.storemanager.service.impl;

import com.example.storemanager.constants.StoreType;
import com.example.storemanager.dao.StoreRepository;
import com.example.storemanager.dao.WorkerRepository;
import com.example.storemanager.dto.StoreDto;
import com.example.storemanager.model.Store;
import com.example.storemanager.service.StoreService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
@Service
public class StoreServiceImpl implements StoreService {

    private StoreRepository storeRepository;
    private final WorkerRepository workerRepository;


    public StoreServiceImpl(StoreRepository storeRepository,
                            WorkerRepository workerRepository) {
        this.storeRepository = storeRepository;
        this.workerRepository = workerRepository;
    }

    @Override
    public List<Store> findAll() {
        List<Store> storeList = new ArrayList<>();
        storeRepository.findAll().forEach(storeList::add);
        return storeList;
    }

    @Override
    public Store saveOrUpdate(Store store) {
        if (store.getStoreId() != null) {
            Optional<Store> optionalStore = storeRepository.findById(store.getStoreId());
            if(optionalStore.isPresent()) {
                Store existingStore = optionalStore.get();
                existingStore.setAddress(store.getAddress());
                existingStore.setStoreType(store.getStoreType());
                existingStore.setStoreStock(store.getStoreStock());
                existingStore.setWorkerList(store.getWorkerList());
                return storeRepository.save(existingStore);
            }
        }
        return storeRepository.save(store);
    }

    @Override
    public void addOrUpdateStore(StoreDto storeDto) {

        Store store;
        if(storeDto.getStoreId() == null) {
            store = new Store();
        }else {
            store = findById(storeDto.getStoreId());
        }
        store.setAddress(storeDto.getAddress());
        String storeTypeCode = storeDto.getStoreType();
        if (!(storeTypeCode.equalsIgnoreCase("store") || storeTypeCode.equalsIgnoreCase("warehouse"))) {
            // throw exception
        }
        store.setStoreType(StoreType.getByCode(storeTypeCode.toLowerCase()));
        saveOrUpdate(store);
    }

    @Override
    public void delete(Store store) {
        storeRepository.delete(store);
    }
    @Override
    public void deleteById (Long id) {
        Store store = findById(id);
        delete(store);
    }
    @Override
    public Store findById(Long id) {
        Optional<Store> optionalStore = storeRepository.findById(id);
        if (optionalStore.isPresent()) {
            return optionalStore.get();
        }
        return null;
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
    public List<Store> findAllStores() {
        Optional<List<Store>> optionalStores = storeRepository.findByStoreType(StoreType.STORE);
        if (optionalStores.isPresent()) {
            return optionalStores.get();
        }
        return null;
    }

    @Override
    public List<Store> findAllWarehouses() {
        Optional<List<Store>> optionalStores = storeRepository.findByStoreType(StoreType.WAREHOUSE);
        if (optionalStores.isPresent()) {
            return optionalStores.get();
        }
        return null;
    }
}
