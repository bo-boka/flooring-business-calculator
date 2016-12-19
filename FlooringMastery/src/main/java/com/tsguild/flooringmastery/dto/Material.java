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
public class Material {
    private String type;
    double materialCostSqFt;
    double laborCost;

    public Material() {
    }

    public Material(String type, double materialCostSqFt, double laborCost) {
        this.type = type;
        this.materialCostSqFt = materialCostSqFt;
        this.laborCost = laborCost;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getMaterialCostSqFt() {
        return materialCostSqFt;
    }

    public void setMaterialCostSqFt(double materialCostSqFt) {
        this.materialCostSqFt = materialCostSqFt;
    }

    public double getLaborCost() {
        return laborCost;
    }

    public void setLaborCost(double laborCost) {
        this.laborCost = laborCost;
    }
    
    
}
