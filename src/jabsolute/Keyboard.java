package jabsolute;

//import javax.swing.*;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Arrays;
import java.util.Vector;
import javax.sound.midi.Instrument;
import javax.sound.midi.MidiChannel;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Soundbank;
import javax.sound.midi.Synthesizer;
import javax.swing.JButton;
import javax.swing.Timer;

//import java.awt.*;
//import java.awt.event.*;
//import java.util.*;
//import javax.sound.midi.*;
public class Keyboard extends JButton implements KeyboardMethods//, FocusListener
{

    // Private Fields
    private static Keyboard lastInstance = null; // temporal
    private boolean enabled = true;
    private KeyboardMethods keyboardMethods;
    private KeyResponder keyResponder = new KeyResponder();
    private int numberOfKeys; // Number of keys in the keyboard
    private int firstKey;  // C = 0, C# = 2, D = 3, D# = 4, ... B = 11
    private Vector keys;
    private int lastKeyPressed = 0;
    private Synthesizer synth = null;
    private MidiChannel currentChannel;
    private MidiChannel[] channels;
    private int velocity = 80;
    private int[] keyStrokes = initKeyStrokes();
    int delay = 1000; // miliseconds
    Timer delayedStopAll = new Timer(delay, new DelayedStopAll());

    // private Key lastKeyPressed; // a prueba ...
    // Constructors //////////////////////////////////////////
    public Keyboard() {
        this(12, 0);
    }

    public Keyboard(int nok) { // number of keys
        this(nok, 0);
    }

    public Keyboard(int nok, int fk) { // number of kesy, first key
        numberOfKeys = nok;
        firstKey = fk % 12;
        setPreferredSize(new Dimension(nok * 12, 9 * 12)); // temporal
        setMinimumSize(new Dimension(nok * 3, 27));
        createKeys();
        addMouseListener(new KeyboardListener());
        addKeyListener(keyResponder);
        addKeyboardMethods(this);
        lastInstance = this; // temporal
        delayedStopAll.setRepeats ( false );
        // addFocusListener ( this );
        // runDefaultMidiSettings ();
    }

    // Public methods ////////////////////////////////////////
    public static Keyboard getLastInstance() {
        return lastInstance;
    }

    public void addKeyboardMethods(KeyboardMethods keyMeth) {
        keyboardMethods = keyMeth;
    }

    // a prueba ...
    //public Key lastKeyPressed () { return lastKeyPressed; }
    public Key getKey(int index) {
        return (Key) keys.get(index % keys.size());
    }

    public Key getLastKeyPressed() {
        return (Key) keys.get(lastKeyPressed);
    }

    @Override
    public void setEnabled(boolean en) {
        super.setEnabled(en);
        enabled = en;
    }

    public void setKeyColor(Key k, Color c) {
        k.setColor(c);
        this.repaint((int) k.getX(), (int) k.getY(), (int) k.getWidth(), (int) k.getHeight());
    }

    public void setKeyColor(int i, Color c) {
        this.setKeyColor(this.getKey(i), c);
    }

    public void setKeyColor(int[] chord, Color c) {
        for (int i = 0; i < chord.length; i++) {
            setKeyColor(chord[i], c);
        }
    }

    public void setKeyColor(Chord chord, Color c) {
        setKeyColor(chord.getNotes(), c);
    }

    public void setKeyDefaultColor(Key k) {
        k.setDefaultColor();
        this.repaint((int) k.getX(), (int) k.getY(), (int) k.getWidth(), (int) k.getHeight());
    }

    public void setKeyDefaultColor(int i) {
        this.setKeyDefaultColor(this.getKey(i));
    }

    public void setKeyDefaultColor(int[] chord) {
        for (int i = 0; i < chord.length; i++) {
            this.setKeyDefaultColor(chord[i]);
        }
    }

    public void setKeyDefaultColor(Chord chord) {
        setKeyDefaultColor(chord.getNotes());
    }

    public void setDefaultColors() {
        for (int i = 0; i < numberOfKeys; i++) {
            setKeyDefaultColor(i);
        }
    }

    public void setCurrentChannel(int cc) {
        currentChannel = channels[cc];
    }

    public void setKeyStrokes(int[] ks) {
        if (ks.length == 12) {
            keyStrokes = ks;
        }
    }

    public void repaint(Key k) {
        this.repaint((int) k.getX(), (int) k.getY(), (int) k.getWidth(), (int) k.getHeight());
    }

    public void repaint(int ik) {
        this.repaint(this.getKey(ik));
    }

    public void repaint(int[] chord) {
        for (int i = 0; i < chord.length; i++) {
            this.repaint(chord[i]);
        }
    }

