package boardgame;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class Player implements Saveable{

    private int gamesWon;
    private int gamesLost;
    private int gamesPlayed;
    private int playerID;

    public Player() {
    }

    /* (non-Javadoc)
     * @see boardgame.Saveable#getStringToSave()
     */
    @Override
    public String getStringToSave() {
        String data = "";
        data = data.concat(Integer.toString(getPlayerID()) + "\n");
        data = data.concat(Integer.toString(getGamesPlayed()) + "\n");
        data = data.concat(Integer.toString(getGamesWon()) + "\n");
        data = data.concat(Integer.toString(getGamesLost())); 
        return data;
    }

    /* (non-Javadoc)
     * @see boardgame.Saveable#loadSavedString(java.lang.String)
     */
    @Override
    public void loadSavedString(String toLoad) {
        String[] array = toLoad.split("\n", 4);
        if (array.length >= 4) {
            setPlayerID(Integer.parseInt(array[0]));
            setGamesPlayed(Integer.parseInt(array[1]));
            setGamesWon(Integer.parseInt(array[2]));
            array[3] = array[3].trim();
            setGamesLost(Integer.parseInt(array[3]));
        }
    }

    protected String getStringFromFile(String absPath) throws IOException {
        String str = "";
        BufferedReader reader = new BufferedReader(new FileReader(absPath));
        String line;
        while((line = reader.readLine()) != null) {
            str = str.concat(line + "\n"); // need to manually add the /n back
        }
        reader.close();
        return str;
    }

    /**
     * saves game data to a specified file in CSV format
     * @param absPath absolute path of file
     * @throws IOException when file not found
     */
    public void saveToFile(String absPath) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(absPath));
        writer.write(getStringToSave());
        writer.close();
    }

    /**
     * obtains player data from a file
     * @param absPath absolute path of the file to read from
     * @throws IOException
     */
    public void loadFromFile(String absPath) throws IOException {
        // read whole file as one string
        loadSavedString(getStringFromFile(absPath));
    }

    // various setters/getters
    public int getGamesWon() {
        return gamesWon;
    }

    public void setGamesWon(int won) {
        this.gamesWon = won;
    }

    public int getGamesLost() {
        return gamesLost;
    }

    public void setGamesLost(int lost) {
        this.gamesLost = lost;
    }

    public int getGamesPlayed() {
        return gamesPlayed;
    }

    public void setGamesPlayed(int played) {
        this.gamesPlayed = played;
    }

    public int getPlayerID() {
        return playerID;
    }

    public void setPlayerID(int id) {
        this.playerID = id;
    }
    
}
