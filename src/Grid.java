import java.util.*;

/**
 * @author Austin Richburg, Doug Key
 * @version November 12, 2016
 * Creates a board for a game of battleship and randomly places the ships.
 */

public class Grid {

    /** The default number of columns */
    private static final int DEFAULT_X_SIZE = 10;

    /** The default number of rows */
    private static final int DEFAULT_Y_SIZE = 10;

    /** The board grid */
    private String[][] board;

    /** The default number of ships */
    private static final int DEFAULT_NUM_OF_SHIPS = 5;

    /** Hash map containing ships and their coordinates */
    private HashMap<String, List> ships;

    /**
     * Creates a default 10x10 board with 5 ships.
     */
    public Grid(){
        board = initBoard(DEFAULT_X_SIZE, DEFAULT_Y_SIZE);
        ships = new HashMap<>(DEFAULT_NUM_OF_SHIPS);
        addShips(DEFAULT_NUM_OF_SHIPS);
        placeShips(DEFAULT_X_SIZE, DEFAULT_Y_SIZE);
    }

    /**
     * Creates a 10x10 board with the user specified number of ships.
     * @param numOfShips The number of ships.
     */
    public Grid(int numOfShips){
        if(numOfShips >= 5){
            System.out.println("Error: Cannot have more than 5 ships.");
        }
        if(numOfShips == 0){
            System.out.println("Error: Must have at least one ship.");
        }
        board = initBoard(DEFAULT_X_SIZE, DEFAULT_Y_SIZE);
        ships = new HashMap<>(numOfShips);
        addShips(numOfShips);
        placeShips(DEFAULT_X_SIZE, DEFAULT_Y_SIZE);
    }

    /**
     * Creates a board with the user specified size and 5 ships.
     * @param xSize The number of columns.
     * @param ySize The number of rows.
     */
    public Grid(int xSize, int ySize){
        board = initBoard(xSize, ySize);
        ships = new HashMap<>(DEFAULT_NUM_OF_SHIPS);
        addShips(DEFAULT_NUM_OF_SHIPS);
        placeShips(xSize, ySize);
    }

    /**
     * Creates a new board with user specified size and number of ships
     * @param xSize The number of columns.
     * @param ySize The number of rows.
     * @param numOfShips The number of ships.
     */
    public Grid(int xSize, int ySize, int numOfShips){
        if(numOfShips >= 5){
            System.out.println("Error: Cannot have more than 5 ships");
        }
        if(numOfShips == 0){
            System.out.println("Error: Must have at least one ship.");
        }
        board = initBoard(xSize, ySize);
        ships = new HashMap<>(numOfShips);
        addShips(numOfShips);
        placeShips(xSize, ySize);
    }

    /**
     * Creates the board for a new game.
     * @param args Command line arguments. There are none in this case
     */
    public static void main(String args[]){
        Grid grid = new Grid();
        grid.printBoard();
    }

    /**
     * Creates the board based on the size the user specifies. The default board is 10x10.
     * @param xSize The number of columns.
     * @param ySize The number of rows.
     * @return The initialized board.
     */
    private String[][] initBoard(int xSize, int ySize){
        String[][] temp = new String[xSize][ySize];
        for(int i = 0; i < xSize; i++){
            for(int j = 0; j < ySize; j++){
                temp[i][j] = "   ";
            }
        }
        return temp;
    }

    /**
     * Prints the board.
     */
    public void printBoard(){
        for(int i = 0; i < board.length; i++){
            System.out.print("+---");
        }
        System.out.println("+");
        for(int i = 0; i < board.length; i++){
            //System.out.println();
            System.out.print("|");
            for(int j = 0; j < board[i].length; j++){
                System.out.print(board[i][j]);
                System.out.print("|");
            }
            System.out.println();
            for(int k = 0; k < board.length; k++){
                System.out.print("+---");
            }
            System.out.println("+");
        }
    }