    public void repaint(Chord chord) {
        repaint(chord.getNotes());
    }

    @Override
    public void paint(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        adjustKeys();
        Key tempKey;
        // paint white keys
        for (int i = 0; i < numberOfKeys; i++) {
            tempKey = (Key) keys.get(i);
            if (tempKey.isWhite()) {
                g2.setColor(tempKey.getColor());
                g2.fill(tempKey);
                g2.setColor(Color.black);
                g2.draw(tempKey);
            }
        }
        // paint black keys 
        for (int i = 0; i < numberOfKeys; i++) {
            tempKey = (Key) keys.get(i);
            if (tempKey.isBlack()) {
                g2.setColor(tempKey.getColor());
                g2.fill(tempKey);
                g2.setColor(Color.black);
                g2.draw(tempKey);

            }
        }
    } // end of method paint 

    public void runDefaultMidiSettings() {
        try {
            synth = MidiSystem.getSynthesizer();
            if (!(synth.isOpen())) {
                synth.open();
            }
        } catch (MidiUnavailableException e) {
            System.err.println("Midi device unavailable");
        }

        channels = synth.getChannels();
        currentChannel = channels[0];
        ///////// LoadInstruments //////////
        Soundbank defaultSoundbank = synth.getDefaultSoundbank();
        Instrument[] instruments = defaultSoundbank.getInstruments();
        // load no more than 30 instruments
        for (int j = 0; (j < instruments.length) && (j <= 30); j++) {
            synth.loadInstrument(instruments[j]);
            //System.out.println(instruments[j] + " loaded");
        }
        //synth.loadAllInstruments(defaultSoundbank);
    }

    public String[] getInstrumentNames() {
        Instrument[] instruments = synth.getLoadedInstruments();
        String[] rv = new String[instruments.length];
        for (int i = 0; i < instruments.length; i++) {
            rv[i] = instruments[i].getName();
        }
        return rv;
    }

    public Instrument[] getInstruments() {
        return synth.getLoadedInstruments();
    }

    public Synthesizer getSynthesizer() {
        return synth;
    }

////////////////////  PLAY  //////////////////    

    public void play(Key k) {
        if (!(synth == null)) {
            if (synth.isOpen()) {
                currentChannel.noteOn(k.getMidiNote(), velocity);
            }
        }
    }

    public void play(int note) {
        if (!(synth == null)) {
            if (synth.isOpen()) {
                // en un futuro agregar inice de la nota = 36
                currentChannel.noteOn(note + 36, velocity);
            }
        }
    }

    public void play(Chord chord) {
        play(chord.getNotes());
    }

    public void play(int[] chord) {
        if (!(synth == null)) {
            if (synth.isOpen()) {
                for (int i = 0; i < chord.length; i++) {
                    this.play(chord[i]);
                }
            }
        }
    }
    
    // Modify function to stop only the notes involved
    // Now it's implementing so that all notes stop after 
    // one second
    // Change function -> play(key, dur)
    public void playOneSecond(Key k){
        play(k);
        delayedStopAll.restart();
    }
    
    public void playOneSecond(int note){
        play(note);
        delayedStopAll.restart();
    }

    public void playOneSecond(int [] chord) {
        play(chord);
        delayedStopAll.restart();
    }
    
    public void playOneSecond(Chord chord){
        play(chord);
        delayedStopAll.restart();
    }
    
///////////////////////// STOP ///////////////////////
    public void stop(Key k) {
        if (!(synth == null)) {
            if (synth.isOpen()) {
                currentChannel.noteOff(k.getMidiNote(), velocity);
            }
        }
    }

    public void stop(int note) {
        if (!(synth == null)) {
            if (synth.isOpen()) {
                currentChannel.noteOff(note + 36, velocity);
            }
        }
    }

    public void stop(int[] chord) {
        if (!(synth == null)) {
            if (synth.isOpen()) {
                for (int i = 0; i < chord.length; i++) {
                    this.stop(chord[i]);
                }
            }
        }
    }

    public void stop(Chord chord) {
        stop(chord.getNotes());
    }

    public void stopAll() {
        if (!(synth == null)) {
            if (synth.isOpen()) {
                currentChannel.allNotesOff();
                currentChannel.allSoundOff();
            }
        }
    }

    public void closeSynth() {
        if (!(synth == null)) {
            if (synth.isOpen()) {
                //System.out.println ("synth closed");
                synth.close();
            }
        }
    }

    public void programChange(int prog) {
        //System.out.println ("program: " + prog);
        currentChannel.programChange(prog);
    }

    // Implementation of KeboardMethods //////////////////////////////////
    @Override
    public void keyPressed(Key k) {
    }

    ;  
    
