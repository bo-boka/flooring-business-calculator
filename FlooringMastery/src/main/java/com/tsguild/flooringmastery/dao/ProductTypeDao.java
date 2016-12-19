/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsguild.flooringmastery.dao;

import com.tsguild.flooringmastery.dto.Material;
import java.io.FileNotFoundException;

/**
 *
 * @author apprentice
 */
public interface ProductTypeDao {

    Material getTypeInfo(String materialType);

    void loadProductDataFile() throws FileNotFoundException;
    
}
