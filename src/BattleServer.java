import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Scanner;

/**
 * @author Austin Richburg, Doug Key
 */

public class BattleServer implements Runnable {

    private ServerSocket welcomeSocket;
    private HashMap<String, Grid> players;
    private final int PORT = 4271;

    public BattleServer(int port) throws IOException {
        welcomeSocket = new ServerSocket(port);
    }

    public void run() {
        try {
            Socket connectionSocket = welcomeSocket.accept();
            Scanner clientIn = new Scanner(connectionSocket.getInputStream());
            DataOutputStream clientOut = new DataOutputStream(connectionSocket.getOutputStream());
            //clientOut.writeBytes("Please enter player name: \n");
            String playerName = clientIn.nextLine();
            if(players.get(playerName) != null){
                clientOut.writeBytes("Player name taken. Choose another name: ");
            }
            players.put(playerName, new Grid());
        }catch(IOException ioe){
            ioe.getMessage();
        }
    }

    public static void main(String args[]){
        try {
            BattleServer tcpServer = new BattleServer(PORT);
            Game game = new Game();
        }catch (IOException ioe){
            ioe.getMessage();
        }
    }

}
