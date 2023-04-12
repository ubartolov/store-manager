package com.example.storemanager.controller;

import com.example.storemanager.dto.*;
import com.example.storemanager.model.Product;
import com.example.storemanager.model.Store;
import com.example.storemanager.service.ProductService;
import com.example.storemanager.service.StoreService;
import com.example.storemanager.service.StoreStockService;
import com.example.storemanager.service.WorkerService;
import com.example.storemanager.testutil.TestUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
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
public class StoreStockControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private TestUtil testUtil;
    @MockBean
    StoreService storeService;
    @MockBean
    ProductService productService;
    @MockBean
    StoreStockService storeStockService;
    @MockBean
    WorkerService workerService;

    @Test
    public void getProductQuantityForWarehouseSuccessful() throws Exception {
        Store warehouse = testUtil.createTransientWarehouseStockAndWorkers();
        Product product = warehouse.getStoreStock().get(0).getProduct();

        when(storeStockService.getStockForWarehouseAndProduct(warehouse.getStoreId(), product.getProductId()))
                .thenReturn(warehouse.getStoreStock().get(0).getQuantity());

        mockMvc.perform(get("/storestock/current-stock/" + warehouse.getStoreId() + "/" + product.getProductId()))
                .andDo(print());
        verify(storeStockService, atMostOnce()).getStockForWarehouseAndProduct(warehouse.getStoreId(), product.getProductId());
    }

    @Test
    public void addProductToStoreSuccessful() throws Exception {
        Store store = testUtil.createTransientStoreStockAndWorkers();
        Store warehouse = testUtil.createTransientWarehouseStockAndWorkers();
        Product product = store.getStoreStock().get(0).getProduct();

        RequestProductDto rpd = new RequestProductDto();
        rpd.setStoreId(store.getStoreId());
        rpd.setWarehouseId(warehouse.getStoreId());
        rpd.setProductId(product.getProductId());
        rpd.setProductName(product.getProductName());
        rpd.setProductPrice(product.getProductPrice());
        rpd.setRequestAmount(200);

        mockMvc.perform(patch("/storestock/reallocate-stock")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(testUtil.getObjectMapper().writeValueAsString(rpd)))
                .andDo(print())
                .andExpect(status().isOk());
        verify(storeStockService, atMostOnce()).addQuantityFromWarehouse(rpd);
    }

    @Test
    public void requestNewProductToStoreSuccessful() throws Exception {
        Store store = testUtil.createTransientStoreStockAndWorkers();
        Product product = testUtil.createTransientProductWithId();
        Store warehouse = testUtil.createTransientWarehouseWithId();

        StoreStockDto storeStockDto = new StoreStockDto();
        storeStockDto.setStoreId(store.getStoreId());
        storeStockDto.setStoreType(store.getStoreType());
        storeStockDto.setProductId(product.getProductId());
        storeStockDto.setProductName(product.getProductName());
        storeStockDto.setQuantity(20);

        when(productService.findByProductNotIn(store.getStoreId())).thenReturn(List.of(product));
        when(storeStockService.getDetailsById(store.getStoreId())).thenReturn(List.of(storeStockDto));
        when(storeService.findAllWarehouses()).thenReturn(List.of(warehouse));

        mockMvc.perform(get("/store/requestproduct/" + store.getStoreId()))
                .andDo(print())
                .andExpect(model().attribute("productInfo", is(List.of(product))))
                .andExpect(model().attribute("storeStock", is(List.of(storeStockDto))))
                .andExpect(model().attribute("warehouses", is(List.of(warehouse))))
                .andExpect(model().attribute("storeId", is(store.getStoreId())))
                .andExpect(view().name("store/requestproduct"));

        verify(productService, atMostOnce()).findByProductNotIn(store.getStoreId());
        verify(storeStockService, atMostOnce()).getDetailsById(store.getStoreId());
        verify(storeService, atMostOnce()).findAllWarehouses();
    }

    @Test
    public void addNewProductToStoreSuccessful() throws Exception {
        Store store = testUtil.createTransientStoreStockAndWorkers();
        Store warehouse = testUtil.createTransientWarehouseStockAndWorkers();
        Product product = warehouse.getStoreStock().get(0).getProduct();

        RequestProductDto requestProductDto = new RequestProductDto();
        requestProductDto.setStoreId(store.getStoreId());
        requestProductDto.setWarehouseId(warehouse.getStoreId());
        requestProductDto.setProductId(product.getProductId());
        requestProductDto.setProductName(product.getProductName());
        requestProductDto.setProductPrice(product.getProductPrice());
        requestProductDto.setRequestAmount(20);

        when(storeStockService.addNewProductToStoreList(List.of(requestProductDto))).thenReturn(List.of(requestProductDto));

        mockMvc.perform(post("/storestock/store-new-product")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(testUtil.getObjectMapper().writeValueAsString(List.of(requestProductDto))))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$[0].storeId", is(requestProductDto.getStoreId().intValue())))
                .andExpect(jsonPath("$[0].warehouseId", is(requestProductDto.getWarehouseId().intValue())))
                .andExpect(jsonPath("$[0].productId", is(requestProductDto.getProductId().intValue())))
                .andExpect(jsonPath("$[0].productName", is(requestProductDto.getProductName())))
                .andExpect(jsonPath("$[0].productPrice", is(requestProductDto.getProductPrice())))
                .andExpect(jsonPath("$[0].requestAmount", is(requestProductDto.getRequestAmount())));
    }

    @Test
    public void updateProductInWarehouseSuccessful() throws Exception {
        Store warehouse = testUtil.createTransientWarehouseStockAndWorkers();
        Product product = warehouse.getStoreStock().get(0).getProduct();

        UpdateProductDto updateProductDto = new UpdateProductDto();
        updateProductDto.setWarehouseId(warehouse.getStoreId());
        updateProductDto.setProductId(product.getProductId());
        updateProductDto.setRequestAmount(20);

        mockMvc.perform(patch("/storestock/update-warehouse-stock")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(testUtil.getObjectMapper().writeValueAsString(updateProductDto)))
                .andExpect(status().isNoContent())
                .andDo(print());
        verify(storeStockService, atMostOnce()).updateProductQuantity(updateProductDto);
    }

    @Test
    public void getWarehouseInfoSuccessful() throws Exception {
        Store warehouse = testUtil.createTransientWarehouseWithId();

        mockMvc.perform(get("/warehouse/addnewproduct/" + warehouse.getStoreId()))
                .andDo(print())
                .andExpect(view().name("warehouse/addnewproduct"))
                .andExpect(model().attribute("warehouseId", equalTo(warehouse.getStoreId())));
    }

    @Test
    public void addNewProductToWarehouseSuccessful() throws Exception {
        Store warehouse = testUtil.createTransientWarehouseStockAndWorkers();
        Product product = warehouse.getStoreStock().get(0).getProduct();

        RequestProductWarehouseDto requestProductDto = new RequestProductWarehouseDto();
        requestProductDto.setWarehouseId(warehouse.getStoreId());
        requestProductDto.setProductName(product.getProductName());
        requestProductDto.setProductPrice(product.getProductPrice());
        requestProductDto.setRequestAmount(20);

        when(storeStockService.addNewProductToWarehouseList(List.of(requestProductDto))).thenReturn(List.of(requestProductDto));

        mockMvc.perform(post("/storestock/warehouse-new-product")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(testUtil.getObjectMapper().writeValueAsString(List.of(requestProductDto))))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$[0].warehouseId", is(requestProductDto.getWarehouseId().intValue())))
                .andExpect(jsonPath("$[0].productName", is(requestProductDto.getProductName())))
                .andExpect(jsonPath("$[0].productPrice", is(requestProductDto.getProductPrice())))
                .andExpect(jsonPath("$[0].requestAmount", is(requestProductDto.getRequestAmount())));
    }

    @Test
    public void deleteProductFromWarehouseSuccessful() throws Exception {
        Store warehouse = testUtil.createTransientWarehouseStockAndWorkers();
        Product product = warehouse.getStoreStock().get(0).getProduct();

        DeleteProductDto deleteProductDto = new DeleteProductDto();
        deleteProductDto.setStoreId(warehouse.getStoreId());
        deleteProductDto.setProductId(product.getProductId());

        mockMvc.perform(delete("/storestock/delete-product-warehouse")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(testUtil.getObjectMapper().writeValueAsString(deleteProductDto)))
                .andExpect(status().isNoContent())
                .andDo(print());

        verify(storeStockService, atMostOnce()).deleteProductFromStoreStock(deleteProductDto);
    }

    @Test
    public void returnProductFromStoreSuccessful() throws Exception{
        Store store = testUtil.createTransientStoreStockAndWorkers();
        Store warehouse = testUtil.createTransientWarehouseStockAndWorkers();
        Product product = store.getStoreStock().get(0).getProduct();

        RequestProductDto requestProductDto = new RequestProductDto();
        requestProductDto.setStoreId(store.getStoreId());
        requestProductDto.setWarehouseId(warehouse.getStoreId());
        requestProductDto.setProductId(product.getProductId());
        requestProductDto.setProductName(product.getProductName());
        requestProductDto.setProductPrice(product.getProductPrice());
        requestProductDto.setRequestAmount(20);

        mockMvc.perform(patch("/storestock/return-product-store")
                .contentType(MediaType.APPLICATION_JSON)
                .content(testUtil.getObjectMapper().writeValueAsString(requestProductDto)))
                .andDo(print())
                .andExpect(status().isNoContent());

        verify(storeStockService, atMostOnce()).returnProductToWarehouse(requestProductDto);
    }
}
