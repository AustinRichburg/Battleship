package Client;

import Client.BattleClient;

import java.io.IOException;

/**
 * The driver for the client side of the battleship game.
 *
 * @author Austin Richburg, Doug Key
 * @version December 5, 2016
 */

public class BattleDriver {

    /**
     * This will parse command line options, instantiates a BattleClient, reads messages from the keyboards,
     * and sends them to the client. The command line arguments are: hostname, port number and user nickname.
     * All of these command line arguments are required.
     * @param args The command line arguments.
     */
    public static void main(String args[]){
        String host, user;
        int port;
        if(args.length != 3){
            System.out.println("Usage: java BattleDriver <hostName> <portNumber> <username>");
            System.exit(1);
        }
        if(args[0].equals("")){
            System.out.println("Please enter a host name");
            System.exit(1);
        }
        host = args[0];
        if(args[1].equals("^[0-9]+$")){
            System.out.println("Port number not valid.");
            System.exit(1);
        }
        port = Integer.parseInt(args[1]);
        if(args[2].equals("")){
            System.out.println("Please enter a user name");
            System.exit(1);
        }
        user = args[2];
        try {
            BattleClient battleClient = new BattleClient(host, port, user);
            battleClient.go();
        }catch(IOException ioe){
            ioe.getMessage();
        }
    }

}
