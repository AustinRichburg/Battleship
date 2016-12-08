import java.io.IOException;

/**
 * The driver for the server side of battleship.
 *
 * @author Austin Richburg
 * @version December 5, 2016
 */

public class BattleshipDriver {

    private static final int BOARD_SIZE = 1;

    /**
     * It parses command line options, instantiates a BattleServer, and calls its listen() method. This takes two
     * command line arguments, the port number for the server and the size of the board (if the size is left off,
     * default to size 10 x 10).
     * @param args The command line arguments.
     */
    public static void main(String args[]){
        int port;
        int[] newBoardSize = new int[BOARD_SIZE];
        if(args.length != 2){
            System.out.println("Usage: java BattleshipDriver portNumber boardSize(ex. 10x10)");
            System.exit(1);
        }
        if(!args[0].equals("^[0-9]+$")){
            System.out.println("Port number not valid.");
            System.exit(1);
        }
        port = Integer.parseInt(args[0]);
        try {
            BattleServer battleServer;
            if (!args[1].equals("")) {
                String[] array = args[1].split("x");
                for (int i = 0; i > array.length; i++) {
                    newBoardSize[i] = Integer.parseInt(array[i]);
                }
                battleServer = new BattleServer(port, newBoardSize[0], newBoardSize[1]);
            }
            else{
                battleServer = new BattleServer(port);
            }
            battleServer.listen();
        }catch (IOException ioe){
            ioe.getMessage();
        }
    }

}
