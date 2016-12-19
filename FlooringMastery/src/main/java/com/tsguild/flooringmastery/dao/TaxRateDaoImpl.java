/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsguild.flooringmastery.dao;

import com.tsguild.flooringmastery.dto.Material;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author apprentice
 */
public class TaxRateDaoImpl implements TaxRateDao {
    
    private final String FILE_NAME;
    private final String DELIMITER;
    private HashMap<String, Double> stateRates;
    
    public TaxRateDaoImpl(){
        
        FILE_NAME = "statetaxrates.txt";
        DELIMITER = ",";
        stateRates=  new HashMap<>();
    }
       
    @Override
    public double getTax(String state){
        return stateRates.get(state);
    }
    
    @Override
    public String valState(String state){
        int count = 0;
        for (String s : stateRates.keySet()){
            if (state.equals(s))
                count++;
        }
        if (count == 0){
            return null;
        } else {
            return state;
        }
    }
    
    @Override
    public void loadTaxRateFile() throws FileNotFoundException {
        
        Scanner reader = new Scanner(new BufferedReader(new FileReader(FILE_NAME)));

        while (reader.hasNextLine()){
            String line = reader.nextLine();
            String[] properties = line.split(DELIMITER);

            if(properties.length != 2)
                continue;

            try{
                double taxRate = Double.parseDouble(properties[1]);
                stateRates.put(properties[0], taxRate);

            } catch(NumberFormatException ex){
                Logger.getLogger(TaxRateDaoImpl.class.getName()).log(Level.SEVERE, null, ex);
                continue;
            }
        }
        reader.close();
    }
}
