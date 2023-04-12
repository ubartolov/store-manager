package com.example.storemanager.controller;


import com.example.storemanager.controller.StoreController;
import com.example.storemanager.dto.StoreDto;
import com.example.storemanager.dto.StoreStockDto;
import com.example.storemanager.dto.WorkerInfoDto;
import com.example.storemanager.exception.AppException;
import com.example.storemanager.model.Product;
import com.example.storemanager.model.Store;
import com.example.storemanager.model.Worker;
import com.example.storemanager.service.StoreService;
import com.example.storemanager.service.StoreStockService;
import com.example.storemanager.service.WorkerService;
import com.example.storemanager.testutil.TestUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.event.annotation.BeforeTestClass;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

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
public class StoreControllerTest {

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
        Store store = testUtil.createTransientStore();
        when(storeService.findAllStores()).thenReturn(List.of(store));

        mockMvc.perform(get("/store/storespage"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("store/storespage"));
        verify(storeService, atMostOnce()).findAllStores();
    }

    @Test
    public void getDetailsSuccessful() throws Exception {
        Store store = testUtil.createTransientStoreWithId();
        Store warehouse = testUtil.createTransientWarehouse();
        Product product = testUtil.createTransientProductWithId();
        Worker worker = testUtil.createTransientWorkerWithPosition();

        StoreStockDto storeStockDto = new StoreStockDto();
        storeStockDto.setStoreId(store.getStoreId());
        storeStockDto.setProductName(product.getProductName());
        storeStockDto.setProductId(product.getProductId());
        storeStockDto.setStoreType(store.getStoreType());
        storeStockDto.setProductPrice(product.getProductPrice());
        storeStockDto.setQuantity(500);

        WorkerInfoDto workerInfoDto = new WorkerInfoDto();
        workerInfoDto.setWorkerId(worker.getWorkerId());
        workerInfoDto.setPositionId(worker.getPosition().getPositionId());
        workerInfoDto.setStoreId(store.getStoreId());
        workerInfoDto.setFirstName(worker.getFirstName());
        workerInfoDto.setLastName(worker.getLastName());
        workerInfoDto.setHomeAddress(worker.getHomeAddress());
        workerInfoDto.setStoreAddress(store.getAddress());
        workerInfoDto.setEmail(worker.getEmail());
        workerInfoDto.setSalary(worker.getPosition().getSalary());
        workerInfoDto.setPositionName(worker.getPosition().getPositionName());


        when(storeService.findById(store.getStoreId())).thenReturn(store);
        when(storeService.findAllWarehouses()).thenReturn(List.of(warehouse));
        when(storeStockService.getDetailsById(store.getStoreId())).thenReturn(List.of(storeStockDto));
        when(workerService.getWorkerDetailsById(store.getStoreId())).thenReturn(List.of(workerInfoDto));

        mockMvc.perform(get("/store/storedetails/" + store.getStoreId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("store/storedetails"))
                .andExpect(model().attribute("storeStock", equalTo(List.of(storeStockDto))));
        verify(storeService, atMostOnce()).findById(store.getStoreId());
        verify(storeService, atMostOnce()).findAllWarehouses();
        verify(storeStockService, atMostOnce()).getDetailsById(store.getStoreId());
        verify(workerService, atMostOnce()). getWorkerDetailsById(store.getStoreId());
    }

    @Test
    public void getDetailsNoStoreId() throws Exception {
        Store store = testUtil.createTransientStoreStockAndWorkers();

        when(storeStockService.getDetailsById(store.getStoreId())).thenThrow(
                new AppException("Store with the ID number '123L' cannot be found"));

        mockMvc.perform(get("/store/storedetails/" + store.getStoreId()))
                .andDo(print())
                .andExpect(jsonPath("prettyErrorMessage",
                        is("Store with the ID number '123L' cannot be found")))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void getDetailsWorkerFail() throws Exception {
        Store store = testUtil.createTransientStoreStockAndWorkers();
        when(storeService.findById(any())).thenReturn(store);
        doThrow(new AppException("Store with the ID number '123L' cannot be found"))
                .when(workerService).getWorkerDetailsById(any());


        mockMvc.perform(get("/store/storedetails/" + store.getStoreId()))
                .andDo(print())
                .andExpect(jsonPath("prettyErrorMessage",
                        is("Store with the ID number '123L' cannot be found")))
                .andExpect(status().isBadRequest());
    }


    @Test
    public void newStoreSuccessful() throws Exception {

        String returnTo = "/store/storespage";

        mockMvc.perform(get("/common/new-store/storespage"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("common/new-store"))
                .andExpect(model().attribute("origin", returnTo));
    }
    

    @Test
    public void editStoreSuccesful() throws Exception {

        String returnTo = "/store/storespage";
        Long storeId = 1L;
        mockMvc.perform(get("/common/new-store/storespage/" + storeId))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("common/new-store"))
                .andExpect(model().attribute("origin", returnTo))
                .andExpect(model().attribute("storeId", storeId));
    }

    @Test
    public void addingNewStoreSuccessful() throws Exception {
        Store store = testUtil.createTransientStoreWithId();

        StoreDto storeDto = new StoreDto();
        storeDto.setAddress(store.getAddress());
        storeDto.setStoreType(store.getStoreType().getCode());

        when(storeService.addOrUpdateStore(storeDto)).thenReturn(store);
        mockMvc.perform(post("/store/add-new-store")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(testUtil.getObjectMapper().writeValueAsString(storeDto)))
                .andDo(print())
                .andExpect(status().isCreated());
    }

    @Test
    public void editingStoreSuccessful() throws Exception {
        Store store = testUtil.createTransientStoreWithId();

        StoreDto storeDto = new StoreDto();
        storeDto.setStoreId(store.getStoreId());
        storeDto.setAddress(store.getAddress());
        storeDto.setStoreType(store.getStoreType().getCode());

        when(storeService.findByIdDto(store.getStoreId())).thenReturn(storeDto);

        mockMvc.perform(get("/store/edit-store/" + store.getStoreId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.storeId", is(store.getStoreId().intValue())))
                .andExpect(jsonPath("$.address", is(store.getAddress())))
                .andExpect(jsonPath("$.storeType", is(store.getStoreType().getCode())));
    }

    @Test
    public void deleteStoreSuccessful() throws Exception {
        Store store = testUtil.createTransientStoreWithId();

        mockMvc.perform(patch("/delete-store/" + store.getStoreId()))
                .andDo(print())
                .andExpect(status().isOk());

        verify(storeService, atMostOnce()).deleteById(store.getStoreId());
    }

}