    /**
     * Adds ships into the Hash Map for ships. The implementation of the Hash Map is:
     * <Name of the Ship, Coordinates of the ship>.
     * @param numOfShips The number of ships being used. Maximum of 5, default is 5.
     */
    private void addShips(int numOfShips){
        switch (numOfShips){
            case 5:
                ships.put("Aircraft Carrier", new ArrayList<>(Collections.nCopies(5, null)));
            case 4:
                ships.put("Battleship", new ArrayList<>(Collections.nCopies(4, null)));
            case 3:
                ships.put("Submarine", new ArrayList<>(Collections.nCopies(3, null)));
            case 2:
                ships.put("Cruiser", new ArrayList<>(Collections.nCopies(3, null)));
            case 1:
                ships.put("Destroyer", new ArrayList<>(Collections.nCopies(2, null)));
        }
    }

    private void placeShips(int xSize, int ySize){
        Random random = new Random();
        List<Integer[]> coordinates = new ArrayList<>();
        int x = 0, y = 0, direction = 0;
        boolean taken;
        for(String ship: ships.keySet()){
            for(int i = 0; i < ships.get(ship).size(); i++){
                if(i == 0) {
                    x = random.nextInt(xSize);
                    y = random.nextInt(ySize);
                    direction = random.nextInt();
                    taken = ifSpotTaken(x, y);
                }
                else{
                    if(direction % 2 == 0){
                        x++;
                        if(x == 10){
                            i = 0;
                            x = 0;
                            clearShip(ship);
                        }
                        taken = ifSpotTaken(x, y);
                    }
                    else{
                        y++;
                        if(y == 10){
                            i = 0;
                            y = 0;
                            clearShip(ship);
                        }
                        taken = ifSpotTaken(x, y);
                    }
                }
                if(taken){
                    i = 0;
                    clearShip(ship);
                }
                placeMark(x, y, ship);
                Integer[] temp = {x, y};
                coordinates.add(i, temp);
            }
            ships.get(ship).clear();
            ships.get(ship).addAll(coordinates);
            System.out.println(ship + ": " + Arrays.deepToString(ships.get(ship).toArray()));
            coordinates.clear();
        }
    }

    private void placeMark(int x, int y, String ship){
        switch (ship) {
            case "Aircraft Carrier":
                board[x][y] = " A ";
                break;
            case "Battleship":
                board[x][y] = " B ";
                break;
            case "Submarine":
                board[x][y] = " S ";
                break;
            case "Cruiser":
                board[x][y] = " C ";
                break;
            case "Destroyer":
                board[x][y] = " D ";
                break;
        }
    }

    private boolean ifSpotTaken(int x, int y){
        boolean taken = false;
        if(!board[x][y].equals("   ")){
            taken = true;
        }
        return taken;
    }

    private void clearShip(String ship){
        String string = "";
        switch (ship) {
            case "Aircraft Carrier":
                string = " A ";
                break;
            case "Battleship":
                string = " B ";
                break;
            case "Submarine":
                string = " S ";
                break;
            case "Cruiser":
                string = " C ";
                break;
            case "Destroyer":
                string = " D ";
                break;
            default:
                System.out.println("Ship not found.");
                break;
        }
        for(int i = 0; i < board.length; i++){
            for(int j = 0; j < board[i].length; j++){
                if(board[i][j].equals(string)){
                    board[i][j] = "   ";
                }
            }
        }
    }

    /**
     * Determines if the attack was a hit or a miss. A '@' symbol marks a miss and a 'X' is a hit.
     * @param x The x coordinate
     * @param y The y coordinate
     */
    public void ifHit(int x, int y){
        if(this.board[x][y].equals("   ") || this.board[x][y].equals(" @ ")){
            board[x][y] = " @ ";
        }
        else{
            board[x][y] = " X ";
        }
    }

    public String[][] getBoard(){
        return board;
    }

    public HashMap<String, List> getShips(){
        return ships;
    }

}