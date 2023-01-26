package com.example.storemanager.dao;

import com.example.storemanager.model.Store;
import com.example.storemanager.model.Worker;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WorkerRepository extends CrudRepository<Worker, Long> {

}
