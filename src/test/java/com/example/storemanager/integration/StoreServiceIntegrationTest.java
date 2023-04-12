package com.example.storemanager.integration;

import com.example.storemanager.constants.StoreType;
import com.example.storemanager.dao.StoreRepository;
import com.example.storemanager.dto.StoreDto;
import com.example.storemanager.exception.AppException;
import com.example.storemanager.model.Store;
import com.example.storemanager.service.StoreService;
import com.example.storemanager.testutil.TestUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class StoreServiceIntegrationTest {

    @Autowired
    private StoreService storeService;

    @Autowired
    private TestUtil testUtil;
    @Autowired
    private StoreRepository storeRepository;

    @Test
    @Transactional
    public void findByIdSuccessful () {
        Store store = testUtil.createPersistentStore();

        Store result = storeService.findById(store.getStoreId());

        assertSame(store.getStoreId(), result.getStoreId());
    }

    @Test
    @Transactional
    public void findByIdFailed () {

        AppException thrown = Assertions.assertThrows(AppException.class, () -> {
            storeService.findById(0L);
        });

        assertEquals("Store with the ID number '0' cannot be found", thrown.getPrettyErrorMessage());
    }

    @Test
    @Transactional
    public void findByIdDtoSuccessful () {
        Store store = testUtil.createPersistentStore();

        StoreDto result = storeService.findByIdDto(store.getStoreId());

        assertEquals(store.getStoreId(), result.getStoreId());
        assertEquals(store.getStoreType().getCode(), result.getStoreType());
        assertEquals(store.getAddress(), result.getAddress());
    }

    @Test
    @Transactional
    public void findByIdDtoFailed () {

        AppException thrown = Assertions.assertThrows(AppException.class, () -> {
            storeService.findByIdDto(0L);
        });
        assertEquals("Store with the ID number '0' cannot be found", thrown.getPrettyErrorMessage());
    }

    @Test
    @Transactional
    public void deleteByIdSuccessful() {
        Store store = testUtil.createPersistentStore();

        storeService.deleteById(store.getStoreId());

        Optional<Store> result = storeRepository.findById(store.getStoreId());
        assertFalse(result.isPresent());
    }

    @Test
    @Transactional
    public void deleteByIdFailed() {
        AppException thrown = Assertions.assertThrows(AppException.class, () -> {
            storeService.deleteById(0L);
        });
        assertEquals("Store with the ID number '0' cannot be found", thrown.getPrettyErrorMessage());
    }

    @Test
    @Transactional
    public void addOrUpdateStoreDtoSuccessful() {
        Store store = testUtil.createPersistentStore();

        StoreDto storeDto = new StoreDto();
        storeDto.setAddress("New Address");
        storeDto.setStoreType(store.getStoreType().getCode());
        storeDto.setStoreId(store.getStoreId());

        Store result = storeService.addOrUpdateStore(storeDto);

        assertEquals(storeDto.getAddress(), result.getAddress());
    }

    @Test
    @Transactional
    public void addNewStoreDtoSuccessful() {

        StoreDto storeDto = new StoreDto();
        storeDto.setAddress("Some Store");
        storeDto.setStoreType(StoreType.STORE.getCode());

        Store result = storeService.addOrUpdateStore(storeDto);

        assertEquals(storeDto.getAddress(), result.getAddress());
    }

    @Test
    @Transactional
    public void addOrUpdateWrongTypeStoreDtoFailed() {

        StoreDto storeDto = new StoreDto();
        storeDto.setAddress("Failed Store");
        storeDto.setStoreType("Wrong Store Type");

        AppException thrown = Assertions.assertThrows(AppException.class, () -> {
            storeService.addOrUpdateStore(storeDto);
        });
        assertEquals("New/Updated Store must be either Store or Warehouse type", thrown.getPrettyErrorMessage());

    }

    @Test
    @Transactional
    public void addOrUpdateWrongAddressStoreDtoFailed() {

        StoreDto storeDto = new StoreDto();
        storeDto.setAddress(null);
        storeDto.setStoreType("Wrong Store Type");

        AppException thrown = Assertions.assertThrows(AppException.class, () -> {
            storeService.addOrUpdateStore(storeDto);
        });
        assertEquals("Address must not be empty", thrown.getPrettyErrorMessage());

    }


}
