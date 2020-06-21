import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class Rocket {

    int posX, posY, size;
    // boolean exploding, destroyed;
    // int explosionStep = 0;
    
    public Rocket(int posX, int posY, int size) {
        this.posX = posX;
        this.posY = posY;
        this.size = size;
    }

    // public void update() {
    //     if(exploding) explosionStep++;
    //     destroyed = explosionStep > EXPLOSION_STEPS;
    // }

    // public boolean colide(Rocket other) {
    //     int d = distance(this.posX + size / 2, this.posY + size /2, 
    //                     other.posX + other.size / 2, other.posY + other.size / 2);
    //     return d < other.size / 2 + this.size / 2 ;
    // }
    
    // public void explode() {
    //     exploding = true;
    //     explosionStep = -1;
    // }

}