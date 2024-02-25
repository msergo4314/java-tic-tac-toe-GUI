package boardgame;

import java.io.IOException;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * class representing a game of NumTicTacToe
 * implements Saveable, and extends Boardgame
 * @author Martin Sergo
 * @version 1.0
 */
public class NumTicTacToe extends BoardGame implements Saveable{

    private int currentPlayer = 1;  // Odd numbers goes first 
    private boolean done;
    private String gameStateMessage;

    private ArrayList<String> validStrings1 = new ArrayList<String>(Arrays.asList("1", "3", "5", "7", "9"));
    
    private ArrayList<String> validStrings2 = new ArrayList<String>(Arrays.asList("0", "2", "4", "6"));

    /**
     * constructor
     */
    public NumTicTacToe() {
        super(3, 3);
        done = false;
    }
    /* (non-Javadoc)
     * @see boardgame.BoardGame#takeTurn(int, int, java.lang.String)
     */
    @Override
    public boolean takeTurn(int across, int down, String input) {
        try {
            validateLocation(down, across);
            validateInput(input);
            setValue(across, down, input);
            if (!isDone()) {
                switchCurrentPlayer();
                setGameStateMessage(nextPlayerMessage());
            }
            return true;
        } catch (Exception e) {
            setGameStateMessage(e.getMessage());
        }
        return false;
    }
    /**
     * creates a new Game
     * @param player the current player
     */
    public void newTicTacToeGame(int player) {
        super.newGame();
        setDone(false);
        setCurrentPlayer(player);
    }

    /* (non-Javadoc)
     * @see boardgame.BoardGame#takeTurn(int, int, int)
     */
    @Override
    public boolean takeTurn(int across, int down, int input) {
        return false; // not used so just a stub
    }

    /* (non-Javadoc)
     * @see boardgame.BoardGame#isDone()
     */
    @Override
    public boolean isDone() {
        if (!done) {
            done = horizontalWin();
        }
        if (!done) {
            done = verticalWin();
        }
        if (!done) {
            done = diagonalWin();
        }
        if (!done) {
            done = isFull();
        }
        return done;
    }

    /* (non-Javadoc)
     * @see boardgame.BoardGame#getWinner()
     */
    @Override
    public int getWinner() {
        if(done) {
            if (horizontalWin()) {
                return getCurrentPlayer();
            }
            if (verticalWin()) {
                return getCurrentPlayer();
            }
            if (diagonalCheck()) {
                return getCurrentPlayer();
            }
            if (isFull()) {
                return 0; // case for tie
            }
        }
        return -1; // case for no winner
    }

    /* (non-Javadoc)
     * @see boardgame.BoardGame#getGameStateMessage()
     */
    @Override
    public String getGameStateMessage() {
        return gameStateMessage;
    }

    private void setGameStateMessage(String theString) {
        gameStateMessage = theString;
    }

    /**
     * loads a game from a file
     * @param absPath the absolute path of the file as a String
     * @throws IOException throws if file not found
     */
    public void loadFromFile(String absPath) throws IOException {
        String fileStr = "";
        BufferedReader reader = new BufferedReader(new FileReader(absPath));
        String line;
        while((line = reader.readLine()) != null) {
            fileStr = fileStr.concat(line + "\n"); // need to manually add the /n back
        }
        reader.close();
        fileStr.trim();
        loadSavedString(fileStr);
        return;
    }
    /**
     * used to return message
     * @return the next message indicating which player's turn it is
     */
    protected String nextPlayerMessage(){
        String player = "Player 1";
        if(currentPlayer == 2){
            player = "Player 2";
        }
        return(player + ", please indicate where you would like to place your token.");
    }
    
    /* (non-Javadoc)
     * @see boardgame.Saveable#getStringToSave()
     */
    @Override
    public String getStringToSave() {
        String gameString = ""; // initially empty
        Iterator<String> iter = iterator();
        String currentChar = "";
        gameString = gameString.concat(findLastPlayer());
        int col = 1;
        while(iter.hasNext()) {
            currentChar = iter.next();
            gameString = gameString.concat(addStrings(currentChar, col));
            if(col == getWidth()) {
              gameString = gameString.concat("\n"); // add newline after three columns
              col = 0; // back to column one (will increment in next line)
            }
            col++;
            currentChar = getNextValue();
        }
        return gameString;
    }

    private String findLastPlayer() {
        if (!isDone()) { // if game is NOT over the last player to go was not current player
            if (getCurrentPlayer() == 1) { // if Odd was last
                return ("E\n"); // if X's turn then O was last
            } else {
                return("O\n"); // if Odd turn then Even was last
            }
        } else {
            if (getCurrentPlayer() == 1) {
               return ("O\n"); // if Odd was last and game has ended
            } else {
                return("E\n");
            }
        }
    }

    /**
     * determines if a board spot needs a comma after it
     * @param givenStr the given string representing the board spot
     * @param col the column of the spot
     * @return the updated string
     */
    private String addStrings(String givenStr, int col) {
        if (!givenStr.equals("")) { // if not blank
            if (col != getWidth()) { // if not final column
                return givenStr.concat(","); // append comma
            } else {
                return givenStr;
            }
        } else if (col != getWidth()) {
            return ","; // if no character, return just comma
        }
        return ""; // if no character AND final column do not print comma
    }

