package src.utils;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

//bullets
public class ShotUI extends Shot {
    private GraphicsContext gc;
        
    public ShotUI(int posX, int posY, int ownerId, GraphicsContext gc) {
        super(posX, posY, ownerId);
        this.gc = gc;
    }

    public void draw() {
        if(!toRemove) {
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
        } else {
            gc.setFill(Color.grayRgb(20));
            gc.fillRect(posX-5, posY-10, size+10, size+30);
        }
        
    }
    
    // public boolean collide(Rocket Rocket) {
    // 	int distance = distance(this.posX + size / 2, this.posY + size / 2, 
    // 			Rocket.posX + Rocket.size / 2, Rocket.posY + Rocket.size / 2);
    // 	return distance  < Rocket.size / 2 + size / 2;
    // }
}