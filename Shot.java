//bullets
public class Shot {
		
    public boolean toRemove;

    int posX, posY, speed = 1;
    static final int size = 6;
        
    public Shot(int posX, int posY) {
        this.posX = posX;
        this.posY = posY;
    }

    public void update() {
        posY-=speed;
    }

    private int distance(int x1, int y1, int x2, int y2) {
		return (int) Math.sqrt(Math.pow((x1 - x2), 2) + Math.pow((y1 - y2), 2));
	}

    public boolean collide(Enemy enemy) {
    	int distance = distance(this.posX + size / 2, this.posY + size / 2, 
        enemy.posX + enemy.size / 2, enemy.posY + enemy.size / 2);
    	return distance  < enemy.size / 2 + size / 2;
    }
}