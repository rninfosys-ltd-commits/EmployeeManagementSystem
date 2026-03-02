package com.schoolapp.dao;

import java.time.LocalDateTime;
import java.util.List;
//import com.codewithswati.ecom.enums.OrderStatus;

import com.schoolapp.enums.OrderStatus;

//import com.employeemanagement.enums.OrderStatus;

public class PurchaseOrderDto {

	private Long id;
	private String orderDescription;
	private LocalDateTime date;
	private Long amount;
	private String address;
	private String payment;
	private OrderStatus orderStatus;
	private Long userId; // ✅ ADD THIS
	private Long customerId;

	public Long getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

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

	public String getPayment() {
		return payment;
	}

	public void setPayment(String payment) {
		this.payment = payment;
	}

	public OrderStatus getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(OrderStatus orderStatus) {
		this.orderStatus = orderStatus;
	}

	public String getTrackingId() {
		return trackingId;
	}

	public void setTrackingId(String trackingId) {
		this.trackingId = trackingId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public List<PurchaseCartItemsDto> getCartItems() {
		return cartItems;
	}

	public void setCartItems(List<PurchaseCartItemsDto> cartItems) {
		this.cartItems = cartItems;
	}

	public String getCouponName() {
		return couponName;
	}

	public void setCouponName(String couponName) {
		this.couponName = couponName;
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

	private String trackingId;
	private String userName;
	private List<PurchaseCartItemsDto> cartItems;
	private String couponName;
	private String email;
	private String mobile;
	private String pincode;

	// --- Getters and Setters ---
}
