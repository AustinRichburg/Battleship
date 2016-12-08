import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author Austin Richburg, Doug Key
 * @version December 5, 2016
 */

public class BattleServer implements MessageListener {

    private ServerSocket welcomeSocket;
    private ArrayList<Socket> connectionSockets;
    private ExecutorService threadPool;
    private Game game;
    private boolean isReadyToStart;


    public BattleServer(int port) throws IOException {
        welcomeSocket = new ServerSocket(port);
        connectionSockets = new ArrayList<>();
        threadPool = Executors.newCachedThreadPool();
        game = new Game();
        isReadyToStart = false;
    }

    public BattleServer(int port, int xSize, int ySize) throws IOException {
        welcomeSocket = new ServerSocket(port);
        connectionSockets = new ArrayList<>();
        threadPool = Executors.newCachedThreadPool();
        game = new Game(xSize, ySize);
        isReadyToStart = false;
    }

    public void go() throws IOException {
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
            listen();
        }
    }

    public void listen(){

    }

    /**
     * Used to notify observers that the subject has received a message.
     *
     * @param message The message received by the subject
     * @param source The source from which this message originated (if needed).
     */
    public void messageReceived(String message, MessageSource source){
        parseCommands(message.split(" "));
    }

    /**
     * Used to notify observers that the subject will not receive new messages; observers can
     * deregister themselves.
     *
     * @param source The <code>MessageSource</code> that does not expect more messages.
     */
    public void sourceClosed(MessageSource source){

    }

    private void parseCommands(String[] command){
        switch(command[0].toLowerCase()){
            case "attack":
                if(game.getStarted()) {
                    game.attack(command[1], Integer.parseInt(command[2]), Integer.parseInt(command[3]));
                }
                else{
                    //send clients that game has not started
                }
                break;
            case "play":
                if(isReadyToStart){
                    game.setStarted(true);
                }
                else{
                    //send clients that game is not ready to start
                }
                break;
            case "show":
                game.showMe();
                game.showOther();
                break;
            case "quit":
                game.quit(command[1]);
                System.out.println("!!!" + command[1] + " has quit the game");
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
