/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsguild.flooringmastery;

import com.tsguild.flooringmastery.ops.FlooringMasteryController;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 *
 * @author apprentice
 */
public class FlooringMasteryApp {
    
    public static void main(String[] args) {
        
        ApplicationContext springFactory = new ClassPathXmlApplicationContext("applicationContext.xml");
        
        FlooringMasteryController controller = springFactory.getBean("controller", FlooringMasteryController.class);
        controller.run();
        
    }
}
