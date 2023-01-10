package com.example.storemanager.controller;

import com.example.storemanager.dao.StoreStockRepository;
import com.example.storemanager.dto.RequestProductDto;
import com.example.storemanager.model.StoreStock;
import com.example.storemanager.service.impl.ProductServiceImpl;
import com.example.storemanager.service.impl.StoreServiceImpl;
import com.example.storemanager.service.impl.StoreStockServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class StoreStockController {

    private final StoreStockServiceImpl storeStockServiceImpl;
    private final ProductServiceImpl productServiceImpl;
    private final StoreServiceImpl storeServiceImpl;

    public StoreStockController (StoreStockServiceImpl storeStockServiceImpl,
                                 StoreStockRepository storeStockRepository,
                                 ProductServiceImpl productServiceImpl, StoreServiceImpl storeServiceImpl) {
        this.storeStockServiceImpl = storeStockServiceImpl;
        this.productServiceImpl = productServiceImpl;
        this.storeServiceImpl = storeServiceImpl;
    }

    @RequestMapping(path = "/storestock/current-stock/{warehouseId}/{productId}", method = RequestMethod.GET)
    @ResponseBody
    public Integer getProductQuantityForWarehouse(@PathVariable(name = "warehouseId") Long warehouseId,
                                                  @PathVariable(name = "productId") Long productId) {
        StoreStock storeStock = storeStockServiceImpl.getStockForWarehouseAndProduct(warehouseId, productId);
        return storeStock.getQuantity();
    }
    @RequestMapping(path = "/storestock/reallocate-stock", method = RequestMethod.PATCH)
    @ResponseBody
    public ResponseEntity<?> addProductToStore(@RequestBody RequestProductDto requestProductDto) {
        storeStockServiceImpl.addAndSubtractQuantity(requestProductDto);
        return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
    }

    @RequestMapping(path = "/store/requestproduct/{storeId}")
    public String getWarehouseInfo(@PathVariable(name = "storeId")Long storeId, Model model) {
        model.addAttribute("productInfo", productServiceImpl.findByProductNotIn(storeId));
        model.addAttribute("storeStock", storeStockServiceImpl.getDetailsById(storeId));
        model.addAttribute("warehouses", storeServiceImpl.findAllWarehouses());
        model.addAttribute("storeId", storeId);
        return "store/requestproduct";
    }

    @RequestMapping(path = "/storestock/store-new-product", method = RequestMethod.POST)
    public ResponseEntity<?> addNewProductToStore(@RequestBody List<RequestProductDto> requestProductDtoList) {
        storeStockServiceImpl.addNewProductToStoreList(requestProductDtoList);
        return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
    }
}