package boardgame;
import java.util.Scanner;

/**
 * class that is run in the console and allows the user to play TicTacToe
 * @author Martin Sergo
 * @Version 1.3
 */
public class TextUI {

    private Scanner scanner = new Scanner(System.in);
    private TicTacToe ticTacToeGame = new TicTacToe();

    public TextUI() {
        // leave blank for now
    }

    /**
     * prints starter messages to console
     */
    public void printStartMessages() {
        System.out.println("Welcome to a game of Tic Tac Toe!"); 
        System.out.println("Player 1 (X) goes first, followed by player 2 (O)");
        System.out.print("The game is won when either player can"); 
        System.out.println(" make a line of 3 characters or all board spaces are used\n");
        return;
    }

    /**
     * declares the winner of the game
     */
    private void declareWinner() {
        if (ticTacToeGame.getWinner() == 1) {
            System.out.println("The winner of the game is player 1! ");
        } else if (ticTacToeGame.getWinner() == 2) {
            System.out.println("the winner of the game is player 2! ");
        } else if (ticTacToeGame.getWinner() == 0) {
            System.out.println("there was a tie!");
        }
        return;
    }

    /**
     * prints closing message to console
     */
    public void closingMessage() {
        System.out.println("Thank you for playing tic tac toe!");
        return;
    }

    /**
     * gettter for "done"
     * @return bool indicating if game is finished yet
     */
    public boolean getDone() {
        return ticTacToeGame.getDone();
    }

    /**
     * setter for "done"
     * @param bool what done is set to
     */
    public void setDone(boolean bool) {
        ticTacToeGame.setDone(bool);
        return;
    }

    /**
     * creates a new game with X going first
     */
    public void newGame() {
        ticTacToeGame.newTicTacToeGame(1); // empty board, sets player to 1, resets "done" state
    }

    /**
     * wrapper for the turn action
     * @param across desired column
     * @param down desired row
     * @param inputStr the input string 
     */
    public void doTurn(int across, int down, String inputStr) {
        //System.out.println(nextPlayerMessage());
            ticTacToeGame.takeTurn(across, down,inputStr);
    }

    /**
     * grabs row from user and does validation
     * @return returns desired row
     */
    public String getRow() {
        String str = "";
        do {
            System.out.print("Enter the row you want to pick (must be int, 1-3): ");
            str = scanner.next();
        } while (hasLetters(str) || Integer.parseInt(str) < 1 || Integer.parseInt(str) > 3);
        return str;
    }

    /**
     * grabs column from user and does validation
     * @return the column desired
     */
    public String getColumn() {
        String str = "";
        do {
            System.out.print("Enter the column you want to pick (must be int, 1-3): ");
            str = scanner.next();
        } while (hasLetters(str) || Integer.parseInt(str) < 1 || Integer.parseInt(str) > 3);
        return str;
    }

    /**
     * method for obtaining the current player
     * @return String indicating who's turn it is (X or O)
     */
    public String getStr() {
        if (ticTacToeGame.getCurrentPlayer() == 1) {
            return "X";
        } else {
            return "O";
        }
    }

    /**
     * wrapper for getting next player message
     * @return returns next message
     */
    public String nextPlayerMessage() {
        return ticTacToeGame.nextPlayerMessage();
    }
    
    private boolean hasLetters(String userStr) {
        boolean hasString = false;

        for(int i = 0; i < userStr.length(); ++i) {
            if (!(userStr.charAt(i) >= '0' && userStr.charAt(i) <= '9')) {
                hasString = true; // if non digit chars present returns true
            }
        }
        return hasString;
    }

    /**
     * asks user to replay the game
     * @return returns char indicating player response
     */
    public char askToReplay() {
        char response;
        do {
            System.out.print("Would you like to play another game of tic tac toe? Enter y/Y or n/N: ");
            response = this.scanner.next().charAt(0);
            this.scanner.nextLine();
        } while (response != 'n'
        && response != 'N'
        && response != 'y'
        && response != 'Y');
        System.out.println();
        return response;
    }

    /**
     * prints game board to console
     */
    public void printBoard() {
        System.out.println("\n 1 2 3");
        System.out.println("-------");
        System.out.print(ticTacToeGame.getStringGrid());
        System.out.println("-------");
        System.out.println(" 1 2 3\n");
        return;
    }

    /**
     * prints the next message
     */
    public void printNextMsg() {
        System.out.println(ticTacToeGame.nextPlayerMessage());
    }

    /* 
     * shows what the class purpose is
     */
    public String toString() {
        String remainder = "printing all text and allowing the user to play ticTacToe via terminal\n";
        return ("the TextUI class is reponsible for " + remainder);
    }

    /**
     * main method for running console TicTacToe game
     * @param args
     */
    public static void main(String[] args) {

        TextUI text = new TextUI();
        char continuePlaying = 'x';
        int row;
        int col;
        text.printStartMessages(); // print the starting messages to console
        do {
            text.newGame();
            do {
                text.printNextMsg();
                row = Integer.parseInt(text.getRow());
                col = Integer.parseInt(text.getColumn());
                text.doTurn(col, row, text.getStr());
                text.printBoard();
                //System.out.println("the string would be\n" + text.ticTacToeGame.getStringToSave());//remove 
                text.ticTacToeGame.loadSavedString(text.ticTacToeGame.getStringToSave());//remove me
                try {
                    text.ticTacToeGame.saveToFile("test.txt");
                } catch(Exception e) {
                    System.out.println("exception occured");
                    }
            } while (!text.getDone());
            text.declareWinner();
            continuePlaying = text.askToReplay();
        } while (continuePlaying != 'n' && continuePlaying != 'N');
        text.closingMessage();
        return;
    }
}
