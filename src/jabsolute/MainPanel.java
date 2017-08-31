//
//  MainPanel.java
//  JAbsolute
//
//  Created by Francisco Fernandez on Fri Feb 27 2004.
//  Copyright (c) 2001-2015. All rights reserved.
//

package jabsolute;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.util.Vector;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;



public class MainPanel extends JPanel 
{
    // Fields
    //private static MainPanel lastInstance = null; /// temporal
    JPanel indicadores = new JPanel(new GridLayout(2,1,20,20));
    Chronometer reloj = new Chronometer ();
    Score score = new Score();
    JPanel botones = new JPanel (new BorderLayout(20,20));
    Controls controles = new Controls(); 
    Selector selector = new Selector();
    Keyboard piano = new Keyboard(48);
    Vector methods = new Vector();
    GeneralMethods mode = null;    // a prueba /////// pendiente null
 //   private boolean testAnswerIsOn = false;
   // final static int TEST = 0;  ////////// moved to ProgramConstants
   // final static int PRACTICE = 1;
   // final static int PLAY = 2;
    //final static int N_MODES = 3; // number of modes available 
 
    // Constructor
    public MainPanel () {
        methods.add(new TestMethods());
        methods.add(new PracticeMethods());
        methods.add(new PlayMethods());
        
        // adding panels
        setLayout ( new BorderLayout(20,20) );
        add(piano, BorderLayout.NORTH);
        add(indicadores, BorderLayout.EAST);
        add(botones, BorderLayout.CENTER);
        
        // indicadores
        indicadores.add(reloj);
        reloj.setBorder(new TitledBorder ("Time"));
        indicadores.add(score);
        score.setBorder(new TitledBorder ("Score"));        
        
        // botones
        botones.add(controles, BorderLayout.CENTER);
        botones.add(selector, BorderLayout.SOUTH);
        selector.setBorder(new TitledBorder("Settings"));
        controles.setBorder(new TitledBorder ("Controls"));
        
        piano.setDefaultCapable(false);        
        piano.runDefaultMidiSettings ();      
        piano.setKeyStrokes(Preferences.getKeyStrokes());
        selector.setInstruments (piano.getInstrumentNames());  
        
        //lastInstance = this; // temporal
    } // END Constructor
    
    public boolean timeIsOn() {
         return reloj.isOn();
    }
    
    public void timeReset () {
         reloj.reset();
    }
    
    public void setMode ( int m ) {
        if ( m >= 0) {
            piano.stopAll();
            selector.reset();
            //System.out.println("setMode() OK before methods.get()");
            //System.out.println("m =" + m + " methods.size()=" + methods.size());
            mode = (GeneralMethods) methods.get(m % methods.size());
            mode.reset();
            piano.addKeyboardMethods( mode );
            reloj.addChronometerMethods ( mode );
            controles.addControlsMethods ( mode );       
            selector.addSelectorMethods ( mode );
        }
    }
    
    public int getMode () {
         return methods.indexOf(mode);
    }
    
    @Override
    public void setEnabled ( boolean b ) {
        controles.setEnabled (b);
        piano.setEnabled (b);
        selector.setEnabled (b);
    }
    
    // temporal
    //public static MainPanel getLastInstance() {
    //    return lastInstance;
    //}
    
/////// Nested classes
//////////////////////////////////////////////////////////
////////      GENERAL METHODS    /////////////////////////
/////////////////////////////////////////////////////////
    class PracticeMethods extends GeneralMethods {
        public PracticeMethods () {
            super();
        }
        
        @Override
        public void reset () {
            JAbsolute.getLastInstance().setTitle ("PRACTICE MODE - Current user: " + 
                   UserManager.getCurrentUser());
            piano.setEnabled (false);
            piano.setDefaultColors();
            selector.setEnabled (true);
            //selector.resetNoteChooser();
            reloj.reset();
            score.reset();
            controles.setEnabled (true, false, false);
            JAbsolute.fileMenu.setEnabled(true);
        }
        
////////KeyboardMethods                 
        @Override
        public void keyPressed ( Key k ) {
            testAnswer ( k );
        }

        @Override
        public void keyReleased ( Key k ) {
            k.useDefaultColor();
            piano.repaint(k);
            // antes: // piano.setKeyColor ( k, k.getDefaultColor() ); 
        }
        
