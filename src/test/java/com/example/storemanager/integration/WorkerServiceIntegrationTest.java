package com.example.storemanager.integration;

import com.example.storemanager.dao.WorkerRepository;
import com.example.storemanager.dto.StoreDto;
import com.example.storemanager.dto.WorkerInfoDto;
import com.example.storemanager.exception.AppException;
import com.example.storemanager.model.Position;
import com.example.storemanager.model.Store;
import com.example.storemanager.model.Worker;
import com.example.storemanager.service.WorkerService;
import com.example.storemanager.testutil.TestUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class WorkerServiceIntegrationTest {

    @Autowired
    private TestUtil testUtil;

    @Autowired
    private WorkerService workerService;

    @Autowired
    private WorkerRepository workerRepository;

    @Test
    @Transactional
    public void findByIdSuccessful () {
        Worker worker = testUtil.createPersistentWorker();

        Worker result = workerService.findById(worker.getWorkerId());

        assertSame(worker.getWorkerId(), result.getWorkerId());
    }

    @Test
    @Transactional
    public void findByIdFailed () {
        AppException thrown = Assertions.assertThrows(AppException.class, () -> {
            workerService.findById(0L);
        });

        assertEquals("Worker with ID '0' does not exist", thrown.getPrettyErrorMessage());
    }

    @Test
    @Transactional
    public void findByIdDtoSuccessful () {
        Worker worker = testUtil.createPersistentWorkerWithPositionAndStore();

        WorkerInfoDto result = workerService.findByIdToDto(worker.getWorkerId());

        assertEquals(worker.getFirstName(), result.getFirstName());
        assertEquals(worker.getLastName(), result.getLastName());
        assertEquals(worker.getEmail(), result.getEmail());
    }

    @Test
    @Transactional
    public void findByIdDtoFailed () {

        AppException thrown = Assertions.assertThrows(AppException.class, () -> {
            workerService.findByIdToDto(0L);
        });
        assertEquals("Worker with ID '0' does not exist", thrown.getPrettyErrorMessage());
    }

    @Test
    @Transactional
    public void addNewWorkerSuccessful() {
        Worker worker = testUtil.createPersistentWorkerWithPositionAndStore();
        Store store = worker.getStore();
        Position position = worker.getPosition();

        WorkerInfoDto workerInfoDto = new WorkerInfoDto();
        workerInfoDto.setFirstName(worker.getFirstName());
        workerInfoDto.setLastName(worker.getLastName());
        workerInfoDto.setHomeAddress(worker.getHomeAddress());
        workerInfoDto.setStoreId(store.getStoreId());
        workerInfoDto.setPositionId(position.getPositionId());

        workerService.addOrUpdateNewWorker(workerInfoDto);

        Optional<Worker> workerOptional = workerRepository.findById(worker.getWorkerId());
        assertTrue(workerOptional.isPresent());
    }

    @Test
    @Transactional
    public void updateNewWorkerSuccessful() {
        Worker worker = testUtil.createPersistentWorkerWithPositionAndStore();
        Store store = testUtil.createPersistentStore();
        Position position = worker.getPosition();

        WorkerInfoDto workerInfoDto = new WorkerInfoDto();
        workerInfoDto.setWorkerId(worker.getWorkerId());
        workerInfoDto.setFirstName("Peter");
        workerInfoDto.setLastName(worker.getLastName());
        workerInfoDto.setHomeAddress(worker.getHomeAddress());
        workerInfoDto.setStoreId(store.getStoreId());
        workerInfoDto.setPositionId(position.getPositionId());

        Worker result = workerService.addOrUpdateNewWorker(workerInfoDto);

        Optional<Worker> workerOptional = workerRepository.findById(worker.getWorkerId());
        assertTrue(workerOptional.isPresent());
        assertEquals(result.getFirstName(), workerInfoDto.getFirstName());
        assertSame(result.getStore().getStoreId(), store.getStoreId());
    }

    @Test
    @Transactional
    public void addNewWorkerNoStoreFailed() {
        Worker worker = testUtil.createWorkerWithPosition();
        Position position = worker.getPosition();

        WorkerInfoDto workerInfoDto = new WorkerInfoDto();
        workerInfoDto.setWorkerId(worker.getWorkerId());
        workerInfoDto.setFirstName("Peter");
        workerInfoDto.setLastName(worker.getLastName());
        workerInfoDto.setHomeAddress(worker.getHomeAddress());
        workerInfoDto.setStoreId(0L);
        workerInfoDto.setPositionId(position.getPositionId());

        AppException thrown = Assertions.assertThrows(AppException.class, () -> {
            workerService.addOrUpdateNewWorker(workerInfoDto);
        });
        assertEquals("Store with the ID number '0' cannot be found", thrown.getPrettyErrorMessage());

    }

    @Test
    @Transactional
    public void addNewWorkerNoPositionFailed() {
        Worker worker = testUtil.createPersistentWorkerWithStore();
        Store store = worker.getStore();

        WorkerInfoDto workerInfoDto = new WorkerInfoDto();
        workerInfoDto.setWorkerId(worker.getWorkerId());
        workerInfoDto.setFirstName("Peter");
        workerInfoDto.setLastName(worker.getLastName());
        workerInfoDto.setHomeAddress(worker.getHomeAddress());
        workerInfoDto.setStoreId(store.getStoreId());
        workerInfoDto.setPositionId(0L);

        AppException thrown = Assertions.assertThrows(AppException.class, () -> {
            workerService.addOrUpdateNewWorker(workerInfoDto);
        });
        assertEquals("Position id does not exist", thrown.getPrettyErrorMessage());
    }

    @Test
    @Transactional
    public void transferWorkerSuccessful() {
        Worker worker = testUtil.createPersistentWorkerWithStore();
        Store store = testUtil.createPersistentStore();

        WorkerInfoDto workerInfoDto = new WorkerInfoDto();
        workerInfoDto.setStoreId(store.getStoreId());
        workerInfoDto.setWorkerId(worker.getWorkerId());

        workerService.transferWorker(workerInfoDto);
        assertSame(worker.getStore().getStoreId(), store.getStoreId());
    }

    @Test
    @Transactional
    public void transferWorkerNoStoreFailed() {
        Worker worker = testUtil.createPersistentWorkerWithStore();

        WorkerInfoDto workerInfoDto = new WorkerInfoDto();
        workerInfoDto.setStoreId(0L);
        workerInfoDto.setWorkerId(worker.getWorkerId());

        AppException thrown = Assertions.assertThrows(AppException.class, () -> {
            workerService.transferWorker(workerInfoDto);
        });
        assertEquals("Store with the ID number '0' cannot be found", thrown.getPrettyErrorMessage());

    }

    @Test
    @Transactional
    public void transferWorkerNoWorkerFailed() {
        Store store = testUtil.createPersistentStore();

        WorkerInfoDto workerInfoDto = new WorkerInfoDto();
        workerInfoDto.setStoreId(store.getStoreId());
        workerInfoDto.setWorkerId(0L);

        AppException thrown = Assertions.assertThrows(AppException.class, () -> {
            workerService.transferWorker(workerInfoDto);
        });
        assertEquals("Worker with ID '0' does not exist", thrown.getPrettyErrorMessage());
    }

    @Test
    @Transactional
    public void deleteWorkerByIdSuccessful() {
        Worker worker = testUtil.createPersistentWorker();

        workerRepository.deleteById(worker.getWorkerId());
        Optional<Worker> result = workerRepository.findById(worker.getWorkerId());
        assertFalse(result.isPresent());
    }

    @Test
    @Transactional
    public void deleteWorkerByIdFailed() {
        AppException thrown = Assertions.assertThrows(AppException.class, () -> {
            workerService.deleteById(0L);
        });
        assertEquals("Worker with ID '0' does not exist", thrown.getPrettyErrorMessage());
    }
}
