package com.example.storemanager.dao;

import com.example.storemanager.model.Product;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends CrudRepository<Product, Long> {

    Optional<List<Product>> findByProductIdNotIn(List<Long> products);
}
