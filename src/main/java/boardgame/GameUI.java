package boardgame;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import java.awt.BorderLayout;
import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.awt.Font;

/**
 * class that creates the JFrame used to play games
 * extends JFrame class
 * @author Martin Sergo
* @version 1.2
*/
public class GameUI extends JFrame{

    private JPanel gamePanel = new JPanel();
    private JPanel playerPanel;
    private Player player1 = new Player();
    private Player player2 = new Player();
    private JMenuBar menuBar; // the menu at the top
    private TicTacToeView subGamePanel;
    private NumTicTacToeView subGamePanel2;
    private int state; // can be 1-3 used to tell if menu can be used yet

    /**
     * constructor for GameUI class
     */
    public GameUI() {
        super();
        menuBar = createMenu();
        setJMenuBar(menuBar);

        player1.setPlayerID(1);
        player2.setPlayerID(2);

        gamePanel.setLayout(new BoxLayout(gamePanel, BoxLayout.Y_AXIS)); // elements arranged vertically
        //ImageIcon frameImage = new ImageIcon("shrek.png");
        setTitle("Board game player"); // frame title
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // exit on close
        setLayout(new BorderLayout()); // border layout is used for the frame
        setSize(800, 500); // just for initial size (can still be resized)
        setResizable(true); // user should be able to resize
        //setIconImage(frameImage.getImage()); // change frame icon because why not
        //getContentPane().setBackground(Color.ORANGE); // frame background colour

        add(makeButtonPanel(), BorderLayout.EAST); // adds a panel on the right
        gamePanel.setBackground(new Color(0,0,0));
        add(gamePanel, BorderLayout.CENTER);

        setVisible(true); // save for the end of the constructor
        begin();
    }

    /**
     * updates the panel that is responsible for showing the player stats
     */
    public void updateLowPanel() {
        gamePanel.remove(playerPanel);
        playerPanel = makeLowPanel();
        gamePanel.add(playerPanel); // adds to bottom, need to remove
        //System.out.println("updated");
    }

    /**
     * creates the menu for loading/saving player or game data
     * @return JMenuBar to be added to the frame
     */
    private JMenuBar createMenu() {
        JMenuBar menuBarLocal = new JMenuBar();
        JMenu menu1 = new JMenu("File");
        JMenu menu2 = new JMenu("Player");
        JMenuItem item1 = new JMenuItem("Save Game");
        JMenuItem item2 = new JMenuItem("Load Game");
        JMenuItem item3 = new JMenuItem("Save Player statistics");
        JMenuItem item4 = new JMenuItem("Load Player statistics");
        menuBarLocal.add(menu1);
        menuBarLocal.add(menu2);
        menu1.add(item1);
        menu1.add(item2);
        menu2.add(item3);
        menu2.add(item4);
        item1.addActionListener(e->askSaveGame());
        item2.addActionListener(e->askLoadGame());
        item3.addActionListener(e->askSavePlayer());
        item4.addActionListener(e->askLoadPlayer());
        return menuBarLocal;
    }

    private JButton makeTicTacToeButton() {
        JButton button = new JButton("Start new Tic Tac Toe game");
        button.setEnabled(true); // button is enabled to start
        button.addActionListener(e -> newTicTacToe());
        button.setFocusable(false); // looks better
        button.setFont(new Font("Times New Roman", Font.PLAIN, 14));
        //button.setSize(150, 150); // button size in JFrame
        return button;
    }

    private JButton makeNTTTButton() {
        JButton button = new JButton("Start new numerical Tic Tac Toe game");
        button.setEnabled(true); // button is enabled to start
        button.setFocusable(false); // looks better
        button.addActionListener(e -> newNTTT());
        button.setFont(new Font("Times New Roman", Font.PLAIN, 13));
        button.setBorder(BorderFactory.createEtchedBorder());
        return button;
    }

    /**
     * toString method showcasing what class does
     */
    public String toString() {
        String myStr = "creating the frame that allows the user to play either game and file operation implementation";
        return "The Game UI class is responsible for" + myStr;
    }

    /**
     * creates a formatted panel to add to the frame
     * @return a JPanel with buttons on it
     */
    public JPanel makeButtonPanel() {
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.add(makeTicTacToeButton());
        buttonPanel.add(makeNTTTButton());
        buttonPanel.setBorder(BorderFactory.createEtchedBorder());
        return buttonPanel;
    }

    /**
     * creates the bottom panel of the frame, which has player info on it
     * @return returns the JPanel to add to the frame
     */
    public JPanel makeLowPanel() {
        JPanel panel = new JPanel();
        JPanel sub1;
        JPanel sub2;
        sub1 = createPlayerPanel(player1);
        sub2 = createPlayerPanel(player2);
        Font myFont = new Font("Comic Sans", Font.PLAIN, 12);

        panel.setLayout(new BorderLayout()); // border layout is enough

        sub1.setBackground(Color.CYAN); // delete later
        sub2.setBackground(Color.RED);
        sub1.setFont(myFont);
        sub2.setFont(myFont);

        panel.add(sub1, BorderLayout.WEST);
        panel.add(sub2, BorderLayout.EAST);
        return panel;
    }

