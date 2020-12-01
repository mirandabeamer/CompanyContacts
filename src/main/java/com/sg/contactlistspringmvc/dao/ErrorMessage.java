/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.contactlistspringmvc.dao;

/**
 *
 * @author mirandabeamer
 */
public class ErrorMessage {
    //this class holds the error message being sent back to the browser
    private String message;
    
    public String getMessage() {
        return message;
    }
    
    public void setMessage(String message){
        this.message = message;
    }
}
