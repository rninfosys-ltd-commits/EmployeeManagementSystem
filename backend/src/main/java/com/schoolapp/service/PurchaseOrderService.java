package com.schoolapp.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.schoolapp.dao.CartItemsDto;
import com.schoolapp.dao.PlacePurchaseOrderRequest;
import com.schoolapp.dao.PurchaseOrderDto;
import com.schoolapp.entity.PurchaseCartItems;
import com.schoolapp.entity.PurchaseOrders;
import com.schoolapp.entity.UserEntity;
import com.schoolapp.enums.OrderStatus;
// import com.schoolapp.repository.PurchaseCartItemsRepository;
import com.schoolapp.repository.PurchaseOrdersRepository;
import com.schoolapp.repository.UserRepository;

import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class PurchaseOrderService {

    @Autowired
    private PurchaseOrdersRepository purchaseOrdersRepository;

    // @Autowired
    // private PurchaseCartItemsRepository purchaseCartItemsRepository;

    @Autowired
    private UserRepository userRepository;

    // ================= PLACE ORDER =================
    public PurchaseOrderDto placeOrder(PlacePurchaseOrderRequest request, String userEmail) {
        try {
            UserEntity user = userRepository.findByEmail(userEmail)
                    .orElseThrow(() -> new RuntimeException("User not found: " + userEmail));

            PurchaseOrders order = new PurchaseOrders();
            order.setUser(user);
            order.setCustomerId(request.getCustomerId());
            if (request.getCustomerId() != null) {
                userRepository.findById(request.getCustomerId()).ifPresent(cl -> {
                    order.setEmail(cl.getEmail());
                    order.setMobile(cl.getMobile());
                });
            }
            order.setOrderDescription(request.getOrderDescription());
            order.setAddress(request.getAddress());
            order.setDate(LocalDateTime.now());
            order.setOrderStatus(OrderStatus.PENDING);
            order.setTrackingId(UUID.randomUUID().toString());
            order.setPincode(request.getPincode());

            long totalAmount = 0;
            List<PurchaseCartItems> cartItems = new ArrayList<>();

            if (request.getCartItems() != null) {
                for (CartItemsDto itemDto : request.getCartItems()) {
                    PurchaseCartItems item = new PurchaseCartItems();
                    item.setProductId(itemDto.getProductId());
                    item.setProductName(itemDto.getProductName());
                    item.setPrice(itemDto.getPrice());
                    item.setQuantity(itemDto.getQuantity());
                    item.setProductImg(itemDto.getProductImg());

                    long priceValue = itemDto.getPrice() != null ? itemDto.getPrice() : 0L;
                    long quantityValue = itemDto.getQuantity() != null ? itemDto.getQuantity() : 0L;
                    long itemTotal = priceValue * quantityValue;

                    item.setTotalAmount(itemTotal);
                    totalAmount += itemTotal;

                    item.setPurchaseOrder(order);
                    cartItems.add(item);
                }
            }

            order.setAmount(totalAmount);
            order.setCartItems(cartItems);

            PurchaseOrders savedOrder = purchaseOrdersRepository.save(order);
            return savedOrder.getPurchaseOrderDto();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error placing order for user " + userEmail + ": " + e.getMessage(),
                    e);
        }
    }

    // ================= GET ALL =================
    public List<PurchaseOrderDto> getAllPurchaseOrder() {
        return purchaseOrdersRepository.findAll().stream()
                .map(PurchaseOrders::getPurchaseOrderDto)
                .collect(Collectors.toList());
    }

    // ================= GET BY ID =================
    public PurchaseOrderDto findPurchaseOrderById(Long id) {
        return purchaseOrdersRepository.findById(id)
                .map(PurchaseOrders::getPurchaseOrderDto)
                .orElseThrow(() -> new RuntimeException("Order not found"));
    }

    // ================= UPDATE STATUS =================
    public PurchaseOrderDto updateStatus(Long id, String status) {
        PurchaseOrders order = purchaseOrdersRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        order.setOrderStatus(OrderStatus.valueOf(status));
        return purchaseOrdersRepository.save(order).getPurchaseOrderDto();
    }

    // ================= DELETE =================
    public String deletePurchaseOrderById(Long id) {
        purchaseOrdersRepository.deleteById(id);
        return "Deleted successfully";
    }
}
