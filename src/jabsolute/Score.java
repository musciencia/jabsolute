//
//  Score.java
//  SimuladorDeAfinacion
//
//  Created by Marilupe Garmilla on Thu Jun 05 2003.
//  Copyright (c) 2001 __MyCompanyName__. All rights reserved.
//
package jabsolute;
import java.awt.*;
import javax.swing.*;

public class Score extends JPanel {
    // fields 
    private int correct = 0;
    private int incorrect = 0;
    private int total = 0;
    private int percent = 0;
    private String right = "Right = 0";
    private String wrong = "Wrong = 0"; 
    private String score = "Score =    0%";
    private Font font;
    private FontMetrics fontMetrics;
    //Label score = new Label(" SCORE ", Label.CENTER);
    //Label good = new Label (" Right = 0 ", Label.CENTER);
    //Label bad = new Label (" Wrong = 0 ", Label.CENTER);
   // Label tot = new Label ("Total = 0", Label.CENTER);
    //Label per = new Label (" Score =    0 % ", Label.CENTER);
    
    public Score () {
        font = this.getFont();
        fontMetrics = this.getFontMetrics(font);
        setPreferredSize(calculatePreferredSize());
    }

    public void reset() {
        correct = 0;
        incorrect = 0;
        total = 0;
        percent = 0;
        updateScore();
    }
    
    public void right () {
        correct ++;
        updateScore();
    }
    
    public void wrong () {
        incorrect ++;
        updateScore();
    }
    
    public int getRight () {
        return correct;
    }
    
    public int getTotal () {
        return total;
    }
    
    public int getWrong () {
         return incorrect;
    }
    
    public int getPercent () {
        return percent;
    }
    
    // Helper methods
    private void updateScore () {
        total = correct + incorrect;
        percent = (int) ( ( (double) correct / (double) total ) * 100);
        right = " Right = " + correct;
        wrong = " Wrong = " + incorrect ;
        score = " Score = " + percent + "%";
        this.repaint();
    }
    
    public String toString() {
         return total + "%";
    }
    
    // overrides paintComponent();
    public void paintComponent( Graphics g ) {
         Graphics2D g2 = (Graphics2D) g;
         super.paintComponent(g);
         int fontHeight = fontMetrics.getHeight();
         
         // Right label
         int textWidth = fontMetrics.stringWidth(right);
         int x = (int)( ( this.getWidth() - textWidth ) / 2.0 );
         int y = (int)( ( this.getHeight() / 4.0 ) + (fontHeight / 2.0 ) );
         g2.drawString( right, x, y);
         // Wrong label
         textWidth = fontMetrics.stringWidth(wrong);
         x = (int)( ( this.getWidth() - textWidth ) / 2.0 );
         y = (int)( ( this.getHeight() / 2.0 ) + (fontHeight / 2.0 ) );
         g2.drawString( wrong, x, y);
         // Right label
         textWidth = fontMetrics.stringWidth(score);
         x = (int)( ( this.getWidth() - textWidth ) / 2.0 );
         y = (int)( ( this.getHeight() * ( 3.0 / 4.0 )) + (fontHeight / 2.0 ) );
         g2.drawString( score, x, y);
         
    }

    // Helper methods
    private Dimension calculatePreferredSize () {
        int textWidth = fontMetrics.stringWidth( score );
        int height = fontMetrics.getHeight();
        return new Dimension ( textWidth + 40, height * 8 );
    }

}
