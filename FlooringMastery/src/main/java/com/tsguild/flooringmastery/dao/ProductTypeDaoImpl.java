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
import java.io.FileWriter;
import java.util.HashMap;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author apprentice
 */
public class ProductTypeDaoImpl implements ProductTypeDao {
    private final String FILE_NAME;
    private final String DELIMITER;
    private HashMap<String, Material> materialData;
    
    public ProductTypeDaoImpl(){
        
        FILE_NAME = "materialdata.txt";
        DELIMITER = ",";
        materialData =  new HashMap<>();
    }
    
    @Override
    public Material getTypeInfo(String materialType){
        return materialData.get(materialType);
    }
    
    @Override
    public void loadProductDataFile() throws FileNotFoundException{
        
        Scanner reader = new Scanner(new BufferedReader(new FileReader(FILE_NAME)));

        while (reader.hasNextLine()){
            String line = reader.nextLine();

            String[] properties = line.split(DELIMITER);

            if(properties.length != 3)
                continue;

            Material aType = new Material();
            aType.setType(properties[0]);


            try{
                double costSqFt = Double.parseDouble(properties[1]);
                double laborSqFt = Double.parseDouble(properties[2]);

                aType.setMaterialCostSqFt(costSqFt);
                aType.setLaborCost(laborSqFt);

            } catch(NumberFormatException ex){
                Logger.getLogger(ProductTypeDaoImpl.class.getName()).log(Level.SEVERE, null, ex);
                continue;
            }

            materialData.put(aType.getType(), aType);

        }
        reader.close();
    }
}
