package com.schoolapp.repository;

//import com.employeemanagement.entity.PartyPrice;
import org.springframework.data.jpa.repository.JpaRepository;

import com.schoolapp.entity.PartyPrice;

//import com.Crmemp.entity.PartyPrice;

import java.util.List;
import java.util.Optional;

public interface PartyPriceRepository extends JpaRepository<PartyPrice, Long> {

    Optional<PartyPrice> findByParty_IdAndProduct_Id(Long partyId, Long productId);

    List<PartyPrice> findByParty_Id(Long partyId);
}
