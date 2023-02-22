package com.example.storemanager.service.impl;

import com.example.storemanager.dao.StoreRepository;
import com.example.storemanager.dao.StoreStockRepository;
import com.example.storemanager.dto.*;
import com.example.storemanager.model.Product;
import com.example.storemanager.model.Store;
import com.example.storemanager.model.StoreStock;
import com.example.storemanager.model.Worker;
import com.example.storemanager.service.ProductService;
import com.example.storemanager.service.StoreService;
import com.example.storemanager.service.StoreStockService;
import com.example.storemanager.service.WorkerService;
import com.example.storemanager.validation.ValidationService;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class StoreStockServiceImpl implements StoreStockService {

    private final StoreStockRepository storeStockRepository;
    private final StoreRepository storeRepository;
    private final StoreService storeService;
    private final ProductService productService;
    private final ValidationService validationService;
    private final WorkerService workerService;

    public StoreStockServiceImpl(StoreStockRepository storeStockRepository,
                                 StoreRepository storeRepository,
                                 StoreService storeService,
                                 ProductService productService,
                                 ValidationService validationService,
                                 WorkerService workerService) {
        this.storeStockRepository = storeStockRepository;
        this.storeRepository = storeRepository;
        this.storeService = storeService;
        this.productService = productService;
        this.validationService = validationService;
        this.workerService = workerService;
    }

    @Override
    public List<StoreStock> findAll() {
        List<StoreStock> storeStockList = new ArrayList<>();
        storeStockRepository.findAll().forEach(storeStockList::add);
        return storeStockList;
    }

    @Override
    public StoreStock findById(Long id) {
        Optional<StoreStock> optionalStore = storeStockRepository.findById(id);
        if (optionalStore.isPresent()) {
            return optionalStore.get();
        }
        return null;
    }



    @Override
    public StoreStock saveOrUpdate(StoreStock storeStock) {
        if (storeStock.getStoreStockId() != null) {
            Optional<StoreStock> optionalStoreStock = storeStockRepository.findById(storeStock.getStoreStockId());
            if (optionalStoreStock.isPresent()) {
                StoreStock existingStoreStock = optionalStoreStock.get();
                existingStoreStock.setStore(storeStock.getStore());
                existingStoreStock.setProduct(storeStock.getProduct());
                existingStoreStock.setQuantity(storeStock.getQuantity());
                return storeStockRepository.save(existingStoreStock);
            }
        }
        return storeStockRepository.save(storeStock);
    }

    @Override
    public void delete(StoreStock storeStock) {
        storeStockRepository.delete(storeStock);
    }

    @Override
    public void deleteProductFromStoreStock(DeleteProductDto deleteProductDto) {
        Store store = storeService.findById(deleteProductDto.getStoreId());
        Product product = productService.findById(deleteProductDto.getProductId());
        StoreStock existingStoreStock = findByStoreAndProduct(store, product);
        delete(existingStoreStock);
    }

    @Override
    public void returnProductToWarehouse(DeleteProductDto deleteProductDto) {
        Store store = storeService.findById(deleteProductDto.getStoreId());
        Product product = productService.findById(deleteProductDto.getProductId());

    }


    @Override
    public StoreStock getStockForWarehouseAndProduct(Long warehouseId, Long productId) {
        Store warehouse = storeService.findById(warehouseId);
//        Product product = productService.findById(productId);
//        Optional<StoreStock> optionalStoreStock = storeStockRepository.findByStoreAndProduct(warehouse, product);
//        if (optionalStoreStock.isPresent()) {
//            return optionalStoreStock.get();
//        }
//        return null;

        for (StoreStock storeStock : warehouse.getStoreStock()) {
            if (storeStock.getProduct().getProductId() == productId) {
                return storeStock;
            }
        }
        return null;
    }

    @Override
    public List<RequestProductDto> addNewProductToStoreList(List<RequestProductDto> requestProductDtoList) {
        List<RequestProductDto> list = new ArrayList<>();
        for (RequestProductDto requestProductDto : requestProductDtoList) {
            RequestProductDto updatedDto = addNewProductToStore(requestProductDto);
            list.add(updatedDto);
        }
        return list;
    }

    @Override
    public List<RequestProductWarehouseDto> addNewProductToWarehouseList(List<RequestProductWarehouseDto> warehouseDtoList) {
        List<RequestProductWarehouseDto> list = new ArrayList<>();
        for (RequestProductWarehouseDto requestProductWarehouseDto : warehouseDtoList) {
            addNewProductToWarehouse(requestProductWarehouseDto);
            list.add(requestProductWarehouseDto);
        }
        return list;
    }

    @Override
    public void addQuantityFromWarehouse(RequestProductDto requestProductDto) {
        Store store = storeService.findById(requestProductDto.getStoreId());
        Store warehouse = storeService.findById(requestProductDto.getWarehouseId());
        Product product = productService.findById(requestProductDto.getProductId());
        validationService.validateInsertAmount(requestProductDto.getRequestAmount());

        StoreStock existingStoreStock = findByStoreAndProduct(store, product);
        StoreStock existingWarehouseStock = findByStoreAndProduct(warehouse, product);

        if (existingStoreStock == null) {
            return;
        }
        if (existingWarehouseStock == null) {
            return;
        }

        existingStoreStock.setQuantity(existingStoreStock.getQuantity() + requestProductDto.getRequestAmount());
        storeStockRepository.save(existingStoreStock);

        existingWarehouseStock.setQuantity(existingWarehouseStock.getQuantity() - requestProductDto.getRequestAmount());
        storeStockRepository.save(existingWarehouseStock);
    }
    @Override
    public void returnProductToWarehouse(RequestProductDto requestProductDto) {
        Store store = storeService.findById(requestProductDto.getStoreId());
        Store warehouse = storeService.findById(requestProductDto.getWarehouseId());
        Product product = productService.findById(requestProductDto.getProductId());
        validationService.validateInsertAmount(requestProductDto.getRequestAmount());

        StoreStock existingStoreStock = findByStoreAndProduct(store, product);
        StoreStock existingWarehouseStock = findByStoreAndProduct(warehouse, product);

        if (existingStoreStock == null) {
            return;
        }
        if (existingWarehouseStock == null) {
            existingWarehouseStock = new StoreStock();
            existingWarehouseStock.setProduct(product);
            existingWarehouseStock.setStore(warehouse);
            existingWarehouseStock.setQuantity(0);
        }

        existingStoreStock.setQuantity(existingStoreStock.getQuantity() - requestProductDto.getRequestAmount());
        storeStockRepository.save(existingStoreStock);

        existingWarehouseStock.setQuantity(existingWarehouseStock.getQuantity() + requestProductDto.getRequestAmount());
        storeStockRepository.save(existingWarehouseStock);
    }

    @Override
    public RequestProductDto addNewProductToStore(RequestProductDto requestProductDto) {
        Store store = storeService.findById(requestProductDto.getStoreId());
        Store warehouse = storeService.findById(requestProductDto.getWarehouseId());
        Product product = productService.findById(requestProductDto.getProductId());

        requestProductDto.setProductName(product.getProductName());
        requestProductDto.setProductPrice(product.getProductPrice());
        validationService.validateInsertAmount(requestProductDto.getRequestAmount());

        Optional<StoreStock> existingStoreStock = storeStockRepository.findByStoreAndProduct(store, product);
        StoreStock existingWarehouseStock = findByStoreAndProduct(warehouse, product);

        StoreStock storeStock;

        if (existingStoreStock.isPresent()) {
            storeStock = existingStoreStock.get();
            storeStock.setQuantity(storeStock.getQuantity() + requestProductDto.getRequestAmount());
        } else {
            storeStock = new StoreStock();
            storeStock.setStore(store);
            storeStock.setProduct(product);
            storeStock.setQuantity(requestProductDto.getRequestAmount());
        }
        storeStockRepository.save(storeStock);

        existingWarehouseStock.setQuantity(existingWarehouseStock.getQuantity() - requestProductDto.getRequestAmount());
        storeStockRepository.save(existingWarehouseStock);

        return requestProductDto;
    }

    @Override
    public void addNewProductToWarehouse(RequestProductWarehouseDto warehouseDto) {
        Product product = new Product();
        product.setProductName(warehouseDto.getProductName());
        product.setProductPrice(warehouseDto.getProductPrice());
        productService.saveOrUpdate(product);

        Store warehouse = storeService.findById(warehouseDto.getWarehouseId());
        StoreStock storeStock = new StoreStock();
        storeStock.setStore(warehouse);
        storeStock.setQuantity(warehouseDto.getRequestAmount());
        storeStock.setProduct(product);
        storeStockRepository.save(storeStock);

    }

    @Override
    public StoreStock findByStoreAndProduct(Store store, Product product) {
        Optional<StoreStock> optionalStoreStock = storeStockRepository.findByStoreAndProduct(store, product);
        if (optionalStoreStock.isPresent()) {
            return optionalStoreStock.get();
        }
        return null;
    }

    @Override
    public List<StoreStockDto> getDetailsById(Long storeId) {
        List<StoreStockDto> list = new ArrayList<>();
        Store store = storeService.findById(storeId);
        for (StoreStock stock : store.getStoreStock()) {
            StoreStockDto dto = new StoreStockDto();
            dto.setStoreType(store.getStoreType());
            dto.setStoreId(stock.getStore().getStoreId());
            dto.setProductId(stock.getProduct().getProductId());
            dto.setProductPrice(stock.getProduct().getProductPrice());
            dto.setProductName(stock.getProduct().getProductName());
            dto.setQuantity(stock.getQuantity());
            list.add(dto);
        }
        return list;
    }

    @Override
    public void updateProductQuantity(UpdateProductDto updateProductDto) {
        Store warehouseId = storeService.findById(updateProductDto.getWarehouseId());
        Product productId = productService.findById(updateProductDto.getProductId());
        Optional<StoreStock> existingStoreStock = storeStockRepository.findByStoreAndProduct(warehouseId, productId);
        validationService.validateInsertAmount(updateProductDto.getRequestAmount());

        StoreStock storeStock;

        if (existingStoreStock.isPresent()) {
            storeStock = existingStoreStock.get();
            storeStock.setQuantity(storeStock.getQuantity() + updateProductDto.getRequestAmount());
            storeStockRepository.save(storeStock);
        }
    }
    @Override
    public void deleteWarehouse(Long existingWarehouseId, Long warehouseId) {
        Store warehouse = storeService.findById(existingWarehouseId);
        Store transferWarehouse = storeService.findById(warehouseId);

        transferAllStoreStock(warehouse, transferWarehouse);
        transferAllWorkers(warehouse, transferWarehouse);
        validationService.validateWarehouseDelete(warehouse);
        storeService.delete(warehouse);
    }

    private void transferAllStoreStock(Store warehouse, Store transferWarehouse) {
        List<StoreStock> existingWarehouseStoreStock = warehouse.getStoreStock();
        List<StoreStock> transferStoreStockList = transferWarehouse.getStoreStock();

        Map<Product, StoreStock> productsInTransferWarehouse = new HashMap<>();

        for (int i = 0; i < transferStoreStockList.size(); i++) {
            StoreStock storeStock = transferStoreStockList.get(i);
            productsInTransferWarehouse.put(storeStock.getProduct(), storeStock);
        }

        Iterator<StoreStock> iterator = existingWarehouseStoreStock.iterator();

        while (iterator.hasNext()) {
            StoreStock existingWarehouseStock = iterator.next();
            Product existingWarehouseProduct = existingWarehouseStock.getProduct();
            Integer originalQuantity = existingWarehouseStock.getQuantity();
            if (productsInTransferWarehouse.containsKey(existingWarehouseProduct)) {
                StoreStock transferStoreStock = productsInTransferWarehouse.get(existingWarehouseProduct);
                transferStoreStock.setQuantity(originalQuantity + transferStoreStock.getQuantity());
            } else {
                StoreStock newStoreStock = new StoreStock();
                newStoreStock.setStore(transferWarehouse);
                newStoreStock.setQuantity(originalQuantity);
                newStoreStock.setProduct(existingWarehouseProduct);
                transferWarehouse.getStoreStock().add(newStoreStock);
            }
            iterator.remove();
            storeStockRepository.delete(existingWarehouseStock);
        }
        storeService.saveOrUpdate(warehouse);
        storeService.saveOrUpdate(transferWarehouse);
    }

    private void transferAllWorkers(Store warehouse, Store transferWarehouse) {
        List<Worker> existingWorkerList = warehouse.getWorkerList();
        List<Worker> transferWorkerList = transferWarehouse.getWorkerList();

        Iterator<Worker> iterator = existingWorkerList.iterator();
        while(iterator.hasNext()) {
            Worker existingworker = iterator.next();
            existingworker.setStore(transferWarehouse);
            transferWorkerList.add(existingworker);
            iterator.remove();
        }
        storeService.saveOrUpdate(warehouse);
        storeService.saveOrUpdate(transferWarehouse);
    }

}
