import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class EnemyThread extends Thread {
    private Map<Integer, Enemy> enemies;
    private Server server;
    private Random randomEngine = new Random();
    // Check if number of enemy is max

    public EnemyThread(Server server, Map<Integer, Enemy> enemies) {
        this.server = server;
        this.enemies = enemies;
    }

    public void run() {
        while (true) {
            if (enemies.size() < Server.MAX_ENEMIES) {
                int randomX = randomEngine.nextInt((740 - 60) + 1) - 60;
                int fixedY = 0;
                
                // Enemy enemy = new Enemy(randomX, fixedY, Server.ENEMY_SIZE);
                // enemies.put(numberOfRockets, enemy);
                int enemyId = server.addEnemy(randomX, fixedY);

                // broadcast
                String message = Message.createNewEnemyMessage(enemyId, randomX, fixedY);
                // System.out.println(message);
                server.broadcast(message, null);
            }

            try {
                for (Map.Entry enemyEle : enemies.entrySet()) {
                    int id = (int) enemyEle.getKey();
                    Enemy enemy = (Enemy) enemyEle.getValue();

                    if (enemy.posY > Server.HEIGHT || enemy.toRemove) {
                        enemies.remove(id);
                        // send remove shot message to client
                        String removeEnemyMessage = Message.createRemoveEnemyMessage(id);
                        // System.out.println(removeEnemyMessage);
                        server.broadcast(removeEnemyMessage, null);
                        continue;
                    }
                    enemy.update();
                    String enemyPosMessage = Message.createEnemyPosMessage(id, enemy.posX, enemy.posY);
                    server.broadcast(enemyPosMessage, null);
                }
            } catch (ConcurrentModificationException ex) {
                
            }

            try {
                Thread.sleep(3);
            } catch (InterruptedException ex) {
                System.out.println("Error in enemy thread!");
                ex.printStackTrace();
            }
        }
    }

}
