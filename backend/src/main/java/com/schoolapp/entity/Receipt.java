package com.schoolapp.entity;

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
@Table(name = "receipts")
public class Receipt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long customerId; // optional (if you store customers as users)
    private Long partyId; // reference to users.id
    private String partyName; // stored for convenience

    private String mobile; // mobile number saved with receipt

    private Integer transactionType; // 1=Receipt,2=Payment
    private Integer paymentMode; // 0=Cash,1=UPI,2=Bank,3=Card

    private String transactionId;
    private Double amount;

    @Column(columnDefinition = "LONGTEXT")
    private String receiptImage;

    private String receiptDate;

    @Column(columnDefinition = "TEXT")
    private String description;

    private Long createdBy; // who created the receipt (user id)
}
