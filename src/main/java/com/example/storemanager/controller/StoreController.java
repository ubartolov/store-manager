package com.example.storemanager.controller;

import com.example.storemanager.service.impl.ProductServiceImpl;
import com.example.storemanager.service.impl.StoreServiceImpl;
import com.example.storemanager.service.impl.StoreStockServiceImpl;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class StoreController {

    private StoreServiceImpl storeServiceImpl;
    private StoreStockServiceImpl storeStockServiceImpl;
    private ProductServiceImpl productServiceImpl;
    public StoreController(StoreServiceImpl storeServiceImpl, StoreStockServiceImpl storeStockServiceImpl,
                           ProductServiceImpl productServiceImpl) {
        this.storeServiceImpl = storeServiceImpl;
        this.storeStockServiceImpl = storeStockServiceImpl;
        this.productServiceImpl = productServiceImpl;
    }


    @RequestMapping("/store/storespage")
    public String getAllStores(Model model) {
        model.addAttribute("stores", storeServiceImpl.findAllStores());
        return "store/storespage";
    }

    @RequestMapping("store/storedetails/{id}")
    public String getDetails(@PathVariable(name = "id") Long storeId, Model model) {
        model.addAttribute("storeStock", storeStockServiceImpl.getDetailsById(storeId));
        model.addAttribute("warehouses", storeServiceImpl.findAllWarehouses());
        model.addAttribute("storeId", storeId);
        return "store/storedetails";
    }


}
