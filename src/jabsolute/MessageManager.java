package jabsolute;

import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.border.*;

public class MessageManager {

    public static final int PLAIN = 0;
    public static final int PASS = 1;
    public static final int DONT_PASS = 2;
    public static final int HUNDRED = 3;
    public static final int FASTER = 4;
    public static final int COMPLITED = 5;
    private static final int N = 6;
    private static final String[] TEXTS
            = {"",
                "You have passed to the next level.",
                "Sorry, keep trying this level.",
                "Wowww!, you got a 100%. Go to the next level.",
                "Very good, but you need to be faster. Keep trying this level",
                "Congratulations, you have succesfully completed your ear-training program"};
    private static ScoreDialog scoreDialog = new ScoreDialog();
    private static UserHistoryDialog userHistoryDialog
            = new UserHistoryDialog();

    public static void showResults(Score s, int status) {
        scoreDialog.setScore(s);
        scoreDialog.setText(TEXTS[status % N]);
        scoreDialog.setVisible(true);
    }

    public static void showUserInfo(User usr) {
        JOptionPane.showMessageDialog(JAbsolute.getLastInstance(),
                usr.getInfo(), "User information",
                JOptionPane.INFORMATION_MESSAGE);
    }

    public static void showUserHistory(User usr) {
        userHistoryDialog.setUser(usr);
        userHistoryDialog.setVisible(true);
        userHistoryDialog.requestCloseFocus();
    }

    public static int getLevelFromUser(User usr) {
        int rv = -1;
        int highestLevel = usr.getHighestLevel();
        String message = "You have not passed level "
                + usr.getHighestLevel() + " yet. \n"
                + "So, you can choose any level below level "
                + usr.getHighestLevel() + ".\n"
                + "Enter level:";
        do {
            String input = JOptionPane.showInputDialog(message/*, usr.getHighestLevel() + ""*/);
            if (input == null) {
                break;
            }
            if (input.length() >= 6) {
                if (input.substring(0, 6).equals("force:")) {
                    input = input.substring(6);
                    highestLevel = ExerciseManager.getTotalLevels(); // -1
                }
            }
            try {
                rv = Integer.parseInt(input);
                if ((rv > highestLevel) || (rv < 0)) {
                    rv = -1;
                }
            } catch (NumberFormatException e) {
                rv = -1;
            }
        } while (rv == -1);
        return rv;
    }
} // end class MessageManager

////////////////////// CLASSES /////////////////
class ScoreDialog extends JDialog implements ActionListener {

    JPanel scorePanel = new JPanel(new GridLayout(3, 1, 5, 5));
    JLabel right = new JLabel("", SwingConstants.CENTER);
    JLabel wrong = new JLabel("", SwingConstants.CENTER);
    JLabel per = new JLabel("", SwingConstants.CENTER);
    JLabel message = new JLabel();
    JPanel labelPanel
            = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 20));
    JButton closeButton = new JButton("Close");
    Box mBox = new Box(BoxLayout.Y_AXIS);
    JPanel buttonPanel
            = new JPanel(new FlowLayout());

    public ScoreDialog() {
        super(JAbsolute.getLastInstance(), "Results");
        this.getContentPane().setLayout(
                new FlowLayout(FlowLayout.CENTER, 20, 20));
        this.getContentPane().add(mBox);
        mBox.add(scorePanel);
        mBox.add(labelPanel);
        mBox.add(buttonPanel);
        scorePanel.add(right);
        scorePanel.add(wrong);
        scorePanel.add(per);
        labelPanel.add(message);
        buttonPanel.add(closeButton);
        closeButton.addActionListener(this);
        scorePanel.setBorder(new TitledBorder("Score"));
        pack();
    }

    public void setVisible(boolean b) {
        setLocation(getCenteredLocation());
        super.setVisible(b);
    }

    public void setScore(Score s) {
        right.setText("Right = " + s.getRight());
        wrong.setText("Wrong = " + s.getWrong());
        per.setText("Score = " + s.getPercent() + "%");
    }

    public void actionPerformed(ActionEvent evt) {
        //System.out.println ("Close pressed");
        this.setVisible(false);
    }

    public void setText(String t) {
        message.setText(t);
        this.validate();
        this.pack();
    }

    private Point getCenteredLocation() {
        int x = 0;
        int y = 0;
        Rectangle ppBounds
                = JAbsolute.getLastInstance().getBounds();
        x = (int) ((ppBounds.width - this.getSize().width) / 2 + ppBounds.x);
        y = (int) ((ppBounds.height - this.getSize().height) / 2 + ppBounds.y);
        return new Point(x, y);
    }

}

class UserHistoryDialog extends JDialog
        implements ActionListener {

    User user = null;
    private JTextArea textArea = new JTextArea(15, 35);
    private JScrollPane scrollPane = new JScrollPane(textArea);
    private JButton saveButton = new JButton("Save");
    private JButton closeButton = new JButton("Close");
    private JPanel buttonPanel
            = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
    private Box mainBox = new Box(BoxLayout.Y_AXIS);

    public UserHistoryDialog() {
        super(JAbsolute.getLastInstance(), "User's history");
        this.getContentPane().setLayout(
                new FlowLayout(FlowLayout.CENTER, 15, 15));
        this.getContentPane().add(mainBox);
        mainBox.add(scrollPane);
        mainBox.add(buttonPanel);
        buttonPanel.add(closeButton);
        buttonPanel.add(saveButton);
        closeButton.addActionListener(this);
        saveButton.addActionListener(this);
        textArea.setEditable(false);
        //textArea.setEnabled(false); // pending
        this.setLocation(JAbsolute.getLastInstance().getLocation());
        pack();
    }

    private Point getCenteredLocation() {
        int x = 0;
        int y = 0;
        Rectangle ppBounds
                = JAbsolute.getLastInstance().getBounds();
        x = (int) ((ppBounds.width - this.getSize().width) / 2 + ppBounds.x);
        y = (int) ((ppBounds.height - this.getSize().height) / 2 + ppBounds.y);
        return new Point(x, y);
    }

    public void setVisible(boolean b) {
        setLocation(getCenteredLocation());
        super.setVisible(b);
    }

    public void setUser(User usr) {
        user = usr;
        textArea.setText(usr.getInfoAndHistory());
        this.validate();
        this.repaint();
    }

    public void requestCloseFocus() {
        //System.out.println("requestCloseFocus() called");
        this.closeButton.requestFocus();
    }

    @Override
    public void actionPerformed(ActionEvent evt) {
        if (evt.getActionCommand().equals(saveButton.getActionCommand())) {
            UserManager.getCurrentUser().saveAsTxt();
            //System.out.println(System.getProperty("user.dir"));
            JOptionPane.showMessageDialog(
                    JAbsolute.getLastInstance(),
                    System.getProperty("user.dir")
                    + System.getProperty("file.separator")
                    + UserManager.getCurrentUser().getName() + ".txt",
                    "File was saved as:",
                    JOptionPane.PLAIN_MESSAGE);
        } else {
            this.setVisible(false);
        }
    }

}