    private JPanel createPlayerPanel(Player givenPlayer) {
        JPanel sub = new JPanel();
        JLabel label1 = new JLabel("Player " + givenPlayer.getPlayerID() + " data:");
        JLabel label2 = new JLabel("Games played: " + givenPlayer.getGamesPlayed());
        JLabel label3 = new JLabel("Games won: " + givenPlayer.getGamesWon());
        JLabel label4 = new JLabel("Games lost: " + givenPlayer.getGamesLost());
        sub.setBorder(BorderFactory.createEtchedBorder());
        sub.setLayout(new BoxLayout(sub, BoxLayout.Y_AXIS));
        sub.add(label1);
        sub.add(label2);
        sub.add(label3);
        sub.add(label4);
        return sub;
    }

    /**
     * starts a TicTacToe Game
     */
    public void newTicTacToe() {
        state = 2;
        gamePanel.removeAll();
        subGamePanel = new TicTacToeView(this);
        gamePanel.add(subGamePanel, BorderLayout.CENTER);
        playerPanel = makeLowPanel();
        gamePanel.add(playerPanel, BorderLayout.SOUTH);
        getContentPane().repaint();
        getContentPane().revalidate();
        //pack();
    }

     /**
     * starts a NumTicTacToe Game
     */
    public void newNTTT() {
        state = 3;
        gamePanel.removeAll();
        subGamePanel2 = new NumTicTacToeView(this);
        gamePanel.add(subGamePanel2, BorderLayout.CENTER);
        playerPanel = makeLowPanel();
        gamePanel.add(playerPanel, BorderLayout.SOUTH);
        getContentPane().repaint();
        getContentPane().revalidate();
        //pack();
    }

    /**
     * resets the Frame to the state it launches in (no games loaded/visible)
     */
    public void begin() {
        gamePanel.removeAll();
        gamePanel.add(starterPanel());
        getContentPane().repaint();
        getContentPane().revalidate();
        state = 1;
        //pack();
        return;
    }
    private JPanel starterPanel() {
        JPanel temp = new JPanel();
        JLabel tempLabel = new JLabel("welcome to the Board game frame! Begin by selecting a game to play");
        tempLabel.setFont(new Font("MV Boli", Font.PLAIN, 15));
        tempLabel.setVerticalTextPosition(JLabel.TOP);
        temp.add(tempLabel);
        return temp;
    }

