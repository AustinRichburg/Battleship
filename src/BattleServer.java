import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Scanner;

/**
 * @author Austin Richburg, Doug Key
 */

public class BattleServer implements Runnable, MessageListener {

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
            String playerName = clientIn.nextLine();
            if(players.get(playerName) != null){
                clientOut.writeBytes("Player name taken. Choose another name: ");
            }
            players.put(playerName, new Grid());
        }catch(IOException ioe){
            ioe.getMessage();
        }
    }

    public void listen(){

    }

    /**
     * Used to notify observers that the subject has received a message.
     *
     * @param message The message received by the subject
     * @param source The source from which this message originated (if needed).
     */
    public void messageReceived(String message, MessageSource source){

    }


    /**
     * Used to notify observers that the subject will not receive new messages; observers can
     * deregister themselves.
     *
     * @param source The <code>MessageSource</code> that does not expect more messages.
     */
    public void sourceClosed(MessageSource source){

    }

}
