package com.schoolapp.entity;

//package com.Crmemp.entity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import com.schoolapp.dao.PurchaseOrderDto;
import com.schoolapp.enums.OrderStatus;

//import com.Crmemp.dto.request.PurchaseOrderDto;
//import com.employeemanagement.enums.OrderStatus;

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
@Table(name = "purchase_orders")
public class PurchaseOrders {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String orderDescription;
	private LocalDateTime date;
	private Long amount;
	private String address;

	@Enumerated(EnumType.STRING)
	@Column(name = "order_status", columnDefinition = "VARCHAR(20)")
	private OrderStatus orderStatus;

	private String trackingId;
	private String email;
	private String mobile;
	private String pincode;

	@Column(name = "customer_id")
	private Long customerId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private UserEntity user;

	@OneToMany(mappedBy = "purchaseOrder", cascade = CascadeType.ALL)
	private List<PurchaseCartItems> cartItems;

	/* ===== DTO CONVERTER ===== */
	public PurchaseOrderDto getPurchaseOrderDto() {
		PurchaseOrderDto dto = new PurchaseOrderDto();
		dto.setId(id);
		dto.setOrderDescription(orderDescription);
		dto.setDate(date);
		dto.setAmount(amount);
		dto.setAddress(address);
		dto.setOrderStatus(orderStatus);
		dto.setTrackingId(trackingId);
		dto.setUserId(user != null ? user.getId() : null);
		dto.setUserName(user != null ? user.getUsername() : null);
		dto.setEmail(email);
		dto.setMobile(mobile);
		dto.setPincode(pincode);
		dto.setCustomerId(customerId);

		if (cartItems != null) {
			dto.setCartItems(cartItems.stream().map(PurchaseCartItems::toDto).collect(Collectors.toList()));
		}
		return dto;
	}
}
