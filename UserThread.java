import java.io.*;
import java.net.*;
 
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
                // System.out.println(clientMessage);
                // server.broadcast(clientMessage, this);
                processClientMessage(clientMessage);
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
        try {
            writer.println(message);
        } catch(NullPointerException ex) {
            System.out.println("NULL");
        }
    }

    void processClientMessage(String message) {
        // System.out.println(message);
        int type = Message.getType(message);
        switch(type) {
            case Message.NEW_PLAYER:
                server.broadcast(message, this);
                break;
            case Message.POSITION:
                server.broadcast(message, this);
                break;
            case Message.CLIENT_NEW_SHOT:
                // System.out.println(message);
                String[] parts = message.split(",");
                int xPos = Integer.parseInt(parts[1]);
                int yPos = Integer.parseInt(parts[2]);
                int shotId = server.addShot(xPos, yPos);
                // System.out.printf("%d,%d,%d\n", shotId, xPos, yPos);
                String broadcastMessage = Message.createServerNewShotMessage(shotId, xPos, yPos);
                server.broadcast(broadcastMessage, null);
                break;
            default:
                break;
        }
    }
}