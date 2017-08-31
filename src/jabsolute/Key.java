//
//  Key.java
//  SimuladorDeAfinacion
//
//  Created by Marilupe Garmilla on Thu May 29 2003.
//  Copyright (c) 2001 __MyCompanyName__. All rights reserved.
//
package jabsolute;

import java.awt.*;
import java.util.Vector;

public class Key extends Rectangle {
    // Private Fields        // more fields pending (noteNum, freq, etc)
    private final boolean WHITE = false;
    private final boolean BLACK = true;
    private int keyNum;
    private boolean keyType;
    private Color color;
    private Color defaultColor;
   // private Vector storedColors = new Vector();
    private int midiNote;
    
    // Constructor
    public Key (int kn ) {
        this ( kn, 0, 0, 0, 0 );
    }
    
    public Key ( int kn, int x, int y, int width, int height ) {
        super ( x, y, width, height );
        keyNum = kn;
        initKeyType();
        setDefaultColor();
        //color = defaultColor;
        midiNote = kn + 36;
    }
    
    // Public Methods
    public int getMidiNote () { return midiNote; }
    
    public int getKeyNum () { return keyNum; }
    
    public Color getColor () { return color; }
    
    public Color getDefaultColor () { return defaultColor; }
    
  //  public Vector getStoredColors () { return storedColors; }
    
    public boolean isBlack () { return keyType; }
    
    public boolean isWhite () { return !keyType; }
    
    // coul be a good idea to return color -- public Key setColor ();
    public void setColor ( Color c ) { color = c; } 
    
    public void setDefaultColor ( Color c ) { defaultColor = c; }
                    
    public void setDefaultColor () { 
        defaultColor = ( isBlack() ? Color.black : Color.white ); 
        color = defaultColor;
    }
    
    public void useDefaultColor () {
       color = defaultColor;
    }
    
    // Private Methods
    private void initKeyType () {
        switch ( keyNum % 12 ) {
            case 1: case 3: case 6: case 8: case 10:
                    keyType = BLACK;
                break;
            
            case 0: case 2: case 4: case 5: case 7: case 9: case 11:
                    keyType = WHITE;
                break;
        }	
    }

} // end class Key
