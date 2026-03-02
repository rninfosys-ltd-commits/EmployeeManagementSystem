package com.schoolapp.dao;


public class CartItemsDto {
    private Long id;
    private Long productId;
    private String productName;
    private Long price;
    private Long quantity;
    private Long userId;
    
    private String productImg;  // <-- FIXED field name
    private String productDescription;

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getProductId() { return productId; }
    public void setProductId(Long productId) { this.productId = productId; }

    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }

    public Long getPrice() { return price; }
    public void setPrice(Long price) { this.price = price; }

    public Long getQuantity() { return quantity; }
    public void setQuantity(Long quantity) { this.quantity = quantity; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getProductImg() { return productImg; }   // <-- FIXED
    public void setProductImg(String productImg) { this.productImg = productImg; }  // <-- FIXED

    public String getProductDescription() { return productDescription; }
    public void setProductDescription(String productDescription) { this.productDescription = productDescription; }
}