    @Override
    public void keyReleased(Key k) {
    }

    ;  
    
    @Override
    public void tuneUp(Key k) {
    }

    ;
    
    @Override
    public void tuneDown(Key k) {
    }

    ;
    
    
    // Private methods //////////////////////////////////////
    private void adjustKeys() { // resizes all the keys according to the current component size
        for (int i = 0; i < numberOfKeys; i++) {
            adjustKey(i);
        }
    }

    private void adjustKey(int index) { // resizes the key according to the current component size
        Key tempKey = (Key) keys.get(index);
        int kn = tempKey.getKeyNum();
        double compW = (double) (this.getWidth() - 2); // - 2 para compensar la ultima tecla
        double compH = (double) (this.getHeight()) - 1; // -1 de compensacion
        double bk = compW / numberOfKeys;
        double octave = bk * 12.0;
        double w1 = (5.0 * bk) / 3.0;
        double w2 = (7.0 * bk) / 4.0;
        double bkh = compH * (2.0 / 3.0);
        double keyX = 0;
        double keyY = 0;
        double keyWidth = 0;
        double keyHeight = 0;
        int compensation = 0;

        switch (kn % 12) {
            case 1:
            case 3:
            case 6:
            case 8:
            case 10:
                keyX = kn * bk;
                keyHeight = bkh;
                keyWidth = bk;
                break;

            case 0:
            case 2:
            case 4:
                keyX = ((kn % 12) / 2) * w1;
                keyX = keyX + (((int) (kn / 12)) * octave);
                keyHeight = compH;
                keyWidth = w1 + 1;
                break;

            case 5:
            case 7:
            case 9:
            case 11:
                keyX = (3 * w1) + ((((kn % 12) + 1) / 2) - 3) * w2;
                keyX = keyX + (((int) (kn / 12)) * octave);
                keyHeight = compH;
                keyWidth = w2 + 1;
                break;
        } // end switch

        // calculate compensation
        compensation = (int) (Math.round((firstKey * bk)));

        tempKey.setBounds((int) (Math.round(keyX)) - compensation,
                (int) (Math.round(keyY)),
                (int) (Math.round(keyWidth)),
                (int) (Math.round(keyHeight)));
    } // end method adjust key 

    private int getKeyIndex(Point p) {
        Key tempKey;

        // Check black keys first
        for (int i = 0; i < numberOfKeys; i++) {
            tempKey = (Key) keys.get(i);
            if (tempKey.isBlack()) {
                if (tempKey.contains(p)) {
                    return i;
                }
            }
        }
        for (int i = 0; i < numberOfKeys; i++) {
            tempKey = (Key) keys.get(i);
            if (tempKey.contains(p)) {
                return i;
            }
        }
        return -1;
    }

    private void createKeys() {
        keys = new Vector(numberOfKeys);
        for (int i = 0; i < numberOfKeys; i++) {
            keys.add(i, new Key(i + firstKey));
        }
    }

    private int[] initKeyStrokes() {
        int[] rv = new int[12];
        rv[0] = KeyEvent.VK_A;
        rv[1] = KeyEvent.VK_W;
        rv[2] = KeyEvent.VK_S;
        rv[3] = KeyEvent.VK_E;
        rv[4] = KeyEvent.VK_D;
        rv[5] = KeyEvent.VK_F;
        rv[6] = KeyEvent.VK_T;
        rv[7] = KeyEvent.VK_G;
        rv[8] = KeyEvent.VK_Y;
        rv[9] = KeyEvent.VK_H;
        rv[10] = KeyEvent.VK_U;
        rv[11] = KeyEvent.VK_J;
        return rv;
    }

///////Implementation of FocusListener ////////////
/*	 public void focusGained(FocusEvent e) { }
	 
     public void focusLost(FocusEvent e) {
     System.out.println ("Keyboard focusLost");
     if (enabled) {
     keyboardMethods.keyReleased ((Key) keys.get (lastKeyPressed));
     }
     }*/
////// Nested classes ///////////////////////////////////////  
////// DelayedNoteOffListener //////////////    
    class DelayedStopAll implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            stopAll();
        }
    }

/////// KeyboardListener class begins ///////////////////////
    class KeyboardListener extends MouseAdapter {

        public void mousePressed(MouseEvent e) {
            if (enabled) {
                lastKeyPressed = getKeyIndex(e.getPoint());
                keyboardMethods.keyPressed((Key) keys.get(lastKeyPressed));
            }
        }

        public void mouseReleased(MouseEvent e) {
            if (enabled) {
                keyboardMethods.keyReleased((Key) keys.get(lastKeyPressed));
            }
        }

        public void mouseExited(MouseEvent e) {
            if (enabled) {
                keyboardMethods.keyReleased((Key) keys.get(lastKeyPressed));
            }
        }
    }// end KeyboardListener
