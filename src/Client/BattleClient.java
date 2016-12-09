package Client;

import Common.ConnectionInterface;
import Common.MessageListener;
import Common.MessageSource;

import java.io.IOException;
import java.net.Socket;

/**
 * @author Austin Richburg, Doug Key
 */

public class BattleClient extends MessageSource implements MessageListener {

    private Socket clientSocket;
    private ConnectionInterface connection;
    private Thread thread;
    private String user;
    private String data;

    public BattleClient(String host, int port, String user) throws IOException{
        clientSocket = new Socket(host, port);
        connection = new ConnectionInterface(clientSocket);
        thread = new Thread(connection);
        thread.start();
        connection.addMessageListener(this);
        this.user = user;
        data = "";
    }

    public void send(String message) throws IOException{
        connection.send(message);
    }

    public void messageReceived(String message, MessageSource source){
        data = message;
    }

    public void sourceClosed(MessageSource source){

    }

    public boolean isConnected(){
        return clientSocket.isConnected();
    }

    public void close(){
        try {
            clientSocket.close();
        }catch(IOException ioe){
            ioe.getMessage();
        }
    }

}
