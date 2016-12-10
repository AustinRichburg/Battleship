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
 *
 * The BattleServer class. This class listens for connections made by the client. It will attempt to connect
 * through the ConnectionInterface's. It will run listen() and once connected, will attempt to handle the commands
 * given by clients through the interfaces.
 */

public class BattleServer implements MessageListener {

    /**
     * The Server Socket that listens.
     */
    private ServerSocket welcomeSocket;

    /**
     * The ArrayList of sockets.
     */
    private ArrayList<Socket> connectionSockets;

    /**
     * The ArrayList of ConnectionInterfaces.
     */
    private ArrayList<ConnectionInterface> connectionInterfaces;

    /**
     * The Hashmap that stores a ConnectionInterface as a key and a player name as a value.
     */
    private HashMap<ConnectionInterface, String> players;

    /**
     * The Thread service used to handle multiple threads.
     */
    private ExecutorService threadPool;

    /**
     * The Game object used to make a new game.
     */
    private Game game;

    /**
     * The boolean values to check if the game is ready to start or if it has started.
     */
    private boolean isReadyToStart, started;

    /**
     * the int values to make the board.
     */
    private int xSize, ySize;

    /**
     * The BattleServer object that signifys the port being used to connect.
     *
     * @param port the number used to connect.
     * @throws IOException Thrown when a socket closes unexpectedly.
     */
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

    /**
     * The BattleServer object that specifies a port, xSize, and ySize.
     *
     * @param port  The port number to connect to
     * @param xSize The horizontal board value.
     * @param ySize The vertical board value.
     * @throws IOException Thrown if a socket is closed unexpectedly.
     */
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

    /**
     * Listen() attempts to add the sockets from the ArrayList. If we have more sockets than we previously did,
     * we make a new ConnectionInterface object and execute a thread on that interface. While we have at least two
     * players in the game we will send them turns.
     *
     * @throws IOException Input Out Exception that is thrown if the sockets are broken.
     */
    public void listen() throws IOException {
        int prevSize = connectionSockets.size();
        while(!started){
            connectionSockets.add(welcomeSocket.accept());
            if(connectionSockets.size() > prevSize){
                ConnectionInterface ci = new ConnectionInterface(connectionSockets.get(prevSize), this);
                connectionInterfaces.add(ci);
                threadPool.execute(ci);
                ci.send("Successfully connected");
                prevSize = connectionSockets.size();
                System.out.println("New user connected");
            }
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
        parseCommands(message.split(" "), (ConnectionInterface)source);
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

    /**
     * parseCommands handles all the commands a client could try to do. This method uses a switch statement depending on
     * what the user types in for the command.
     *
     * @param command The string that contains the command the client gives at the command line.
     * @param source  The ConnectionInterface used to communicate the commands between Connection Interfaces.
     */
    private void parseCommands(String[] command, ConnectionInterface source){
        switch(command[0].toLowerCase()){
            case "join":
                players.put(source, command[1]);
                for(ConnectionInterface element : connectionInterfaces){
                    element.send("!!! " + command[1] + " joined the game");
                }
                System.out.println(command[1] + " joined the game");
                System.out.flush();
                checkNumOfPlayers();
                break;
            case "attack":
                if(game != null) {
                    String result = "";
                    if(game.getTurn().equals(players.get(source))) {
                        if (command.length == 4) {
                            if (game.getStarted()) {
                                result = game.attack(command[1], Integer.parseInt(command[2]), Integer.parseInt(command[3]));
                                game.nextTurn();
                                for (ConnectionInterface element : connectionInterfaces) {
                                    element.send(result);
                                    element.send("!!! It is " + game.getTurn() + "'s turn");
                                }
                            }
                        }
                        else {
                            source.send("Attack command is \"Ex. attack username 3 4\"");
                        }
                    }
                    else{
                        source.send("It is not your turn");
                    }
                }
                else{
                    source.send("Game has not begun");
                }
                break;
            case "play":
                if(isReadyToStart && !started){
                    if(xSize == -1 && ySize == -1) {
                        game = new Game();
                    }else{
                        game = new Game(xSize, ySize);
                    }
                    for(ConnectionInterface element : connectionInterfaces){
                        game.addPlayer(players.get(element));
                    }
                    game.setStarted(true);
                    started = true;
                    for(ConnectionInterface element : connectionInterfaces){
                        element.send("!!! The game has begun !!!");
                        element.send("!!! It is " + game.getTurn() + "'s turn");
                    }
                }
                else{
                    source.send("Not enough players to start the game");
                }
                break;
            case "show":
                if(game != null) {
                    if (players.get(source).equals(command[1])) {
                        source.send(game.printBoard(players.get(source)));
                    } else {
                        source.send(game.showOther(command[1]));
                    }
                }
                else{
                    source.send("Game has not begun");
                }
                break;
            case "quit":
                if(game != null) {
                    game.quit(command[1]);
                }
                source.send("You have left the game");
                for(ConnectionInterface element : connectionInterfaces){
                    element.send("!!! " + players.get(source) + " has surrendered");
                }
                sourceClosed(source);
                checkNumOfPlayers();
                break;
            default:
                source.send("Not a valid command");
        }
    }

    /**
     * checkNumOfPlayers is a helper method to ask if at least two players are ready to play the game. Prints a useful
     * message if the game is or is not ready.
     */
    private void checkNumOfPlayers(){
        if(connectionSockets.size() >= 2){
            isReadyToStart = true;
            for(ConnectionInterface element : connectionInterfaces){
                element.send("Game is ready to start!");
            }
        }
        else {
            isReadyToStart = false;
            System.out.println("Game not ready to start");
        }
    }

}
