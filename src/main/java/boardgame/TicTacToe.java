package boardgame;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Iterator;
import java.io.IOException; 

/**
 * class representing a game of TicTacToe
 * implements Saveable, and extends Boardgame
 * @author Martin Sergo
 * @version 1.0
 */
public class TicTacToe extends BoardGame implements Saveable{

    private int currentPlayer = 1;  // X goes first 
    private boolean done;
    private String gameStateMessage;
    private static String[] validStrings = {"X", "O"};

    /**
     * constructor
     */
    public TicTacToe() {
        super(3,3);
        this.done = false;
        nextPlayerMessage();
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
     * creates a new TicTacToe game
     * @param player the player who's turn will be set
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
                return horizontalInt();
            }
            if (verticalWin()) {
                return verticalInt();
            }
            if (diagonalCheck()) {
                return diagonalInt();
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
     * creates the next message and returns it
     * @return returns the next message, indicating who's turn it is
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
        //System.out.println("width is " + getWidth());
        while(iter.hasNext()) {
            currentChar = iter.next();
            //System.out.println("current char is " + currentChar + " and col is " + col);
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
            if (getCurrentPlayer() == 1) {
                return ("O\n"); // if X's turn then O was last
            } else {
                return("X\n"); // if O's turn then X was last
            }
        } else {
            if (getCurrentPlayer() == 1) {
               return ("X\n");
            } else {
                return("O\n");
            }
        }
    }

    private String addStrings(String givenStr, int col) {
        if (givenStr.equals("X")) {
            if (col != getWidth()) {
                return givenStr.concat(",");
            } else {
                return givenStr;
            }
        } else if (givenStr.equals("O")) {
            if (col != getWidth()) {
                return givenStr.concat(",");
            } else {
                return givenStr;
            }
        }
        if (col != getWidth()) {
            return ","; // if no character, return just comma
        }
        return ""; // if no character AND final column do not print comma
    }

    /* (non-Javadoc)
     * @see boardgame.Saveable#loadSavedString(java.lang.String)
     */
    @Override
    public void loadSavedString(String toLoad) {
        //System.out.printf("original String is: " + toLoad);
        if (toLoad.charAt(0) == 'X') {
            toLoad = toLoad.replaceFirst("X\n", "");
            setCurrentPlayer(2);
        } else if (toLoad.charAt(0) == 'O') {
            toLoad = toLoad.replaceFirst("O\n", "");
            setCurrentPlayer(1);
        }
        //System.out.printf("reduced string:\n%s\n", toLoad);
        String[] outer = toLoad.split("\n");
        for (int i =0; i < outer.length; ++i) {
            //outer[i] = outer[i].concat(",");
            //System.out.printf("substring %d: %s\n",i+1, outer[i]);
            parseStr(outer[i], i+1);
        }
    }

    /**
     * saves the game data to a file in CSV format
     * @param absPath the absolute path of the file as a string
     * @throws IOException throws if file not found
     */
    public void saveToFile(String absPath) throws IOException{
        BufferedWriter writer = new BufferedWriter(new FileWriter(absPath));
        writer.write(getStringToSave());
        writer.close();
        return;
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
            //System.out.print(line + "\n");
        }
        fileStr.trim(); // remove trailing whitespace
        reader.close();
        loadSavedString(fileStr);
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
     * tests if a certain spot is valid(on the board, and not already taken)
     * @param rows given row
     * @param col given column
     * @throws Exception throws if location is invalid
     */
    public void validateLocation(int rows, int col) throws Exception {
        if (rows > getHeight() || col > getWidth() || col < 1 || rows < 1 || !getCell(col, rows).equals(" ")) {
            throw new IllegalArgumentException("The location given is not valid!");
        }
        return;
    }

    /** validates a string input
     * @param input the string input
     * @throws Exception throws if invalid string
     */
    public void validateInput(String input) throws Exception {
        for (String currentValidStr: validStrings) {
            if(currentValidStr.equals(input)) {
                return;
            }
        }
        throw new IllegalArgumentException("That input was invalid!");
    }

    private boolean rowCheck(int row){
        boolean match = false;
        if(!getCell(1, row).equals(" ")) { // checks for spaces
            match = getCell(1, row).equals(getCell(2, row));
        }
        if (match) {
            match = getCell(2, row).equals(getCell(3, row));
        }
        return match;
    }
    
    private boolean columnCheck(int col){
        boolean match = false;

        if (!getCell(col, 1).equals(" ")) {
            match = getCell(col, 1).equals(getCell(col, 2));
        }
        if (match) {
            match = getCell(col, 2).equals(getCell(col, 3));
        }
        return match;
    
    }
    
    private boolean diagonalCheck(){
        if (getCell(2,2).equals(" ")) {
            return false; // can't have diagonals if middle is still blank
        }
        if (getCell(1,1).equals(getCell(3,3))
        && getCell(2,2).equals(getCell(3,3))) {
            return true;
        } else if (getCell(3,1).equals(getCell(1,3))
        && getCell(2,2).equals(getCell(1,3))) {
            return true;
        }
        return false;
    
    }
    private boolean horizontalWin(){
        // check each row
        for(int i = 1; i <= getHeight(); i++) {
            if (rowCheck(i)) {
                return true;
            }
        }
        return false;
    }
        
    private boolean verticalWin(){
        for(int i = 1; i <= getHeight(); i++) {
            if (columnCheck(i)) {
                return true;
            }
        }
     return false;    
    }
        
    private boolean diagonalWin(){
        return diagonalCheck();    
    }

    private void switchCurrentPlayer() {
        if(currentPlayer == 1){
            currentPlayer = 2;
        }else{
            currentPlayer = 1;
        }
    }

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
     * returns instance variable done
     * @return the instance of done
     */
    public boolean getDone() {
        return done;
    }

    /**
     * getter for current player
     * @return the current player who's turn it is
     */
    public int getCurrentPlayer() {
        return this.currentPlayer;
    }

    private int horizontalInt() {
        for (int i=1; i <= 3; ++i) { // only used when game is won by horizontal line
            if (rowCheck(i)) {
                if (getCell(1,i).equals("X")) {
                    return 1;
                } else {
                    return 2;
                }
            }
        }
        return -1; // should never occur
    }

    private int verticalInt() {
        for (int i=1; i <= 3; ++i) {
            if (columnCheck(i)) {
                if (getCell(i,1).equals("X")) {
                    return 1;
                } else {
                    return 2;
                }
            }
        }
        return -1; // should never occur
    }

    private int diagonalInt() { // only used when game is already won by diagonal
        if (getCell(2,2).equals("X")) {
            return 1;
        } else if (getCell(2,2).equals("O")) {
            return 2;
        }
        return -1; // should never occur as well
    }

    /**
     * setter for "done"
     * @param bool the value done is set to
     */
    public void setDone(boolean bool) {
        done = bool;
        return;
    }

}
