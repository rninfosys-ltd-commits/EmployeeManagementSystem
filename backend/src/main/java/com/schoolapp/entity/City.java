package com.schoolapp.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "cities")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class City {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @ManyToOne
    @JoinColumn(name = "taluka_id", nullable = false)
    private Taluka taluka;

    // ✅ NEW
    @ManyToOne
    @JoinColumn(name = "district_id", nullable = false)
    private District district;

    // ✅ NEW
    @ManyToOne
    @JoinColumn(name = "state_id", nullable = false)
    private State state;
}
