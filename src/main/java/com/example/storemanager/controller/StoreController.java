package com.example.storemanager.controller;

import com.example.storemanager.dto.StoreDto;
import com.example.storemanager.service.impl.ProductServiceImpl;
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
public class StoreController {

    private StoreServiceImpl storeServiceImpl;
    private StoreStockServiceImpl storeStockServiceImpl;
    private ProductServiceImpl productServiceImpl;
    private WorkerServiceImpl workerService;
    public StoreController(StoreServiceImpl storeServiceImpl, StoreStockServiceImpl storeStockServiceImpl,
                           ProductServiceImpl productServiceImpl, WorkerServiceImpl workerService) {
        this.storeServiceImpl = storeServiceImpl;
        this.storeStockServiceImpl = storeStockServiceImpl;
        this.productServiceImpl = productServiceImpl;
        this.workerService = workerService;
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
        model.addAttribute("workers", workerService.getWorkerDetailsById(storeId));
        return "store/storedetails";
    }

    @RequestMapping(path = "/common/new-store/{origin}")
    public String newStore (@PathVariable(name = "origin")String origin, Model model) {
        String returnTo = origin.equals("storespage") ? "/store/storespage" : "/warehouse/warehousespage";
        model.addAttribute("origin", returnTo);
        return "common/new-store";
    }

    @RequestMapping(path = "/common/new-store/{origin}/{storeId}")
    public String editStore (@PathVariable(name = "storeId")Long id, @PathVariable(name = "origin") String origin, Model model) {
        String returnTo = origin.equals("storespage") ? "/store/storespage" : "/warehouse/warehousespage";
        model.addAttribute("storeId", id);
        model.addAttribute("origin", returnTo);
        return "common/new-store";
    }

    @RequestMapping(path = "/store/add-new-store", method = RequestMethod.POST)
    public ResponseEntity<?> addingNewStore (@RequestBody StoreDto storeDto) {
        storeServiceImpl.addOrUpdateStore(storeDto);
        return new ResponseEntity<>(null, HttpStatus.CREATED);
    }

    @RequestMapping(path = "/store/edit-store/{storeId}", method = RequestMethod.GET)
    public ResponseEntity<?> editingStore (@PathVariable(name = "storeId") Long id) {
        StoreDto storeDto = storeServiceImpl.findByIdDto(id);
        return new ResponseEntity<>(storeDto, HttpStatus.CREATED);
    }
}