    /* (non-Javadoc)
     * @see boardgame.Saveable#loadSavedString(java.lang.String)
     */
    @Override
    public void loadSavedString(String toLoad) {
        if (toLoad.charAt(0) == 'E') {
            toLoad = toLoad.replaceFirst("E\n", ""); // remove last player
            setCurrentPlayer(1);
        } else if (toLoad.charAt(0) == 'O') {
            toLoad = toLoad.replaceFirst("O\n", "");
            setCurrentPlayer(2);
        }
        String[] outer = toLoad.split("\n");
        for (int i =0; i < outer.length; ++i) {
            parseStr(outer[i], i+1);
        }
    }

    /**
     * saves the game to a file in csv format
     * @param absPath the absolute path of the file
     * @throws IOException throws if file was not located
     */
    public void saveToFile(String absPath) throws IOException{
        BufferedWriter writer = new BufferedWriter(new FileWriter(absPath));
        writer.write(getStringToSave());
        writer.close();
        return;
    }

    private void parseStr(String str, int row) {
        String[] miniStr = str.split(",");
        int i = 0;
        for (String w: miniStr) {
            //System.out.println(w);
            if (w.equals("")) {
                setValue(i+1, row, " ");
            } else {
                setValue(i+1, row, w); // just add
            }
            i++;
        }
        return;
    }

    /**
     * validates a board location
     * @param rows the row desired
     * @param col the column desired
     * @throws Exception throws if spot taken or not on board
     */
    public void validateLocation(int rows, int col) throws Exception {
        if (rows > getHeight() || col > getWidth() || col < 1 || rows < 1 || !getCell(col, rows).equals(" ")) {
            throw new IllegalArgumentException("The location given is not valid!");
        }
        return;
    }

    /**
     * checks if the input string is a valid entry
     * @param input string to be checked
     * @throws Exception throws exception if input won't work
     */
    public void validateInput(String input) throws Exception {
        if (getCurrentPlayer() == 1) {
            int i = 0;
            for (String s: validStrings1) {
                if (s.equals(input)) {
                    validStrings1.remove(i);
                    return;
                }
                i++;
            }
        } else if (getCurrentPlayer() == 2) {
            int i = 0;
            for (String s: validStrings2) {
                if (s.equals(input)) {
                    validStrings2.remove(i);
                    return;
                }
                i++;
            }
        }
        throw new IllegalArgumentException("That input was invalid!");
    }

    private boolean rowCheck(int row){
        int sum = 0;
        for (int i = 1; i <= getWidth(); ++i) {
            if(!getCell(i, row).equals(" ")) { // checks for spaces
                sum += Integer.parseInt(getCell(i, row));
            }
        }
        if (sum == 15) {
            return true;
        }
        return false;
    }
    
    private boolean columnCheck(int col){
        int sum = 0;
        for (int i = 1; i <= getWidth(); ++i) {
            if(!getCell(col, i).equals(" ")) { // checks for spaces
                sum += Integer.parseInt(getCell(col, i));
            }
        }
        if (sum == 15) {
            return true;
        }
        return false;
    }
    
    private boolean diagonalCheck(){
        int sum = 0;
        if (getCell(2,2).equals(" ")) {
            return false; // can't have diagonals if middle is still blank
        }
        sum += Integer.parseInt(getCell(2,2)); // add diagonal to sum
        if (!getCell(1,1).equals(" ") && (!getCell(3,3).equals(" "))) {
            sum += Integer.parseInt(getCell(1, 1)) + Integer.parseInt(getCell(3, 3));
        } else if (!getCell(1,3).equals(" ") && (!getCell(3,1).equals(" "))) {
            sum += Integer.parseInt(getCell(1, 3)) + Integer.parseInt(getCell(3, 1));
        }
        if (sum == 15) {
            return true;
        }
        return false;
    
    }
     /**
     * returns bool indicating if there is a horizontal win
     * @return bool indicating if there is a horizontal win
     */
    private boolean horizontalWin(){
        // check each row
        for(int i = 1; i <= getHeight(); i++) {
            if (rowCheck(i)) {
                return true;
            }
        }
        return false;
    }
     /**
     * returns bool indicating if there is a vertical win
     * @return bool indicating if there is a vertical win
     */
    private boolean verticalWin(){
        for(int i = 1; i <= getHeight(); i++) {
            if (columnCheck(i)) {
                return true;
            }
        }
     return false;    
    }
        
    /**
     * returns bool indicating if there is a diagonal win
     * @return bool indicating if there is a diagonal win
     */
    private boolean diagonalWin(){
        return diagonalCheck();    
    }

    /**
     * swap player turns
     */
    private void switchCurrentPlayer() {
        if(currentPlayer == 1){
            currentPlayer = 2;
        }else{
            currentPlayer = 1;
        }
    }

    /**
     * checks if the board is full
     * @return boolean indicating if board is full
     */
    private boolean isFull(){
        Iterator<String> iter = iterator();
        int counter = 0;
            while(iter.hasNext()){
                if(!iter.next().equals(" ")){
                counter++;
                }
            }
        if(counter == getWidth()*getHeight()){
            return true;
        }
        return false;
    
    }

    private void setCurrentPlayer(int newPlayer) {
        this.currentPlayer = newPlayer;
    }

    /**
     * getter for "done"
     * @return the boolean "done"
     */
    public boolean getDone() {
        return done;
    }

    /**
     * getter for currentPlayer
     * @return returns player who's turn it is
     */
    public int getCurrentPlayer() {
        return currentPlayer;
    }

    /**
     * sets the done boolean to input
     * @param bool the desired bool
     */
    public void setDone(boolean bool) {
        done = bool;
        return;
    }

}