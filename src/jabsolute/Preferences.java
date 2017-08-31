package jabsolute;

import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.util.Vector;
import javax.swing.event.*;
import javax.sound.midi.*;

public class Preferences extends JDialog implements
        MusicConstants {

    private static Vector currentChordTypes = initCurrentChordTypes();
    private static DefaultComboBoxModel comboChordTypes
            = initComboChordTypes();
    private static DefaultListModel listChordTypes
            = initListChordTypes();
    private static Object none = currentChordTypes.get(0);
    private static Object random = currentChordTypes.get(
            currentChordTypes.size() - 1);
    private static int[] keyStrokes = initKeyStrokes();

    private JTabbedPane mainTabbedPane = new JTabbedPane();
    private JPanel buttonPanel
            = new JPanel(new FlowLayout(FlowLayout.RIGHT));
    private JButton closeButton = new JButton("Close");

    //// Constructor
    public Preferences(JFrame owner) {
        super(owner, "Preferences", true);
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(mainTabbedPane, BorderLayout.NORTH);
        getContentPane().add(buttonPanel, BorderLayout.SOUTH);
        buttonPanel.add(closeButton);
        closeButton.addActionListener(new CloseButtonListener());
        mainTabbedPane.add("Chords", new ChordsPanel());
        mainTabbedPane.add("Keyboard", new KeyboardPanel());
        //mainTabbedPane.add("Edit Levels", new LevelsPanel());
        pack();
    }

    private static Vector initCurrentChordTypes() {
        Vector rv = new Vector();
        rv.add("None");
        rv.add(MAJOR_TRIAD);
        rv.add(MINOR_TRIAD);
        rv.add(DIMINISHED_TRIAD);
        rv.add("Random");
        return rv;
    }

    private static DefaultListModel initListChordTypes() {
        DefaultListModel rv = new DefaultListModel();
        for (int i = 0; i < currentChordTypes.size(); i++) {
            rv.addElement(currentChordTypes.get(i));
        }
        return rv;
    }

    private static DefaultComboBoxModel initComboChordTypes() {
        DefaultComboBoxModel rv = new DefaultComboBoxModel();
        for (int i = 0; i < currentChordTypes.size(); i++) {
            rv.addElement(currentChordTypes.get(i));
        }
        return rv;
    }

    private static void updateListChordTypes() {
        listChordTypes.removeAllElements();
        for (int i = 0; i < currentChordTypes.size(); i++) {
            listChordTypes.addElement(
                    currentChordTypes.get(i));
        }
    }

    private static void updateComboChordTypes() {
        comboChordTypes.removeAllElements();
        for (int i = 0; i < currentChordTypes.size(); i++) {
            comboChordTypes.addElement(
                    currentChordTypes.get(i));
        }
    }

    private static int[] initKeyStrokes() {
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

    private Point getCenteredLocation() {
        int x, y;
        Rectangle ppBounds;
        ppBounds = JAbsolute.getLastInstance().getBounds();
        x = (int) ((ppBounds.width - this.getSize().width) / 2 + ppBounds.x);
        y = (int) ((ppBounds.height - this.getSize().height) / 2 + ppBounds.y);
        return new Point(x, y);
    }

    @Override
    public void setVisible(boolean b) {
        setLocation(getCenteredLocation());
        super.setVisible(b);
    }

    public static int[] getKeyStrokes() {
        return keyStrokes;
    }

    public static Vector getCurrentChordTypes() {
        return currentChordTypes;
    }

    public static DefaultComboBoxModel getComboChordTypes() {
        return comboChordTypes;
    }

    class CloseButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            setVisible(false);
        }
    }

/////////////////// ChordsPanel //////////////////////////// 
    class ChordsPanel extends JPanel implements
            ListSelectionListener,
            ActionListener {

        int[] defaultSelectedIndices = {0, 1, 2};
        JList allChords = new JList(AVAILABLE_CHORD_TYPES);
        JList selectedChords = new JList(listChordTypes);
        Box leftBox = new Box(BoxLayout.Y_AXIS);
        Box rightBox = new Box(BoxLayout.Y_AXIS);
        JLabel avail = new JLabel("Available Chord Types");
        JLabel selec = new JLabel("Selected Chord types");
        JPanel mPanel = new JPanel(new BorderLayout());
        JButton rdButton = new JButton("Restore defaults");
        JPanel boxPanel
                = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JPanel buttonPanel
                = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        public ChordsPanel() {
            setLayout(new FlowLayout(FlowLayout.CENTER));
            leftBox.add(avail);
            leftBox.add(new JScrollPane(allChords));
            rightBox.add(selec);
            rightBox.add(new JScrollPane(selectedChords));
            boxPanel.add(leftBox);
            boxPanel.add(rightBox);
            buttonPanel.add(rdButton);
            rdButton.addActionListener(this);
            mPanel.add(boxPanel, BorderLayout.NORTH);
            mPanel.add(buttonPanel, BorderLayout.SOUTH);
            add(mPanel);
            allChords.addListSelectionListener(this);
            selectedChords.setEnabled(false);
        }

        public void valueChanged(ListSelectionEvent e) {
            Object[] selectedValues = allChords.getSelectedValuesList().toArray();
            // allChords.getSelectedValues(); //Deprecated 1.7
            currentChordTypes.removeAllElements();
            currentChordTypes.add(none);
            for (int i = 0; i < selectedValues.length; i++) {
                currentChordTypes.add(selectedValues[i]);
            }
            currentChordTypes.add(random);
            updateListChordTypes();
            updateComboChordTypes();
        }

        public void restoreDefaults() {
            allChords.setSelectedIndices(defaultSelectedIndices);
        }

        public void actionPerformed(ActionEvent e) {
            restoreDefaults();
        }
    } // end ChordPanel

