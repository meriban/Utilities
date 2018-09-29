/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.utilities.utils;

import java.util.ArrayList;

/**
 *
 * @author fran
 */
public class ParseResult {
    
    public static final int NO_ERRORS = 0;
    public static final int SOME_ERRORS = 1;
    public static final int ALL_ERRORS = 2;
    public static final int NO_INPUT = 3;
    
    int soundness = 0;
    ArrayList<String> errorReport;
    String parsedString;
    
    public void setSoundness(int s){
        soundness = s;
    }
    
    public int getSoundness(){
        return soundness;
    }
    
    public void setErrorReport(ArrayList<String> r){
        errorReport = r;
    }
    
    public ArrayList<String> getErrorReport(){
        return errorReport;
    }
    
    public void setParsedString(String string){
        parsedString = string;
    }
    public String getParsedString(){
        return parsedString;
    }
    
}
