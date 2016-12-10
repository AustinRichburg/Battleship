package Common;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

/**
 * ConnectionInterface is the class responsible for sending messages to and receiving messages from remote hosts.
 *
 * @author Austin Richburg
 * @author Doug Key
 * @version December 5, 2016
 */

public class ConnectionInterface extends MessageSource implements Runnable{

    /**
     * The Scanner to read in from.
     */
    private Scanner from;

    /**
     * The DataOutputStream that sends messages to clients.
     */
    private DataOutputStream to;

    /**
     * The PrintStream used to print messages.
     */
    private PrintStream ps;

    /**
     * The Socket used for a connection through ConnectionInterfaces.
     */
    private Socket connectionSocket;

    /**
     * Constructor used to create a ConnectionInterface for the server side.
     * @param connectionSocket The socket that the connectionInterface is connected to
     * @throws IOException
     */
    public ConnectionInterface(Socket connectionSocket, MessageListener ml) throws IOException{
        from = new Scanner(new InputStreamReader(connectionSocket.getInputStream()));
        to = new DataOutputStream(connectionSocket.getOutputStream());
        ps = new PrintStream(connectionSocket.getOutputStream());
        this.connectionSocket = connectionSocket;
        addMessageListener(ml);
    }

    /**
     * The run method asks if a connectionSocket is connected to another socket and if the scanner has a message
     * to be sent. It sends a message to everyone involved if it does have a messages.
     */
    public void run(){
        while(connectionSocket.isConnected()){
            if(from.hasNextLine())
            notifyReceipt(from.nextLine());
        }
    }

    /**
     * The send method. This method will print the string needed to be sent. It will then flush the PrintStream.
     *
     * @param sendData The string of data to be sent.
     */
    public void send(String sendData){
        ps.println(sendData);
        ps.flush();
    }

}
