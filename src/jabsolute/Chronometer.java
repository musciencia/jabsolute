package jabsolute;

import java.awt.*;
import java.util.*;
import javax.swing.Timer;
import javax.swing.JLabel;
import java.lang.*;
import javax.swing.*;
import java.awt.event.*;


public class Chronometer extends JPanel implements ChronometerMethods, ActionListener {
    // Fiedls    
    private ChronometerMethods chronometerMethods;
    private int initialTime;
    private int period; // miliseconds
    private int stopAt; // miliseconds
    private Timer timer; ///// pendiente delay y action listener
    private int lastRecordedTime = 0;
    private int elapsedTime = 0;
    private int remainingTime;
    private boolean isOn = false;
    private String text = " 00:00 ";
    private Font font;
    private FontMetrics fontMetrics;
    
    public Chronometer () {
        this ( 59 * 60 , 1 );
    }
        
    public Chronometer ( int sa) { // sa en segundos (start at)
        this ( sa, 1 );
    }
    
    public Chronometer ( int sa, int p) { // sa y p en segundos 
        setLayout(new BorderLayout());
        period = p * 1000;
        stopAt = sa * 1000;
        remainingTime = stopAt;
        timer = new Timer( period , this);
        addChronometerMethods ( this );
        font = this.getFont();
        fontMetrics = this.getFontMetrics(font);
        setPreferredSize(calculatePreferredSize());
    }
    
    // public methods //////////////////////////////////////////
    public boolean isOn () {
        return isOn;
    }
    
    public void addChronometerMethods (ChronometerMethods chronMeth) {
        chronometerMethods = chronMeth;
    }

    public void start () {
        elapsedTime = 0;
        lastRecordedTime = 0;
        timer.start();
        //timer.schedule (new Task(), new Date(), period);
        isOn = true;
    }
    
    public void stop () {
        timer.stop(); 
        isOn = false;
    }
    
    public void reset () {
        if ( isOn ) { timer.stop(); }
        elapsedTime = 0;
        lastRecordedTime = 0;
        remainingTime = 0;
        updateTime();
        isOn = false;
        // pending
    }
    
    public void reset ( int sa, int p ) {
     // pending
    }// en segundos
    
    public void setStopAt ( int s ) { // en segundos
        stopAt = s * 1000;
        remainingTime = stopAt;
       // System.out.println("StopAt " + stopAt);
    }
    
    public void setPeriod ( int p ) { // en segundos
        period = p * 1000;
    }
    
    
    public String long2min ( int t ) { // t en milisegundos
        int tempSec;
        int tempMin;
        
        tempSec = ((int)(t/1000.0)) % 60;
        tempMin = ((int)(t/60000.0)) % 60;
        
        return (tempMin + ":" + tempSec ); 
    }

    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D)g;
        int textWidth = fontMetrics.stringWidth( text );
        int ascent = fontMetrics.getAscent();
        int x = (int)(( this.getWidth() - textWidth ) / 2.0 );
        int y = (int)( (this.getHeight() - ascent ) / 2.0 + ascent );

        super.paintComponent(g);        
        g2.drawString ( text , x , y );
    }

    // private methods /////////////////////////////////////
    private void updateTime () {   
        text = (long2min(remainingTime));
        this.repaint();
    }
    
    private Dimension calculatePreferredSize() {
        int textWidth = fontMetrics.stringWidth( text );
        int height = fontMetrics.getHeight();
        return new Dimension ( textWidth + 20, height * 3 );
    }
        
    // Implemented Methods /////////////////////////////////
    public void timeExpired() { };
    
    public void actionPerformed ( ActionEvent e) {
            if (elapsedTime <= (stopAt - 1000 )) {
            
                Date tempDate = new Date();
                if (lastRecordedTime == 0 ) {
                    initialTime = (int) tempDate.getTime ();
                }            
                lastRecordedTime = (int) tempDate.getTime();
                elapsedTime = lastRecordedTime - initialTime;
                remainingTime = stopAt - elapsedTime;
                updateTime ();

            } else { 
                this.stop (); 
                chronometerMethods.timeExpired();
            }
    }
        
} // end Chronometer class

