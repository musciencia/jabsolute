//
//  KeyboardMethods.java
//  SimuladorDeAfinacion
//
//  Created by Marilupe Garmilla on Wed Jun 25 2003.
//  Copyright (c) 2001-2015 Muscience. All rights reserved.
//

package jabsolute;

public interface KeyboardMethods {
        // Abstract methods
    public abstract void keyPressed ( Key k ); 
    
    public abstract void keyReleased ( Key k ); 
        
    public abstract void tuneUp ( Key k );
    
    public abstract void tuneDown ( Key k );
    
}
