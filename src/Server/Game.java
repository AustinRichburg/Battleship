package Server;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author Austin Richburg, Doug Key
 * @version November 12, 2016
 * This class is an individual instance of the Grid class and contains the logic to play a game of
 * battleship.
 */

public class Game extends Grid {

    /** A hash map with keys of player names and values of their boards */
    private HashMap<String, Grid> players;

    /** An array list of the player names */
    private ArrayList<String> playerNames;

    /** If the game has started or not */
    private boolean started;

    /** The index of the player's turn in the playerNames array list */
    private int index;

    /**
     * Default constructor.
     */
    public Game(){
        super();
        players = new HashMap<>();
        playerNames = new ArrayList<>();
        started = false;
    }

    /**
     * Creates a game with the specified number of ships
     * @param numOfShips The number of ships
     */
    public Game(int numOfShips){
        super(numOfShips);
        players = new HashMap<>();
        playerNames = new ArrayList<>();
        started = false;
    }

    /**
     * Creates a game of the specified size
     * @param xSize The number of rows
     * @param ySize The number of columns
     */
    public Game(int xSize, int ySize){
        super(xSize, ySize);
        players = new HashMap<>();
        playerNames = new ArrayList<>();
        started = false;
    }

    /**
     * Making a game of a certain size and with a certain number of ships
     * @param xSize The number of rows
     * @param ySize The number of columns
     * @param numOfShips The number of ships
     */
    public Game(int xSize, int ySize, int numOfShips){
        super(xSize, ySize, numOfShips);
        players = new HashMap<>();
        playerNames = new ArrayList<>();
        started = false;
    }

    /**
     * Attacks a user at a specific point
     * @param user The user that is being attacked
     * @param x The x coordinate being targeted
     * @param y The y coordinate being targets
     * @return If it was a hit or a miss
     */
    public String attack(String user, int x, int y){
        String result = "User does not exist";
        if(players.get(user) != null ) {
            players.get(user).ifHit(x, y);
            result = ifHit(x, y);
        }
        return result;
    }

    /**
     * Shows another user's board, sans ships
     * @param user The user
     * @return The user's board
     */
    public String showOther(String user){
        String board = "User does not exist";
        if(players.get(user) != null) {
            board = players.get(user).printBoardNoShips();
        }
        return board;
    }

    /**
     * Progresses the turn to the next user in the array list
     */
    public void nextTurn(){
        if(index >= (players.size()-1)){
            index = 0;
        }
        index++;
    }

    /**
     * Gets the current user's turn
     * @return Whose turn it is
     */
    public String getTurn(){
        return playerNames.get(index);
    }

    /**
     * Removes a user when they quit
     * @param user The user who is quitting
     */
    public void quit(String user){
        players.remove(user);
    }

    /**
     * Prints the user's board with ships
     * @param user The user
     * @return The board with ships
     */
    public String printBoard(String user){
        return players.get(user).printBoard();
    }

    /**
     * Adds a player to the game
     * @param playerName The player's name
     */
    public void addPlayer(String playerName){
        players.put(playerName, new Grid());
        playerNames.add(playerName);
    }

    /**
     * Sets the game to 'started'
     * @param value If the game has started or not
     */
    public void setStarted(boolean value){
        started = value;
    }

    /**
     * Gets if the game has started
     * @return If the game has started or not
     */
    public boolean getStarted(){
        return started;
    }


}
