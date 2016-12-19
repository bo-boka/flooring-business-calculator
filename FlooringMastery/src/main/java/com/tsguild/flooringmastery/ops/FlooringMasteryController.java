/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsguild.flooringmastery.ops;

import com.tsguild.flooringmastery.dao.OrderDao;
import com.tsguild.flooringmastery.dao.ProductTypeDao;
import com.tsguild.flooringmastery.dao.TaxRateDao;
import com.tsguild.flooringmastery.dto.Material;
import com.tsguild.flooringmastery.dto.Order;
import com.tsguild.flooringmastery.ui.ConsoleIO;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author apprentice
 */
public class FlooringMasteryController {
    
    private ConsoleIO console;
    private OrderDao orderDao;
    private TaxRateDao taxRateDao;
    private ProductTypeDao typeDao;
    HashMap<Integer, Order> dailyOrders;
    
    public FlooringMasteryController(ConsoleIO console, OrderDao orderDao, TaxRateDao taxRateDao, ProductTypeDao typeDao) {
        this.console = console;
        this.orderDao = orderDao;
        this.taxRateDao = taxRateDao;
        this.typeDao = typeDao;
    }
    
    public void run(){

        boolean keepGoing = true; 
        
        console.print("\n\n***WELCOME TO FLOORING MASTERY APP***");
           
        try { 
            orderDao.loadOrderDatabase();
        } catch (IOException ex) {
            Logger.getLogger(FlooringMasteryController.class.getName()).log(Level.SEVERE, null, ex);
            console.print("Could not find file folder.");
            console.print("Please contact your system administrator.");
            keepGoing = false;
        }
        try {
            taxRateDao.loadTaxRateFile();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(FlooringMasteryController.class.getName()).log(Level.SEVERE, null, ex);
            console.print("State Tax Rate File Not Found.");
            console.print("Alert: You will have to edit tax rate and tax total via the edit option.");
        }
        try {
            typeDao.loadProductDataFile();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(FlooringMasteryController.class.getName()).log(Level.SEVERE, null, ex);
            console.print("Product Material File Not Found.");
            console.print("Alert: You will have to edit material costs and labor costs via the edit option.");
        }
        String mode = orderDao.loadConfigFile();
        
        console.print("*Your config mode is: " + mode);
        
        while (keepGoing){
            menu();
            int userChoice = console.readInt("Enter a number: ");
            switch (userChoice){
                case 1:
                    displayOrders();
                    break;
                case 2:
                    addOrder();
                    break;
                case 3:
                    editOrder();
                    break;
                case 4:
                    deleteOrder();
                    break;
                case 5:
                    if (mode.equalsIgnoreCase("prod")){
                        try {
                            orderDao.saveFiles();
                        } catch (IOException ex) {
                            Logger.getLogger(FlooringMasteryController.class.getName()).log(Level.SEVERE, null, ex);
                            console.print("Unable to save file :(");
                        }
                    } else if (mode.equalsIgnoreCase("test")){
                        console.print("You are in test mode.");
                        console.print("Unable to save file.");
                    } else if (mode.equalsIgnoreCase("none")){
                        System.out.println("Configuration environment not defined.");
                        System.out.println("Unable to save file.");
                    } 
                    break;
                case 6:
                    keepGoing = false;
                    break;
                default:
                    break;
            }
        }
    }
    
    private void menu(){
        
        console.print("=====================================================");
        console.print("\n\t**Order Menu**");
        console.print("1. Display Orders");
        console.print("2. Add Order");
        console.print("3. Edit Order");
        console.print("4. Remove Order");
        console.print("5. Save");
        console.print("6. Exit");
        console.print("\n=====================================================");
    }
    
    private void displayOrders(){
        
        do {
            String date = valiDate("Enter date (mmddyyyy): ");
            String dateKey = orderDao.convertDateKey(date);
            dailyOrders = orderDao.getDateOrders(dateKey);
        } while (dailyOrders == null);
        console.print("*+*+*+*+*+*+*+*+*+*+*+*+*+*+*+*");
        console.print("");
        for (Order o : dailyOrders.values()){
            console.print(o.getOrderNum() + " :: " + o.getCustomerName() + " :: " + o.getState() + " :: " + o.getMaterialType() + " :: $"+ o.getTotal());
        }
        console.print("\n*+*+*+*+*+*+*+*+*+*+*+*+*+*+*+*");
    }
    
