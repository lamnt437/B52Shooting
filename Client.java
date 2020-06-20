import java.io.*;
import java.net.*;

public class Client {
    protected BufferedReader reader;
    protected BufferedWriter writer;
    protected String host;
    protected int port;
    protected Socket clientSocket;
    protected PlayGround playGround;
    // protected ClientReadThread readThread;

    public Client(String host, int port, PlayGround playGround) {
        this.host = host;
        this.port = port;
        this.playGround = playGround;

        // attempt to create client socket
        try {
            clientSocket = new Socket(host, port);

            InputStream input = clientSocket.getInputStream();
            reader = new BufferedReader(new InputStreamReader(input));

            OutputStream output = clientSocket.getOutputStream();
            writer = new BufferedWriter(new OutputStreamWriter(output));

            // get player id and set to playground
            String initMessage = reader.readLine();
            int playerId = Integer.parseInt(initMessage);
            playGround.setCurrentPlayerId(playerId);

            execute();

        } catch(IOException ex) {
            System.out.println("Error when trying to create client socket: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    public void execute() {
        new ClientReadThread(reader, this).start();
        // new WriteThread().start(this, writer)
    }

    public void sendPosition(int playerId, int xPos, int yPos) {
        int messageType = 1;
        String message = String.format("%d,%d,%d,%d", messageType, playerId, xPos, yPos);
        

        try {
            // System.out.println("(" + message + ")");
            writer.write(message);
            writer.newLine();
            writer.flush();
        } catch(IOException ex) {
            System.out.println("Error occured when client trying to send message: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    public void sendNewShotMessage(int xPos, int yPos) {
        String message = Message.createClientNewShotMessage(xPos, yPos);
        try {
            // System.out.println("(" + message + ")");
            writer.write(message);
            writer.newLine();
            writer.flush();
        } catch(IOException ex) {
            System.out.println("Error occured when client trying to send message: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    public void processMessage(String message) {
        int messageType = Message.getType(message);

        switch(messageType) {
            case Message.NEW_PLAYER:
                playGround.addPlayer();
                break;

            case Message.POSITION:
                String[] parts = message.split(",");
                int playerId = Integer.parseInt(parts[1]);
                int xPos = Integer.parseInt(parts[2]);
                int yPos = Integer.parseInt(parts[3]);

                playGround.updatePlayer(playerId, xPos, yPos);
                break;

            case Message.SERVER_NEW_SHOT:
                String[] partshot = message.split(",");
                int shotId = Integer.parseInt(partshot[1]);
                int shotXPos = Integer.parseInt(partshot[2]);
                int shotYPos = Integer.parseInt(partshot[3]);

                playGround.addShot(shotId, shotXPos, shotYPos);
                break;

            case Message.SHOT_POS:
                String[] shotpos = message.split(",");
                int id = Integer.parseInt(shotpos[1]);
                int shotX = Integer.parseInt(shotpos[2]);
                int shotY = Integer.parseInt(shotpos[3]);

                playGround.updateShot(id, shotX, shotY);
                break;

            case Message.REMOVE_SHOT:
                String[] shotremove = message.split(",");
                int removeId = Integer.parseInt(shotremove[1]);

                playGround.removeShot(removeId);
                break;

            case Message.NEW_ENEMY:
                String[] partenemy = message.split(",");
                int enemyId = Integer.parseInt(partenemy[1]);
                int enemyXPos = Integer.parseInt(partenemy[2]);
                int enemyYPos = Integer.parseInt(partenemy[3]);

                playGround.addEnemy(enemyId, enemyXPos, enemyYPos);
                break;

            case Message.ENEMY_POS:
                String[] enemypos = message.split(",");
                int posId = Integer.parseInt(enemypos[1]);
                int enemyX = Integer.parseInt(enemypos[2]);
                int enemyY = Integer.parseInt(enemypos[3]);

                playGround.updateEnemy(posId, enemyX, enemyY);
                break;

            case Message.REMOVE_ENEMY:
                String[] enemyRemove = message.split(",");
                int removeEnemyId = Integer.parseInt(enemyRemove[1]);

                playGround.removeEnemy(removeEnemyId);
                break;

            default:
                break;
        }
    }
}