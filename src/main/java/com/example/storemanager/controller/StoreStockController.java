package com.example.storemanager.controller;

import com.example.storemanager.dao.StoreStockRepository;
import com.example.storemanager.dto.DeleteProductDto;
import com.example.storemanager.dto.RequestProductDto;
import com.example.storemanager.dto.RequestProductWarehouseDto;
import com.example.storemanager.dto.UpdateProductDto;
import com.example.storemanager.service.ProductService;
import com.example.storemanager.service.StoreService;
import com.example.storemanager.service.StoreStockService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class StoreStockController {

    private final StoreStockService storeStockServiceImpl;
    private final ProductService productServiceImpl;
    private final StoreService storeServiceImpl;

    public StoreStockController (StoreStockService storeStockServiceImpl,
                                 StoreStockRepository storeStockRepository,
                                 ProductService productServiceImpl, StoreService storeServiceImpl) {
        this.storeStockServiceImpl = storeStockServiceImpl;
        this.productServiceImpl = productServiceImpl;
        this.storeServiceImpl = storeServiceImpl;
    }

    @RequestMapping(path = "/storestock/current-stock/{warehouseId}/{productId}", method = RequestMethod.GET)
    @ResponseBody
    public Integer getProductQuantityForWarehouse(@PathVariable(name = "warehouseId") Long warehouseId,
                                                  @PathVariable(name = "productId") Long productId) {
        Integer storeStock = storeStockServiceImpl.getStockForWarehouseAndProduct(warehouseId, productId);
        return storeStock;
    }
    @RequestMapping(path = "/storestock/reallocate-stock", method = RequestMethod.PATCH)
    @ResponseBody
    public ResponseEntity<?> addProductToStore(@RequestBody RequestProductDto requestProductDto) {
        storeStockServiceImpl.addQuantityFromWarehouse(requestProductDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(path = "/store/requestproduct/{storeId}")
    public String requestNewProductToStore(@PathVariable(name = "storeId")Long storeId, Model model) {
        model.addAttribute("productInfo", productServiceImpl.findByProductNotIn(storeId));
        model.addAttribute("storeStock", storeStockServiceImpl.getDetailsById(storeId));
        model.addAttribute("warehouses", storeServiceImpl.findAllWarehouses());
        model.addAttribute("storeId", storeId);
        return "store/requestproduct";
    }

    @RequestMapping(path = "/storestock/store-new-product", method = RequestMethod.POST)
    public ResponseEntity<?> addNewProductToStore(@RequestBody List<RequestProductDto> requestProductDtoList) {
        List<RequestProductDto> result = storeStockServiceImpl.addNewProductToStoreList(requestProductDtoList);
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    @RequestMapping(path = "/storestock/update-warehouse-stock", method = RequestMethod.PATCH)
    public ResponseEntity<?> updateProductInWarehouse(@RequestBody UpdateProductDto updateProductDto) {
        storeStockServiceImpl.updateProductQuantity(updateProductDto);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @RequestMapping(path = "/warehouse/addnewproduct/{warehouseId}")
    public String getWarehouseInfo(@PathVariable(name = "warehouseId")Long warehouseId, Model model) {
        model.addAttribute("warehouseId", warehouseId);
        return "warehouse/addnewproduct";
    }

    @RequestMapping(path = "/storestock/warehouse-new-product", method = RequestMethod.POST)
    public ResponseEntity<?> addNewProductToWarehouse(@RequestBody List<RequestProductWarehouseDto> warehouseDtoList) {
        List<RequestProductWarehouseDto> result = storeStockServiceImpl.addNewProductToWarehouseList(warehouseDtoList);
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }
    @RequestMapping(path = "/storestock/delete-product-warehouse", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteProductFromWarehouse(@RequestBody DeleteProductDto deleteProductDto) {
        storeStockServiceImpl.deleteProductFromStoreStock(deleteProductDto);
        return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
    }

    @RequestMapping(path = "/storestock/return-product-store", method = RequestMethod.PATCH)
    public ResponseEntity<?> returnProductFromStore(@RequestBody RequestProductDto requestProductDto) {
        storeStockServiceImpl.returnProductToWarehouse(requestProductDto);
        return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
    }


}