    private void addOrder(){
        String state = "";
        String materialType = "";
        Material material = new Material();
        String confirm = "";
        
        String date = valiDate("Enter date (mmddyyyy): ");
        String dateKey = orderDao.convertDateKey(date);
        int orderNum = orderDao.getOrderNum(dateKey);
        String custName = console.readString("Name: ");
        do{
            String stateInput = console.readString("State: ");
            state = taxRateDao.valState(stateInput);
        }while (state == null);
        double taxRate = taxRateDao.getTax(state);
        do{
            materialType = console.readString("Material Type:");
            material = typeDao.getTypeInfo(materialType);
        } while (material == null);
        double area = console.readDouble("Area: ", 5, 2000000);
        double costSqFt = material.getMaterialCostSqFt();
        double laborSqFt = material.getLaborCost();
        Order newOrder = new Order(orderNum, custName, state, taxRate, materialType, area, costSqFt, laborSqFt);
        
        console.print("=====================================================");
        console.print("\n\t\tORDER SUMMARY ");
        console.print("\tName: " + custName);
        console.print("\tState/Tax Rate: " + state + "/" + taxRate + "%");
        console.print("\tArea: " + area);
        console.print("\tMaterial: "+ materialType);
        console.print("\tMaterial Total/Cost per sq ft: $" + newOrder.getMaterialCost() + "/$" + costSqFt);
        console.print("\tLabor Total/ Cost per sq ft: $" + newOrder.getTotalLaborCost() + "/$" + laborSqFt);
        console.print("\tTax: $" + newOrder.getTax());
        console.print("\t**-----------------------**");
        console.print("\t\tTotal: $" + newOrder.getTotal());
        console.print("\n======================================================");
        do {
            confirm = console.readString("Confirm order: (y/n)");
        } while (!confirm.equalsIgnoreCase("y") && !confirm.equalsIgnoreCase("n"));
        if (confirm.equalsIgnoreCase("y")){
            orderDao.addOrder(newOrder, dateKey);
        }
    }
    
