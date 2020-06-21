package src.server;

import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.List;
import src.utils.*;

public class EnemyThread extends Thread {
    private Map<Integer, Enemy> enemies;
    private Map<Integer,Rocket> players;
    private Server server;
    private Random randomEngine = new Random();
    // Check if number of enemy is max

    public EnemyThread(Server server, Map<Integer, Enemy> enemies, Map<Integer,Rocket> players) {
        this.server = server;
        this.enemies = enemies;
        this.players = players;
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

                    /* check collide with players */
                    try {
                        for (Map.Entry playerElm : players.entrySet()) {  
                            int playerId = (int) playerElm.getKey();
                            Rocket player = (Rocket) playerElm.getValue();
                            if(player.isActive && player.collide(enemy)) {
                                // System.out.println("Player collide!");
                                server.makeGameOver(playerId);
                                // player.toRemove = true;
                                enemy.toRemove = true;
                            }
                        }
                    } catch(NullPointerException ex) {
                        System.out.println("Null when check collide!");
                    }

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
