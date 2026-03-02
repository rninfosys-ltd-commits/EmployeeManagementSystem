package com.schoolapp.entity;

import com.schoolapp.dao.SellCartItemsDto;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import jakarta.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "sell_cart_items")
public class SellCartItems {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private Long productId;
	private String productName;
	private Long quantity;
	private Long price;

	@Lob
	private String productImg;

	private Long totalAmount;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "order_id")
	private SellOrder sellOrder;

	public SellCartItemsDto toDto() {
		SellCartItemsDto dto = new SellCartItemsDto();
		dto.setOrderId(sellOrder != null ? sellOrder.getId() : null);
		dto.setProductId(productId);
		dto.setProductName(productName);
		dto.setQuantity(quantity);
		dto.setPrice(price);
		dto.setProductImg(productImg);
		dto.setTotalAmount(totalAmount);
		return dto;
	}
}
