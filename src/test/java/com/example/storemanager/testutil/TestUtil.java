package com.example.storemanager.testutil;

import com.example.storemanager.constants.StoreType;
import com.example.storemanager.dao.*;
import com.example.storemanager.model.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


@Component
public class TestUtil {

    @Autowired
    private  StoreStockRepository storeStockRepository;

    @Autowired
    private  StoreRepository storeRepository;

    @Autowired
    private  ProductRepository productRepository;

    @Autowired
    private  PositionRepository positionRepository;

    @Autowired
    private  WorkerRepository workerRepository;


    public  Store createTransientStore() {
        Store store = new Store();
        store.setAddress("Square Park 46");
        store.setStoreType(StoreType.STORE);
        return store;
    }

    public  Store createTransientStoreWithId() {
        Store store = createTransientStore();
        store.setStoreId(123L);
        return store;
    }
    public  Store createTransientWarehouse() {
        Store warehouse = new Store();
        warehouse.setAddress("Spooner Street 63");
        warehouse.setStoreType(StoreType.WAREHOUSE);
        return warehouse;
    }

    public  Store createTransientWarehouseWithId() {
        Store warehouse = createTransientWarehouse();
        warehouse.setStoreId(234L);
        return warehouse;
    }

    public StoreStock createTransientStoreStock() {
        StoreStock storeStock = new StoreStock();
        storeStock.setQuantity(500);
        return storeStock;    }

    public Store createTransientWarehouseStockAndWorkers () {
        Store warehouse = createTransientWarehouseWithId();
        StoreStock storeStock = createTransientStoreStock();
        Product product = createTransientProductWithId();
        Worker worker = createTransientWorkerWithPosition();
        warehouse.getStoreStock().add(storeStock);
        warehouse.getWorkerList().add(worker);
        storeStock.setProduct(product);
        storeStock.setStore(warehouse);
        product.getStoreStock().add(storeStock);
        return warehouse;
    }
    public Store createTransientStoreStockAndWorkers () {
        Store store = createTransientStoreWithId();
        StoreStock storeStock = createTransientStoreStock();
        Product product = createTransientProductWithId();
        Worker worker = createTransientWorkerWithPosition();
        store.getStoreStock().add(storeStock);
        store.getWorkerList().add(worker);
        storeStock.setProduct(product);
        storeStock.setStore(store);
        product.getStoreStock().add(storeStock);
        return store;
    }

    public Worker createTransientWorker() {
        Worker worker = new Worker();
        worker.setFirstName("Stewie");
        worker.setLastName("Griffin");
        worker.setHomeAddress("Schponner Schtreet");
        worker.setEmail("stewieGriff");
        return worker;
    }

    public Worker createTransientWorkerWithId() {
        Worker worker = createTransientWorker();
        worker.setWorkerId(123L);
        return worker;
    }

    public Position createTransientPosition() {
        Position position = new Position();
        position.setPositionName("General Manager");
        position.setSalary(5000);
        return position;
    }

    public Worker createTransientWorkerWithPosition () {
        Worker worker = createTransientWorkerWithId();
        Position position = createTransientPosition();
        worker.setPosition(position);
        return worker;
    }

    public Worker createTransientWorkerWithStoreAndPosition () {
        Worker worker = createTransientWorkerWithId();
        Position position = createTransientPosition();
        Store store = createTransientStoreWithId();
        worker.setStore(store);
        worker.setPosition(position);
        return worker;
    }
    public  Product createTransientProduct() {
        Product product = new Product();
        product.setProductName("Apple");
        product.setProductPrice(500);
        return product;
    }
    public  Product createTransientProductWithId() {
        Product product = createTransientProduct();
        product.setProductId(123L);
        return product;
    }

    public  Store createPersistentStore() {
        return createPersistentStore(StoreType.STORE);
    }

    public Store createPersistentStore(StoreType storeType) {
        Store store = new Store();
        store.setStoreType(storeType);
        store.setAddress("Quehog Hill " + new Random().nextInt(1, 1000));
        return storeRepository.save(store);
    }

    public Store createPersistentWarehouse() {
        Store warehouse = new Store();
        warehouse.setStoreType(StoreType.WAREHOUSE);
        warehouse.setAddress("Mountain Road " + new Random().nextInt(1, 1000));
        return storeRepository.save(warehouse);
    }
    public  Product createPersistentProduct() {
        Product product = new Product();
        product.setProductName("Orange " + StringUtils.randomAlphanumeric(15));
        product.setProductPrice(50);
        return productRepository.save(product);
    }
    public Worker createPersistentWorker() {
        Worker worker = new Worker();
        worker.setFirstName("Stewie");
        worker.setLastName("Griffin"  + StringUtils.randomAlphanumeric(15));
        worker.setHomeAddress("Schponner Schtreet"  + StringUtils.randomAlphanumeric(15));
        worker.setEmail("stewieGriff" + StringUtils.randomAlphanumeric(15) + "@gmail.com");
        return workerRepository.save(worker);
    }

    public Position createPersistentPosition() {
        Position position = new Position();
        position.setPositionName("General Manager");
        position.setSalary(5000);
        return positionRepository.save(position);
    }

    public Worker createWorkerWithPosition () {
        Worker worker = createPersistentWorker();
        Position position = createPersistentPosition();
        worker.setPosition(position);
        return workerRepository.save(worker);
    }
    public Worker createPersistentWorkerWithStore () {
        Worker worker = createPersistentWorker();
        Store store = createPersistentStore();
        worker.setStore(store);
        return workerRepository.save(worker);
    }
    public Worker createPersistentWorkerWithPositionAndStore() {
        Worker worker = createPersistentWorker();
        Position position = createPersistentPosition();
        Store store = createPersistentStore();
        worker.setPosition(position);
        worker.setStore(store);
        return workerRepository.save(worker);
    }

    public  StoreStock createPersistentStoreStock() {
        StoreStock storeStock = new StoreStock();
        storeStock.setQuantity(500);
        return storeStockRepository.save(storeStock);
    }

    public StoreStock createPersistentWarehouseStock() {
        StoreStock warehouseStock = new StoreStock();
        warehouseStock.setQuantity(500);
        return storeStockRepository.save(warehouseStock);
    }

    public Store createPersistentStoreWithStock() {
        return createPersistentStoreWithStock(StoreType.STORE);
    }

    public Store createPersistentWarehouseWithStock() {
        return createPersistentStoreWithStock(StoreType.WAREHOUSE);
    }

    public Store createPersistentWarehouseWithWorkersAndStock() {
        Store warehouse = createPersistentStoreWithStock(StoreType.WAREHOUSE);
        Worker worker = createWorkerWithPosition();

        warehouse.addWorkerList(worker);
        storeRepository.save(warehouse);
        return warehouse;
    }
    public Store createPersistentStoreWithStock(StoreType storeType) {
        Store store = createPersistentStore(storeType);
        StoreStock storeStock = createPersistentStoreStock();
        Product product = createPersistentProduct();
        store.getStoreStock().add(storeStock);
        storeStock.setStore(store);
        storeStock.setProduct(product);
        product.getStoreStock().add(storeStock);
        productRepository.save(product);
        storeStockRepository.save(storeStock);
        return storeRepository.save(store);
    }

    public ObjectMapper getObjectMapper() {
        return new ObjectMapper();
    }

}
