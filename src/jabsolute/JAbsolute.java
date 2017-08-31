package jabsolute;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.URL;
import javax.help.CSH;
import javax.help.HelpBroker;
import javax.help.HelpSet;
import javax.help.HelpSetException;
import javax.swing.ButtonGroup;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

/**
 * This is the entry of the program. This is the main window of the user 
 * interface.
 * @author Francisco Fernandez @ Muscience
 */
public class JAbsolute extends JFrame
        implements  ActionListener {
    private static final String iconPath = "/resources/JAbsoluteIcon.png";
    private static final String jAbsoluteLogoPath = 
            "/resources/JAbsoluteLogo.png";
    private static String os = System.getProperty("os.name");
    private static int modifier = ( FranUtilities.StringContains(os,"mac")?
        KeyEvent.META_MASK:
        KeyEvent.CTRL_MASK );
    private static JFrame lastInstance = null;
    static final JMenuBar mainMenuBar = new JMenuBar();
    private AboutBox aboutBox;
    private MainPanel mainPanel;
    private Preferences preferences = new Preferences(this);
    private String helpSetFileName = "jabsolutemanual.hs";//"pphandbook.hs";
    private ClassLoader cl;
    private URL url;
    /// Pendiente dividir el siguiente enunciado y manejar exceptions
    private HelpSet helpSet;
    private HelpBroker helpBroker;
    
    static final JMenu fileMenu = new JMenu("File");
    private JMenu currentUser = new JMenu("Current user");
    private JMenuItem newUser = new JMenuItem("New user");
    private JMenuItem delete = new JMenuItem("Delete");
    private JMenuItem info = new JMenuItem("Show info");
    private JMenuItem history = new JMenuItem("Show history");
    private JMenuItem level = new JMenuItem("Change level");
    private JMenuItem prefMenuItem = new JMenuItem("Preferences...");
    private ButtonGroup usersButtonGroup;
    private JRadioButtonMenuItem currentUserMenuItem = null;
    private JMenuItem escMenuItem = new JMenuItem("Stop test");
    
    static final JMenu modeMenu = new JMenu("Mode");
    private JRadioButtonMenuItem testMenuItem;
    private JRadioButtonMenuItem playMenuItem;
    private JRadioButtonMenuItem practiceMenuItem;
    private JRadioButtonMenuItem clearSelectionMenuItem;
    private ButtonGroup modeButtonGroup;
    
    static final JMenu helpMenu = new JMenu("Help");
    private JMenuItem aboutMenuItem = new JMenuItem("About JAbsolute");
    private JMenuItem handbookMenuItem = new JMenuItem("JAbsolute Manual");
    
    private UserListener userListener;
    
    public void addUserMenuItems() {
        String [] userNames = UserManager.getUserNames();
        usersButtonGroup = new ButtonGroup();
        userListener = new UserListener();
        
        for (int i = 0 ; i < userNames.length ; i++) {
            JRadioButtonMenuItem tempMI = new JRadioButtonMenuItem(userNames[i]);
            tempMI.addActionListener(userListener);
            fileMenu.add(tempMI);
            usersButtonGroup.add(tempMI);
        }
    }
    
    public void addFileMenuItems() {
        fileMenu.add( newUser );
        newUser.setAccelerator( KeyStroke.getKeyStroke(KeyEvent.VK_N, modifier));
        newUser.addActionListener(this);
        
        fileMenu.add( currentUser );
        
        currentUser.add(delete);
        delete.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_D, modifier));
        delete.addActionListener(this);
        
        currentUser.add(info);
        info.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_I, modifier));
        info.addActionListener(this);
        
        currentUser.add(history);
        history.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_H, modifier));
        history.addActionListener(this);
        
        currentUser.add(level);
        level.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, modifier));
        level.addActionListener(this);
        
        fileMenu.addSeparator();
        
        fileMenu.add(prefMenuItem);
        prefMenuItem.addActionListener(this);
        
        fileMenu.addSeparator();
        
        fileMenu.add(escMenuItem);
        escMenuItem.setVisible(true);
        //escMenuItem.setVisible(false);
        escMenuItem.addActionListener(this);
        escMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0));
        
        fileMenu.addSeparator();
        
        addUserMenuItems();
    }
    
    public void addModeMenuItems() {
        modeButtonGroup = new ButtonGroup();
        testMenuItem = new JRadioButtonMenuItem("Test");
        modeMenu.add(testMenuItem).setEnabled(true);
        testMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_T,modifier));
        testMenuItem.addActionListener(this);
        modeButtonGroup.add(testMenuItem);
        
        practiceMenuItem = new JRadioButtonMenuItem("Practice");
        modeMenu.add(practiceMenuItem).setEnabled(true);
        practiceMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P,modifier));
        practiceMenuItem.addActionListener(this);
        modeButtonGroup.add(practiceMenuItem);
        
        playMenuItem = new JRadioButtonMenuItem("Play");
        modeMenu.add(playMenuItem).setEnabled(true);
        playMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_L,modifier));
        playMenuItem.addActionListener(this);
        modeButtonGroup.add(playMenuItem);
        
        clearSelectionMenuItem = new JRadioButtonMenuItem();
        modeButtonGroup.add(clearSelectionMenuItem);
    }
    
    public final void addHelpMenuItems() {
        if (!FranUtilities.StringContains(os,"mac")){
            helpMenu.add(aboutMenuItem);
            aboutMenuItem.addActionListener(this);
        }
        helpMenu.add(handbookMenuItem);
        handbookMenuItem.addActionListener(helpLauncher( helpSetFileName ));
    }
    
    public final void addMenus() {
        addFileMenuItems();
        addModeMenuItems();
        addHelpMenuItems();
        mainMenuBar.add(fileMenu);
        mainMenuBar.add(modeMenu);
        mainMenuBar.add(helpMenu);
        // fileMenu.setMnemonic(KeyEvent.VK_F);
        // modeMenu.setMnemonic(KeyEvent.VK_M);
        setJMenuBar(mainMenuBar);
    }
    
    public final Image loadProgramIcon() {
        URL imgURL = JAbsolute.class.getResource(iconPath);
        Image returnValue = null;
        ImageIcon imageIcon;
        
        if (imgURL != null) {
            imageIcon = new ImageIcon(imgURL);
            returnValue = imageIcon.getImage();
            //System.out.println(returnValue);
        } else {
            System.err.println("Couldn't find file: " + iconPath);
        }
        return returnValue;
    }
    
    public static Icon getProgramIcon() {
        URL imgURL = JAbsolute.class.getResource(iconPath);
        ImageIcon returnValue = null;
        
        if (imgURL != null) {
            returnValue = new ImageIcon(imgURL);
        } else {
            System.err.println("Couldn't find file: " + iconPath);
        }
        return returnValue;
    }
    
    public static Icon getJAbsoluteLogo() {
        //String iconPath = "/resources/JAbsoluteMuscienceLogo.png";
        URL imgURL = JAbsolute.class.getResource(jAbsoluteLogoPath);
        ImageIcon returnValue = null;
        
        if (imgURL != null) {
            returnValue = new ImageIcon(imgURL);
        } else {
            System.err.println("Couldn't find file: " + jAbsoluteLogoPath);
        }
        return returnValue;
    }
    
    // Constructor
    public JAbsolute() {
        super("JAbsolute");
        this.setIconImage(loadProgramIcon());
        
        this.getContentPane().setLayout(new FlowLayout(FlowLayout.CENTER, 20, 30));
        mainPanel = new MainPanel();
        addWindowListener(new WindowCloser());
        this.getContentPane().add( mainPanel );
        addMenus();
        
        lastInstance = this; // prueba... antes estaba abajo para crear aboutBox
        aboutBox = new AboutBox();
        Toolkit.getDefaultToolkit();
//        MRJApplicationUtils.registerAboutHandler(this);
//        MRJApplicationUtils.registerQuitHandler(this);
//        MRJApplicationUtils.registerOpenApplicationHandler(this);
        
        mainPanel.setEnabled(false);
        setResizable(false);
        pack();
        setLocation(getCenteredLocation());
        setVisible(true);
        // lastInstance = this; .... Prueba
        
        //UserManager.loadUsers ();  //// a prueba
    }
    
    public static JFrame getLastInstance() {
        return lastInstance;
    }
    
    //// MRJ interfaces
    public void handleAbout() {
        aboutBox.setResizable(false);
        aboutBox.setVisible(true);
        //aboutBox.setVisible(true);
    }
    
    public void handleQuit() {
        mainPanel.piano.closeSynth();
        if (!(UserManager.getCurrentUser() == null) ) {
            UserManager.saveUsers();
            //System.out.println(UserManager.getCurrentUser().getHistoryText());
        }
        System.exit(0);
    }
    
    public void handleOpenApplication() {
        //System.out.println("Application is opening");
    }
    
    public void actionPerformed( ActionEvent newEvent ) {
        //System.out.println("actionPerformed() called:  " + newEvent );
        if (!mainPanel.timeIsOn()) {
            if (newEvent.getActionCommand().equals(testMenuItem.getActionCommand())) doTest();
            else if (newEvent.getActionCommand().equals(practiceMenuItem.getActionCommand())) doPractice();
            else if (newEvent.getActionCommand().equals(playMenuItem.getActionCommand())) doPlay();
            else if (newEvent.getActionCommand().equals(newUser.getActionCommand())) doNewUser();
            else if (newEvent.getActionCommand().equals(delete.getActionCommand())) doDelete();
            else if (newEvent.getActionCommand().equals(prefMenuItem.getActionCommand())) doPreferences();
            else if (newEvent.getActionCommand().equals(info.getActionCommand())) doShowInfo();
            else if (newEvent.getActionCommand().equals(level.getActionCommand())) doSetLevel();
            else if (newEvent.getActionCommand().equals(history.getActionCommand())) doShowHistory();
            else if (newEvent.getActionCommand().equals(aboutMenuItem.getActionCommand())) doOpenAbout();
        } else if (newEvent.getActionCommand().equals(escMenuItem.getActionCommand())) doEscape();
        else {
            JOptionPane.showMessageDialog(mainMenuBar, "Time is runing, you cannot perform this\n" +
                    "operation. If you want to terminate your exercise,\n" +
                    "you can press ESC any time");
            modeMenu.getItem(mainPanel.getMode()).setSelected(true);
        }
    }
    
    ///////////////////////// Actions /////////////////////
    public void doTest() {
        mainPanel.setEnabled(false);
        if ( !(UserManager.getCurrentUser() == null) ) {
            mainPanel.setMode( ProgramConstants.TEST );
        } else {
            JOptionPane.showMessageDialog(mainMenuBar, "No User selected.\n" +
                    "Please select user from 'File' menu.\n" +
                    "If you are a new user, choose 'New user' from 'File Menu'");
            clearSelectionMenuItem.setSelected(true);
        }
    }
    
    public void doPractice() {
        mainPanel.setEnabled(false);
        if ( !(UserManager.getCurrentUser() == null) ) {
            fileMenu.setEnabled(true);
            mainPanel.setMode( ProgramConstants.PRACTICE );
        } else {
            JOptionPane.showMessageDialog(mainMenuBar, "No User selected.\n" +
                    "Please select user from 'File' menu.\n" +
                    "If you are a new user, choose 'New user' from 'File Menu'");
            clearSelectionMenuItem.setSelected(true);
        }
    }
    
    public void doPlay() {
        mainPanel.setEnabled(false);
        if ( !(UserManager.getCurrentUser() == null) ) {
            fileMenu.setEnabled(true);
            mainPanel.setMode( ProgramConstants.PLAY );
            mainPanel.piano.requestFocus();//// temporal
        } else {
            JOptionPane.showMessageDialog(mainMenuBar, "No User selected.\n" +
                    "Please select user from 'File' menu.\n" +
                    "If you are a new user, choose 'New user' from 'File Menu'");
            clearSelectionMenuItem.setSelected(true);
        }
    }
    
    public void doNewUser() {
        String usr;
        while ( UserManager.contains(usr = JOptionPane.showInputDialog( mainMenuBar, "Enter your name",
                "New user", JOptionPane.PLAIN_MESSAGE))) {
            JOptionPane.showMessageDialog( mainMenuBar, "User already exists !");
        }
        if ( !(usr == null) && (usr.length() > 0) ) {
            //     int itemCount = fileMenu.getItemCount();
            JRadioButtonMenuItem tempMI = new JRadioButtonMenuItem( usr );
            usersButtonGroup.add(tempMI);
            tempMI.addActionListener(userListener);
            fileMenu.add(tempMI);
            tempMI.doClick();
        }
    }
    
    public void doDelete() {
        if ( !( currentUserMenuItem == null ) ) {
            int option = JOptionPane.showInternalConfirmDialog(
                    mainMenuBar, "Are you sure you want to delete " +
                    UserManager.getCurrentUser() +"?");
            if (option == JOptionPane.OK_OPTION ) {
                UserManager.removeCurrentUser();
                fileMenu.remove(currentUserMenuItem);
                usersButtonGroup.remove(currentUserMenuItem);
                currentUserMenuItem = null;
                clearSelectionMenuItem.setSelected(true);
                mainPanel.setEnabled(false);
            }
        } else {
            JOptionPane.showMessageDialog(mainMenuBar, "No User selected.");
        }
    }
    
    public void doPreferences() {
        preferences.setVisible(true);
    }
    
    public void doEscape() {
        //System.out.println("doEscape() called");
        if (mainPanel.timeIsOn()){
            int option = JOptionPane.showConfirmDialog(
                    mainMenuBar, "Time is running, this will reset\n"
                    + "current mode, and terminate your exercise.\n"
                    + "Are you sure you want to reset current mode?");
            if (option == JOptionPane.OK_OPTION){
                mainPanel.setMode( mainPanel.getMode() );
            }
        }
    }
    
    public void doShowInfo() {
        if ( !( currentUserMenuItem == null ) ) {
            User tempUser = UserManager.getCurrentUser();
            MessageManager.showUserInfo(tempUser);
        } else {
            JOptionPane.showMessageDialog(mainMenuBar, "No User selected.");
        }
    }
    
    public void doShowHistory() {
        if ( !( currentUserMenuItem == null ) ) {
            User tempUser = UserManager.getCurrentUser();
            MessageManager.showUserHistory(tempUser);
        } else {
            JOptionPane.showMessageDialog(mainMenuBar, "No User selected.");
        }
    }
    
    public void doSetLevel() {
        if ( !( currentUserMenuItem == null ) ) {
            int level = MessageManager.getLevelFromUser(UserManager.getCurrentUser());
            if ( !(level == -1) ){
                UserManager.getCurrentUser().setCurrentLevel(level);
                //System.out.println("doSetLevel() ok before mainPanel.setMode()");
                mainPanel.setMode( mainPanel.getMode() );
            }
        } else {
            JOptionPane.showMessageDialog(mainMenuBar, "No User selected.");
        }
    }
    
    public void doOpenAbout() { aboutBox.setVisible(true); }
    
    private Point getCenteredLocation() {
        Point rv = new Point();
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        rv.x = (screenSize.width - this.getWidth())/2;
        rv.y = (screenSize.height - this.getHeight())/2;
        return rv;
    }
    
    class WindowCloser extends WindowAdapter {
        public void windowClosing(WindowEvent e) {
            handleQuit();
        }
    }
    
    class UserListener implements ActionListener {
        public void actionPerformed(ActionEvent evt) {
            if (!mainPanel.timeIsOn()) {
                currentUserMenuItem = (JRadioButtonMenuItem) evt.getSource();
                String usr = currentUserMenuItem.getText();
                UserManager.setCurrentUser( usr );
                setTitle(  "Welcome " + usr +" !" );
                clearSelectionMenuItem.setSelected(true);
                mainPanel.setEnabled(false);
            } else {
                fileMenu.getItem(UserManager.getCurrentUserIndex() +
                        (fileMenu.getItemCount() - UserManager.getUserCount())).setSelected(true);
                JOptionPane.showMessageDialog(mainMenuBar, "Time is runing, you cannot perform this\n" +
                        "operation. If you want to terminate your exercise,\n" +
                        "you can press ESC any time");
                modeMenu.getItem(mainPanel.getMode()).setSelected(true);
            }
        }
    }
    
    private ActionListener helpLauncher(String nameURL) {
        ActionListener returnValue = null;
        //System.out.println("helpLauncher called");
        // Get classLoader
        try {
            cl = this.getClass().getClassLoader();
        } catch ( SecurityException ee) {
            System.err.println(ee);
        }
        //System.out.println(cl);
        //System.out.println(nameURL);
        // Find and create HelpSet
        url = HelpSet.findHelpSet(cl , nameURL );
        //System.out.println(url);
        if ( url != null ) {
            try {
                helpSet = new HelpSet(cl, url);
            } catch (HelpSetException ee) {
                System.err.println( ee );
            }
            //System.out.println(helpSet);
            
            // Create HelpBroker
            helpBroker = helpSet.createHelpBroker();
            //System.out.println(helpBroker);
            returnValue = new CSH.DisplayHelpFromSource( helpBroker );
        } else {
            System.err.println("Help files not found");
        }
        return returnValue;
    } // end of helpLauncher()
    
    
    public static void main(String args[]) {
        //System.out.println(  os );
        UIManager.LookAndFeelInfo [] infos = UIManager.getInstalledLookAndFeels();
        /*for ( int i = 0 ; i < infos.length ; i++ ) {
            System.out.println( infos [i] );
        }*/
        
        try {
            //UIManager.setLookAndFeel("com.sun.java.swing.plaf.gtk.GTKLookAndFeel" );
            //UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel" );
            //UIManager.setLookAndFeel("com.sun.java.swing.plaf.motif.MotifLookAndFeel" );
            //UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel" );
            UIManager.setLookAndFeel( UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) { 
            System.err.println(e);
        }
        //JFrame.setDefaultLookAndFeelDecorated(true);
        new JAbsolute();
        
        
        SwingUtilities.getRootPane(Controls.getDefaultButton()).
                setDefaultButton(Controls.getDefaultButton());
    }
} // end PerfectPitch
