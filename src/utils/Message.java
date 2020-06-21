package src.utils;

public class Message {
    public static final int POSITION = 1;
    public static final int NEW_PLAYER = 2;
    public static final int NEW_ENEMY = 3;
    public static final int CLIENT_NEW_SHOT = 4;
    public static final int SERVER_NEW_SHOT = 5;
    public static final int SHOT_POS = 6;
    public static final int REMOVE_SHOT = 7;
    public static final int ENEMY_POS = 8;
    public static final int REMOVE_ENEMY = 9;
    public static final int REMOVE_PLAYER = 10;
    public static final int INCREMENT_SCORE = 11;
    public static final int GAME_OVER = 12;

    /* messages for player handling */

    public static String createInitIdMessage(int playerId) {
        String message = Integer.toString(playerId);
        return message;
    }

    public static String createNewPlayerMessage(int playerId) {
        String message = String.format("%d,%d", NEW_PLAYER, playerId);
        return message;
    }

    public static String createPosMessage(int playerId, int xPos, int yPos) {
        String message = String.format("%d,%d,%d,%d", POSITION, playerId, xPos, yPos);
        return message;
    }

    public static String createRemovePlayerMessage(int playerId) {
        String message = String.format("%d,%d", REMOVE_PLAYER, playerId);
        return message;
    }

    /* messages for shot handling */

    // message sent to server when client shoots a new shot
    public static String createClientNewShotMessage(int xPos, int yPos) {
        String message = String.format("%d,%d,%d", CLIENT_NEW_SHOT, xPos, yPos);
        return message;
    }

    // message broadcasted to all clients when server created a new shot
    public static String createServerNewShotMessage(int shotId, int xPos, int yPos) {
        String message = String.format("%d,%d,%d,%d", SERVER_NEW_SHOT, shotId, xPos, yPos);
        return message;
    }

    // message broadcasted to all clients when server updates shot position
    public static String createShotPosMessage(int shotId, int xPos, int yPos) {
        String message = String.format("%d,%d,%d,%d", SHOT_POS, shotId, xPos, yPos);
        return message;
    }

    // message broadcasted to all clients when server removes shot
    public static String createRemoveShotMessage(int shotId) {
        String message = String.format("%d,%d", REMOVE_SHOT, shotId);
        return message;
    }

    /* messages for enemy handling */

    public static String createNewEnemyMessage(int enemyId, int xPos, int yPos) {
        String message = String.format("%d,%d,%d,%d", NEW_ENEMY, enemyId, xPos, yPos);
        return message;
    }

    public static String createEnemyPosMessage(int enemyId, int xPos, int yPos) {
        String message = String.format("%d,%d,%d,%d", ENEMY_POS, enemyId, xPos, yPos);
        return message;
    }

    public static String createRemoveEnemyMessage(int enemyId) {
        String message = String.format("%d,%d", REMOVE_ENEMY, enemyId);
        return message;
    }

    public static String createMessageIncrementScore() {
        String message = String.format("%d", INCREMENT_SCORE);
        return message;
    }

    public static String createGameOverMessage() {
        String message = String.format("%d", GAME_OVER);
        return message;
    }
 
    public static int getType(String message) {
        String[] parts = message.split(",");
        int type = Integer.parseInt(parts[0]);
        return type;
    }
}