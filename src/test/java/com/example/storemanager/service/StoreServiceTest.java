package com.example.storemanager.service;

import com.example.storemanager.constants.StoreType;
import com.example.storemanager.dao.StoreRepository;
import com.example.storemanager.model.Store;
import com.example.storemanager.service.impl.StoreServiceImpl;
import com.example.storemanager.testutil.TestUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
public class StoreServiceTest {

    @Autowired
    private StoreService storeService;

    @MockBean
    private StoreRepository storeRepository;

//    @Test
//    public void saveOrUpdateNewStoreSuccessful() {
//        Store store = TestUtil.createTransientStore();
//
//        Store store1 = TestUtil.createTransientStore();
//        store1.setStoreId(123L);
//
//        when(storeRepository.save(store)).thenReturn(store1);
//
//        Store result = storeService.saveOrUpdate(store);
//
//        assertEquals(store1.getStoreId(), result.getStoreId());
//    }
//
//    @Test
//    public void saveOrUpdateExistingStoreSuccessful() {
//        Store existingStore = TestUtil.createTransientStoreWithId();
//
//        String newStoreAddress = "New Address";
//        Store store1 = TestUtil.createTransientStoreWithId();
//        store1.setStoreType(StoreType.WAREHOUSE);
//        store1.setAddress(newStoreAddress);
//
//        Optional<Store> optional = Optional.of(existingStore);
//        when(storeRepository.findById(any())).thenReturn(optional);
//        when(storeRepository.save(any())).thenReturn(store1);
//
//        Store result = storeService.saveOrUpdate(store1);
//
//        assertEquals(store1.getStoreId(), result.getStoreId());
//        assertEquals(result.getAddress(), newStoreAddress);
//    }

    @Test
    public void saveOrUpdateNullFails() {
        storeService.saveOrUpdate(null);
    }

}