    /**
     * asks user to save player
     * called from menu
     */
    protected void askSavePlayer() {
        if (state == 1) {
            JOptionPane.showMessageDialog(null, 
            "You cannot save at this time", "Unable to save", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        String[] optionString = {"Save Player 1", "Save Player 2", "Do not save"}; 
        int choice = JOptionPane.showOptionDialog(null, 
        "Pick which player to save",
        "Saving Player data to file",
        JOptionPane.YES_NO_CANCEL_OPTION,
        JOptionPane.INFORMATION_MESSAGE,
        null,
        optionString, 0);
        if (choice == 2) {
            return; // user selected "cancel"
        } else if (choice == 0){
            launchFileChooserSavePlayer(player1);
        } else if (choice == 1){
            launchFileChooserSavePlayer(player2);
        }
        return;
    }

    private void launchFileChooserSavePlayer(Player playerToSave) throws IOException{
        Path currentRelativePath = Paths.get("");
        String s = currentRelativePath.toAbsolutePath().toString();
        //System.out.println("Current absolute path is: " + s);
        JFileChooser chooser = new JFileChooser(s);
        int response = chooser.showSaveDialog(null);
        if (response == JFileChooser.APPROVE_OPTION) {
            File file = chooser.getSelectedFile();
            String absPath = file.getAbsolutePath();
            try {
                playerToSave.saveToFile(absPath);
            } catch (IOException e) {
                errorOccurred();
            }
        }
    }

    /**
     * asks player to load player profile from file
     */
    protected void askLoadPlayer() {
        if (state == 1) {
            JOptionPane.showMessageDialog(null, 
            "You cannot load at this time", "Unable to load", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        String[] optionString = {"Load Player 1", "Load Player 2", "Do not load"}; 
        int choice = JOptionPane.showOptionDialog(null, 
        "Pick which player to load",
        "loading Player data from a file",
        JOptionPane.YES_NO_CANCEL_OPTION,
        JOptionPane.INFORMATION_MESSAGE,
        null,
        optionString, 0);
        if (choice == 2) {
            return; // user selected "cancel"
        } else if (choice == 0){
            launchFileChooserLoadPlayer(player1);
        } else if (choice == 1){
            launchFileChooserLoadPlayer(player2);
        }
        return;
    }

    private void launchFileChooserLoadPlayer(Player playerToSave) {
        Path currentRelativePath = Paths.get("");
        String s = currentRelativePath.toAbsolutePath().toString();
        JFileChooser chooser = new JFileChooser(s);
        int response = chooser.showOpenDialog(null);
        if (response == JFileChooser.APPROVE_OPTION) {
            File file = chooser.getSelectedFile();
            String absPath = file.getAbsolutePath();
            try {
                playerToSave.loadFromFile(absPath);
                updateLowPanel();
                this.getContentPane().repaint();
                this.getContentPane().validate();
            } catch (IOException e) {
                errorOccurred();
            }
        }
    }

    private void errorOccurred() {
        int choice = JOptionPane.showConfirmDialog(null, "Error occured. Restart program?",
          "Error", JOptionPane.YES_NO_OPTION, JOptionPane.ERROR_MESSAGE);
        if (choice == 0) {
            new GameUI();
        } else {
            System.exit(-1);
        }
        return; 
    }

    private void askSaveGame() {
        TicTacToe ticTacToeGame;
        NumTicTacToe numTicTacToeGame;
        if (state == 1) {
           JOptionPane.showMessageDialog(null, 
           "You cannot save at this time", "Unable to save", JOptionPane.INFORMATION_MESSAGE);
        } else if (state == 2) { 
            ticTacToeGame = subGamePanel.getTicTacToe(); 
            launchFileChooserSaveGameTicTacToe(ticTacToeGame);
        } else if (state == 3) {
            numTicTacToeGame = subGamePanel2.getNumTicTacToe(); 
            launchFileChooserSaveGameNumTicTacToe(numTicTacToeGame);
        }
        return;
    }

    private void launchFileChooserSaveGameTicTacToe(TicTacToe currentGame) {
        Path currentRelativePath = Paths.get("");
        String s = currentRelativePath.toAbsolutePath().toString();
        JFileChooser chooser = new JFileChooser(s);
        int response = chooser.showSaveDialog(null);
        if (response == JFileChooser.APPROVE_OPTION) {
            File file = chooser.getSelectedFile();
            String absPath = file.getAbsolutePath();
            try {
                currentGame.saveToFile(absPath);
            } catch (IOException e) {
                errorOccurred();
            }
        }
    }

    private void launchFileChooserSaveGameNumTicTacToe(NumTicTacToe currentGame) {
        Path currentRelativePath = Paths.get("");
        String s = currentRelativePath.toAbsolutePath().toString();
        //System.out.println("Current absolute path is: " + s);
        JFileChooser chooser = new JFileChooser(s);
        int response = chooser.showSaveDialog(null);
        if (response == JFileChooser.APPROVE_OPTION) {
            File file = chooser.getSelectedFile();
            String absPath = file.getAbsolutePath();
            try {
                currentGame.saveToFile(absPath);
            } catch (IOException e) {
                errorOccurred();
            }
        }
    }

    private void askLoadGame() {
        TicTacToe ticTacToeGame;
        NumTicTacToe numTicTacToeGame;
        if (state == 1) {
           JOptionPane.showMessageDialog(null, 
           "You cannot load at this time", "Unable to load", JOptionPane.INFORMATION_MESSAGE);
        } else if (state == 2) { 
            ticTacToeGame = subGamePanel.getTicTacToe(); 
            launchFileChooserLoadGameTicTacToe(ticTacToeGame);
        } else if (state == 3) {
            numTicTacToeGame = subGamePanel2.getNumTicTacToe(); 
            launchFileChooserLoadGameNumTicTacToe(numTicTacToeGame);
        }
        return;
    }

    private void launchFileChooserLoadGameTicTacToe(TicTacToe ticTacToeGame) {
        Path currentRelativePath = Paths.get("");
        String s = currentRelativePath.toAbsolutePath().toString();
        JFileChooser chooser = new JFileChooser(s);
        int response = chooser.showOpenDialog(null);
        if (response == JFileChooser.APPROVE_OPTION) {
            File file = chooser.getSelectedFile();
            String absPath = file.getAbsolutePath();
            try {
                ticTacToeGame.loadFromFile(absPath);
                subGamePanel.updateGameView();
                this.getContentPane().repaint();
                this.getContentPane().validate();
            } catch (IOException e) {
                errorOccurred();
            }
        }
    }
    
    private void launchFileChooserLoadGameNumTicTacToe(NumTicTacToe numTicTacToeGame) {
        Path currentRelativePath = Paths.get("");
        String s = currentRelativePath.toAbsolutePath().toString();
        JFileChooser chooser = new JFileChooser(s);
        int response = chooser.showOpenDialog(null);
        if (response == JFileChooser.APPROVE_OPTION) {
            File file = chooser.getSelectedFile();
            String absPath = file.getAbsolutePath();
            try {
                numTicTacToeGame.loadFromFile(absPath);
                subGamePanel2.updateGameView();
                this.getContentPane().repaint();
                this.getContentPane().validate();
            } catch (IOException e) {
                errorOccurred();
            }
        }
    }

    /**
     * getter for player1
     * @return the instance variable player1
     */
    public Player getPlayer1() {
        return player1;
    }
    /**
     * getter for player2
     * @return the instance variable player2
     */
    public Player getPlayer2() {
        return player2;
    }

    /**
     * main method for creating the GUI
     * @param args not important/useful, only here because of checkstyle
     */
    public static void main(String[] args) {
        new GameUI();
    }
}
