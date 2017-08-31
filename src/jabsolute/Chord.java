package jabsolute;

public class Chord implements MusicConstants {
	private int root;
	private ChordType chordType;
	
	// constructor
	public Chord () { this ( C, MAJOR_TRIAD );}
	public Chord (int r, ChordType ct) {
		root = r;
		chordType = ct;
	}
	
/*	public int [] getFirstInversionNotes () {
		// pending for future releases
	}*/
	
	public int[] getNotes () {
		int [] rv = chordType.getDegrees();
		for ( int i = 0 ; i < rv.length ; i++ ){
			rv[i] += root;
		}
		return rv;
	}
	
	public void setRoot (int r) {
		root = r;
	}
	
	public void setChordType ( ChordType ct ) {
		chordType = ct;
	}
	
	public String toString () {
		String rv;
		rv =  NOTE_NAMES[root % 12] + chordType.getShortName();
		return rv;
	}
	
}

