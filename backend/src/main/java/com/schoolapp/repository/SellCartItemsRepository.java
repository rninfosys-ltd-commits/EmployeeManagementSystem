package com.schoolapp.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

import com.schoolapp.entity.SellCartItems;
import com.schoolapp.entity.SellOrder;
//
//import com.Crmemp.entity.SellCartItems;
//import com.Crmemp.entity.SellOrder;

public interface SellCartItemsRepository extends JpaRepository<SellCartItems, Long> {

    List<SellCartItems> findBySellOrder(SellOrder order);
}
