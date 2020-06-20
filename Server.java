import java.net.ServerSocket;
import java.net.Socket;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.HashSet;

public class Server {
    /* constant */
    public static final int ENEMY_SIZE = 60;
    public static final int PLAYER_SIZE = 60;
    public static final int MAX_ENEMIES = 10;
    public static final int HEIGHT = 600;

    private int port;
    private Set<UserThread> userThreads = new HashSet<>();
    private List<Integer> players;
    // enemy hash map

    private Map<Integer,Enemy> enemies;

    // private Set<Shot> shots;
    private Map<Integer,Shot> shots;
    private int numberOfPlayers = 0;
    private int numberOfShots = 0;
    private int numberOfEnemies = 0;
 
    public Server(int port) {
        this.port = port;
        players = new ArrayList<>();        
        shots = new HashMap<Integer,Shot>();
        enemies = new HashMap<Integer,Enemy>();
    }
 
    public void execute() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
 
            System.out.println("Server is listening on port " + port);

            // create a new shotThread for updating shot positions
            ShotThread shotThread = new ShotThread(this, shots);
            shotThread.start();

            EnemyThread enemyThread = new EnemyThread(this, enemies);
            enemyThread.start();
 
            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("New player connected");

                players.add(numberOfPlayers);
                UserThread newUser = new UserThread(socket, this, numberOfPlayers);
                broadcast(Message.createNewPlayerMessage(numberOfPlayers), newUser);
                numberOfPlayers++;

                newUser.start();
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
 
    /**
     * Stores username of the newly connected client.
     */
    // void addUserName(String userName) {
    //     userNames.add(userName);
    // }
 
    /**
     * When a client is disconneted, removes the associated username and UserThread
     */
    // void removeUser(String userName, UserThread aUser) {
    //     boolean removed = userNames.remove(userName);
    //     if (removed) {
    //         userThreads.remove(aUser);
    //         System.out.println("The user " + userName + " quitted");
    //     }
    // }
 
    // Set<String> getUserNames() {
    //     return this.userNames;
    // }
 
    /**
     * Returns true if there are other users connected (not count the currently connected user)
     */
    // boolean hasUsers() {
    //     return !this.userNames.isEmpty();
    // }

    public int addShot(int xPos, int yPos) {
        Shot newShot = new Shot(xPos, yPos);

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
}