        @Override
        public void tuneUp ( Key k ) { };
    
        @Override
        public void tuneDown ( Key k ) { };

////////ChronometerMethods        
        @Override
        public void timeExpired () { 
            //System.out.println("timeIsOn() = " + timeIsOn());
            delayedAsk.stop();
            MessageManager.showResults(score, MessageManager.PLAIN);
            controles.setEnabled (true,false,false);
            piano.setEnabled(false);
            piano.stopAll();   /// cambiar por channel
            piano.setDefaultColors();
            //selector.resetNoteChooser();
            selector.setEnabled (true);
            score.reset();
            JAbsolute.fileMenu.setEnabled(true);
        }  

////////ControlsMethods
        @Override
        public void runStart () {
        //    PerfectPitchX.fileMenu.setEnabled(false);
            selector.setEnabled(false);
            Settings settings = selector.getSettings();
            controles.setEnabled (false, false, true);
            
            if (settings.getNotesInvolved().length > 0 ) {
                ExerciseManager.setCurrentSettings ( settings );
            
                //selector.setEnabled(false);
                int t = settings.getDuration();
                // controles.start.setEnabled(false);
                reloj.setStopAt( t * 60 ); // t * 60;
                reloj.start();
                score.reset();
                                
                ask();
                controles.setEnabled(false, true, true);   
                piano.requestFocus(); // temporal    
            } else { 
                JOptionPane.showMessageDialog(selector, "Choose at least one note");
                reset();
            }      
        } // END of runStart()
        
        @Override
        public void runRepeat() {
            repeat();
            piano.requestFocus();
        }
        
        @Override
        public void runAnswer () {
            if ( !(delayedAsk.isRunning () ) ) {
                 piano.stop(ExerciseManager.getLast());
                 piano.playOneSecond(ExerciseManager.getTestNote());
                 piano.setKeyColor(ExerciseManager.getTestNote(), 
                                   ProgramConstants.ANSWER_COLOR);
                 score.wrong();
                 piano.requestFocus();
            }
        }

        // SelectorMethods
        @Override
        public void instrumentSelected () {
            Settings tempSettings = selector.getSettings();
            piano.programChange(tempSettings.getInstrument());
            //System.out.println ("piano.requestFocus()");
         //   piano.requestFocus(); /////temporal
        }

        
////////////////// Helper Methods for GeneralMethods    
        @Override
        protected void repeat() {
                piano.requestFocus();
                piano.stopAll(); // antes: //(ExerciseManager.getLast());
                piano.playOneSecond(ExerciseManager.getLast());
                //piano.play(ExerciseManager.getLast());
                //FranUtilities.pauseOneSecond();
                //piano.stopAll();
        }
    
        @Override
        protected void ask() {
            piano.requestFocus();
        //    piano.setEnabled(false);
            piano.stopAll(); // antes: //(ExerciseManager.getLast());   /// cambiar por channel
            piano.setDefaultColors(); //antes: //(ExerciseManager.getLast());
            int [] notes = ExerciseManager.getNew();
            piano.playOneSecond(notes);
            //piano.play(notes);
            //FranUtilities.pauseOneSecond();
            //piano.stopAll();
            ExerciseManager.resetCount();
            if (reloj.isOn())
                piano.setEnabled(true);
        }
        
        @Override
        protected void testAnswer ( Key k ) {
            if ( !(delayedAsk.isRunning () ) ) {
         //       testAnswerIsOn = true;
                int testNote = ExerciseManager.getTestNote();
                if (ExerciseManager.answerIsRight( k.getKeyNum() ) ) {
                    Key tempKey = piano.getKey (testNote);
                    tempKey.setDefaultColor (ProgramConstants.RIGHT_COLOR);
                    tempKey.useDefaultColor();
                    piano.repaint(tempKey);
                    // antes: // piano.setKeyColor (testNote, ProgramConstants.RIGHT_COLOR);
                    if ( (ExerciseManager.getCount() >= 
                              ExerciseManager.getLast().length) ||
                              (ExerciseManager.getSimul()== 1) ) {
                         score.right();
                         delayedAsk.restart (); // antes start();
                    } 
                                
                } else {
                    k.setColor (ProgramConstants.WRONG_COLOR);
                    piano.repaint(k);
                    // antes: //piano.setKeyColor (k, ProgramConstants.WRONG_COLOR);
                    score.wrong();
                    repeat();
                }
                // testAnswerIsOn = false;
            }
        } // END of testAnswer()
                    
    } // END of class PracticeMethods 

//////////////////// 	PlayMethods ///////////////////
    class PlayMethods extends GeneralMethods {
        int currentColorIndex = 0;
        Color [] colorPalette = new Color[30];
        int colorSteps = 51; 
    	  Chord [] chordMemory = new Chord [12];
        // Construcor
        public PlayMethods () {
            super();
            initChordMemory ();
            initColorPalette ();
        }
        