////////// KeyListener begins ///////////////////

    class KeyResponder implements KeyListener {

        private boolean[] keyStatus = new boolean[12]; // KeyStatus prevents the program from
        // playing many times a note when holding a key
        // for a long time
        private int[] extras = new int[12];
        final int SHIFT_DOWN = -12;
        final int ALT_DOWN = 12;
        final int SHIFT_AND_ALT_DOWN = -24;

        public KeyResponder() {
            Arrays.fill(keyStatus, false);
            Arrays.fill(extras, 0);
        }

        public void keyPressed(KeyEvent e) {
            //System.out.println ("keyPressed( KeyEvent e ) called");
            if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                if (!keyStatus[lastKeyPressed % 12]) {
                    lastKeyPressed = (lastKeyPressed + 1) % numberOfKeys;
                    keyboardMethods.keyPressed((Key) keys.get(lastKeyPressed));
                    keyStatus[lastKeyPressed % 12] = true;
                }
            }

            if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                if (!keyStatus[lastKeyPressed % 12]) {
                    lastKeyPressed = (lastKeyPressed - 1) % numberOfKeys;
                    lastKeyPressed = ((lastKeyPressed < 0) ? numberOfKeys - 1 : lastKeyPressed);
                    keyboardMethods.keyPressed((Key) keys.get(lastKeyPressed));
                    keyStatus[lastKeyPressed % 12] = true;
                }
            }

            if (e.getKeyCode() == KeyEvent.VK_UP) {
                keyboardMethods.tuneUp((Key) keys.get(lastKeyPressed));
            }

            if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                keyboardMethods.tuneDown((Key) keys.get(lastKeyPressed));
            }

            if (!e.isControlDown()) {
                int keyIndex = -1;
                int tempIndex = -1;
                int extraValue = 0;
                int keyCode = e.getKeyCode();

                for (int i = 0; i < 12; i++) {
                    if (keyStrokes[i] == e.getKeyCode()) {
                        tempIndex = i;
                        break;
                    }
                }

                if (!(tempIndex < 0) && (!keyStatus[tempIndex])) {
                    if (e.isShiftDown()) {
                        extraValue = SHIFT_DOWN;
                    }
                    if (e.isAltDown()) {
                        extraValue = ALT_DOWN;
                    }
                    if (e.isShiftDown() && e.isAltDown()) {
                        extraValue = SHIFT_AND_ALT_DOWN;
                    }
                    keyIndex = tempIndex + extraValue + 24;
                    if (enabled) {
                        lastKeyPressed = keyIndex % numberOfKeys;  // a prueba ...              
                        keyboardMethods.keyPressed((Key) keys.get(keyIndex % numberOfKeys));
                        keyStatus[tempIndex] = true;
                        extras[tempIndex] = extraValue;
                    }
                }
            }
        }// End keyPressed 

        public void keyReleased(KeyEvent e) {
            //System.out.println ("keyReleased( KeyEvent e ) called");

            if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                keyboardMethods.keyReleased((Key) keys.get(lastKeyPressed));
                keyStatus[lastKeyPressed % 12] = false;
            }

            if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                keyboardMethods.keyReleased((Key) keys.get(lastKeyPressed));
                keyStatus[lastKeyPressed % 12] = false;
            }

            if (e.getKeyCode() == KeyEvent.VK_UP) {
            }

            if (e.getKeyCode() == KeyEvent.VK_DOWN) {
            }

            if (!e.isControlDown()) {
                int keyIndex = -1;
                int tempIndex = -1;
                int extraValue = 0;
                int keyCode = e.getKeyCode();

                for (int i = 0; i < 12; i++) {
                    if (keyStrokes[i] == e.getKeyCode()) {
                        tempIndex = i;
                        break;
                    }
                }

                if (!(tempIndex < 0)) {
                    if (e.isShiftDown()) {
                        extraValue = - 12;
                    }
                    if (e.isAltDown()) {
                        extraValue = 12;
                    }
                    if (e.isShiftDown() && e.isAltDown()) {
                        extraValue = -24;
                    }
                    keyIndex = tempIndex + extras[tempIndex] + 24;
                    if (enabled) {
                        keyboardMethods.keyReleased((Key) keys.get(keyIndex % numberOfKeys));
                        keyStatus[tempIndex] = false;
                        extras[tempIndex] = 0;
                    }
                }
            }
        } // End key released

        @Override
        public void keyTyped(KeyEvent e) {
            //System.out.println("KeyTyped:" + e);
        }
    } // end KeyResponder 

} // end class Keyboard

