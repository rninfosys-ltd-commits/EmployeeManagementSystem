package com.schoolapp.entity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import com.schoolapp.dao.SellOrderDto;
import com.schoolapp.enums.OrderStatus;

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
@Table(name = "sell_orders")
public class SellOrder {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String orderDescription;
	private LocalDateTime date;
	private Long amount;
	private String address;

	@Enumerated(EnumType.STRING)
	private OrderStatus orderStatus;

	private UUID trackingId;
	private String email;
	private String mobile;
	private String pincode;

	@Column(name = "customer_id")
	private Long customerId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private UserEntity user;

	@OneToMany(mappedBy = "sellOrder", cascade = CascadeType.ALL)
	private List<SellCartItems> cartItems;

	public SellOrderDto getSellOrderDto() {
		SellOrderDto dto = new SellOrderDto();
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
			dto.setCartItems(
					cartItems.stream()
							.map(SellCartItems::toDto)
							.collect(Collectors.toList()));
		}
		return dto;
	}
}
