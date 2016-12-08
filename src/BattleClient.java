import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

/**
 * @author Austin Richburg, Doug Key
 */

public class BattleClient extends MessageSource {

    private Socket clientSocket;
    private ConnectionInterface connection;
    private String user;
    private boolean turn;

    public BattleClient(String host, int port, String user) throws IOException{
        clientSocket = new Socket(host, port);
        connection = new ConnectionInterface(clientSocket);
        this.user = user;
        turn = false;
    }

    public void go() throws IOException{
        Scanner scanIn = new Scanner(System.in);
        connection.send("join " + user);
        while(!turn){
           
        }
        System.out.println("It is your turn");
        connection.send(scanIn.nextLine());
    }

    public void setTurn(boolean turn){
        this.turn = turn;
    }

}
