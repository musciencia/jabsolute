//   POR DESAPARECER
//  ExerciseManager.java
//  SimuladorDeAfinacion
//
//  Created by Francisco Fernandez Sat Jun 28 2003.
//  Copyright (c) 2001-2015 Muscience. All rights reserved.
//
//  C C# D D# E F F# G G# A A# B 
//  0 1  2 3  4 5 6  7 8  9 10 11
package jabsolute;

import java.util.Arrays;
import java.util.Vector;

public class ExerciseManager implements MusicConstants {

    private static Settings currentSettings;
    private static int[] last;
    private static int[] orden;
    private static int notes;   // number of notes involved 
    private static int simul = 1;   // number of notes in the chord (simultaneous) 
    private static int count = 0;  // cuenta los indices de las notas que se van respondiendo 
    private static Vector levels = initOriginalLevels();
    private static boolean isOctaveSensitive;
    private static Vector currentChordTypes
            = Preferences.getCurrentChordTypes();
    private static Vector currentInstruments;

    public static Vector getCurrentChordTypes() {
        return currentChordTypes;
    }

    public static int getTotalLevels() {
        return levels.size();
    }

    public static int getLastLevel() {
        //System.out.println (levels.size()-1);
        return levels.size() - 1;
    }

    public static void setCurrentChordTypes(Vector cts) {
        currentChordTypes = cts;
    }

    public static void addChordType(Object ct) {
        currentChordTypes.add(ct);
    }

    public static void setCurrentSettings(Settings s) {
        currentSettings = s;
        orden = currentSettings.getNotesInvolved();
        simul = currentSettings.getNotesPerChord();
        notes = orden.length;
        last = new int[simul];
        isOctaveSensitive = s.isOctaveSensitive();
    }

    public static boolean answerIsRight(int n) {
        //System.out.println ("isOctaveSensitive" + isOctaveSensitive); 
        boolean tempReturn
                = (isOctaveSensitive ? last[count % last.length] == n
                        : (((last[count % last.length]) % 12) == (n % 12)));
        if (tempReturn) {
            count++;
        }
        return tempReturn;
    }

    public static int getTestIndex() {
        return count % last.length;
    }

    public static Vector getLevels() {
        return levels;
    }

    public static Settings getLevel(int index) {
        Settings rv = (Settings) levels.get(index % (levels.size()));
        return rv;
    }

    public static int getCount() {
        return count;
    }

    public static int getSimul() {
        return simul;
    }

    public static int getTestNote() {
        return last[count % last.length];
    }

    public static void resetCount() {
        count = 0;
    }

    public static int[] getNew() {
        Object tempChordType
                = currentChordTypes.get(currentSettings.getChordType());
        int[] tempReturn = new int[simul];  // Potential memory leak  
        int randomNote;
        int randomOctave;
        int randomKey; // Key on the keyboard
        int rootForChord;
        Chord tempChord;
        // If there is only one note to chose from allow repeats
        boolean allowNoteRepeat = (notes < 2);
        Arrays.fill(tempReturn, -1);

        /// chordType
        if (tempChordType instanceof ChordType) {
            randomNote = orden[(int) (Math.random() * notes)];
            randomOctave = (int) (Math.random() * 4.0);
            rootForChord = randomNote + (randomOctave * 12);
            tempChord = new Chord(rootForChord, (ChordType) tempChordType);
            tempReturn = tempChord.getNotes();
        } else if (tempChordType.equals("Random")) {
            randomNote = orden[(int) (Math.random() * notes)];
            randomOctave = (int) (Math.random() * 4.0);
            rootForChord = randomNote + (randomOctave * 12);
            int idx = (int) (Math.random()
                    * (currentChordTypes.size() - 2));
            idx = idx + 1;
            tempChordType = currentChordTypes.get(idx);
            //System.out.println("3 tempReturn[0] =" + tempReturn[0]);
            tempChord = new Chord(rootForChord, (ChordType) tempChordType);
            tempReturn = tempChord.getNotes();
        } else {

            for (int i = 0; i < tempReturn.length; i++) {
                do {
                    randomNote = orden[(int) (Math.random() * notes)];
                    randomOctave = (int) (Math.random() * 4.0);
                    randomKey = randomNote + (randomOctave * 12);
                } while (contains(tempReturn, randomKey, allowNoteRepeat));
                tempReturn[i] = randomKey;
            }
            Arrays.sort(tempReturn);
        }
        last = tempReturn;
        return tempReturn;
    } // END getNew() 

