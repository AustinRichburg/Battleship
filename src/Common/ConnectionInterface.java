package Common;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

/**
 * ConnectionInterface is the class responsible for sending messages to and receiving messages from remote hosts.
 *
 * @author Austin Richburg, Doug Key
 * @version December 5, 2016
 */

public class ConnectionInterface extends MessageSource implements Runnable{

    private Scanner from;
    private DataOutputStream to;
    private PrintStream ps;
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

    public void run(){
        while(connectionSocket.isConnected()){
            if(from.hasNextLine())
            notifyReceipt(from.nextLine());
        }
    }

    public void send(String sendData){
        ps.println(sendData);
        ps.flush();
    }

}
