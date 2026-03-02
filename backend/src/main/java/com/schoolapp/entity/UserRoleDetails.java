package com.schoolapp.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "user_role_details")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRoleDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String routeName;
    private String gstNo;
    private String address;
    private String state;
    private String dist;
    private String tq;
    private String city;
    private String balance;
}
