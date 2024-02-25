package boardgame;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.Font;
import java.awt.GridLayout;

import boardgame.ui.PositionAwareButton;

public class NumTicTacToeView extends JPanel {

    private GameUI root;
    private NumTicTacToe myGame;
    private PositionAwareButton[][] buttonGrid;
    private JLabel topString;
    private JPanel gamePanel;

    public NumTicTacToeView(GameUI givenRoot) {
        super();
        //setSize(400, 400);
        //setBackground(Color.BLUE);
        root = givenRoot;
        setLayout(new BorderLayout());
        setGameController(new NumTicTacToe()); // set the board to be a new board first
        topString = new JLabel();
        topString.setText("Numerical Tic Tac Toe Board");
        topString.setFont(new Font("Times New Roman", Font.BOLD, 14));

        add(topString, BorderLayout.NORTH); // add label at top
        add(makeRestartButton(), BorderLayout.SOUTH);
        gamePanel = makeButtonGrid();
        add(gamePanel, BorderLayout.CENTER);
        //root.askLoadPlayer();
    }
    
    protected NumTicTacToe getNumTicTacToe() {
        return this.myGame;
    }

    public void setGameController(NumTicTacToe argument) {
        this.myGame = argument; // set the game
    }
    private JButton makeRestartButton() {
        JButton button = new JButton("Restart Game");
        button.setEnabled(true); // button is enabled to start
        button.addActionListener(e -> root.newNTTT());
        button.setFocusable(false); // looks better
        button.setFont(new Font("Times New Roman", Font.PLAIN, 14));
        return button;
    }
    private JPanel makeButtonGrid() {
        JPanel panel = new JPanel();
        buttonGrid = new PositionAwareButton[3][3]; // hardcode the length
        panel.setLayout(new GridLayout(3,3, 5, 5)); // 10 pixels of space
        for(int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                buttonGrid[i][j] = new PositionAwareButton(); // no title
                buttonGrid[i][j].setBorder(BorderFactory.createEtchedBorder()); // border
                buttonGrid[i][j].setAcross(j);
                buttonGrid[i][j].setDown(i);
                buttonGrid[i][j].setFont(new Font("MV Boli", Font.BOLD, 14));
                buttonGrid[i][j].setFocusable(false);
                //buttonGrid[i][j].setSize(40, 40);
                buttonGrid[i][j].setMinimumSize(new Dimension(40, 40));
                if (!myGame.getCell(j+1, i+1).equals(" ")) {
                    buttonGrid[i][j].setText(myGame.getCell(j+1, i+1));
                    buttonGrid[i][j].setEnabled(false); // turn off
                }
                buttonGrid[i][j].addActionListener(e->{
                    putStringOnBoard(e);
                    getGameState();
                    });
                panel.add(buttonGrid[i][j], BorderLayout.CENTER);
            }
        }
        panel.setBorder(BorderFactory.createEtchedBorder());
        return panel;
    }

    private void getGameState() {
        String winStr = "";
        if(myGame.isDone()){
            if (myGame.getWinner() == 0) {
                winStr = "The game was a tie!";
                root.getPlayer1().setGamesLost((root.getPlayer1().getGamesLost()+1));
                root.getPlayer2().setGamesLost((root.getPlayer2().getGamesLost()+1));
            } else if (myGame.getCurrentPlayer() == 1) {
                winStr = "Player one wins!";
                root.getPlayer1().setGamesWon((root.getPlayer1().getGamesWon()+1));
                root.getPlayer2().setGamesLost((root.getPlayer2().getGamesLost()+1));
            } else if (myGame.getCurrentPlayer() == 2) {
                winStr = "Player two wins!";
                root.getPlayer1().setGamesLost((root.getPlayer1().getGamesLost()+1));
                root.getPlayer2().setGamesWon((root.getPlayer2().getGamesWon()+1));
            }
            root.getPlayer1().setGamesPlayed((root.getPlayer1().getGamesPlayed()+1));
            root.getPlayer2().setGamesPlayed((root.getPlayer2().getGamesPlayed()+1));
            int selection = JOptionPane.showConfirmDialog(null,
            winStr + " Would you like to play again?", "PlayAgain?", JOptionPane.YES_NO_OPTION);
            if(selection == JOptionPane.YES_OPTION){
                root.newNTTT();
            } else{
                root.begin();
            }
            //root.askSavePlayer();
        }
    }

    protected void updateGrid() {
        for (int i = 0; i < 3; i++){
            for (int j = 0; j < 3; j++){  
                buttonGrid[i][j].setText(myGame.getCell(buttonGrid[i][j].getAcross(),buttonGrid[i][j].getDown())); 
            }
        }
    }

    protected void newGame() {
        myGame.newGame();
        updateGrid();
    }

    public void updateGameView() {
        //updateGrid();
        remove(gamePanel);
        gamePanel = makeButtonGrid();
        //gamePanel.setBackground(Color.darkGray);
        add(gamePanel);
    }

    private void putStringOnBoard(ActionEvent e) {
        String num = "";
        String user;
        PositionAwareButton usedButton = ((PositionAwareButton)(e.getSource()));
        if(myGame.getCurrentPlayer() == 1) {
            user = "Player one";
        } else {
            user = "Player two";
        }     
        do {
            num = JOptionPane.showInputDialog("Please enter your number," + user, JOptionPane.PLAIN_MESSAGE);
            if (num == null) {
                break;
            }
            //System.out.println(num);
        } while (!myGame.takeTurn(usedButton.getAcross()+1,usedButton.getDown()+1, num));
        if(e.getSource() == usedButton && num != null) {
            usedButton.setEnabled(false); // cannot use again
        }
        
        if(num != null){
            usedButton.setText(myGame.getCell(usedButton.getAcross()+1,usedButton.getDown()+1));
        }
        return;
    }
}
