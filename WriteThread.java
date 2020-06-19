import java.io.IOException;

import javafx.io.*;
import javafx.net.*;

public class WriteThread {
    protected BufferredWriter writer;
    protected Client client;

    public WriteThread(Client client, BufferedWriter writer) {
        this.writer = writer;
        this.client = client;
    }

    public void run() {
        while(true) {
            try {
                
            } catch(IOException ex) {
                System.out.println("Error when trying to create client socket: " + ex.getMessage());
                ex.printStackTrace();
            }
        }
    }
}