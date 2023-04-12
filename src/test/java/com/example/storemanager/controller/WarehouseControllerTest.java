package com.example.storemanager.controller;

import com.example.storemanager.dto.StoreStockDto;
import com.example.storemanager.dto.WorkerInfoDto;
import com.example.storemanager.model.Product;
import com.example.storemanager.model.Store;
import com.example.storemanager.model.Worker;
import com.example.storemanager.service.StoreService;
import com.example.storemanager.service.StoreStockService;
import com.example.storemanager.service.WorkerService;
import com.example.storemanager.testutil.TestUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.atMostOnce;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
public class WarehouseControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private TestUtil testUtil;
    @MockBean
    StoreService storeService;
    @MockBean
    StoreStockService storeStockService;
    @MockBean
    WorkerService workerService;

    @Test
    public void findAllStoresSuccessful() throws Exception {
        Store warehouse = testUtil.createTransientWarehouse();
        when(storeService.findAllStores()).thenReturn(List.of(warehouse));

        mockMvc.perform(get("/warehouse/warehousespage"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("/warehouse/warehousespage"));
        verify(storeService, atMostOnce()).findAllWarehouses();
    }

    @Test
    public void getDetailsSuccessful() throws Exception {
        Store warehouse = testUtil.createTransientWarehouseWithId();
        Product product = testUtil.createTransientProductWithId();
        Worker worker = testUtil.createTransientWorkerWithPosition();

        StoreStockDto storeStockDto = new StoreStockDto();
        storeStockDto.setStoreId(warehouse.getStoreId());
        storeStockDto.setProductName(product.getProductName());
        storeStockDto.setProductId(product.getProductId());
        storeStockDto.setStoreType(warehouse.getStoreType());
        storeStockDto.setProductPrice(product.getProductPrice());
        storeStockDto.setQuantity(500);

        WorkerInfoDto workerInfoDto = new WorkerInfoDto();
        workerInfoDto.setWorkerId(worker.getWorkerId());
        workerInfoDto.setPositionId(worker.getPosition().getPositionId());
        workerInfoDto.setStoreId(warehouse.getStoreId());
        workerInfoDto.setFirstName(worker.getFirstName());
        workerInfoDto.setLastName(worker.getLastName());
        workerInfoDto.setHomeAddress(worker.getHomeAddress());
        workerInfoDto.setStoreAddress(warehouse.getAddress());
        workerInfoDto.setEmail(worker.getEmail());
        workerInfoDto.setSalary(worker.getPosition().getSalary());
        workerInfoDto.setPositionName(worker.getPosition().getPositionName());


        when(storeService.findById(warehouse.getStoreId())).thenReturn(warehouse);
        when(storeStockService.getDetailsById(warehouse.getStoreId())).thenReturn(List.of(storeStockDto));
        when(workerService.getWorkerDetailsById(warehouse.getStoreId())).thenReturn(List.of(workerInfoDto));

        mockMvc.perform(get("/warehouse/warehousedetails/" + warehouse.getStoreId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("/warehouse/warehousedetails"))
                .andExpect(model().attribute("storeStock", equalTo(List.of(storeStockDto))));
        verify(storeService, atMostOnce()).findById(warehouse.getStoreId());
        verify(storeService, atMostOnce()).findAllWarehouses();
        verify(storeStockService, atMostOnce()).getDetailsById(warehouse.getStoreId());
        verify(workerService, atMostOnce()). getWorkerDetailsById(warehouse.getStoreId());
    }

    @Test
    public void deleteStoreSuccessful() throws Exception {
        Store warehouse = testUtil.createTransientWarehouseStockAndWorkers();
        Store existingWarehouse = testUtil.createTransientWarehouseStockAndWorkers();
        existingWarehouse.setStoreId(124L);

        mockMvc.perform(patch("/warehouse/delete-store/" + existingWarehouse.getStoreId() + "/" + warehouse.getStoreId()))
                .andDo(print())
                .andExpect(status().isOk());

        verify(storeStockService, atMostOnce()).deleteWarehouse(existingWarehouse.getStoreId(), warehouse.getStoreId());
    }
}
