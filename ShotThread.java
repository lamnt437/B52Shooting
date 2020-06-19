import java.util.Map;
import java.util.*;

public class ShotThread extends Thread {
    // broadcast update to all clients
    Map<Integer,Shot> shots;
    Server server;

    public ShotThread(Server server, Map<Integer,Shot> shots) {
        this.shots = shots;
        this.server = server;
    }

    public void run() {
        while(true) {
            try {
                for(Map.Entry shotElm : shots.entrySet()) {
                    int id = (int) shotElm.getKey();
                    Shot shot = (Shot) shotElm.getValue();
    
                    if(shot.posY < 0 || shot.toRemove)  { 
                        shots.remove(id);
                        // send remove shot message to client
                        String removeShotMessage = Message.createRemoveShotMessage(id);
                        server.broadcast(removeShotMessage, null);
                        continue;
                    }
    
                    shot.update();
                    String shotPosMessage = Message.createShotPosMessage(id, shot.posX, shot.posY);
                    server.broadcast(shotPosMessage, null);
                }
            } catch(ConcurrentModificationException ex) {
                // System.out.println("Concurrent!");
            }

            try {
                Thread.sleep(3);
            } catch(InterruptedException ex) {
                System.out.println("Error in shot thread!");
                ex.printStackTrace();
            }
        }
    }
}