    private void editOrder(){
        Order editOrder = new Order();
        int ordNum;
        String dateKey = "";
        String date = "";
        String state = "";
        do{
            date = valiDate("Enter date (mmddyyyy): ");
            dateKey = orderDao.convertDateKey(date);
            ordNum = console.readInt("Order Number: ");
            editOrder = orderDao.getOrder(ordNum, dateKey);
        }while (editOrder == null);

        String custName = console.readString("Full name ("+editOrder.getCustomerName()+"): ");
        if (!custName.isEmpty()){
            editOrder.setCustomerName(custName);
        }
        String stateInput = console.readString("State ("+editOrder.getState()+"): ");
        if (!stateInput.isEmpty()){
            state = taxRateDao.valState(stateInput);
            while (state == null){
                console.print("Invalid State.");
                stateInput = console.readString("State: ");
                state = taxRateDao.valState(stateInput);
            }
            editOrder.setState(state);
        }
        String sTaxRate = console.readString("State tax ("+editOrder.getTaxRate()+"): ");
        if (!sTaxRate.isEmpty()){
            double d = Double.parseDouble(sTaxRate);
            while (d < 0){
                console.print("Invalid. Entry cannot be less than zero.");
                d = console.readDouble("State tax ("+editOrder.getTaxRate()+"): ");
            }
            editOrder.setTaxRate(d);
        }
        String materialType = console.readString("Material Type ("+editOrder.getMaterialType()+"): ");
        if (!materialType.isEmpty()){
            Material material = typeDao.getTypeInfo(materialType);
            while (material == null){
                materialType = console.readString("Material Type ("+editOrder.getMaterialType()+"): ");
                material = typeDao.getTypeInfo(materialType);
            }
            editOrder.setMaterialType(materialType);
        }
        String sArea = console.readString("Area ("+editOrder.getArea()+"): ");
        if (!sArea.isEmpty()){
            double d = Double.parseDouble(sArea);
            while (d < 0){
                console.print("Invalid. Entry cannot be less than zero.");
                d = console.readDouble("Area ("+editOrder.getArea()+"): ");
            }
            editOrder.setArea(d);
        }
        String sCostSqFt = console.readString("Material Cost per sq ft($"+editOrder.getCostSqFt()+"): ");
        if (!sCostSqFt.isEmpty()){
            double d = Double.parseDouble(sCostSqFt);
            while (d < 0){
                console.print("Invalid. Entry cannot be less than zero.");
                d = console.readDouble("Material Cost per sq ft($"+editOrder.getCostSqFt()+"): ");
            }
            editOrder.setCostSqFt(d);
        }
        String sLaborSqFt = console.readString("Labor Cost per sq ft($"+editOrder.getLaborSqFt()+"): ");
        if (!sLaborSqFt.isEmpty()){
            double d = Double.parseDouble(sLaborSqFt);
            while (d < 0){
                console.print("Invalid. Entry cannot be less than zero.");
                d = console.readDouble("Labor Cost per sq ft($"+editOrder.getLaborSqFt()+"): ");
            }
            editOrder.setLaborSqFt(d);
        }
        String sTotalMaterialCost = console.readString("Material Cost ($"+editOrder.getMaterialCost()+"): ");
        if (!sTotalMaterialCost.isEmpty()){
            double d = Double.parseDouble(sTotalMaterialCost);
            while (d < 0){
                console.print("Invalid. Entry cannot be less than zero.");
                d = console.readDouble("Material Cost ($"+editOrder.getMaterialCost()+"): ");
            }
            editOrder.setMaterialCost(d);
        }
        String sTotalLabor = console.readString("Labor Cost ($"+editOrder.getTotalLaborCost()+"): ");
        if (!sTotalLabor.isEmpty()){
            double d = Double.parseDouble(sTotalLabor);
            while (d < 0){
                console.print("Invalid. Entry cannot be less than zero.");
                d = console.readDouble("Labor Cost ($"+editOrder.getTotalLaborCost()+"): ");
            }
            editOrder.setTotalLaborCost(d);
        }
        String sTax = console.readString("Tax ($"+editOrder.getTax()+"): ");
        if (!sTax.isEmpty()){
            double d = Double.parseDouble(sTax);
            while (d < 0){
                console.print("Invalid. Entry cannot be less than zero.");
                d = console.readDouble("Tax ($"+editOrder.getTax()+"): ");
            }
            editOrder.setTax(d);
        }
        String sTotal = console.readString("TOTAL ($"+editOrder.getTotal()+"): ");
        if (!sTotal.isEmpty()){
            double d = Double.parseDouble(sTotal);
            while (d < 0){
                console.print("Invalid. Entry cannot be less than zero.");
                d = console.readDouble("TOTAL ($"+editOrder.getTotal()+"): ");
            }
            editOrder.setTotal(d);
        }
        String date2 = console.readString("Date ("+ date +"): ");
        if (!date2.isEmpty()){
            Matcher matcher;
            String regex = "^(1[0-2]|0[1-9])(3[01]|[12][0-9]|0[1-9])[0-9]{4}$";
//            String regex = "/^(0[1-9]|1[0-2])\/(0[1-9]|1\d|2\d|3[01])\/(19|20)\d{2}$/";
            Pattern pattern = Pattern.compile(regex);
            matcher = pattern.matcher(date2);
            while (!matcher.matches()){
                  console.print("Invalid date format.");
                  date2 = console.readString("Date ("+ date +"): ");
                  matcher = pattern.matcher(date2);
            }
            String dateKey2 = orderDao.convertDateKey(date);
            int newOrderNum = orderDao.getOrderNum(dateKey2);
            editOrder.setOrderNum(newOrderNum); //add new order num
            orderDao.addOrder(editOrder, dateKey2); 
            
            dailyOrders = orderDao.getDateOrders(dateKey);  //resequence order numbers
            for (int i = ordNum; i < dailyOrders.size(); i++) { //shortcut method
                Order ord = orderDao.getOrder(i + 1, dateKey);
                ord.setOrderNum(i);
                orderDao.addOrder(ord, dateKey);
            }
            orderDao.removeOrder(dailyOrders.size(), dateKey);
        }
        console.print("Your order has been updated.");
    }
    private void deleteOrder(){
        Order o = new Order();
        int ordNum = 0;
        String dateKey = "";
        do{
            String date = valiDate("Enter date (mmddyyyy): ");
            dateKey = "Orders_"+ date +".txt";
            ordNum = console.readInt("Order Number: ");
            o = orderDao.getOrder(ordNum, dateKey);
        }while (o == null);
        console.print("=====================================================");
        console.print("\n\t\tORDER SUMMARY ");
        console.print("\tName: " + o.getCustomerName());
        console.print("\tState/Tax Rate: " + o.getState() + "/" + o.getTaxRate() + "%");
        console.print("\tArea: " + o.getArea());
        console.print("\tMaterial: " + o.getMaterialType());
        console.print("\tMaterial Total/Cost per sq ft: " + o.getMaterialCost() + "/$" + o.getCostSqFt());
        console.print("\tLabor Total/ Cost per sq ft: $" + o.getTotalLaborCost() + "/$" + o.getLaborSqFt());
        console.print("\tTax: $" + o.getTax());
        console.print("\t**-----------------------**");
        console.print("\t\tTotal: $" + o.getTotal());
        console.print("\n======================================================");
        String confirm = console.readString("\nConfirm delete: (y/n)");
        if (confirm.equalsIgnoreCase("y")){
            dailyOrders = orderDao.getDateOrders(dateKey);
            for (int i = ordNum; i < dailyOrders.size(); i++) {
                Order ord = orderDao.getOrder(i + 1, dateKey);
                ord.setOrderNum(i);
                orderDao.addOrder(ord, dateKey);
            }
            orderDao.removeOrder(dailyOrders.size(), dateKey);
            console.print("Removal processed.");
        }
    }
    
    private String valiDate(String prompt){
        Matcher matcher;
        String date;
        do {
            date = console.readString(prompt);
            String regex = "^(1[0-2]|0[1-9])(3[01]|[12][0-9]|0[1-9])[0-9]{4}$";
            Pattern pattern = Pattern.compile(regex);
            matcher = pattern.matcher(date);
        } while (!matcher.matches());
        return date;
    }
    
    
}
