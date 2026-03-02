package com.schoolapp.dao;

import java.util.List;

public class PlacePurchaseOrderRequest {

    private Long userId;
    private String address;
    private String orderDescription;
    private String email;
    private String mobile;
    private String pincode;
    private List<CartItemsDto> cartItems;
    private Long customerId;

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    // getters & setters

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getOrderDescription() {
        return orderDescription;
    }

    public void setOrderDescription(String orderDescription) {
        this.orderDescription = orderDescription;
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

    public List<CartItemsDto> getCartItems() {
        return cartItems;
    }

    public void setCartItems(List<CartItemsDto> cartItems) {
        this.cartItems = cartItems;
    }
}
