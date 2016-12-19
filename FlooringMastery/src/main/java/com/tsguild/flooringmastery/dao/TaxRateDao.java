/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsguild.flooringmastery.dao;

import java.io.FileNotFoundException;

/**
 *
 * @author apprentice
 */
public interface TaxRateDao {

    double getTax(String state);
    
    String valState(String state);

    void loadTaxRateFile() throws FileNotFoundException;
    
}
