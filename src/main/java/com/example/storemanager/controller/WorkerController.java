package com.example.storemanager.controller;

import com.example.storemanager.dto.WorkerInfoDto;
import com.example.storemanager.model.Store;
import com.example.storemanager.model.Worker;
import com.example.storemanager.service.impl.PositionServiceImpl;
import com.example.storemanager.service.impl.StoreServiceImpl;
import com.example.storemanager.service.impl.WorkerServiceImpl;
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
public class WorkerController {

    private final WorkerServiceImpl workerService;
    private final PositionServiceImpl positionService;
    private final StoreServiceImpl storeService;

    public WorkerController(WorkerServiceImpl workerService, PositionServiceImpl positionService, StoreServiceImpl storeService) {
        this.workerService = workerService;
        this.positionService = positionService;
        this.storeService = storeService;
    }

    @RequestMapping(path = "/worker/staffdetails")
    public String getAllWorkers(Model model) {
        model.addAttribute("workers", workerService.getAllWorkerDetails());
        model.addAttribute("stores", storeService.findAll());
        return "/worker/staffdetails";
    }

    @RequestMapping(path = "/worker/add-new-worker")
    public String newWorkerPage(Model model){
        model.addAttribute("positions", positionService.findAll());
        model.addAttribute("stores", storeService.findAll());
        return "/worker/add-new-worker";
    }

    @RequestMapping(path = "/worker/new-worker", method = RequestMethod.POST)
    public ResponseEntity<?> addNewWorker(@RequestBody WorkerInfoDto worker) {
        workerService.addOrUpdateNewWorker(worker);
        return new ResponseEntity<>(null, HttpStatus.CREATED);
    }
    @RequestMapping(path = "/worker/edit-existing-worker", method = RequestMethod.PATCH)
    public ResponseEntity<?> editExistingWorker(@RequestBody WorkerInfoDto worker) {
        workerService.addOrUpdateNewWorker(worker);
        return new ResponseEntity<>(null, HttpStatus.OK);
    }

    @RequestMapping(path = "/worker/add-new-worker/{workerId}")
    public String editWorker (@PathVariable(name = "workerId") Long workerId, Model model) {
        model.addAttribute("workerId", workerId);
        model.addAttribute("positions", positionService.findAll());
        model.addAttribute("stores", storeService.findAll());
        return "/worker/add-new-worker";
    }
    @RequestMapping(path = "/worker/delete-worker/{workerId}", method = RequestMethod.PATCH)
    public ResponseEntity<?> deleteWorker(@PathVariable(name = "workerId") Long workerId) {
        workerService.deleteById(workerId);
        return new ResponseEntity<>(null, HttpStatus.ACCEPTED);
    }

    @RequestMapping(path = "/worker/edit-worker/{workerId}", method = RequestMethod.GET)
    public ResponseEntity<?> returnWorker(@PathVariable(name = "workerId") Long id) {
        WorkerInfoDto workerInfoDto = workerService.findByIdToDto(id);
        return new ResponseEntity<>(workerInfoDto, HttpStatus.ACCEPTED);
    }

    @RequestMapping(path = "/worker/transfer-worker", method = RequestMethod.PATCH)
    public ResponseEntity<?> transferWorker(@RequestBody WorkerInfoDto dto) {
        workerService.transferWorker(dto);
        return new ResponseEntity<>(null, HttpStatus.CREATED);
    }

    @RequestMapping(path = "/worker/get-worker/{workerId}", method = RequestMethod.GET)
    public ResponseEntity<?> transferWorkerTo(@PathVariable(name = "workerId") Long id) {
        WorkerInfoDto worker = workerService.findByIdToDto(id);
        return new ResponseEntity<>(worker, HttpStatus.ACCEPTED);
    }

}