    public static int[] getLast() {
        return last;
    }

/////// Helper methods  
    /**
     * Check if the array already contains the keyboardKey
     *
     * @param array Array with keyboard keys (notes)
     * @param keyboardKey The keyboard key (note) to be tested
     * @param allowNoteRepeat If you want to allow the same note in different
     * octaves to be present
     * @return true or false
     */
    private static boolean contains(int[] array, int keyboardKey,
            boolean allowNoteRepeat) {
        boolean returnValue = false;
        for (int j = 0; j < array.length; j++) {
            if (allowNoteRepeat) {
                returnValue = array[j] == keyboardKey;
            } else {
                // Don't allow to repeat notes in different octaves
                // Notes in different octaves are hard to hear
                returnValue = ((array[j] % 12) == (keyboardKey % 12));
            }
            // The value has been found, stop looking
            if (returnValue) {
                break;
            }
        }
        return returnValue;
    }

    /*  TO BE DELETED  private static Vector initCurrentChordTypes () { 
     Vector rv = new Vector ();
     rv.add ("None"); 
     rv.add (MAJOR); 
     rv.add (MINOR); 
     rv.add (DIMINISHED); 
     rv.add ("Random"); 
     return rv;
     } TO BE DELETED */
    private static Vector initLevels() {
        Vector rv = new Vector();
        boolean[] tempArray = new boolean[12];
        Arrays.fill(tempArray, false);

        // Level 0:  C, E -> Notes per chord = 1 
        tempArray[C] = true;
        tempArray[Note.E] = true;
        rv.add(new Settings(tempArray, 1, 5, 0, 0, false));

        // Level 1: C, E, G# -> Notes per chord = 1
        tempArray[Gs] = true;
        rv.add(new Settings(tempArray, 1, 5, 0, 0, false));

        // Level 2: C, E, G# -> Notes per chord = 2
        rv.add(new Settings(tempArray, 2, 5, 0, 0, false));

        // Level 3: C, E, G# -> Notes per chord = 3
        //rv.add(new Settings(tempArray, 3, 5, 0, 0, false));
        tempArray[D] = true;
        rv.add(new Settings(tempArray, 1, 5, 0, 0, false));
        rv.add(new Settings(tempArray, 2, 5, 0, 0, false));
        //rv.add(new Settings(tempArray, 3, 5, 0, 0, false));
        tempArray[Fs] = true;
        rv.add(new Settings(tempArray, 1, 5, 0, 0, false));
        rv.add(new Settings(tempArray, 2, 5, 0, 0, false));
        //rv.add(new Settings(tempArray, 3, 5, 0, 0, false));
        tempArray[Bb] = true;
        rv.add(new Settings(tempArray, 1, 5, 0, 0, false));
        rv.add(new Settings(tempArray, 2, 5, 0, 0, false));
        rv.add(new Settings(tempArray, 3, 5, 0, 0, false));
        tempArray[Cs] = true;
        rv.add(new Settings(tempArray, 1, 5, 0, 0, false));
        rv.add(new Settings(tempArray, 2, 5, 0, 0, false));
        rv.add(new Settings(tempArray, 3, 5, 0, 0, false));
        tempArray[Eb] = true;
        rv.add(new Settings(tempArray, 1, 5, 0, 0, false));
        rv.add(new Settings(tempArray, 2, 5, 0, 0, false));
        rv.add(new Settings(tempArray, 3, 5, 0, 0, false));
        tempArray[F] = true;
        rv.add(new Settings(tempArray, 1, 5, 0, 0, false));
        rv.add(new Settings(tempArray, 2, 5, 0, 0, false));
        rv.add(new Settings(tempArray, 3, 5, 0, 0, false));
        tempArray[G] = true;
        rv.add(new Settings(tempArray, 1, 5, 0, 0, false));
        rv.add(new Settings(tempArray, 2, 5, 0, 0, false));
        rv.add(new Settings(tempArray, 3, 5, 0, 0, false));
        tempArray[A] = true;
        rv.add(new Settings(tempArray, 1, 5, 0, 0, false));
        rv.add(new Settings(tempArray, 2, 5, 0, 0, false));
        rv.add(new Settings(tempArray, 3, 5, 0, 0, false));
        tempArray[B] = true;
        rv.add(new Settings(tempArray, 1, 5, 0, 0, false));
        rv.add(new Settings(tempArray, 2, 5, 0, 0, false));
        rv.add(new Settings(tempArray, 3, 5, 0, 0, false));

        return rv;
    }

