package com.schoolapp.entity;

import com.schoolapp.dao.PurchaseCartItemsDto;

//import com.Crmemp.dto.request.PurchaseCartItemsDto;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "purchase_cart_items")
public class PurchaseCartItems {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private Long productId;
	private String productName;
	private Long quantity;
	private Long price;

	@Lob
	@Column(columnDefinition = "LONGTEXT")
	private String productImg;

	private Long totalAmount;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "order_id")
	private PurchaseOrders purchaseOrder;

	/* ===== DTO CONVERTER ===== */
	public PurchaseCartItemsDto toDto() {
		PurchaseCartItemsDto dto = new PurchaseCartItemsDto();
		dto.setOrderId(purchaseOrder != null ? purchaseOrder.getId() : null);
		dto.setProductId(productId);
		dto.setProductName(productName);
		dto.setQuantity(quantity);
		dto.setPrice(price);
		dto.setProductImg(productImg);
		dto.setTotalAmount(totalAmount);
		return dto;
	}
}
