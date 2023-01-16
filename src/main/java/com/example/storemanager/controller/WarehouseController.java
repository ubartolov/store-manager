package com.example.storemanager.controller;

import com.example.storemanager.service.impl.StoreServiceImpl;
import com.example.storemanager.service.impl.StoreStockServiceImpl;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class WarehouseController {

    public final StoreServiceImpl storeService;
    public final StoreStockServiceImpl storeStockService;


    public WarehouseController(StoreServiceImpl storeService, StoreStockServiceImpl storeStockService) {
        this.storeService = storeService;
        this.storeStockService = storeStockService;
    }

    @RequestMapping(path = "/warehouse/warehousespage")
    public String getAllWarehouses(Model model) {
        model.addAttribute("warehouses", storeService.findAllWarehouses());
        return "warehouse/warehousespage";
    }

    @RequestMapping(path = "warehouse/warehousedetails/{warehouseId}")
    public String getWarehouseDetails(@PathVariable Long warehouseId, Model model) {
        model.addAttribute("storeStock", storeStockService.getDetailsById(warehouseId));
        model.addAttribute("warehouseId", warehouseId);
        return "warehouse/warehousedetails";
    }
}
