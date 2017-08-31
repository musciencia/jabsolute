//
//  GeneralMethods.java
//  JAbsolute
//
//  Created by Francisco Fernandez on Sun Mar 21 2004.
//  Copyright (c) 2001-2015 Muscience. All rights reserved.
//
package jabsolute;

import java.awt.event.*;
import javax.swing.Timer;

public abstract class GeneralMethods implements 
    KeyboardMethods, ChronometerMethods, ControlsMethods, SelectorMethods {
    // A timer delayedAsk is needed so that the user has enough time (1sec.) to see
    // the color blue if he scores right, otherwise the next question comes
    // so fast that the color blue is never seen
    int delay = 1000; // miliseconds
    Timer delayedAsk = new Timer (delay, new AskListener());
        
    public GeneralMethods () {
        delayedAsk.setRepeats ( false );
    }
        
    class AskListener implements ActionListener {
        @Override
        public void actionPerformed ( ActionEvent e) {
            ask();
        }
    }
        
    public void reset () {
    }
        
////////KeyboardMethods                 
    @Override
    public void keyPressed ( Key k ) {
    }

    @Override
    public void keyReleased ( Key k ) {
    }

////////ChronometerMethods        
    @Override
    public void timeExpired () { 
    }  

////////ControlsMethods
    @Override
    public void runStart () {            
    } // END of runStart()
        
    @Override
    public void runRepeat() {
    }
        
    @Override
    public void runAnswer () {
    }
    
////////SelectorMethods 
    @Override
    public void instrumentSelected() {
    }
        
////////////////// Helper Methods for GeneralMethods    
    protected void repeat() {
    }

    protected void ask() {
    }
        
    protected void testAnswer ( Key k ) {
    } // END of testAnswer()
                    
} // END of class GeneralMethods 

