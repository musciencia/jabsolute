package jabsolute;

public class ChordType {
	private int [] degrees;
	private String name;
	private String shortName;
	
	public ChordType ( String nm, int [] deg ) {
		name = nm;
		degrees = deg;
	}
	
	public ChordType ( String nm, int [] deg, String sn ) {
		this( nm, deg );
		shortName = sn;
	}
	
	public int [] getDegrees () {
		return (int [])degrees.clone();
	}
	
	public String getName () {
		return name;
	}
	
	public String getShortName () {
		String rv;
		if ( shortName == null ) {
			rv = name;
		}else { rv = shortName; }
		return rv;
	}
	
	public String toString () {
		return name;
	}
}
