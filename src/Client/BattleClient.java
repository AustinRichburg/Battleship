package Client;

import Common.ConnectionInterface;
import Common.MessageListener;
import Common.MessageSource;

import java.io.IOException;
import java.net.Socket;

/**
 * The BattleClient class. This class handles methods that send and receive messages. Also, it contains methods to
 * close the sockets correctly.
 * @author Austin Richburg
 * @author Doug Key
 */

public class BattleClient extends MessageSource implements MessageListener {
    /**
     * The Socket the client uses.
     */
    private Socket clientSocket;

    /**
     * The ConnectionInterface the processes use to communicate.
     */
    private ConnectionInterface connection;

    /**
     * The Thread that each process will be ran onto.
     */
    private Thread thread;

    /**
     * This constructor creates a BattleClient object with a host and port.
     *
     * @param host The host server where the processes will communicate.
     * @param port The number the processes communicate on.
     * @throws IOException
     */
    public BattleClient(String host, int port) throws IOException{
        clientSocket = new Socket(host, port);
        connection = new ConnectionInterface(clientSocket, this);
        thread = new Thread(connection);
        thread.start();
    }

    /**
     * The send method. It allows ConnectionInterfaces to send messages to one another by way of a client or server.
     *
     * @param message the string a client or server gives.
     * @throws IOException This exception is thrown if the ConnectionInterface is thrown.
     */
    public void send(String message) throws IOException{
        connection.send(message);
    }

    /**
     * A method that is required from MessageSource. Prints to all clients if a message is recieved.
     * @param message The message received by the subject
     * @param source The source from which this message originated (if needed).
     */
    public void messageReceived(String message, MessageSource source){
        System.out.println(message);
    }

    /**
     * This method is included in MessageSource.
     * @param source The <code>MessageSource</code> that does not expect more messages.
     */
    public void sourceClosed(MessageSource source){

    }

    /**
     * isConnected asks if the current client socket is currently connected.
     * @return clientSocket the BattleClient's socket.
     */
    public boolean isConnected(){
        return clientSocket.isConnected();
    }

    /**
     * The close method attempts to close the client properly.
     *
     * @throws IOException If the socket terminates prematurely.
     */
    public void close() {
        try {
            clientSocket.close();
        } catch (IOException ioe) {
            ioe.getMessage();

        }

    }

}
