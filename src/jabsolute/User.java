//
//  User.java
//  PerfectPitch
//
//  Created by Marilupe Garmilla on Thu Mar 18 2004.
//  Copyright (c) 2001 __MyCompanyName__. All rights reserved.
//
package jabsolute;
import java.util.*;
import java.io.*;

public class User implements Serializable {
 //   static String NEW_LINE = System.getProperty("line.separator"); 
    int currentLevel;
    int highestLevel;
    String name;
    Vector history;
    
    // Consstructor
    public User (String n) {
        name = n;
        currentLevel = 0;
        history = new Vector();
    }
    
    // public methods
    public void setCurrentLevel ( int cl ) {
        currentLevel = cl;
        if (currentLevel >= highestLevel ){ 
             highestLevel = currentLevel;
        } 
    }
        
    public void nextLevel () {        
        currentLevel= (currentLevel + 1) % ExerciseManager.getTotalLevels();
        if (currentLevel >= highestLevel ){ 
             highestLevel = currentLevel;
        } 
    }
    
    public void addSession (int score) {
         history.add(new Session (currentLevel, score ));
    }
    
    public int getCurrentLevel () {
        return currentLevel;
    }
    
    public int getHighestLevel () {
        return highestLevel;
    }
    
    public String getInfo() {
         String rv="";
         rv = "Username: " + getName() + "\n" +
              "Current level: " + getCurrentLevel() + "\n" +
              "Highest level: " + getHighestLevel() + "\n" ;
         return rv;             
    }
    
    public String getInfoAndHistory(){
         return  getInfo() + getHistoryText();
    }
    
    public String getName () { return name;} 
    
    public void saveAsTxt (String fileName) {
        try {
            FileOutputStream fileOut = new FileOutputStream(fileName);
            PrintWriter out = new PrintWriter ( fileOut );
            //out.writeBytes ( getHistoryText() );
            out.println(getInfo());
            out.println(getHistoryText());
            out.close();
        } catch ( IOException e ) {
            //System.out.println ("Error saving " + fileName );
        }         
    }
    
    public void saveAsTxt () {
         saveAsTxt( name + ".txt" );
    }
    
    public String toString () { 
         return name; 
    }
    
    public String getHistoryText() {
         String rv = "HISTORY\n" +
                     "=======\n";
         
         
         for (int i=0 ; i<history.size() ; i++ ){
              rv = rv + (Session) history.get(i) + "\n"; 
         }
         return rv;
    }
}

