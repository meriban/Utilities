/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.utilities.controls;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.BooleanPropertyBase;
import javafx.css.PseudoClass;
import javafx.scene.control.Button;

/**
 *
 * @author franz
 */
public class FunctionButton extends Button {

    private static final PseudoClass RUNNING = PseudoClass.getPseudoClass("running");

    BooleanProperty running = new BooleanPropertyBase(false) {
        @Override
        public void invalidated() {
            pseudoClassStateChanged(RUNNING, get());
        }

        /**
         * Returns the {@code Object} that contains this property. If this
         * property is not contained in an {@code Object}, {@code null} is
         * returned.
         *
         * @return the containing {@code Object} or {@code null}
         */
        @Override
        public Object getBean() {
            return FunctionButton.this;
        }

        @Override
        public String getName() {
            return "running";
        }
    };
    
    public FunctionButton(){
        running.set(false);
        getStyleClass().add("function-button");
    }
    
    /**
     * Gets the value of {@link FunctionButton#running}. 
     * 
     * I.e. whether the {@code FunctionButton}'s {@link FunctionButton#running} 
     * property is on ({@code true}) or off ({@code false}). 
     * 
     * @return {@code true} or {@code false})
     */
    public boolean isRunning(){
        return running.get();
    }
    
    /**
     * Sets the value of {@link FunctionButton#running}.
     * 
     * @param running the value 
     */
    public void setRunning(boolean running){
        this.running.set(running);
    }
    
    /**
     * Gets the {@link BooleanProperty) {@link FunctionButton#running}.
     * 
     * @return the {@code BooleanProperty}
     */
    public BooleanProperty getRunningProperty(){
        return running;
    }

}