//
//  UserManager.java
//  JAbsolute
//
//  Created by Marilupe Garmilla on Thu Mar 18 2004.
//  Copyright (c) 2001 __MyCompanyName__. All rights reserved.
//
package jabsolute;
import java.util.*;
import java.io.*;
//import javax.swing.ButtonGroup*;

public class UserManager {
    private static User currentUser = null;
    private static Vector users = loadUsers();
    private static final String DATA_FILE = "users.dat";
    
    // Public methods
    public static Vector loadUsers() {
        Vector rv = new Vector ();
        try {
            FileInputStream fileIn = new FileInputStream(DATA_FILE);
            ObjectInputStream in = new ObjectInputStream ( fileIn );
            rv = (Vector) in.readObject();
            in.close();
        } catch ( Exception e ) {
            System.err.println ( DATA_FILE + " does not exist or cannot be read" );
            System.err.println("A new file " + DATA_FILE + 
                    " will be created when the program ends");
            //saveUsers();
        }
        return rv;
    }
    
    public static void saveUsers() {
        try {
            FileOutputStream fileOut = new FileOutputStream(DATA_FILE);
            ObjectOutputStream out = new ObjectOutputStream ( fileOut );
            out.writeObject ( users );
            out.close();
        } catch ( IOException e ) {
            System.err.println("Error saving " + DATA_FILE );
        }
        
      /*  User tempUsr = null;
        for (int i=0 ; i<users.size();i++){
             tempUsr = (User)users.get(i);
             tempUsr.saveAsTxt();
        }*/
    }
    
    public static void addUser (String name) {
        users.add ( new User (name) );
    }
    
    public static void addUser (User usr) {
        users.add (usr);
    }
        
    public static User getCurrentUser () { return currentUser ;}
    
    public static int getCurrentUserIndex () {
         return users.indexOf(currentUser);
    }
    
    public static String [] getUserNames () {
        String [] rv = new String [users.size()];
        for ( int i = 0 ; i < users.size() ; i ++) {
            rv [i] = ((User) users.get(i)).getName();
        }
        return rv;
    }
    
    public static int getUserCount() { return users.size();}
    
    public static void removeUser (int index) { // corregir posible error
        users.remove ( index );
    }
    
    public static void removeUser (String nm) { // name
    }
    
    public static void removeUser (User usr) {
        users.remove ( usr ); // corregir si usr es currentUser, set to null
    }
    
    public static void removeCurrentUser () {
        if ( !( currentUser == null ) ) {
            //System.out.println (currentUser + " removed");
            users.remove ( currentUser );
            currentUser = null;
        }
    }
    
    public static void setCurrentUser ( User usr ) { /// return boolean
        if ( users.contains(usr) ) {
            currentUser = usr;
        } else { 
            System.err.println ("usr does not exist"); 
        }
    }
    
    public static void setCurrentUser ( String nm ) {
        User tempUsr = null;
        if ( !(nm == null) ) {
            for (int i = 0 ; i <  users.size() ; i++) {
                if (nm.equals ( ( (User) users.get(i) ).toString() ) ) {
                    tempUsr = (User) users.get(i);
                    break;
                }
            }
            if (tempUsr == null) { 
                tempUsr = new User( nm );
                addUser ( tempUsr );
            }
            currentUser = tempUsr;
            //System.out.println ("currentUser = " + currentUser );
        }
    }
    
    public static boolean contains ( String nm ) {
        boolean rv = false;
        if ( !(nm == null) ) {
            for (int i = 0 ; i <  users.size() ; i++) {
                if (nm.equals ( ((User) users.get(i)).getName() )) {
                    rv = true;
                    break;
                }
            }
        }
        return rv;
    } 
}
