package com.schoolapp.controller;
//

//package com.Crmemp.controller;
//
//import com.Crmemp.entity.Product;
//import com.Crmemp.services.ProductService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.schoolapp.entity.Products;
import com.schoolapp.service.ProductsService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/products")

public class ProductsController {

    private final ProductsService service;

    public ProductsController(ProductsService service) {
        this.service = service;
    }

    // ===============================
    // ADD PRODUCT (COMPANY_OWNER / USER)
    // ===============================

    @PostMapping("/addProduct")
    public ResponseEntity<Products> addProductForLoggedInUser(
            @RequestBody Products product) {

        Products saved = service.addProductForLoggedInUser(product);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PostMapping("/{userId}")
    public ResponseEntity<Products> addProduct(
            @PathVariable Long userId,
            @RequestBody Products product) {

        Products saved = service.addProduct(product, userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    // ===============================
    // GET ALL PRODUCTS
    // ===============================
    @GetMapping
    public ResponseEntity<List<Products>> getAll() {
        return ResponseEntity.ok(service.getAllProducts());
    }

    // ===============================
    // GET PRODUCTS BY USER
    // ===============================
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Products>> getByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(service.getProductsByUser(userId));
    }

    // ===============================
    // SEARCH PRODUCTS BY NAME
    // ===============================
    @GetMapping("/search/{name}")
    public ResponseEntity<List<Products>> search(@PathVariable String name) {
        return ResponseEntity.ok(service.searchByName(name));
    }

    // ===============================
    // GET PRODUCT BY ID
    // ===============================
    @GetMapping("/{id}")
    public ResponseEntity<Products> getById(@PathVariable Long id) {
        Products product = service.getById(id);
        if (product != null) {
            return ResponseEntity.ok(product);
        }
        return ResponseEntity.notFound().build();
    }

    // ===============================
    // UPDATE PRODUCT
    // ===============================
    @PutMapping("/{id}")
    public ResponseEntity<Products> update(
            @PathVariable Long id,
            @RequestBody Products product) {

        Products updated = service.update(id, product);
        if (updated != null) {
            return ResponseEntity.ok(updated);
        }
        return ResponseEntity.notFound().build();
    }

    // ===============================
    // DELETE PRODUCT
    // ===============================
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        try {
            service.delete(id);
            return ResponseEntity.ok(
                    Map.of("message", "Product with id " + id + " deleted successfully"));
        } catch (RuntimeException e) {
            return ResponseEntity
                    .badRequest()
                    .body(Map.of("error", e.getMessage()));
        }
    }
}
