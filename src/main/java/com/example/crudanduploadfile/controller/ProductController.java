package com.example.crudanduploadfile.controller;

import com.example.crudanduploadfile.model.Product;
import com.example.crudanduploadfile.model.ReponseObject;
import com.example.crudanduploadfile.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping(path = "/api/v1/products")
public class ProductController {
    //DI = Dependency Injection
    @Autowired
    private ProductRepository repository;

    @GetMapping("")
    ResponseEntity<ReponseObject> getAllProducts() {
        return ResponseEntity.status(HttpStatus.OK).body(
                new ReponseObject("OKE", "Query list product successfully!", repository.findAll())
        );
    }

    // Get detail product
    @GetMapping("/{id}")
    ResponseEntity<ReponseObject> getProduct(@PathVariable(value = "id") Long id) {
        Optional<Product> foundProduct = repository.findById(id);
        if (foundProduct.isPresent()) {
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ReponseObject("ok", "Query product successfully!", foundProduct)
            );
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ReponseObject("false", "Cannot find product with id = " + id, "")
            );
        }
    }
    // Create product
    @PostMapping("")
    ResponseEntity<ReponseObject> insertProduct(@RequestBody Product newProduct) {
        List<Product> foundProducts = repository.findByProductName(newProduct.getProductName().trim());
        if (foundProducts.size() > 0) {
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
                    new ReponseObject("FALSE", "Product name already exists!", "")
            );
        }
        return ResponseEntity.status(HttpStatus.OK).body(
                new ReponseObject("OKE", "Insert product successfully!", repository.save(newProduct))
        );
    }
    // Update product
    @PutMapping("/{id}")
    ResponseEntity<ReponseObject> updateProduct(@RequestBody Product newProduct, @PathVariable Long id) {
        Optional<Product> updataProduct = repository.findById(id);
        if (updataProduct.isPresent()) {
            updataProduct.map(product -> {
                product.setProductName(newProduct.getProductName());
                product.setYear(newProduct.getYear());
                product.setPrice(newProduct.getPrice());
                product.setUrl(newProduct.getUrl());
                return repository.save(product);
            });
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ReponseObject("FAILED", "Product update failed!'\n'product does not exist!","")
            );
        }
        return ResponseEntity.status(HttpStatus.OK).body(
                new ReponseObject("OKE", "Update product successfully!", updataProduct)
        );
    }
    // Delete product
    @DeleteMapping("/{id}")
    ResponseEntity<ReponseObject> deleteProduct(@PathVariable Long id){
        boolean exists = repository.existsById(id);
        if (exists){
            repository.deleteById(id);
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ReponseObject("OKE","Delete product successfully!","")
            );
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ReponseObject("failed","Cannot find product to delete","")
        );
    }
}
