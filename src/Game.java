import java.util.HashMap;

/**
 * @author Austin Richburg, Doug Key
 * @version November 12, 2016
 * This class is an individual answer of the Grid class and contains the logic to play a game of
 * battleship.
 */

public class Game extends Grid {

    private String name;
    private String[][] board;
    public HashMap<String, Grid> players;

    public Game(){
        super();
        board = super.getBoard();
    }

    public Game(int numOfShips){
        super(numOfShips);
        board = super.getBoard();
    }

    public Game(int xSize, int ySize){
        super(xSize, ySize);
        board = super.getBoard();
    }

    public Game(int xSize, int ySize, int numOfShips){
        super(xSize, ySize, numOfShips);
        board = super.getBoard();
    }

    public static void main(String args[]){
        Game game = new Game();
        //game.players.put("Austin", new Grid());
        System.out.println(game.getName());
        game.printBoard();
        game.ifHit(1, 7);
        game.ifHit(3, 6);
        game.ifHit(8, 2);
        game.ifHit(7, 9);
        game.ifHit(2, 1);
        game.ifHit(4, 6);
        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        game.printBoard();
    }

    public void ifHit(int x, int y){
        if(this.board[x][y].equals("   ") || this.board[x][y].equals(" @ ")){
            board[x][y] = " @ ";
        }
        else{
            board[x][y] = " X ";
        }
        printHit(x, y);
    }

    public void printHit(int x, int y){
        String shipString = "";
    }

    public void printBoard(){
        super.printBoard();
    }

    public String[][] getBoard(){
        return this.board;
    }

    public void setName(String newName){
        this.name = newName;
    }

    public String getName(){
        return name;
    }


}