    private static Vector initOriginalLevels() {
        Vector rv = new Vector();
        boolean[] tempArray = new boolean[12];
        Arrays.fill(tempArray, false);

        tempArray[Eb] = true;
        tempArray[Note.Fs] = true;
        rv.add(new Settings(tempArray, 1, 5, 0, 0, false));
        tempArray[A] = true;
        rv.add(new Settings(tempArray, 1, 5, 0, 0, false));
        tempArray[C] = true;
        rv.add(new Settings(tempArray, 1, 5, 0, 0, false));
        rv.add(new Settings(tempArray, 2, 5, 0, 0, false));
        //rv.add(new Settings(tempArray, 3, 5, 0, 0, false));
        tempArray[E] = true;
        rv.add(new Settings(tempArray, 1, 5, 0, 0, false));
        rv.add(new Settings(tempArray, 2, 5, 0, 0, false));
        //rv.add(new Settings(tempArray, 3, 5, 0, 0, false));
        tempArray[G] = true;
        rv.add(new Settings(tempArray, 1, 5, 0, 0, false));
        rv.add(new Settings(tempArray, 2, 5, 0, 0, false));
        //rv.add(new Settings(tempArray, 3, 5, 0, 0, false));
        tempArray[Bb] = true;
        rv.add(new Settings(tempArray, 1, 5, 0, 0, false));
        rv.add(new Settings(tempArray, 2, 5, 0, 0, false));
        //rv.add(new Settings(tempArray, 3, 5, 0, 0, false));
        tempArray[Cs] = true;
        rv.add(new Settings(tempArray, 1, 5, 0, 0, false));
        rv.add(new Settings(tempArray, 2, 5, 0, 0, false));
        //rv.add(new Settings(tempArray, 3, 5, 0, 0, false));
        tempArray[D] = true;
        rv.add(new Settings(tempArray, 1, 5, 0, 0, false));
        rv.add(new Settings(tempArray, 2, 5, 0, 0, false));
        //rv.add(new Settings(tempArray, 3, 5, 0, 0, false));
        tempArray[F] = true;
        rv.add(new Settings(tempArray, 1, 5, 0, 0, false));
        rv.add(new Settings(tempArray, 2, 5, 0, 0, false));
        //rv.add(new Settings(tempArray, 3, 5, 0, 0, false));
        tempArray[Ab] = true;
        rv.add(new Settings(tempArray, 1, 5, 0, 0, false));
        rv.add(new Settings(tempArray, 2, 5, 0, 0, false));
        //rv.add(new Settings(tempArray, 3, 5, 0, 0, false));
        tempArray[B] = true;
        rv.add(new Settings(tempArray, 1, 5, 0, 0, false));
        rv.add(new Settings(tempArray, 2, 5, 0, 0, false));
        //rv.add(new Settings(tempArray, 3, 5, 0, 0, false));

        return rv;
    }

    /*    class IntArray {
     public int [] array;
     public int size;
        
     public IntArray ( int n ){
     array = new int [n];
     size = 0;
     }
     }*/
} // END of class ExerciseManager

