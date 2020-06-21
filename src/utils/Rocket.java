package src.utils;

public class Rocket {

    public int posX, posY, size;
    // boolean exploding, destroyed;
    // int explosionStep = 0;
    public boolean isActive = true;
    
    public Rocket(int posX, int posY, int size) {
        this.posX = posX;
        this.posY = posY;
        this.size = size;
    }

    // public void update() {
    //     if(exploding) explosionStep++;
    //     destroyed = explosionStep > EXPLOSION_STEPS;
    // }

    private int distance(int x1, int y1, int x2, int y2) {
		return (int) Math.sqrt(Math.pow((x1 - x2), 2) + Math.pow((y1 - y2), 2));
	}

    public boolean collide(Enemy other) {
        int d = distance(this.posX + size / 2, this.posY + size /2, 
                        other.posX + other.size / 2, other.posY + other.size / 2);
        return d < other.size / 2 + this.size / 2 ;
    }
    
    // public void explode() {
    //     exploding = true;
    //     explosionStep = -1;
    // }

}