//
//  Selector.java
//  PerfectPitch
//
//  Created by Francisco Fernandez on Sun Feb 29 2004.
//  Copyright (c) 2001 Muscience. All rights reserved.
//
//  Cuidado!
//  noteChooser = piano para seleccionar notas (componente)
//  keysChosen = array que contiene las teclas seleccionadas con tipos boolean
//  notesInvolved = array de enteros que contiene las notas que entran en juego
package jabsolute;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.JPanel; 
import javax.swing.JComboBox; 
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.DefaultComboBoxModel;

public class Selector extends JPanel implements  
            KeyboardMethods,  
            ItemListener,  
            SelectorMethods, 
            MusicConstants
{
    // Fields
    static final int NOTE_CHOOSER = 1; 
    static final int NOTES_PER_CHORD = 2; 
    static final int DURATION = 3; 
    static final int CHORD_TYPE = 4; 
    static final int INSTRUMENT = 5; 
    SelectorMethods selectorMethods; 
    Keyboard noteChooser = new Keyboard (12); 
    JComboBox notesPerChord = new JComboBox(); 
    JComboBox duration = new JComboBox(); 
    JComboBox chordType = new JComboBox(  
    			Preferences.getComboChordTypes()); 
    JComboBox instrument = new JComboBox(); 
    JCheckBox octaveSensitive = new JCheckBox (); 
    JPanel comboPanel = new JPanel (new GridLayout (5, 2, 5, 10 )); 
    //JPanel labelPanel = new JPanel (new GridLayout (5, 1, 5, 10 ));    
    boolean [] keysChosen = new boolean [12];
    
    // Constructor
    public Selector () { 
        this ( new Settings() ); 
    } 
    
    public Selector ( Settings ss ) { 
        setLayout (new FlowLayout (FlowLayout.CENTER, 20, 10) ); 
        noteChooser.setPreferredSize (new Dimension (12*12, 9*12) ); 
        noteChooser.addKeyboardMethods ( this ); 
        noteChooser.setDefaultCapable(false);
        //add( labelPanel );
        add( comboPanel );
        add( noteChooser ); 
        
        /*labelPanel*/comboPanel.add(new JLabel("Notes per chord"));
        comboPanel.add(notesPerChord);
        /*labelPanel*/comboPanel.add(new JLabel("Time (minutes)"));
        comboPanel.add(duration);
        /*labelPanel*/comboPanel.add(new JLabel("Chord type"));
        comboPanel.add(chordType);
        /*labelPanel*/comboPanel.add(new JLabel("Instrument"));
        comboPanel.add(instrument);
        /*labelPanel*/comboPanel.add(new JLabel ("Octave sensitive"));
        comboPanel.add(octaveSensitive);
            
        for (int i = 1; i <= 4; i++) { notesPerChord.addItem (new Integer (i) );}
        for (int i = 1; i <= 20; i++) { duration.addItem (new Integer (i) ); }
        instrument.addItem ( "Piano" );
        setSettings ( ss );
        instrument.addItemListener ( this );
        addSelectorMethods ( this );
        noteChooser.setKeyStrokes(Preferences.getKeyStrokes());
    }
    
    // Public Methods 
    public void addSelectorMethods ( SelectorMethods sm) { 
        selectorMethods = sm; 
    } 
     
    public void setEnabled (boolean b) { 
        noteChooser.setEnabled(b); 
        notesPerChord.setEnabled(b); 
        duration.setEnabled(b); 
        chordType.setEnabled(b); 
        instrument.setEnabled(b); 
        octaveSensitive.setEnabled (b); 
    } 
    
    public void setEnabled ( boolean nc, boolean npc, boolean du,  
                             boolean ct, boolean in, boolean os) { 
        noteChooser.setEnabled (nc); 
        notesPerChord.setEnabled (npc); 
        duration.setEnabled (du); 
        chordType.setEnabled (ct); 
        instrument.setEnabled (in); 
        octaveSensitive.setEnabled (os); 
    } 
    
    public void setEnabled ( int c, boolean b ) { // ( component, false or true )
        switch ( c ) { 
            case 1: 
                noteChooser.setEnabled (b); 
                break; 
                
            case 2: 
                notesPerChord.setEnabled (b); 
                break; 
            
            case 3: 
                duration.setEnabled (b); 
                break; 
            
            case 4: 
                chordType.setEnabled (b); 
                break; 
            
            case 5: 
                instrument.setEnabled (b); 
                break; 
                
            case 6: 
                octaveSensitive.setEnabled (b); 
                break; 
        } 
    } 
     
    public void setSettings ( Settings s ) { 
        setKeysChosen ( s.getKeysChosen() ); 
        notesPerChord.setSelectedIndex ( s.getNotesPerChord() - 1); 
        duration.setSelectedIndex (s.getDuration() - 1); 
        chordType.setSelectedIndex (s.getChordType() ); 
        instrument.setSelectedIndex (s.getInstrument() ); 
        octaveSensitive.setSelected (s.isOctaveSensitive()); 
    } 
    
    public void reset() {
        resetNoteChooser();
        notesPerChord.setSelectedIndex(0);
        duration.setSelectedIndex (4);
        chordType.setSelectedIndex(0);
        instrument.setSelectedIndex(0);
        octaveSensitive.setSelected(false);
    }
    
    public void setKeysChosen ( boolean [] kc ) {
        Key tempKey = null;
        for (int i = 0 ; i <  (kc.length) ; i++ ) {
            int j = i % 12;
            tempKey = noteChooser.getKey(j);
            keysChosen[j] = kc[j];
            if ( kc[j] ) {
                noteChooser.setKeyColor ( tempKey, Color.lightGray );
            } else {
                noteChooser.setKeyColor ( tempKey, tempKey.getDefaultColor());
            }
        }  
    }
    
    public void setInstruments( Object [] items) {
        //comboPanel.remove ( 3 );
        //instrument = new JComboBox ( items );
        instrument.setModel (new DefaultComboBoxModel (items));
        //comboPanel.add(instrument, 3);
        comboPanel.validate();
        instrument.addItemListener(this);
        //instrument.validate();
    } 
    
    public Settings getSettings () { 
        Settings rv = new Settings (  
            keysChosen, 
            notesPerChord.getSelectedIndex() + 1, 
            duration.getSelectedIndex() + 1, 
            chordType.getSelectedIndex(), 
            instrument.getSelectedIndex(), 
            octaveSensitive.isSelected() 
            ); 
                                    
        return rv; 
    } 
    
    public ChordType getChordType () { 
    		ChordType rv; 
		   Object item = chordType.getSelectedItem(); 
		   if ( item instanceof ChordType ) {  
		   	rv = (ChordType) item;  
			} else  if ( item.toString().equals("Random")) { 
				int idx = (int)(Math.random() * (chordType.getItemCount()-2)); 
				idx = idx + 1; 
				item = chordType.getItemAt( idx ); 
				rv = (ChordType) item; 
			} else { rv = null ; } 
			return rv; 
    }
    
    public void resetNoteChooser () { 
        Key tempKey;
        Color tempColor;
        for (int i = 0 ; i < keysChosen.length ; i++ ) {
            tempKey = noteChooser.getKey(i);
            tempColor = tempKey.getDefaultColor();
            noteChooser.setKeyColor (tempKey, tempColor);
            keysChosen [i] = false;
        }
    }
    
    // KeyboardMethods 
    public void keyPressed ( Key k ) { 
        Color color = k.getColor(); 
        Color defaultColor = k.getDefaultColor(); 
        if ( color == Color.lightGray ) { 
            noteChooser.setKeyColor (k, defaultColor); 
            keysChosen [k.getKeyNum()] = false; 
        } else { 
            noteChooser.setKeyColor (k, Color.lightGray); 
            keysChosen [k.getKeyNum()] = true; 
        } 
    } 
    
    public void keyReleased ( Key k ) { 
    } 
    
    public void tuneUp ( Key k ) { };
    
    public void tuneDown ( Key k ) { };

    
    // SelectorMethods
    public void instrumentSelected() { 
    } 
    
    // ItemListener
    public void itemStateChanged ( ItemEvent e) { 
        selectorMethods.instrumentSelected();  
    } 
    

} // END of class Selector 
