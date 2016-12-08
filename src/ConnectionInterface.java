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

    private Socket connectionSocket;
    private Scanner fromClient;
    private DataOutputStream toClient;

    private Socket clientSocket;
    private Scanner fromServer;
    private DataOutputStream toServer;

    /**
     * Constructor used to create a ConnectionInterface for the server side.
     * @param connectionSocket
     * @throws IOException
     */
    public ConnectionInterface(Socket connectionSocket) throws IOException{
        fromClient = new Scanner(connectionSocket.getInputStream());
        toClient = new DataOutputStream(connectionSocket.getOutputStream());
    }

    /**
     * Constructor used to create a ConnectionInterface for the client side.
     * @param host The host
     * @param port The port number
     * @throws IOException
     */
    public ConnectionInterface(String host, int port) throws IOException {
        clientSocket = new Socket(host, port);
        fromServer = new Scanner(clientSocket.getInputStream());
        toServer = new DataOutputStream(clientSocket.getOutputStream());
    }

    public void run(){
        while(connectionSocket.isConnected()){

        }
    }

    public void sendServer(String sendData){
        try {
            toServer.writeBytes(sendData);
        }catch(IOException ioe){
            ioe.getMessage();
        }
    }

    public String receiveFromServer(){
        String data = "";
        while (fromServer.hasNextLine()){
            data += fromServer.nextLine();
        }
        return data;
    }

    public void sendClient(String sendData){
        try {
            toClient.writeBytes(sendData);
        }catch (IOException ioe){
            ioe.getMessage();
        }
    }

    public String receiveFromClient(){
        String data = "";
        while(fromClient.hasNextLine()){
            data += fromClient.nextLine();
        }
        return data;
    }

}
