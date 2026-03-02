package com.schoolapp.service;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.schoolapp.entity.Products;
import com.schoolapp.entity.UserEntity;
import com.schoolapp.repository.ProductRepository;
import com.schoolapp.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
public class ProductsService {

    private final ProductRepository productRepo;
    private final UserRepository userRepo;

    public ProductsService(ProductRepository productRepo, UserRepository userRepo) {
        this.productRepo = productRepo;
        this.userRepo = userRepo;
    }

    @Transactional
    public Products addProduct(Products product, Long userId) {

        UserEntity user = userRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        product.setCreatedBy(user);

        return productRepo.save(product);
    }

    @Transactional
    public Products addProductForLoggedInUser(Products product) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String username = authentication.getName();

        UserEntity user = userRepo.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        product.setCreatedBy(user);

        return productRepo.save(product);
    }

    // ===============================
    // GET ALL PRODUCTS (ADMIN)
    // ===============================
    public List<Products> getAllProducts() {
        return productRepo.findAll();
    }

    // ===============================
    // GET PRODUCTS BY USER
    // ===============================
    // ===============================
    // GET PRODUCTS BY USER (SUPPLIER)
    // ===============================
    public List<Products> getProductsByUser(Long userId) {

        userRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // ✅ Only products created by this supplier
        return productRepo.findByCreatedBy_Id(userId);
    }

    // ===============================
    // SEARCH
    // ===============================
    public List<Products> searchByName(String name) {
        return productRepo.findAllByNameContaining(name);
    }

    // ===============================
    // GET BY ID
    // ===============================
    public Products getById(Long id) {
        return productRepo.findById(id).orElse(null);
    }

    // ===============================
    // UPDATE
    // ===============================
    @Transactional
    public Products update(Long productId, Products updated) {

        Optional<Products> optional = productRepo.findById(productId);
        if (optional.isEmpty())
            return null;

        Products product = optional.get();
        product.setName(updated.getName());
        product.setUnitPrice(updated.getUnitPrice());
        product.setDescription(updated.getDescription());
        product.setImg(updated.getImg());

        return productRepo.save(product);
    }

    // ===============================
    // DELETE
    // ===============================
    public void delete(Long id) {

        if (!productRepo.existsById(id)) {
            throw new RuntimeException("Product not found");
        }

        try {
            productRepo.deleteById(id);
        } catch (Exception e) {
            // 🔥 FK constraint comes here
            throw new RuntimeException(
                    "Cannot delete product. This product is already used in purchase orders.");
        }
    }
}
