package jabsolute;

import java.util.*;
import java.io.*;


class Session implements Serializable {
    Date date;
    int level;
    int score; // percent

    // constructor
    public Session (int l, int s) {
        date = new Date ();
        level = l;
        score = s;
    }
    
    public String toString() {
         String rv = date + "\n";
         rv = rv + "Level: " + level + ", Score: " + score;
         return rv;
    }
}