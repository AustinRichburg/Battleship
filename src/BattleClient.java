import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

/**
 * @author Austin Richburg, Doug Key
 */

public class BattleClient extends MessageSource implements MessageListener{

    private Socket clientSocket;
    private ConnectionInterface connection;
    private String user;
    private boolean turn;

    public BattleClient(String host, int port, String user) throws IOException{
        clientSocket = new Socket(host, port);
        connection = new ConnectionInterface(host, port);
        this.user = user;
        turn = false;
    }

    public void go() throws IOException{
        Scanner scanIn = new Scanner(System.in);
        connection.sendServer(user);
        while(!turn){
            String currPlayer = connection.receiveFromServer();
            System.out.println("It is " + currPlayer + "'s turn");
        }
        System.out.println("It is your turn");
        connection.sendServer(scanIn.nextLine());
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
