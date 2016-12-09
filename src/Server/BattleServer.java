package Server;

import Common.ConnectionInterface;
import Common.MessageListener;
import Common.MessageSource;

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
    private ArrayList<ConnectionInterface> connectionInterfaces;
    private HashMap<ConnectionInterface, String> players;
    private ExecutorService threadPool;
    private Game game;
    private boolean isReadyToStart, started;
    private int xSize, ySize;


    public BattleServer(int port) throws IOException {
        welcomeSocket = new ServerSocket(port);
        connectionSockets = new ArrayList<>();
        connectionInterfaces = new ArrayList<>();
        players = new HashMap<>();
        threadPool = Executors.newCachedThreadPool();
        xSize = -1;
        ySize = -1;
        isReadyToStart = false;
        started = false;
    }

    public BattleServer(int port, int xSize, int ySize) throws IOException {
        welcomeSocket = new ServerSocket(port);
        connectionSockets = new ArrayList<>();
        connectionInterfaces = new ArrayList<>();
        players = new HashMap<>();
        threadPool = Executors.newCachedThreadPool();
        this.xSize = xSize;
        this.ySize = ySize;
        isReadyToStart = false;
        started = false;
    }

    public void listen() throws IOException {
        int prevSize = connectionSockets.size();
        while(!started){
            connectionSockets.add(welcomeSocket.accept());
            if(connectionSockets.size() > prevSize){
                ConnectionInterface ci = new ConnectionInterface(connectionSockets.get(prevSize));
                connectionInterfaces.add(ci);
                threadPool.execute(ci);
                ci.addMessageListener(this);
                prevSize = connectionSockets.size();
            }
            checkNumOfPlayers();
        }
        while(started && (connectionSockets.size() > 2) && !welcomeSocket.isClosed()){
            for(ConnectionInterface element : connectionInterfaces){
                element.send(game.getTurn());
            }
        }
    }

    /**
     * Used to notify observers that the subject has received a message.
     *
     * @param message The message received by the subject
     * @param source The source from which this message originated (if needed).
     */
    public void messageReceived(String message, MessageSource source){
        System.out.println("Here");
        parseCommands(message.split(" "), (ConnectionInterface)source);
        System.out.println(message);
    }

    /**
     * Used to notify observers that the subject will not receive new messages; observers can
     * deregister themselves.
     *
     * @param source The <code>MessageSource</code> that does not expect more messages.
     */
    public void sourceClosed(MessageSource source){
        players.remove(source);
        connectionInterfaces.remove(source);
        source.removeMessageListener(this);
    }

    private void parseCommands(String[] command, ConnectionInterface source){
        switch(command[0].toLowerCase()){
            case "join":
                players.put(source, command[1]);
                source.send("Joined");
            case "attack":
                if(game.getStarted() && game.getTurn().equals(players.get(source))) {
                    game.attack(command[1], Integer.parseInt(command[2]), Integer.parseInt(command[3]));
                    game.nextTurn();
                    for(ConnectionInterface element : connectionInterfaces){
                        element.send("!!! It is " + game.getTurn() + "'s turn");
                    }
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
                    started = true;
                }
                else{
                    source.send("Not enough players to start the game");
                }
                break;
            case "show":
                game.printBoard();
                game.showOther(command[1]);
                break;
            case "quit":
                game.quit(command[1]);
                for(ConnectionInterface element : connectionInterfaces){
                    element.send("!!! " + players.get(source) + " has surrendered");
                }
                break;
            default:
                source.send("Not a valid command");
        }
    }

    private void checkNumOfPlayers(){
        if(connectionSockets.size() >= 2){
            isReadyToStart = true;
            for(ConnectionInterface element : connectionInterfaces){
                element.send("Game is ready to start!");
            }
        }
        else {
            System.out.println("Game not ready to start");
        }
    }

}
