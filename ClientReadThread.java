import java.io.BufferedReader;
import java.io.IOException;

public class ClientReadThread extends Thread {
    protected BufferedReader reader;
    protected Client client;

    public ClientReadThread(BufferedReader reader, Client client) {
        this.client = client;
        this.reader = reader;
    }

    public void run() {
        while(true) {
            try {
                String message = reader.readLine();
                // System.out.println("(" + message + ")");
                client.processMessage(message);
            } catch(IOException ex) {
                System.out.println("Error occurs when client reading: " + ex.getMessage());
                ex.printStackTrace();
            }
        }
    }
}