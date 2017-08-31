//
//  Controls.java
//  PerfectPitch
//
//  Created by Marilupe Garmilla on Sat Feb 28 2004.
//  Copyright (c) 2001 __MyCompanyName__. All rights reserved.
//
package jabsolute;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import javax.swing.JPanel;
import javax.swing.JButton;

public class Controls extends JPanel implements ActionListener, ControlsMethods {
    // Fields
    private static JButton defaultButton = null; // temporal
    JButton start = new JButton ("Start");
    JButton repeat = new JButton ("Repeat");
    JButton answer = new JButton ("Answer");
    ControlsMethods controlsMethods;

    // Constructor
    public Controls () {
        setLayout (new GridLayout(1,3,30,5));
        add(start); 
        add(repeat);  repeat.setMnemonic (KeyEvent.VK_R);
        add(answer);  answer.setMnemonic (KeyEvent.VK_A);
        start.addActionListener ( this );
        repeat.addActionListener ( this );
        answer.addActionListener ( this );
        addControlsMethods (this);
        repeat.setDefaultCapable(false);
        //repeat.setFocusable(false);
        answer.setDefaultCapable(false);
        //help.setFocusable(false);
        defaultButton = start; // temporal
    }
////Public methods
    public final void addControlsMethods (ControlsMethods contMeth) {
        controlsMethods = contMeth;
    }

    @Override
    public void setEnabled ( boolean b ) {
        start.setEnabled (b);
        repeat.setEnabled (b);
        answer.setEnabled (b);
    }
    
    public void setEnabled ( boolean st, boolean rp, boolean hl) {
        start.setEnabled (st);
        repeat.setEnabled (rp);
        answer.setEnabled(hl);
    }
    
    // temporal
    public static JButton getDefaultButton () { return defaultButton;}
    
//// Methods implementation
    @Override
    public void runStart() { 
        //System.out.println ("runStart()"); 
    }
    
    @Override
    public void runRepeat() { 
        //System.out.println ("runRepeat()"); 
    }
    
    @Override
    public void runAnswer() { 
        //System.out.println ("runAnswer()"); 
    }

    @Override
    public void actionPerformed ( ActionEvent evt ) {
        String buttonName = evt.getActionCommand();
  /// START          
        if (buttonName.equals(start.getText())) {
            controlsMethods.runStart();
  /// REPEAT          
        } else if (buttonName.equals (repeat.getText())) {
            controlsMethods.runRepeat();
  /// HELP          
        } else if (buttonName.equals (answer.getText())) {
            controlsMethods.runAnswer();
        }
    } // END of ActionPerformed
} // END of Controls


