package com.example.store.service;

import com.example.store.entity.Product;
import com.example.store.entity.dto.ProductDto;
import com.example.store.entity.dto.ProductDtoComplete;
import com.example.store.repository.OrderRepository;
import com.example.store.repository.ProductRepository;
import com.example.store.utils.NoSuchProductException;
import com.example.store.utils.ProductDtoMapper;
import javax.transaction.Transactional;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Log4j2
public class ProductService {
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ProductDtoMapper productDtoMapper;
    @Autowired
    private OrderRepository orderRepository;

    public List<ProductDtoComplete> getProducts() {
        return productRepository.findAll().stream().map(el -> productDtoMapper.mapEntityToDto(el)).toList();
    }


    public ProductDtoComplete getProductByName(String name) {
        return productDtoMapper.mapEntityToDto(productRepository.findByNameIgnoreCase(name).orElseThrow(() -> new NoSuchProductException("Product " + name + " doesn't exist")));
    }

    public ProductDtoComplete getProductById(Long id) {
        return productDtoMapper.mapEntityToDto(productRepository.findById(id).orElseThrow(() -> new NoSuchProductException("Product " + id + " doesn't exist")));
    }

    @Transactional
    public ProductDtoComplete addProduct(ProductDto product) {
        if (!productRepository.existsByNameIgnoreCase(product.getName()))
            return productDtoMapper.mapEntityToDto(productRepository.save(productDtoMapper.mapDtoToEntity(product)));
        else
            throw new RuntimeException("Product " + product.getName() + " already exists");
    }

    @Transactional
    public ProductDtoComplete updateProduct(ProductDto product, Long id) {
        Product productEntity = productRepository.findById(id).orElseThrow(() -> new NoSuchProductException("Product " + id + " doesn't exist"));
        productEntity.setName(product.getName());
        productEntity.setStockQuantity(product.getStockQuantity());
        productEntity.setUnitPrice(product.getUnitPrice());
        return productDtoMapper.mapEntityToDto(productRepository.save(productEntity));
    }

    @Transactional
    public boolean deleteProduct(Long id) {
        boolean exists = productRepository.existsById(id);
        if (!exists)
            return false;
        productRepository.deleteById(id);
        return true;
    }

    @Transactional
    public void deleteAllProducts() {
            productRepository.deleteAll();
    }
}