////////////////////// KeyboardPanel /////////////////////////
    class KeyboardPanel extends JPanel implements
            KeyListener,
            ActionListener,
            FocusListener,
            MouseListener {

        JPanel notes = new JPanel(new GridLayout(6, 4, 20, 10));
        JPanel buttonPanel
                = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton rdButton = new JButton("Restore defaults");
        JPanel mPanel = new JPanel(new BorderLayout(0, 10));
        JTextField[] textFields = new JTextField[12];

        public KeyboardPanel() {
            this.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 20));
            mPanel.add(notes, BorderLayout.NORTH);
            mPanel.add(buttonPanel, BorderLayout.SOUTH);
            this.add(mPanel);
            addNoteNames();
            updateTextFields();
            rdButton.addActionListener(this);
            buttonPanel.add(rdButton);
            addFocusListener(this);
        }

        private void addNoteNames() {
            MouseListener[] ml = null;
            JPanel jp = null;
            for (int i = 0; i < NOTE_NAMES.length; i++) {
                jp = new JPanel(
                        new FlowLayout(FlowLayout.RIGHT, 20, 0));
                jp.add(new JLabel(NOTE_NAMES[i] + " = "));
                notes.add(jp);
                textFields[i] = new JTextField(2);
                textFields[i].addKeyListener(this);
                textFields[i].addFocusListener(this);
                ml = (MouseListener[]) textFields[i].getListeners(MouseListener.class);
                for (int j = 0; j < ml.length; j++) {
                    textFields[i].removeMouseListener(ml[j]);
                }
                textFields[i].addMouseListener(this);
                notes.add(textFields[i]);
            }
        }

        private void requestNextFocus(JTextField tf) {
            for (int i = 0; i < textFields.length; i++) {
                if (tf == textFields[i]) {
                    int nIdx = (i + 1) % textFields.length;
                    textFields[nIdx].requestFocus();
                    //textFields[nIdx].setText(null);  
                    break;
                }
            }
        }

        private boolean keyStrokeTaken(KeyEvent e) {
            boolean rv = false;
            for (int i = 0; i < textFields.length; i++) {
                if (keyStrokes[i] == e.getKeyCode()) {
                    rv = true;
                    break;
                }
            }
            return rv;
        }

        private int getTextFieldIndex(JTextField tf) {
            int rv = -1;
            for (int i = 0; i < textFields.length; i++) {
                if (textFields[i] == tf) {
                    rv = i;
                    break;
                }
            }
            return rv;
        }

        private void updateTextFields() {
            for (int i = 0; i < textFields.length; i++) {
                textFields[i].setText(KeyEvent.getKeyText(keyStrokes[i]));
            }
        }

        public void keyTyped(KeyEvent e) {
        }

        public void keyPressed(KeyEvent e) {
        }

        public void keyReleased(KeyEvent e) {
            JTextField source = (JTextField) e.getSource();
            int keyCode = e.getKeyCode();
            int idx = getTextFieldIndex(source);
            if (!keyStrokeTaken(e)) {
                source.setText(KeyEvent.getKeyText(keyCode));
                keyStrokes[idx] = keyCode;
                requestNextFocus(source);
            } else {
                source.setText(KeyEvent.getKeyText(keyStrokes[idx]));
                source.selectAll();
                //System.out.println ("already in use");
            }
        }

        public void focusGained(FocusEvent e) {
            if (e.getSource() instanceof JPanel) {
                textFields[0].requestFocus();
            } else {
                JTextField tf = (JTextField) e.getSource();
                tf.selectAll();
            }
        }

        public void focusLost(FocusEvent e) {
        }

        private void restoreDefaults() {
            keyStrokes[0] = KeyEvent.VK_A;
            keyStrokes[1] = KeyEvent.VK_W;
            keyStrokes[2] = KeyEvent.VK_S;
            keyStrokes[3] = KeyEvent.VK_E;
            keyStrokes[4] = KeyEvent.VK_D;
            keyStrokes[5] = KeyEvent.VK_F;
            keyStrokes[6] = KeyEvent.VK_T;
            keyStrokes[7] = KeyEvent.VK_G;
            keyStrokes[8] = KeyEvent.VK_Y;
            keyStrokes[9] = KeyEvent.VK_H;
            keyStrokes[10] = KeyEvent.VK_U;
            keyStrokes[11] = KeyEvent.VK_J;
            updateTextFields();
        }

        public void actionPerformed(ActionEvent e) {
            restoreDefaults();
        }

        public void mouseClicked(MouseEvent e) {
        }

        public void mousePressed(MouseEvent e) {
            JTextField tf = (JTextField) e.getSource();
            tf.requestFocus();
        }

        public void mouseReleased(MouseEvent e) {
        }

        public void mouseExited(MouseEvent e) {
        }

        public void mouseEntered(MouseEvent e) {
        }

        public void setVisible(boolean aFlag) {
            super.setVisible(aFlag);
            textFields[0].requestFocus();
        }
    } // end KeyboardPanel

     //////////////////// LevelsPanel ///////////////////////////
     /* class LevelsPanel extends JPanel {
     public LevelsPanel () {
          
     }
     } // end class LevelsPanel */
} // end class Preferences

