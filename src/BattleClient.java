import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

/**
 * @author Austin Richburg, Doug Key
 */

public class BattleClient {

    private Socket clientSocket;

    public BattleClient(String host, int port) throws IOException{
        clientSocket = new Socket(host, port);
    }

    public void go() throws IOException{
        Scanner scanIn = new Scanner(System.in);
        DataOutputStream toServer = new DataOutputStream(clientSocket.getOutputStream());
        Scanner clientIn = new Scanner(clientSocket.getInputStream());
        System.out.println("Enter a player name: \n");
        String playerName = scanIn.nextLine();
        toServer.writeBytes(playerName);
    }

}
