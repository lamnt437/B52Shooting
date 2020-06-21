package src.utils;

public class Enemy {
    public boolean toRemove;

    public int posX, posY, size, speed = 1;

    public Enemy(int posX, int posY, int size) {
        this.posX = posX;
        this.posY = posY;
        this.size = size;
    }

    // public Enemy() {

    // }

    public void update() {
        posY+=speed;
    }
}
