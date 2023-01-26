package com.example.store.controller;

import com.example.store.entity.dto.ProductDto;
import com.example.store.entity.dto.ProductDtoComplete;
import com.example.store.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(value = "api/products")
public class ProductController {
    @Autowired
    ProductService productService;

    @GetMapping
    public List<ProductDtoComplete> getProducts() {
        return productService.getProducts();
    }

    @Deprecated
    @GetMapping(value = "/byName/{name}")
    public ProductDtoComplete getProductByName(@PathVariable String name) {
        return productService.getProductByName(name);
    }

    @GetMapping(value = "/{id}")
    public ProductDtoComplete getProductById(@PathVariable Long id) {
        return productService.getProductById(id);
    }

    @PostMapping
    public ResponseEntity<?> addProduct(@RequestBody ProductDto product){
        ProductDtoComplete productDto = productService.addProduct(product);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(productDto.getId())
                .toUri();
        return ResponseEntity.created(location).build();
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<?> updateProduct(@RequestBody ProductDto product, @PathVariable Long id) {
        ProductDtoComplete productDto = productService.updateProduct(product, id);
        return new ResponseEntity<ProductDtoComplete>(productDto, HttpStatus.OK);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable Long id) {
        boolean deleted = productService.deleteProduct(id);
        if (!deleted)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<Long>(id, HttpStatus.OK);
    }

    @DeleteMapping
    public ResponseEntity<?> deleteAllProducts() {
        productService.deleteAllProducts();
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
