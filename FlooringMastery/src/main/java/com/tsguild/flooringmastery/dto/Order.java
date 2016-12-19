/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsguild.flooringmastery.dto;

/**
 *
 * @author apprentice
 */
public class Order {
    
    protected int orderNum;
    protected String customerName;
    protected String state;
    protected double taxRate;
    protected String materialType;
    protected double area;
    protected double costSqFt;
    protected double laborSqFt;
    protected double materialCost;
    protected double totalLaborCost;
    protected double tax;
    protected double total;
    
    public Order(){}

    public Order(int orderNum, String customerName, String state, double taxRate, String materialType, double area, double costSqFt, double laborSqFt) {
        this.orderNum = orderNum;
        this.customerName = customerName;
        this.state = state;
        this.taxRate = taxRate;
        this.materialType = materialType;
        this.area = area;
        this.costSqFt = costSqFt;
        this.laborSqFt = laborSqFt;
        this.materialCost = Math.round(costSqFt * area * 100.0) /100.0;
        this.totalLaborCost = Math.round(laborSqFt * area * 100.0) /100.0;
        this.tax = Math.round((materialCost + totalLaborCost) * (taxRate/100) * 100.0) / 100.0; //100 can be cancelled?
        this.total = Math.round((totalLaborCost + materialCost + tax) * 100.0) / 100.0;
        
    }

    public int getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(int orderNum) {
        this.orderNum = orderNum;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public double getTaxRate() {
        return taxRate;
    }

    public void setTaxRate(double taxRate) {
        this.taxRate = taxRate;
    }

    public String getMaterialType() {
        return materialType;
    }

    public void setMaterialType(String materialType) {
        this.materialType = materialType;
    }

    public double getArea() {
        return area;
    }

    public void setArea(double area) {
        this.area = area;
    }

    public double getCostSqFt() {
        return costSqFt;
    }

    public void setCostSqFt(double costSqFt) {
        this.costSqFt = costSqFt;
    }

    public double getLaborSqFt() {
        return laborSqFt;
    }

    public void setLaborSqFt(double laborSqFt) {
        this.laborSqFt = laborSqFt;
    }

    public double getMaterialCost() {
        return materialCost;
    }

    public void setMaterialCost(double materialCost) {
        this.materialCost = materialCost;
    }

    public double getTotalLaborCost() {
        return totalLaborCost;
    }

    public void setTotalLaborCost(double totalLaborCost) {
        this.totalLaborCost = totalLaborCost;
    }

    public double getTax() {
        return tax;
    }

    public void setTax(double tax) {
        this.tax = tax;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    
}
