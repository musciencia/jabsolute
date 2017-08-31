package jabsolute;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class AboutBox extends JDialog // Prueba .... antes JFrame
                      implements ActionListener
{
    protected JButton okButton;
    protected JLabel programName = new JLabel ("JAbsolute") ; 
    protected JLabel version = new JLabel("Version 1.0.0 (Oct 2014)");
    protected JLabel author = new JLabel ("By Francisco Fernandez");
    protected JLabel copyright = new JLabel ("\u00a9 2014 Muscience");
    protected JPanel mainPanel = new JPanel(new BorderLayout(10, 20) );

    public AboutBox() {
	     super(JAbsolute.getLastInstance(), "About JAbsolute", true); // prueba... antes super();
        this.getContentPane().setLayout(new FlowLayout(FlowLayout.CENTER, 30, 30));
        this.setFont(new Font ("SansSerif", Font.BOLD, 14));

        Box textBox = new Box (BoxLayout.Y_AXIS);
        textBox.add(programName);
        textBox.add(version);
        textBox.add(author);
        textBox.add(copyright);
        mainPanel.add (new JLabel(JAbsolute.getJAbsoluteLogo()), BorderLayout.NORTH);
        mainPanel.add (textBox, BorderLayout.CENTER);
		
        okButton = new JButton("OK");
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add (okButton);
        okButton.addActionListener(this);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        //setResizable(false);
        this.getContentPane().add(mainPanel);
        this.pack();
        this.setLocation(getCenteredLocation());
    }
    
    private Point getCenteredLocation () {
        Point rv = new Point();
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        rv.x = (screenSize.width - this.getWidth())/2;
        rv.y = (screenSize.height - this.getHeight())/2;
        return rv;
    }

	
    public void actionPerformed(ActionEvent newEvent) {
        setVisible(false);
    }	
	
}