package com.example.storemanager.controller;

import com.example.storemanager.dto.StoreDto;
import com.example.storemanager.model.Store;
import com.example.storemanager.service.impl.StoreServiceImpl;
import com.example.storemanager.service.impl.StoreStockServiceImpl;
import com.example.storemanager.service.impl.WorkerServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class WarehouseController {

    public final StoreServiceImpl storeService;
    public final StoreStockServiceImpl storeStockService;
    public final WorkerServiceImpl workerService;


    public WarehouseController(StoreServiceImpl storeService, StoreStockServiceImpl storeStockService, WorkerServiceImpl workerService) {
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

}
