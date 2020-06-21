package src.utils;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

public class EnemyUI extends Enemy {
    private GraphicsContext gc;
    private Image img;
        
    public EnemyUI(int posX, int posY, int size, Image image, GraphicsContext gc) {
        super(posX, posY, size);
        this.gc = gc;
        this.img = image;
    }
    
    public void draw() {
        // if(exploding) {
        //     gc.drawImage(EXPLOSION_IMG, explosionStep % EXPLOSION_COL * EXPLOSION_W, (explosionStep / EXPLOSION_ROWS) * EXPLOSION_H + 1,
        //             EXPLOSION_W, EXPLOSION_H,
        //             posX, posY, size, size);
        // }
        // else {
            gc.drawImage(img, posX, posY, size, size);
        // }
    }
}