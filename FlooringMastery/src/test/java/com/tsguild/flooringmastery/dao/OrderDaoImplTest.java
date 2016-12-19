/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsguild.flooringmastery.dao;

import com.tsguild.flooringmastery.dto.Order;
import java.util.HashMap;
import junit.framework.Assert;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author apprentice
 */
public class OrderDaoImplTest {
    
    OrderDaoImpl instance;
    Order order1 = new Order(1, "Jesus", "CA", 6.3, "Carpet", 423, 3.2, 4.5);
    Order order2 = new Order(2, "Mom", "KY", 7.3, "Carpet", 623, 4.2, 5.5);
    Order order3 = new Order(3, "Buddy", "MI", 5.5, "Tile", 241, 7.4, 5.2);
    String dateKey = "aKey";
    
    public OrderDaoImplTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        instance = new OrderDaoImpl();
    }
    
    @After
    public void tearDown() {
    }

    @Test
    public void testEmptyDao(){
        Assert.assertNull("Asking for orders in a nonexistant date should return null", instance.getDateOrders(dateKey));
    }
    
    @Test
    public void testAddAndRetrieveOrder() {
        System.out.println("addOrder");
        instance.addOrder(order1, dateKey);
        Order matchOrder = instance.getOrder(1, dateKey);
        Assert.assertEquals(matchOrder, order1);
    }

    
    @Test
    public void testGetOrderNum() {
        System.out.println("getOrderNum");
        instance.addOrder(order1, dateKey);
        instance.addOrder(order2, dateKey);
        int expResult = 3;
        int result = instance.getOrderNum(dateKey);
        assertEquals("Order num is 1 more than size of map", expResult, result);
    }
   

    @Test 
    public void testGetDateOrdersWithSize() {
        System.out.println("getDateOrders size");
        instance.addOrder(order1, dateKey);
        instance.addOrder(order2, dateKey);
        int result = instance.getDateOrders(dateKey).size();
        assertEquals("Expected size is 2", 2, result);
    }


    @Test 
    public void testRemoveOrder() {
        System.out.println("removeOrder");
        instance.addOrder(order1, dateKey);
        instance.addOrder(order2, dateKey);
        instance.addOrder(order3, dateKey);
        instance.removeOrder(1, dateKey);
        Assert.assertEquals("Size should be 2 after removal", 2, instance.getDateOrders(dateKey).size());
    }
    
    @Test
    public void testOrderNumbersOfRemovedOrders(){
        System.out.println("removeOrder");
        instance.addOrder(order1, dateKey);
        instance.addOrder(order2, dateKey);
        instance.addOrder(order3, dateKey);
        instance.removeOrder(1, dateKey);
        Assert.assertNull("Order num 1 no longer exists", instance.getDateOrders(dateKey).get(1));
    }
    
    @Test
    public void testAfterRemovalOfNonexistent() {
        System.out.println("removeOrder");
        instance.addOrder(order1, dateKey);
        instance.addOrder(order2, dateKey);
        instance.addOrder(order3, dateKey);
        instance.removeOrder(4, dateKey);
        Assert.assertEquals(3, instance.getDateOrders(dateKey).size());
    }


    @Test 
    public void testLoadOrderDatabase() throws Exception {
        System.out.println("loadOrderDatabase");
        instance.loadOrderDatabase();
        Assert.assertEquals(120.88, instance.getOrder(1, "Orders_10012016.txt").getArea());
       
    }

    @Test
    public void testLoadConfigFile() {
        System.out.println("loadConfigFile");
        String expResult = "prod";
        String result = instance.loadConfigFile();
        assertEquals(expResult, result);
    }


    
}
