import java.io.*;
import java.net.*;
 
/**
 * This thread handles connection for each connected client, so the server
 * can handle multiple clients at the same time.
 *
 * @author www.codejava.net
 */
public class UserThread extends Thread {
    private Socket socket;
    private Server server;
    private PrintWriter writer;
    private int userId;
 
    public UserThread(Socket socket, Server server, int userId) {
        this.socket = socket;
        this.server = server;
        this.userId = userId;
    }
 
    public void run() {
        try {
            InputStream input = socket.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(input));
 
            OutputStream output = socket.getOutputStream();
            writer = new PrintWriter(output, true);

            // send initialized id to player
            sendMessage(Message.createInitIdMessage(userId));
 
            String clientMessage;
 
            do {
                clientMessage = reader.readLine();
                System.out.println(clientMessage);
                server.broadcast(clientMessage, this);
            } while (!clientMessage.equals("bye"));
 
            socket.close(); 
        } catch (IOException ex) {
            System.out.println("Error in UserThread: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
 
    /**
     * Sends a message to the client.
     */
    void sendMessage(String message) {
        writer.println(message);
    }
}