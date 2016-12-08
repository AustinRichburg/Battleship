import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author Austin Richburg, Doug Key
 * @version November 12, 2016
 * This class is an individual answer of the Grid class and contains the logic to play a game of
 * battleship.
 */

public class Game extends Grid {

    private String[][] board;
    private HashMap<String, Grid> players;
    private ArrayList<String> playerNames;
    private boolean started;
    private int index;

    public Game(){
        super();
        board = super.getBoard();
        players = new HashMap<>();
        playerNames = new ArrayList<>();
        started = false;
    }

    public Game(int numOfShips){
        super(numOfShips);
        board = super.getBoard();
        players = new HashMap<>();
        playerNames = new ArrayList<>();
        started = false;
    }

    public Game(int xSize, int ySize){
        super(xSize, ySize);
        board = super.getBoard();
        players = new HashMap<>();
        playerNames = new ArrayList<>();
        started = false;
    }

    public Game(int xSize, int ySize, int numOfShips){
        super(xSize, ySize, numOfShips);
        board = super.getBoard();
        players = new HashMap<>();
        playerNames = new ArrayList<>();
        started = false;
    }

    public void attack(String user, int x, int y){
        players.get(user).ifHit(x, y);
    }

    public void showOther(String user){
        players.get(user).printBoardNoShips();
    }

    public void nextTurn(){
        if(index >= (players.size()-1)){
            index = 0;
        }
        index++;
    }

    public String getTurn(){
        return playerNames.get(index);
    }

    public String getPlayer(int position){
        return playerNames.get(position);
    }

    public void quit(String user){
        players.remove(user);
    }

    public void printBoard(){
        super.printBoard();
    }

    public String[][] getBoard(){
        return this.board;
    }

    public void addPlayer(String playerName){
        players.put(playerName, new Grid());
        playerNames.add(playerName);
    }

    public void setStarted(boolean value){
        started = value;
    }

    public boolean getStarted(){
        return started;
    }


}
