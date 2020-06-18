public class Message {
    public static final int POSITION = 1;
    public static final int NEW_PLAYER = 2;

    public static String createInitIdMessage(int playerId) {
        String message = Integer.toString(playerId);
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

    public static int getType(String message) {
        String[] parts = message.split(",");
        int type = Integer.parseInt(parts[0]);
        return type;
    }
}