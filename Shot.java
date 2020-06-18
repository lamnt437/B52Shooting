import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

//bullets
public class Shot {
		
    public boolean toRemove;

    int posX, posY, speed = 20;
    static final int size = 6;
    private GraphicsContext gc;
        
    public Shot(int posX, int posY, GraphicsContext gc) {
        this.posX = posX;
        this.posY = posY;
        this.gc = gc;
    }

    public void update() {
        posY-=speed;
    }
    

    public void draw() {
        // gc.setFill(Color.RED);
        gc.setFill(Color.YELLOWGREEN);
        gc.fillRect(posX-5, posY-10, size+10, size+30);
        // if (score >=50 && score<=70 || score>=120) {
        // 	gc.setFill(Color.YELLOWGREEN);
        // 	speed = 50;
        // 	gc.fillRect(posX-5, posY-10, size+10, size+30);
        // } else {
        // gc.fillOval(posX, posY, size, size);
        // }
        // gc.fillOval(posX, posY, size, size);
    }
    
    // public boolean collide(Rocket Rocket) {
    // 	int distance = distance(this.posX + size / 2, this.posY + size / 2, 
    // 			Rocket.posX + Rocket.size / 2, Rocket.posY + Rocket.size / 2);
    // 	return distance  < Rocket.size / 2 + size / 2;
    // }
}