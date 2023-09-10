package com.tutorial.api.demo.controllers;

import com.tutorial.api.demo.models.Product;
import com.tutorial.api.demo.models.ResponseObject;
import com.tutorial.api.demo.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "/api/v1/Products")
public class ProductController {
    @Autowired
    private ProductRepository repository;

    @GetMapping("")
    List<Product> getAllProducts(){
        return repository.findAll();
    }
    // Return an object with: data, message, status
    @GetMapping("/{id}")
    ResponseEntity<ResponseObject> findById(@PathVariable Long id){
        Optional<Product> foundProduct = repository.findById(id);
        return foundProduct.isPresent() ?
                ResponseEntity.status(HttpStatus.OK).body(
                        new ResponseObject("OK","Query product successfully",foundProduct)
                ): ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ResponseObject("false","Cannot find product by id = "+id,null)
        );
    }

    @PostMapping("/insert")
    ResponseEntity<ResponseObject> insertProduct(@RequestBody Product newProduct){
        List<Product> foundProducts = repository.findByProductName(newProduct.getProductName().trim());
        if(foundProducts.size()>0){
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
                    new ResponseObject("Fail","Product name already taken", "")
            );
        }
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("OK","Insert Product Successfully", repository.save(newProduct))
        );
    }
    //update, insert = update if found, otherwise insert
    @PutMapping("/{id}")
    ResponseEntity<ResponseObject> updateProduct(@RequestBody Product newProduct, @PathVariable Long id){
        Product updatedProduct = repository.findById(id).map(product -> {
            product.setProductName(newProduct.getProductName());
            product.setProductYear(newProduct.getProductYear());
            product.setPrice(newProduct.getPrice());
            product.setUrl(newProduct.getUrl());
            return repository.save(product);
        }).orElseGet(()->{
            newProduct.setId(id);
            return repository.save(newProduct);
        });
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("OK","Update Product Successfully", updatedProduct)
        );
    }

    @DeleteMapping("/{id}")
    ResponseEntity<ResponseObject> deleteProduct(@PathVariable Long id){
        boolean exists = repository.existsById(id);
        if(exists){
            repository.deleteById(id);
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("OK","Delete product successfully","")
            );
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ResponseObject("OK","Cannot find product to delete","")
        );
    }
}







