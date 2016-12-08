package Client;

import Common.ConnectionInterface;
import Common.MessageListener;
import Common.MessageSource;

import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

/**
 * @author Austin Richburg, Doug Key
 */

public class BattleClient extends MessageSource implements MessageListener {

    private Socket clientSocket;
    private ConnectionInterface connection;
    private String user;
    private String data;

    public BattleClient(String host, int port, String user) throws IOException{
        clientSocket = new Socket(host, port);
        connection = new ConnectionInterface(clientSocket);
        connection.addMessageListener(this);
        this.user = user;
        data = "";
    }

    public void go() throws IOException{
        Scanner scanIn = new Scanner(System.in);
        connection.send("join " + user);
        while(clientSocket.isConnected() && !clientSocket.isClosed()) {
            while (!data.equals(user)) {
                System.out.println("!!! It is " + data + "'s turn");
            }
            System.out.println("It is your turn");
            String command = scanIn.nextLine();
            connection.send(command);
            if(command.toLowerCase().equals("quit")){
                clientSocket.close();
            }
        }
    }

    public void messageReceived(String message, MessageSource source){
        data = message;
    }

    public void sourceClosed(MessageSource source){

    }

}
