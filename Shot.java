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
    
    // public boolean collide(Rocket Rocket) {
    // 	int distance = distance(this.posX + size / 2, this.posY + size / 2, 
    // 			Rocket.posX + Rocket.size / 2, Rocket.posY + Rocket.size / 2);
    // 	return distance  < Rocket.size / 2 + size / 2;
    // }
}