        private void initChordMemory () {
        		for ( int i = 0 ; i < chordMemory.length ; i++) {
		      	chordMemory [i] = new Chord();
		      }
        }
        
        private void initColorPalette () {
        //System.out.println ("initColorPalette() called");
           int r = 0;
           int g = 255;
           int b = 255;
           int count = 0;
           
           // decrease green
           for (  int i = 5 ; i >= 0 ; i-- ) {
              g = i * 51;
              colorPalette[count] = new Color ( r, g, b );
              count++;
           } // at the end of the loop ( 0, 0, 255)
           
           // increase red
           for (  int i = 1 ; i < 6 ; i++) {
              r = i * 51;
              colorPalette[count] = new Color ( r, g, b );
              count++;
           } // at the end of the loop ( 255, 0 , 255)

           // decrease blue
           for (  int i = 4 ; i >= 0 ; i-- ) {
              b = i * 51;
              colorPalette[count] = new Color ( r, g, b );
              count++;
           } // at the end of the loop ( 255, 0, 0)
           
           // increase green
           for (  int i = 1 ; i < 6 ; i++) {
              g = i * 51;
              colorPalette[count] = new Color ( r, g, b );
              count++;
           } // at the end of the loop ( 255, 255 , 0)
           
           // decrease red
           for (  int i = 4 ; i >= 0 ; i-- ) {
              r = i * 51;
              colorPalette[count] = new Color ( r, g, b );
              count++;
           } // at the end of the loop ( 0, 255, 0)
           
           // increase blue
           for (  int i = 1 ; i < 5 ; i++) {
              b = i * 51;
              colorPalette[count] = new Color ( r, g, b );
              count++;
           } // at the end of the loop ( 0, 255 , 204)
           
        } // end initColorPalette
        
        @Override
        public void reset() {
            JAbsolute.getLastInstance().setTitle ("PLAY MODE - Current user: " +  
                   UserManager.getCurrentUser());
            controles.setEnabled(false);
            selector.setEnabled(false, false, false, true, true, false); 
            selector.resetNoteChooser();
            reloj.reset();
            score.reset();
            piano.setDefaultColors();
            piano.setEnabled (true);
        }
    
        // Keyboard methods
        @Override
        public void keyPressed ( Key k ) {
        		ChordType ct = selector.getChordType();
		      int keyNum = k.getKeyNum(); 
		      
        		if ( ct == null ) {	      
            	piano.play( k );  
	            k.setColor(ProgramConstants.PLAY_COLOR);          
	            piano.repaint(k);
	            // antes : // piano.setKeyColor(k,Color.blue);
            } else {
            	chordMemory[keyNum % 12].setChordType ( ct );
	            chordMemory[keyNum % 12].setRoot ( keyNum );
               piano.play ( chordMemory[keyNum % 12] ); 
               piano.setKeyColor ( chordMemory[keyNum % 12], ProgramConstants.PLAY_COLOR );
            }
        } 
    
        @Override
        public void keyReleased ( Key k ) {
        		ChordType ct = selector.getChordType();
		      int keyNum = k.getKeyNum();
		      
        		if ( ct == null ) {	      
            	piano.stop( k );
	            k.useDefaultColor ();
            	piano.repaint(k); 
            } else {
               piano.stop ( chordMemory[keyNum % 12] ); 
              	piano.setKeyDefaultColor ( chordMemory[keyNum % 12] );
            }
        } 

        @Override
        public void tuneUp ( Key k ) {
            // there are 30 colors available but 15 is enough,
            // if you want more change the following statement to 
            //                  ( currentColorIndex + 1 ) 
            currentColorIndex = ( currentColorIndex + 2 ) % colorPalette.length;            
            k.setDefaultColor(colorPalette[currentColorIndex]);
            k.useDefaultColor();
            piano.repaint (k);
        };
    
