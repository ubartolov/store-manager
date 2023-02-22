package com.example.storemanager.controller;

import com.example.storemanager.model.Store;
import com.example.storemanager.service.StoreService;
import com.example.storemanager.service.StoreStockService;
import com.example.storemanager.service.WorkerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class WarehouseController {

    public final StoreService storeService;
    public final StoreStockService storeStockService;
    public final WorkerService workerService;


    public WarehouseController(StoreService storeService, StoreStockService storeStockService, WorkerService workerService) {
        this.storeService = storeService;
        this.storeStockService = storeStockService;
        this.workerService = workerService;
    }

    @RequestMapping(path = "/warehouse/warehousespage")
    public String getAllWarehouses(Model model) {
        model.addAttribute("warehouses", storeService.findAllWarehouses());
        return "warehouse/warehousespage";
    }

    @RequestMapping(path = "warehouse/warehousedetails/{warehouseId}")
    public String getWarehouseDetails(@PathVariable Long warehouseId, Model model) {
        model.addAttribute("storeStock", storeStockService.getDetailsById(warehouseId));
        Store store = storeService.findById(warehouseId);
        model.addAttribute("warehouseId", store.getStoreId());
        model.addAttribute("warehouseAddress", store.getAddress());
        model.addAttribute("workers", workerService.getWorkerDetailsById(warehouseId));
        return "warehouse/warehousedetails";
    }

    @RequestMapping(path = "/warehouse/delete-store/{existingWarehouseId}/{warehouseId}", method = RequestMethod.PATCH)
    public ResponseEntity<?> deleteStore(@PathVariable(name = "existingWarehouseId") Long existingWarehouseId,
                                         @PathVariable(name = "warehouseId") Long warehouseId) {
        storeStockService.deleteWarehouse(existingWarehouseId, warehouseId);
        return new ResponseEntity<>(null, HttpStatus.OK);
    }

}
