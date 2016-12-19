/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsguild.flooringmastery.dao;

import com.tsguild.flooringmastery.dto.Material;
import java.util.HashMap;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author apprentice
 */
public class ProductTypeDaoImplTest {
    
    ProductTypeDaoImpl instance;
    Material material1 = new Material("Rocks", .50, 2.50);
    HashMap<String, Material> stuff = new HashMap<>();
    
    public ProductTypeDaoImplTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        instance = new ProductTypeDaoImpl();
    }
    
    @After
    public void tearDown() {
    }

    
//    @Test
//    public void testGetTypeInfo() {
//        System.out.println("getTypeInfo");
//        stuff.put("Rocks", material1);
//        String result = instance.getTypeInfo("Rocks").getType();
//        assertEquals("expected match", "Rocks", result);
//        
//    }

    
//    @Test
//    public void testLoadProductDataFile() {
//        System.out.println("loadProductDataFile");
//        instance.loadProductDataFile();
//        Assert.assertEquals("Carpet", instance.getTypeInfo("Carpet").getType());
//        
//    }
    
}
