/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsguild.flooringmastery.dao;

import com.tsguild.flooringmastery.dto.Order;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;

/**
 *
 * @author apprentice
 */
public interface OrderDao {

    void addOrder(Order order, String dateKey);

    HashMap getDateOrders(String dateKey);

    Order getOrder(int orderN, String date);

    int getOrderNum(String dateKey);
    
    String convertDateKey(String date);

    String loadConfigFile();

    void loadOrderDatabase() throws IOException;

    void removeOrder(int orderNumber, String date);

    void saveFiles() throws IOException;
    
}
