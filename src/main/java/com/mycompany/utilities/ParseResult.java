/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.utilities;

import java.util.ArrayList;

/**
 *
 * @author fran
 */
public class ParseResult {
    
    static final int NO_ERRORS = 0;
    static final int SOME_ERRORS = 1;
    static final int ALL_ERRORS = 2;
    static final int NO_INPUT = 3;
    
    int soundness = 0;
    ArrayList<String> errorReport;
    
    void setSoundness(int s){
        soundness = s;
    }
    
    int getSoundness(){
        return soundness;
    }
    
    void setErrorReport(ArrayList<String> r){
        errorReport = r;
    }
    
    ArrayList<String> getErrorReport(){
        return errorReport;
    }
    
}
