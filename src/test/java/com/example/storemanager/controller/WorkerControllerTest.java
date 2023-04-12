package com.example.storemanager.controller;

import com.example.storemanager.dto.WorkerInfoDto;
import com.example.storemanager.model.Position;
import com.example.storemanager.model.Store;
import com.example.storemanager.model.Worker;
import com.example.storemanager.service.PositionService;
import com.example.storemanager.service.StoreService;
import com.example.storemanager.service.WorkerService;
import com.example.storemanager.testutil.TestUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
public class WorkerControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private TestUtil testUtil;
    @MockBean
    WorkerService workerService;
    @MockBean
    StoreService storeService;
    @MockBean
    PositionService positionService;

    @Test
    public void getAllWorkersSuccessful() throws Exception {
        Worker worker = testUtil.createTransientWorkerWithStoreAndPosition();
        Position position = worker.getPosition();
        Store store = worker.getStore();

        Store existingStores = testUtil.createTransientStoreWithId();
        Store existingWarehouse = testUtil.createTransientWarehouseWithId();

        List<Store> allStores = new ArrayList<>();
        allStores.add(existingStores);
        allStores.add(existingWarehouse);

        WorkerInfoDto workerInfoDto = new WorkerInfoDto();
        workerInfoDto.setWorkerId(worker.getWorkerId());
        workerInfoDto.setFirstName(worker.getFirstName());
        workerInfoDto.setLastName(worker.getLastName());
        workerInfoDto.setHomeAddress(worker.getHomeAddress());
        workerInfoDto.setEmail(worker.getEmail());
        workerInfoDto.setPositionName(position.getPositionName());
        workerInfoDto.setSalary(position.getSalary());
        workerInfoDto.setStoreId(store.getStoreId());
        workerInfoDto.setStoreAddress(store.getAddress());

        when(workerService.getAllWorkerDetails()).thenReturn(List.of(workerInfoDto));
        when(storeService.findAll()).thenReturn(allStores);

        mockMvc.perform(get("/worker/staffdetails"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(model().attribute("workers", equalTo(List.of(workerInfoDto))))
                .andExpect(model().attribute("stores", equalTo(allStores)))
                .andExpect(view().name("/worker/staffdetails"));
        verify(workerService, atMostOnce()).getAllWorkerDetails();
        verify(storeService, atMostOnce()).findAll();
    }

    @Test
    public void newWorkerPageSuccessful() throws Exception {
        Position position = testUtil.createTransientPosition();
        Store store = testUtil.createTransientStoreWithId();
        Store warehouse = testUtil.createTransientWarehouse();

        List<Store> storeList = new ArrayList<>();
        storeList.add(store);
        storeList.add(warehouse);

        when(positionService.findAll()).thenReturn(List.of(position));
        when(storeService.findAll()).thenReturn(storeList);

        mockMvc.perform(get("/worker/add-new-worker"))
                .andDo(print())
                .andExpect(model().attribute("positions", equalTo(List.of(position))))
                .andExpect(model().attribute("stores", equalTo(storeList)))
                .andExpect(view().name("/worker/add-new-worker"));

        verify(positionService, atMostOnce()).findAll();
        verify(storeService, atMostOnce()).findAll();
    }

    @Test
    public void addNewWorkerSuccessful() throws Exception {
        Store store = testUtil.createTransientStoreWithId();
        Worker worker = testUtil.createTransientWorkerWithId();
        Position position = testUtil.createTransientPosition();

        WorkerInfoDto workerInfoDto = new WorkerInfoDto();
        workerInfoDto.setWorkerId(worker.getWorkerId());
        workerInfoDto.setFirstName(worker.getFirstName());
        workerInfoDto.setLastName(worker.getLastName());
        workerInfoDto.setHomeAddress(worker.getHomeAddress());
        workerInfoDto.setEmail(worker.getEmail());
        workerInfoDto.setPositionName(position.getPositionName());
        workerInfoDto.setSalary(position.getSalary());
        workerInfoDto.setStoreId(store.getStoreId());
        workerInfoDto.setStoreAddress(store.getAddress());

        mockMvc.perform(post("/worker/new-worker")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(testUtil.getObjectMapper().writeValueAsString(workerInfoDto)))
                .andDo(print())
                .andExpect(status().isCreated());
    }

    @Test
    public void editExistingWorkerSuccessful() throws Exception {
        Worker worker = testUtil.createTransientWorkerWithStoreAndPosition();
        Store store = worker.getStore();
        Position position = worker.getPosition();

        WorkerInfoDto workerInfoDto = new WorkerInfoDto();
        workerInfoDto.setWorkerId(worker.getWorkerId());
        workerInfoDto.setFirstName(worker.getFirstName());
        workerInfoDto.setLastName(worker.getLastName());
        workerInfoDto.setHomeAddress(worker.getHomeAddress());
        workerInfoDto.setEmail(worker.getEmail());
        workerInfoDto.setPositionName(position.getPositionName());
        workerInfoDto.setSalary(position.getSalary());
        workerInfoDto.setStoreId(store.getStoreId());
        workerInfoDto.setStoreAddress(store.getAddress());

        mockMvc.perform(patch("/worker/edit-existing-worker")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(testUtil.getObjectMapper().writeValueAsString(workerInfoDto)))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void editWorkerSuccessful() throws Exception {
        Worker worker = testUtil.createTransientWorkerWithId();
        Store store = testUtil.createTransientStoreWithId();
        Position position = testUtil.createTransientPosition();

        when(storeService.findAll()).thenReturn(List.of(store));
        when(positionService.findAll()).thenReturn(List.of(position));

        mockMvc.perform(get("/worker/add-new-worker/" + worker.getWorkerId()))
                .andDo(print())
                .andExpect(model().attribute("workerId", equalTo(worker.getWorkerId())))
                .andExpect(model().attribute("stores", equalTo(List.of(store))))
                .andExpect(model().attribute("positions", equalTo(List.of(position))))
                .andExpect(view().name("/worker/add-new-worker"));
        verify(storeService, atMostOnce()).findAll();
        verify(positionService, atMostOnce()).findAll();
    }

    @Test
    public void deleteWorkerSuccessful() throws Exception {
        Worker worker = testUtil.createTransientWorkerWithId();

        mockMvc.perform(delete("/worker/delete-worker/" + worker.getWorkerId()))
                .andDo(print())
                .andExpect(status().isAccepted());
        verify(workerService, atMostOnce()).deleteById(worker.getWorkerId());
    }

    @Test
    public void returnWorkerSuccessfull() throws Exception {
        Worker worker = testUtil.createTransientWorkerWithStoreAndPosition();
        Store store = worker.getStore();
        Position position = worker.getPosition();

        WorkerInfoDto workerInfoDto = new WorkerInfoDto();
        workerInfoDto.setWorkerId(worker.getWorkerId());
        workerInfoDto.setFirstName(worker.getFirstName());
        workerInfoDto.setLastName(worker.getLastName());
        workerInfoDto.setHomeAddress(worker.getHomeAddress());
        workerInfoDto.setEmail(worker.getEmail());
        workerInfoDto.setPositionName(position.getPositionName());
        workerInfoDto.setSalary(position.getSalary());
        workerInfoDto.setStoreId(store.getStoreId());
        workerInfoDto.setStoreAddress(store.getAddress());

        when(workerService.findByIdToDto(worker.getWorkerId())).thenReturn(workerInfoDto);

        mockMvc.perform(get("/worker/edit-worker/" + worker.getWorkerId()))
                .andDo(print())
                .andExpect(jsonPath("$.workerId", is(workerInfoDto.getWorkerId().intValue())))
                .andExpect(jsonPath("$.firstName", is(workerInfoDto.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(workerInfoDto.getLastName())))
                .andExpect(jsonPath("$.homeAddress", is(workerInfoDto.getHomeAddress())))
                .andExpect(jsonPath("$.email", is(workerInfoDto.getEmail())))
                .andExpect(jsonPath("$.positionName", is(workerInfoDto.getPositionName())))
                .andExpect(jsonPath("$.salary", is(workerInfoDto.getSalary())))
                .andExpect(jsonPath("$.storeId", is(workerInfoDto.getStoreId().intValue())))
                .andExpect(jsonPath("$.storeAddress", is(workerInfoDto.getStoreAddress())))
                .andExpect(status().isAccepted());

        verify(workerService, atMostOnce()).findByIdToDto(worker.getWorkerId());
    }

    @Test
    public void transferWorkerSuccessful() throws Exception {
        Worker worker = testUtil.createTransientWorkerWithStoreAndPosition();
        Store store = testUtil.createTransientStoreWithId();
        Position position = worker.getPosition();

        WorkerInfoDto workerInfoDto = new WorkerInfoDto();
        workerInfoDto.setWorkerId(worker.getWorkerId());
        workerInfoDto.setFirstName(worker.getFirstName());
        workerInfoDto.setLastName(worker.getLastName());
        workerInfoDto.setHomeAddress(worker.getHomeAddress());
        workerInfoDto.setEmail(worker.getEmail());
        workerInfoDto.setPositionName(position.getPositionName());
        workerInfoDto.setSalary(position.getSalary());
        workerInfoDto.setStoreId(store.getStoreId());
        workerInfoDto.setStoreAddress(store.getAddress());

        mockMvc.perform(patch("/worker/transfer-worker")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(testUtil.getObjectMapper().writeValueAsString(workerInfoDto)))
                .andExpect(status().isCreated())
                .andDo(print());

        verify(workerService, atMostOnce()).transferWorker(workerInfoDto);
    }

    @Test
    public void transferWorkerToSuccessful() throws Exception {
        Worker worker = testUtil.createTransientWorkerWithStoreAndPosition();
        Store store = testUtil.createTransientStoreWithId();
        Position position = worker.getPosition();

        WorkerInfoDto workerInfoDto = new WorkerInfoDto();
        workerInfoDto.setWorkerId(worker.getWorkerId());
        workerInfoDto.setFirstName(worker.getFirstName());
        workerInfoDto.setLastName(worker.getLastName());
        workerInfoDto.setHomeAddress(worker.getHomeAddress());
        workerInfoDto.setEmail(worker.getEmail());
        workerInfoDto.setPositionName(position.getPositionName());
        workerInfoDto.setSalary(position.getSalary());
        workerInfoDto.setStoreId(store.getStoreId());
        workerInfoDto.setStoreAddress(store.getAddress());

        when(workerService.findByIdToDto(worker.getWorkerId())).thenReturn(workerInfoDto);

        mockMvc.perform(get("/worker/get-worker/" + worker.getWorkerId()))
                .andDo(print())
                .andExpect(jsonPath("$.workerId", is(workerInfoDto.getWorkerId().intValue())))
                .andExpect(jsonPath("$.firstName", is(workerInfoDto.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(workerInfoDto.getLastName())))
                .andExpect(jsonPath("$.homeAddress", is(workerInfoDto.getHomeAddress())))
                .andExpect(jsonPath("$.email", is(workerInfoDto.getEmail())))
                .andExpect(jsonPath("$.positionName", is(workerInfoDto.getPositionName())))
                .andExpect(jsonPath("$.salary", is(workerInfoDto.getSalary())))
                .andExpect(jsonPath("$.storeId", is(workerInfoDto.getStoreId().intValue())))
                .andExpect(jsonPath("$.storeAddress", is(workerInfoDto.getStoreAddress())))
                .andExpect(status().isAccepted());
        verify(workerService, atMostOnce()).findByIdToDto(worker.getWorkerId());
    }
}
