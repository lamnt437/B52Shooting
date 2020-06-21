package src.server;

import java.net.ServerSocket;
import java.net.Socket;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.HashSet;
import src.utils.*;

public class Server {
    /* constant */
    public static final int ENEMY_SIZE = 60;
    public static final int PLAYER_SIZE = 60;
    public static final int MAX_ENEMIES = 10;
    public static final int HEIGHT = 600;

    private int port;
    private Set<UserThread> userThreads = new HashSet<>();
    private Map<Integer,Rocket> players;
    // enemy hash map

    private Map<Integer,Enemy> enemies;

    // private Set<Shot> shots;
    private Map<Integer,Shot> shots;
    private int numberOfPlayers = 0;
    private int numberOfShots = 0;
    private int numberOfEnemies = 0;

    // start generate enemy
    private boolean startEnemy = false;
 
    public Server(int port) {
        this.port = port;
        players = new HashMap<Integer,Rocket>();        
        shots = new HashMap<Integer,Shot>();
        enemies = new HashMap<Integer,Enemy>();
    }
 
    public void execute() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
 
            System.out.println("Server is listening on port " + port);

            // create a new shotThread for updating shot positions
            ShotThread shotThread = new ShotThread(this, shots, enemies);
            shotThread.start();

            EnemyThread enemyThread = new EnemyThread(this, enemies, players);
            
 
            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("New player connected");

                int playerId = addPlayer();
                UserThread newUser = new UserThread(socket, this, playerId, players);
                broadcast(Message.createNewPlayerMessage(playerId), newUser);

                newUser.start();

                if(!startEnemy) {
                    enemyThread.start();
                    startEnemy = true;
                }
                    
                userThreads.add(newUser);
            }
 
        } catch (IOException ex) {
            System.out.println("Error in the server: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
 
    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Syntax: java Server <port-number>");
            System.exit(0);
        }
 
        int port = Integer.parseInt(args[0]);
 
        Server server = new Server(port);
        server.execute();
    }
 
    /**
     * Delivers a message from one user to others (broadcasting)
     */
    void broadcast(String message, UserThread excludeUser) {
        // System.out.println(message);
        for (UserThread aUser : userThreads) {
            if (aUser != excludeUser) {
                aUser.sendMessage(message);
            }
        }
    }

    public int addPlayer() {
        
        Rocket newPlayer = new Rocket(400, 600, PLAYER_SIZE);
        
        int id = numberOfPlayers;
        players.put(id,newPlayer);
        numberOfPlayers++;
        // System.out.printf("Number of Player: %d\n", players.size());
        newPlayer.isActive = false;
        return id;
    }

    public int addShot(int xPos, int yPos, int ownerId) {
        Shot newShot = new Shot(xPos, yPos, ownerId);

        // add new shot to shot list
        shots.put(numberOfShots, newShot);
        int id = numberOfShots;
        numberOfShots++;

        return id;
    }

    public int addEnemy(int xPos, int yPos) {
        Enemy newEnemy = new Enemy(xPos, yPos, ENEMY_SIZE);

        enemies.put(numberOfEnemies, newEnemy);
        int id = numberOfEnemies;
        numberOfEnemies++;
        return id;
    }

    public void incrementScore(int userId) {
        for (UserThread aUser : userThreads) {
            if (aUser.getUserId() == userId) {
                aUser.incrementScore();
                String message = Message.createMessageIncrementScore();
                aUser.sendMessage(message);
            }
        }
    }

    public void makeGameOver(int lostPlayerId) {
        for(UserThread aUser : userThreads) {
            if(aUser.getUserId() == lostPlayerId) {
                // send game over message to lost player
                String gameOverMessage = Message.createGameOverMessage();
                aUser.sendMessage(gameOverMessage);
            } else {
                // broadcast remove user message
                String removePlayerMessage = Message.createRemovePlayerMessage(lostPlayerId);
                aUser.sendMessage(removePlayerMessage);
            }
        }

        players.remove(lostPlayerId);
    }
}