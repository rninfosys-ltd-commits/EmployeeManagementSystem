package com.schoolapp.service;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import org.json.JSONException;

import com.schoolapp.dao.SellOrderDto;

public interface SellOrderService {

    List<SellOrderDto> getAllSellOrders();

    SellOrderDto changeSellOrderStatus(Long orderId, String status);

    String getCombinedSellOrderData(Long orderId)
            throws ClassNotFoundException, SQLException, JSONException, IOException;
}
