package com.example.storemanager.db_init;

import com.example.storemanager.constants.StoreType;
import com.example.storemanager.dao.PositionRepository;
import com.example.storemanager.dao.ProductRepository;
import com.example.storemanager.dao.StoreRepository;
import com.example.storemanager.dao.StoreStockRepository;
import com.example.storemanager.dao.WorkerRepository;
import com.example.storemanager.model.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("db-init")
@SpringBootTest
public class DBInit {

    @Autowired
    private StoreRepository storeRepository;

    @Autowired
    private StoreStockRepository storeStockRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private WorkerRepository workerRepository;

    @Autowired
    private PositionRepository positionRepository;


    @Test
    public void initDatabase() {

        Store store1 = new Store();
        store1.setAddress("Square Park 46");
        store1.setStoreType(StoreType.STORE);
        storeRepository.save(store1);

        Store store2 = new Store();
        store2.setAddress("Broadway Street 78");
        store2.setStoreType(StoreType.STORE);
        storeRepository.save(store2);

        Store warehouse1 = new Store();
        warehouse1.setStoreType(StoreType.WAREHOUSE);
        warehouse1.setAddress("Spooner Street 78");
        storeRepository.save(warehouse1);

        Store warehouse2 = new Store();
        warehouse2.setStoreType(StoreType.WAREHOUSE);
        warehouse2.setAddress("Quake Street 78");
        storeRepository.save(warehouse2);

        Product product1 = new Product();
        product1.setProductName("Witcher III");
        product1.setProductPrice(60);
        productRepository.save(product1);

        StoreStock storeStock1 = new StoreStock();
        storeStock1.setProduct(product1);
        storeStock1.setStore(store1);
        storeStock1.setQuantity(70);
        storeStockRepository.save(storeStock1);

        StoreStock storeStock2 = new StoreStock();
        storeStock2.setProduct(product1);
        storeStock2.setStore(warehouse1);
        storeStock2.setQuantity(500);
        storeStockRepository.save(storeStock2);

        StoreStock storeStock3 = new StoreStock();
        storeStock3.setProduct(product1);
        storeStock3.setStore(store2);
        storeStock3.setQuantity(45);
        storeStockRepository.save(storeStock3);

        StoreStock storeStock4 = new StoreStock();
        storeStock4.setProduct(product1);
        storeStock4.setStore(warehouse2);
        storeStock4.setQuantity(450);
        storeStockRepository.save(storeStock4);

        Product product2 = new Product();
        product2.setProductName("NBA 2k23");
        product2.setProductPrice(59);
        productRepository.save(product2);

        StoreStock storeStock5 = new StoreStock();
        storeStock5.setProduct(product2);
        storeStock5.setStore(store1);
        storeStock5.setQuantity(40);
        storeStockRepository.save(storeStock5);

        StoreStock storeStock6 = new StoreStock();
        storeStock6.setProduct(product2);
        storeStock6.setStore(warehouse1);
        storeStock6.setQuantity(100);
        storeStockRepository.save(storeStock6);

        StoreStock storeStock7 = new StoreStock();
        storeStock7.setProduct(product2);
        storeStock7.setStore(store2);
        storeStock7.setQuantity(49);
        storeStockRepository.save(storeStock7);

        StoreStock storeStock8 = new StoreStock();
        storeStock8.setProduct(product2);
        storeStock8.setStore(warehouse2);
        storeStock8.setQuantity(375);
        storeStockRepository.save(storeStock8);

        Product product3 = new Product();
        product3.setProductName("Street Fighter IV");
        product3.setProductPrice(35);
        productRepository.save(product3);

        StoreStock storeStock9 = new StoreStock();
        storeStock9.setProduct(product3);
        storeStock9.setStore(store1);
        storeStock9.setQuantity(100);
        storeStockRepository.save(storeStock9);

        StoreStock storeStock10 = new StoreStock();
        storeStock10.setProduct(product3);
        storeStock10.setStore(warehouse1);
        storeStock10.setQuantity(900);
        storeStockRepository.save(storeStock10);

//    StoreStock storeStock11 = new StoreStock();
//    storeStock11.setProduct(product3);
//    storeStock11.setStore(store2);
//    storeStock11.setQuantity(10);
//    storeStockRepository.save(storeStock11);
//
//    StoreStock storeStock12 = new StoreStock();
//    storeStock12.setProduct(product3);
//    storeStock12.setStore(warehouse2);
//    storeStock12.setQuantity(600);
//    storeStockRepository.save(storeStock12);

        Product product4 = new Product();
        product4.setProductName("Red Dead Redemption II");
        product4.setProductPrice(60);
        productRepository.save(product4);

        StoreStock storeStock13 = new StoreStock();
        storeStock13.setProduct(product4);
        storeStock13.setStore(store1);
        storeStock13.setQuantity(50);
        storeStockRepository.save(storeStock13);

//    StoreStock storeStock14 = new StoreStock();
//    storeStock14.setProduct(product4);
//    storeStock14.setStore(warehouse1);
//    storeStock14.setQuantity(900);
//    storeStockRepository.save(storeStock14);

        StoreStock storeStock15 = new StoreStock();
        storeStock15.setProduct(product4);
        storeStock15.setStore(store2);
        storeStock15.setQuantity(120);
        storeStockRepository.save(storeStock15);

        StoreStock storeStock16 = new StoreStock();
        storeStock16.setProduct(product4);
        storeStock16.setStore(warehouse2);
        storeStock16.setQuantity(580);
        storeStockRepository.save(storeStock16);

        Position position = new Position();
        position.setPositionName("Manager");
        position.setSalary(85000);
        positionRepository.save(position);

        Position position1 = new Position();
        position1.setPositionName("CTO");
        position1.setSalary(95000);
        positionRepository.save(position1);

        Position position2 = new Position();
        position2.setPositionName("CEO");
        position2.setSalary(75000);
        positionRepository.save(position2);

        Position position3 = new Position();
        position3.setPositionName("Salesperson");
        position3.setSalary(65000);

        Worker worker = new Worker();
        worker.setFirstName("Peter");
        worker.setLastName("Griffin");
        worker.setEmail("griffindor69@gmail.com");
        worker.setHomeAddress("Spooner Street 100");
        worker.setPosition(position);
        worker.setStore(store1);
        workerRepository.save(worker);

        Worker worker1 = new Worker();
        worker1.setFirstName("Lois");
        worker1.setLastName("Griffin");
        worker1.setEmail("housewife11@gmail.com");
        worker1.setHomeAddress("Rhode Island 50");
        worker1.setPosition(position1);
        worker1.setStore(store2);
        workerRepository.save(worker1);
    }
}
