package jabsolute;

public interface MusicConstants {
	// degrees
	static final int ROOT = 0;
	static final int FLAT_SECOND = 1;
	static final int SECOND = 2;
	static final int FLAT_THIRD = 3;
	static final int THIRD = 4;
	static final int FOURTH = 5;
	static final int SHARP_FOUTH = 6;
	static final int FLAT_FIFTH = 6;
	static final int FIFTH = 7;
	static final int SHARP_FIFTH = 8;
	static final int FLAT_SIXTH = 8;
	static final int SIXTH = 9;
	static final int FLAT_FLAT_SEVENTH = 9;
	static final int FLAT_SEVENTH = 10;
	static final int SEVENTH = 11;
	static final int OCTAVE = 12;
	
	//intervals pending
	
	//Notes
    public static final int C = 0;	 
    public static final int Cs = 1; 
    public static final int Db = 1; 
    public static final int D = 2; 
    public static final int Ds = 3; 
    public static final int Eb = 3; 
    public static final int E = 4; 
    public static final int F = 5; 
    public static final int Fs = 6; 
    public static final int Gb = 6; 
    public static final int G = 7; 
    public static final int Gs = 8; 
    public static final int Ab = 8; 
    public static final int A = 9; 
    public static final int As = 10;
    public static final int Bb = 10; 
    public static final int B = 11; 
    
    public static final String [] NOTE_NAMES = {"C","C#","D","Eb","E","F","F#","G","G#","A","Bb","B"};
	 // ChordTypes,  arrays need to be revised
	 public static final int  [] majorTriad = {ROOT, THIRD, FIFTH};
	 public static final ChordType MAJOR_TRIAD = 
	 new ChordType ( "Major triad", majorTriad , "");
	 
	 public static final int [] minorTriad = {ROOT, FLAT_THIRD, FIFTH };
	 public static final ChordType MINOR_TRIAD =
	 new ChordType ( "Minor triad", minorTriad , "m");
	 
	 public static final int [] diminishedTriad = {ROOT, FLAT_THIRD, FLAT_FIFTH};
	 public static final ChordType DIMINISHED_TRIAD =
	 new ChordType ( "Diminished triad", diminishedTriad, "d");
	 
	 public static final int [] augmentedTriad = {ROOT, THIRD, SHARP_FIFTH};
	 public static final ChordType AUGMENTED_TRIAD =
	 new ChordType ( "Augmented triad", augmentedTriad, "#5");
	 
	 public static final int [] major7 = {ROOT, THIRD, FIFTH, SEVENTH};
	 public static final ChordType MAJOR_7 =
	 new ChordType ( "Major 7", major7, "MA7");
	 
	 public static final int [] dominant7 = {ROOT, THIRD, FIFTH, FLAT_SEVENTH};
	 public static final ChordType DOMINANT_7 =
	 new ChordType ( "Dominant 7", dominant7, "7");

	 public static final int [] minor7 = {ROOT, FLAT_THIRD, FIFTH, FLAT_SEVENTH};
	 public static final ChordType MINOR_7 =
	 new ChordType ( "Minor 7", minor7, "m7");

	 public static final int [] minor7b5 = {ROOT, FLAT_THIRD, FLAT_FIFTH, FLAT_SEVENTH};
	 public static final ChordType MINOR_7b5 =
	 new ChordType ( "Minor 7b5", minor7b5, "m7b5");

	 public static final int [] diminished = {ROOT, FLAT_THIRD, FLAT_FIFTH, FLAT_FLAT_SEVENTH};
	 public static final ChordType DIMINISHED =
	 new ChordType ( "Diminished", diminished, "dim");
	 
	 public static final ChordType [] AVAILABLE_CHORD_TYPES =
	                   { MAJOR_TRIAD, 
	                     MINOR_TRIAD, 
	                     DIMINISHED_TRIAD,
	                     AUGMENTED_TRIAD, 
	                     MAJOR_7, 
	                     DOMINANT_7, 
	                     MINOR_7, 
	                     MINOR_7b5,
	                     DIMINISHED
	                    };
}