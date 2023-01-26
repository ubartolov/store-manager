package com.example.storemanager.controller;

import com.example.storemanager.service.impl.WorkerServiceImpl;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class WorkerController {

    private final WorkerServiceImpl workerService;

    public WorkerController(WorkerServiceImpl workerService) {
        this.workerService = workerService;
    }

    @RequestMapping(path = "/worker/staffdetails/{id}")
    public String getAllWorkers(@PathVariable(name = "id") Long id, Model model) {
        model.addAttribute("workers", workerService.getWorkerDetails(id));
        return "/worker/staffdetails";
    }
}
