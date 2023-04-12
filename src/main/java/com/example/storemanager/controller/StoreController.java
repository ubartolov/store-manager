package com.example.storemanager.controller;

import com.example.storemanager.dto.StoreDto;
import com.example.storemanager.model.Store;
import com.example.storemanager.service.StoreService;
import com.example.storemanager.service.StoreStockService;
import com.example.storemanager.service.WorkerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@Controller
public class StoreController {

    private final StoreService storeService;
    private final StoreStockService storeStockService;
    private final WorkerService workerService;

    public StoreController(StoreService storeService, StoreStockService storeStockService, WorkerService workerService) {
        this.storeService = storeService;
        this.storeStockService = storeStockService;
        this.workerService = workerService;
    }


    @RequestMapping("/store/storespage")
    public String getAllStores(Model model) {
        List<Store> storeList = storeService.findAllStores();
        model.addAttribute("stores", storeList);
        return "store/storespage";
    }

    @RequestMapping("/store/storedetails/{id}")
    public String getDetails(@PathVariable(name = "id") Long storeId, Model model) {
        model.addAttribute("storeStock", storeStockService.getDetailsById(storeId));
        model.addAttribute("warehouses", storeService.findAllWarehouses());
        Store store = storeService.findById(storeId);
        model.addAttribute("storeId", store.getStoreId());
        model.addAttribute("storeAddress", store.getAddress());
        model.addAttribute("workers", workerService.getWorkerDetailsById(storeId));
        return "store/storedetails";
    }

    @RequestMapping(path = "/common/new-store/{origin}")
    public String newStore(@PathVariable(name = "origin") String origin, Model model) {
        String returnTo = origin.equals("storespage") ? "/store/storespage" : "/warehouse/warehousespage";
        model.addAttribute("origin", returnTo);
        return "common/new-store";
    }

    @RequestMapping(path = "/common/new-store/{origin}/{storeId}")
    public String editStore(@PathVariable(name = "storeId") Long id, @PathVariable(name = "origin") String origin, Model model) {
        String returnTo = origin.equals("storespage") ? "/store/storespage" : "/warehouse/warehousespage";
        model.addAttribute("storeId", id);
        model.addAttribute("origin", returnTo);
        return "common/new-store";
    }


    @RequestMapping(path = "/store/add-new-store", method = RequestMethod.POST)
    public ResponseEntity<?> addingNewStore(@RequestBody StoreDto storeDto) {
        storeService.addOrUpdateStore(storeDto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @RequestMapping(path = "/store/edit-store/{storeId}", method = RequestMethod.GET)
    public ResponseEntity<?> editingStore(@PathVariable(name = "storeId") Long id) {
        StoreDto storeDto = storeService.findByIdDto(id);
        return new ResponseEntity<>(storeDto, HttpStatus.OK);
    }

    @RequestMapping(path = "/delete-store/{storeId}", method = RequestMethod.PATCH)
    public ResponseEntity<?> deleteStore(@PathVariable(name = "storeId") Long id) {
        storeService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
