package com.schoolapp.dao;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import com.schoolapp.enums.OrderStatus;

//import com.Crmemp.enums.OrderStatus;  // Corrected import for OrderStatus

public class SellOrderDto {

    private Long id;
    private String orderDescription;
    private LocalDateTime date;
    private Long amount;
    private String address;
    private OrderStatus orderStatus; // Correct type for OrderStatus
    private Long userId;
    private Long customerId;

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    private UUID trackingId;
    private String userName;
    private List<SellCartItemsDto> cartItems;
    private String email;
    private String mobile;
    private String pincode;

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOrderDescription() {
        return orderDescription;
    }

    public void setOrderDescription(String orderDescription) {
        this.orderDescription = orderDescription;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(OrderStatus orderStatus) { // Corrected setter type
        this.orderStatus = orderStatus;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public UUID getTrackingId() {
        return trackingId;
    }

    public void setTrackingId(UUID trackingId) {
        this.trackingId = trackingId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public List<SellCartItemsDto> getCartItems() {
        return cartItems;
    }

    public void setCartItems(List<SellCartItemsDto> cartItems) {
        this.cartItems = cartItems;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }
}
