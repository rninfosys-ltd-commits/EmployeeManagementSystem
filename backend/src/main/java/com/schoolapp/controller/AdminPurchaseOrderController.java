package com.schoolapp.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.schoolapp.dao.PlacePurchaseOrderRequest;
import com.schoolapp.dao.PurchaseOrderDto;
import com.schoolapp.service.PurchaseOrderService;

@RestController
@RequestMapping("/api/admin")
public class AdminPurchaseOrderController {

    private final PurchaseOrderService purchaseOrderService;

    public AdminPurchaseOrderController(PurchaseOrderService purchaseOrderService) {
        this.purchaseOrderService = purchaseOrderService;
    }

    @PostMapping("/purchase-order/place")
    public ResponseEntity<?> placeOrder(@RequestBody PlacePurchaseOrderRequest request,
            java.security.Principal principal) {
        try {
            PurchaseOrderDto result = purchaseOrderService.placeOrder(request, principal.getName());
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(Map.of(
                    "error", e.getMessage() != null ? e.getMessage() : "Unknown error",
                    "type", e.getClass().getSimpleName()));
        }
    }

    // ================= CART =================

    @PostMapping("/purchase-cart")
    public ResponseEntity<Map<String, String>> saveCartItem(@RequestBody Object cartItem) {
        return ResponseEntity.ok(Map.of("message", "Cart item saved successfully"));
    }

    @DeleteMapping("/purchase-cart/{id}")
    public ResponseEntity<Map<String, String>> deleteCartItem(@PathVariable Long id) {
        return ResponseEntity.ok(Map.of("message", "Cart item deleted successfully", "id", id.toString()));
    }

    // ================= GET ALL =================
    @GetMapping("/purchase-orders")
    public List<PurchaseOrderDto> getAllOrders() {
        return purchaseOrderService.getAllPurchaseOrder();
    }

    // ================= GET BY ID =================
    @GetMapping("/purchase-order/details/{id}")
    public PurchaseOrderDto getOrderDetails(@PathVariable Long id) {
        return purchaseOrderService.findPurchaseOrderById(id);
    }

    // ================= UPDATE STATUS =================
    @PutMapping("/purchase-order/status/{id}")
    public PurchaseOrderDto updateOrderStatus(@PathVariable Long id, @RequestParam String status) {
        return purchaseOrderService.updateStatus(id, status);
    }

    // ================= DELETE =================
    @DeleteMapping("/purchase-order/{id}")
    public ResponseEntity<Map<String, String>> deleteOrder(@PathVariable Long id) {
        purchaseOrderService.deletePurchaseOrderById(id);
        return ResponseEntity.ok(Map.of("message", "Deleted successfully"));
    }
}
