//
//  Settings.java
//  PerfectPitch
//
//  Created by Francisco Fernandez on Sun Feb 29 2004.
//  Copyright (c) 2001 Muscience. All rights reserved.
//
package jabsolute;

public class Settings {
    // Fields          
    private int [] notesInvolved;  
    private boolean [] keysChosen;
    private int notesPerChord;
    private int duration; // minutes
    private int chordType;
    private int instrument;
    private boolean octaveSensitive;
    
    // Constructor
    public Settings () {
        this ( new boolean [12] , 1, 5, 0, 0, false );
    }
    
    /**
     * 
     * @param ni Notes Involved
     * @param npc Notes per Chord
     * @param d Duration
     * @param ct Chord Type
     * @param in Instrument
     * @param os Octave Sensitive
     */
    public Settings ( int [] ni, int npc, int d, int ct, int in, boolean os ) {
        boolean [] tempArray = new boolean [12];
        for (int i = 0 ; i < ni.length ; i++) {
            tempArray [ (ni[i]) ] = true;
        }
        keysChosen = tempArray;
        notesPerChord = npc;
        duration = d;
        chordType = ct;
        instrument = in;
        setNotesInvolved();        
        octaveSensitive = os;
    }
    
    public Settings ( boolean [] kc, int npc, int d, int ct, int in, boolean os  ) {
        keysChosen = (boolean []) kc.clone();
        notesPerChord = npc;
        duration = d;
        chordType = ct;
        instrument = in;
        setNotesInvolved();
        octaveSensitive = os;
      //  System.out.println ("********************************");
       // System.out.println (this);
    }
    
    public int [] getNotesInvolved () { return notesInvolved; }
    
    public boolean [] getKeysChosen () { return keysChosen; }
    
    public int getNotesPerChord () { return notesPerChord; } 
    
    public int getDuration () { return duration; }

    public int getChordType () { return chordType; }
    
    public int getInstrument () { return instrument; }
    
    public boolean isOctaveSensitive () { return octaveSensitive; }

    
    @Override
    public String toString () {
        String rv = "\n";
        for (int i = 0 ; i < 12 ; i++ ) {
            rv = rv + i + " = " + keysChosen [i] + "\n";
        }
        for (int i = 0 ; i < notesInvolved.length ; i++) {
            rv = rv + notesInvolved [i] + "\n";
        } 
        rv = rv + "notesPerChord = " + notesPerChord + "\n";
        rv = rv + "duration = " + duration + "\n";
        rv = rv + "chordType = " + chordType + "\n";
        rv = rv + "instrument = " + instrument + "\n";
        rv = rv + "octaveSensitive =" + octaveSensitive;
        return rv;
    }
    
    // private methods
    private void setNotesInvolved () {
        int count = 0;
        for (int i = 0 ; i < keysChosen.length ; i++ ) {
            if ( keysChosen [ i ] ) { count ++; }
        }
        notesInvolved = new int [ count ];
        count = 0;
        for ( int i = 0 ; i < keysChosen.length ; i++ ) {
            if ( keysChosen [ i ] ) {
                notesInvolved [ count ] = i;
                count++;
            }
        }
    }
    
} // END of class Settings
