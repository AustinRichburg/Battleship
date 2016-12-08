package Common;

import java.io.DataOutputStream;
import java.io.IOException;
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
    private Socket connectionSocket;

    /**
     * Constructor used to create a ConnectionInterface for the server side.
     * @param connectionSocket The socket that the connectionInterface is connected to
     * @throws IOException
     */
    public ConnectionInterface(Socket connectionSocket) throws IOException{
        from = new Scanner(connectionSocket.getInputStream());
        to = new DataOutputStream(connectionSocket.getOutputStream());
        this.connectionSocket = connectionSocket;
    }

    public void run(){
        String data = "";
        while(connectionSocket.isConnected()){
            while(from.hasNextLine()){
                data += from.nextLine();
            }
            notifyReceipt(data);
        }
    }

    public void send(String sendData){
        try {
            to.writeBytes(sendData);
        }catch(IOException ioe){
            ioe.getMessage();
        }
    }

}
