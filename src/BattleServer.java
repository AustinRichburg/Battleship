import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author Austin Richburg, Doug Key
 * @version December 5, 2016
 */

public class BattleServer implements MessageListener {

    private ServerSocket welcomeSocket;
    private ArrayList<Socket> connectionSockets;
    private HashMap<ConnectionInterface, String> players;
    private ExecutorService threadPool;
    private Game game;
    private boolean isReadyToStart;
    private int xSize, ySize;


    public BattleServer(int port) throws IOException {
        welcomeSocket = new ServerSocket(port);
        connectionSockets = new ArrayList<>();
        players = new HashMap<>();
        threadPool = Executors.newCachedThreadPool();
        xSize = -1;
        ySize = -1;
        isReadyToStart = false;
    }

    public BattleServer(int port, int xSize, int ySize) throws IOException {
        welcomeSocket = new ServerSocket(port);
        connectionSockets = new ArrayList<>();
        players = new HashMap<>();
        threadPool = Executors.newCachedThreadPool();
        this.xSize = xSize;
        this.ySize = ySize;
        isReadyToStart = false;
    }

    public void listen() throws IOException {
        int prevSize = connectionSockets.size();
        while(!game.getStarted()){
            connectionSockets.add(welcomeSocket.accept());
            if(connectionSockets.size() > prevSize){
                threadPool.submit(new ConnectionInterface(connectionSockets.get(prevSize)));
                prevSize = connectionSockets.size();
            }
            checkNumOfPlayers();
        }
        while(game.getStarted() && (connectionSockets.size() > 2)){

        }
    }

    /**
     * Used to notify observers that the subject has received a message.
     *
     * @param message The message received by the subject
     * @param source The source from which this message originated (if needed).
     */
    public void messageReceived(String message, MessageSource source){
        parseCommands(message.split(" "), (ConnectionInterface)source);
    }

    /**
     * Used to notify observers that the subject will not receive new messages; observers can
     * deregister themselves.
     *
     * @param source The <code>MessageSource</code> that does not expect more messages.
     */
    public void sourceClosed(MessageSource source){
        if(connectionSockets.size() < 2){

        }
    }

    private void parseCommands(String[] command, ConnectionInterface source){
        switch(command[0].toLowerCase()){
            case "join":
                players.put(source, command[1]);
            case "attack":
                if(game.getStarted() && game.getTurn().equals(players.get(source))) {
                    game.attack(command[1], Integer.parseInt(command[2]), Integer.parseInt(command[3]));
                }
                else{
                    source.send("Game has not begun.");
                }
                break;
            case "play":
                if(isReadyToStart){
                    if(xSize == -1 && ySize == -1) {
                        game = new Game();
                    }else{
                        game = new Game(xSize, ySize);
                    }
                    game.setStarted(true);
                }
                else{
                    source.send("Game not ready");
                }
                break;
            case "show":
                game.printBoard();
                game.showOther(command[1]);
                break;
            case "quit":
                game.quit(command[1]);
                System.out.println("!!! " + command[1] + " has quit the game");
                break;
            default:
                System.out.println("Not a valid command");
        }
    }

    private void checkNumOfPlayers(){
        if(connectionSockets.size() > 2){
            isReadyToStart = true;
            System.out.println("Game is ready to start!");
        }
        else{
            System.out.println("Not enough players to start the game.");
        }
    }

}
