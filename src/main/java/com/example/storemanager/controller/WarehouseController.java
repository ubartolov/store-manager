package com.example.storemanager.controller;

import com.example.storemanager.service.impl.StoreServiceImpl;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class WarehouseController {

    public final StoreServiceImpl storeService;


    public WarehouseController(StoreServiceImpl storeService) {
        this.storeService = storeService;
    }

    @RequestMapping(path = "/warehouse/warehousespage")
    public String getAllWarehouses(Model model) {
        model.addAttribute("warehouses", storeService.findAllWarehouses());
        return "warehouse/warehousespage";
    }
}