        @Override
        public void tuneDown ( Key k ) { 
            currentColorIndex = ( currentColorIndex - 2 ) % colorPalette.length;   
            currentColorIndex = ( currentColorIndex < 0 ? colorPalette.length - 1 : 
                                  currentColorIndex );         
            k.setDefaultColor(colorPalette[currentColorIndex]);
            k.useDefaultColor();
            piano.repaint (k);
        };
        
 
        // Chronometer Methods
        
        // Controls Methods
        
        // SelectorMethods
        @Override
        public void instrumentSelected () {
            Settings tempSettings = selector.getSettings();
            piano.programChange(tempSettings.getInstrument());
            piano.requestFocus();
        }
    }
    
///////////////////////// TestMethods /////////////////////
    class TestMethods extends PracticeMethods {
        // Construcor
        public TestMethods () {
            super();
        }
        
        @Override
        public void reset () {     
            User tempUsr = UserManager.getCurrentUser();
            JAbsolute.getLastInstance().setTitle (   
                   "TEST MODE - Current user: " + 
                   UserManager.getCurrentUser()+
                   "   Level: " +
                   UserManager.getCurrentUser().getCurrentLevel());
            if ( !(tempUsr == null) ) { 
                selector.setSettings ( ExerciseManager.getLevel( tempUsr.getCurrentLevel()) );
            }
            controles.setEnabled(true, false, false);/// temporal methods
            selector.setEnabled( false ); 
            // Give the option to change intrument in test mode
            selector.setEnabled(Selector.INSTRUMENT, true);
            piano.setDefaultColors();
            piano.setEnabled(false);
            reloj.reset();
            score.reset();
            JAbsolute.fileMenu.setEnabled(true);
            controles.start.requestFocus();
        }
        
        // Controls Methods
     //   public void runStart() {
       //     super.runStart();
         //   PerfectPitchX.fileMenu.setEnabled(false);
        //}
    
        // Keyboard methods
        // Chronometer Methods
        @Override
        public void timeExpired() { 
            delayedAsk.stop();
            piano.setEnabled(false);
            controles.setEnabled(false);
            piano.stopAll();   /// cambiar por channel
            piano.setDefaultColors(); 
            int duration = (selector.getSettings()).getDuration(); 
            int nperChord = (selector.getSettings()).getNotesPerChord(); 
            UserManager.getCurrentUser().addSession(score.getPercent()); 
            if ( score.getPercent() == 100) { 
                 if (score.getTotal() > (10*duration)/nperChord) {
                      if (UserManager.getCurrentUser().getCurrentLevel() >=
                          ExerciseManager.getLastLevel() ) {
                          MessageManager.showResults( score, MessageManager.COMPLITED);
                      } else { 
                          MessageManager.showResults( score,MessageManager.HUNDRED ); 
                      } 
                      UserManager.getCurrentUser().nextLevel(); 
                 } else { 
                      MessageManager.showResults( score,MessageManager.FASTER ); 
                 } 
            } else if ( score.getPercent() >= (100 - nperChord*5) ){ 
                 if (score.getTotal() > (10*duration)/nperChord) { 
                      if (UserManager.getCurrentUser().getCurrentLevel() >=
                          ExerciseManager.getLastLevel() ) {
                          MessageManager.showResults( score, MessageManager.COMPLITED);
                      } else {
                           MessageManager.showResults( score,MessageManager.PASS ); 
                      }
                      UserManager.getCurrentUser().nextLevel(); 
                 } else { 
                      MessageManager.showResults( score,MessageManager.FASTER ); 
                 } 
            } else { 
                 MessageManager.showResults( score,MessageManager.DONT_PASS ); 
            } 
   /*         if ( (score.getPercent() >= 95) &&  
                 (score.getTotal() > ( (10 * duration) / nperChord ) ) ) {
                  MessageManager.showResults( score,MessageManager.PASS );
                  UserManager.getCurrentUser().nextLevel();
            } else {
                  MessageManager.showResults(score, MessageManager.DONT_PASS);
            }*/
            reset(); 
        }  
        
        // Controls Methods

    } // End TestMethods
    
         
} // END of class MainPanel

