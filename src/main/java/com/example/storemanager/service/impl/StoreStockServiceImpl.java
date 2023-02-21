package com.example.storemanager.service.impl;

import com.example.storemanager.dao.StoreRepository;
import com.example.storemanager.dao.StoreStockRepository;
import com.example.storemanager.dto.*;
import com.example.storemanager.model.Product;
import com.example.storemanager.model.Store;
import com.example.storemanager.model.StoreStock;
import com.example.storemanager.service.ProductService;
import com.example.storemanager.service.StoreStockService;
import com.example.storemanager.validation.ValidationService;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class StoreStockServiceImpl implements StoreStockService {

    private final StoreStockRepository storeStockRepository;
    private final StoreRepository storeRepository;
    private final StoreServiceImpl storeService;
    private final ProductService productService;
    private final ValidationService validationService;

    public StoreStockServiceImpl(StoreStockRepository storeStockRepository,
                                 StoreRepository storeRepository,
                                 StoreServiceImpl storeService,
                                 ProductService productService,
                                 ValidationService validationService) {
        this.storeStockRepository = storeStockRepository;
        this.storeRepository = storeRepository;
        this.storeService = storeService;
        this.productService = productService;
        this.validationService = validationService;
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
    public void returnProductAndDeleteWarehouse(Long existingWarehouseId, Long warehouseId) {
        Store warehouse = storeService.findById(existingWarehouseId);
        List<StoreStock> existingStoreStockList = warehouse.getStoreStock();
        Store transferWarehouse = storeService.findById(warehouseId);
        List<StoreStock> transferStoreStockList = transferWarehouse.getStoreStock();

        Map<Product, Integer> productsInTransferWarehouse = new HashMap<>();

        for (int i = 0; i < transferStoreStockList.size(); i++) {
            productsInTransferWarehouse.put(transferStoreStockList.get(i).getProduct(), i);
        }

        for (StoreStock existingWarehouseStock : existingStoreStockList) {
            if (productsInTransferWarehouse.containsKey(existingWarehouseStock.getProduct())) {
                StoreStock transferStoreStock = transferStoreStockList.get(productsInTransferWarehouse.get(existingWarehouseStock.getProduct()));
                Integer originalQuantity = transferStoreStock.getQuantity();
                transferStoreStock.setQuantity(originalQuantity + transferStoreStock.getQuantity());
                storeStockRepository.save(transferStoreStock);
                existingWarehouseStock.setQuantity(0);
                storeStockRepository.save(existingWarehouseStock);
            } else {
                StoreStock newStoreStock = new StoreStock();
                newStoreStock.setStore(transferWarehouse);
                newStoreStock.setQuantity(existingWarehouseStock.getQuantity());
                newStoreStock.setProduct(existingWarehouseStock.getProduct());
                storeStockRepository.save(newStoreStock);
                existingWarehouseStock.setQuantity(0);
                storeStockRepository.save(existingWarehouseStock);
            }
        }
        deleteWarehouse(existingWarehouseId);
    }

    public void deleteWarehouse(Long existingWarehouseId) {
        Store warehouse = storeService.findById(existingWarehouseId);
        Iterator<StoreStock> iterator = warehouse.getStoreStock().iterator();
        while (iterator.hasNext()) {
            StoreStock storeStock = iterator.next();
            if (storeStock.getQuantity() > 0) {
                //TODO throw exception
            }
            warehouse.getStoreStock().remove(storeStock);
        }
    }

}
