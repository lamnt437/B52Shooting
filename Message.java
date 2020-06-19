public class Message {
    public static final int POSITION = 1;
    public static final int NEW_PLAYER = 2;
    public static final int OPPONENT_POS = 3;
    public static final int CLIENT_NEW_SHOT = 4;
    public static final int SERVER_NEW_SHOT = 5;
    public static final int SHOT_POS = 6;
    public static final int REMOVE_SHOT = 7;
    // public static final int ACK_SHOT = 6;

    public static String createInitIdMessage(int playerId) {
        String message = Integer.toString(playerId);
        return message;
    }

    /* shot message being sent from Client to Server, and broadcasted to all other clients */
    // public static String createShotMessage(int playerId, int xPos, int yPos) {
    //     String message = String.format("%d,%d,%d,%d", SHOT, playerId, xPos, yPos);
    //     return message;
    // }

    /* shot position message being sent from Server to Client */
    public static String createShotPosMessage(int shotId, int xPos, int yPos) {
        String message = String.format("%d,%d,%d,%d", SHOT_POS, shotId, xPos, yPos);
        return message;
    }

    public static String createPosMessage(int playerId, int xPos, int yPos) {
        String message = String.format("%d,%d,%d,%d", POSITION, playerId, xPos, yPos);
        return message;
    }

    public static String createNewPlayerMessage(int playerId) {
        String message = String.format("%d,%d", NEW_PLAYER, playerId);
        return message;
    }

    public static String createOpponentPosMessage(int oppId, int xPos, int yPos) {
        String message = String.format("%d,%d,%d,%d", OPPONENT_POS, oppId, xPos, yPos);
        return message;
    }

    public static String createClientNewShotMessage(int xPos, int yPos) {
        String message = String.format("%d,%d,%d", CLIENT_NEW_SHOT, xPos, yPos);
        return message;
    }

    public static String createServerNewShotMessage(int shotId, int xPos, int yPos) {
        String message = String.format("%d,%d,%d,%d", SERVER_NEW_SHOT, shotId, xPos, yPos);
        return message;
    }

    public static String createRemoveShotMessage(int shotId) {
        String message = String.format("%d,%d", REMOVE_SHOT, shotId);
        return message;
    }

    public static int getType(String message) {
        String[] parts = message.split(",");
        int type = Integer.parseInt(parts[0]);
        return type;
    }
}