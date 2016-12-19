/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsguild.flooringmastery.dao;

import com.tsguild.flooringmastery.dto.Order;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;

/**
 *
 * @author apprentice
 */
public class OrderDaoImpl implements OrderDao {

    private final String DELIMITER = ",";
    private HashMap<String, HashMap<Integer, Order>> database;
    private String folderName = "Order_Database/";
    
    public OrderDaoImpl(){
        database = new HashMap<>();
    }
    
    @Override
    public void addOrder(Order order, String dateKey) {
        if(database.containsKey(dateKey)) {
            database.get(dateKey).put(order.getOrderNum(), order);
        } else {
            database.put(dateKey, new HashMap<>());
            database.get(dateKey).put(order.getOrderNum(), order);
        }
    }

    @Override
    public int getOrderNum(String dateKey) {
        if (database.get(dateKey) == null){
            return 1;
        } else {
            int size = database.get(dateKey).size() + 1;
            return size;
        }
    }
    
    @Override
    public String convertDateKey(String date){
        String dateKey = "Orders_"+ date +".txt";
        return dateKey;
    }
    
    @Override
    public HashMap getDateOrders(String dateKey){
        return database.get(dateKey);
    }
    
    @Override
    public Order getOrder(int orderN, String date){
        return database.get(date).get(orderN);
    }
    
    @Override
    public void removeOrder(int orderNumber, String date){
        database.get(date).remove(orderNumber);
    }

    @Override
    public void loadOrderDatabase() throws IOException { 

        Stream<java.nio.file.Path> paths = Files.walk(Paths.get(folderName)); 
        paths.forEach(filePath -> {
            if (Files.isRegularFile(filePath)) {
                try {
                    String fileStuff;
                    String file = filePath.toString();

                    Scanner reader = new Scanner(new BufferedReader(new FileReader(file)));
                    while (reader.hasNextLine()) {
                        fileStuff = reader.nextLine();
                        String[] properties = fileStuff.split(DELIMITER);

                        if (properties.length != 12) continue;

                        String name = properties[1];
                        String state = properties[2];
                        String type = properties[4];

                        try {
                            int ordNum = Integer.parseInt(properties[0]);
                            double taxRate = Double.parseDouble(properties[3]);
                            double area = Double.parseDouble(properties[5]);
                            double costSqFt = Double.parseDouble(properties[6]);
                            double laborSqFt = Double.parseDouble(properties[7]);

                            Order order = new Order(ordNum, name, state, taxRate, type, area, costSqFt, laborSqFt);
                            order.setMaterialCost(Double.parseDouble(properties[8]));
                            order.setTotalLaborCost(Double.parseDouble(properties[9]));
                            order.setTax(Double.parseDouble(properties[10]));
                            order.setTotal(Double.parseDouble(properties[11]));
                            String dateKey = filePath.getFileName().toString();
                            this.addOrder(order, dateKey);
                        } catch (NumberFormatException e){
                            Logger.getLogger(OrderDaoImpl.class.getName()).log(Level.SEVERE, null, e);
                        }
                    }
                    reader.close();  
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(OrderDaoImpl.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }
    
    @Override
    public String loadConfigFile(){
        String mode = "";
        try {
            Scanner reader = new Scanner(new BufferedReader(new FileReader("configmode.txt")));
            
            while (reader.hasNextLine()){
                mode = reader.nextLine();
            }
            reader.close();
            
        } catch (FileNotFoundException ex) {
            Logger.getLogger(TaxRateDaoImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (mode.equalsIgnoreCase("Test")){
            return "test";
        } else if (mode.equalsIgnoreCase("Prod")){
            return "prod";
        } else {
            return "none";
        }
    }

    @Override
    public void saveFiles() throws IOException {
        
        Set<Map.Entry<String, HashMap<Integer, Order>>> both = database.entrySet();
        for (Map.Entry<String, HashMap<Integer, Order>> entry : both) {
            PrintWriter writer = new PrintWriter(new FileWriter(folderName+entry.getKey()));
            for (Order o : entry.getValue().values()){
                writer.println(o.getOrderNum() + DELIMITER + o.getCustomerName() + DELIMITER + o.getState() + DELIMITER + o.getTaxRate() + DELIMITER +
                        o.getMaterialType() + DELIMITER + o.getArea() + DELIMITER + o.getCostSqFt() + DELIMITER + o.getLaborSqFt() + DELIMITER + 
                        o.getMaterialCost() + DELIMITER + o.getTotalLaborCost() + DELIMITER + o.getTax() + DELIMITER + o.getTotal());
            }
            writer.flush();
            writer.close();
        }
    }
}
