package src.client;

import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.canvas.Canvas;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;
import java.util.*;
import src.utils.*;

public class PlayGround extends Application {
    static final Image PLAYER_IMG = new Image("./images/player.png");
    static final Image BOMBS_IMG[] = { new Image("./images/1.png"), new Image("./images/2.png"),
            new Image("./images/3.png"), new Image("./images/4.png"), new Image("./images/5.png"),
            new Image("./images/6.png"), new Image("./images/7.png"), new Image("./images/8.png"),
            new Image("./images/9.png"), new Image("./images/10.png"), };
    public static final int WIDTH = 800;
    public static final int HEIGHT = 600;
    public static final int PLAYER_SIZE = 60;
    public static final int ENEMY_SIZE = 60;
    public static final int MAX_SHOTS = 1000;
    public static final int MAX_ENEMIES = 10;

    private static final Random RAND = new Random();

    private GraphicsContext gc;

    // Rocket player;
    List<RocketUI> players;
    Map<Integer, ShotUI> shots;
    Map<Integer, EnemyUI> enemies;
    int currentPlayerId = -1;
    private double mouseX;
    private double mouseY;
    private int score = 0;
    private boolean gameOver = false;

    /* network */
    protected Client client;

    /* start */
    public void start(Stage stage) throws Exception {
        Canvas canvas = new Canvas(WIDTH, HEIGHT);
        gc = canvas.getGraphicsContext2D();

        setup();

        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(10), e -> run(gc)));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();

        canvas.setCursor(Cursor.MOVE);
        canvas.setOnMouseMoved(e -> {
            mouseX = e.getX();
            mouseY = e.getY();
        });
        canvas.setOnMouseClicked(e -> {
            if (!gameOver) {
                RocketUI player = players.get(currentPlayerId);
                if (shots.size() < MAX_SHOTS) {
                    ShotUI newShot = player.shoot();
                    // shots.put(1, newShot);
                    client.sendNewShotMessage(newShot.posX, newShot.posY);
                }
            }

            // if(gameOver) {
            // gameOver = false;
            // setup();
            // }
        });

        // mouse click event handling for shooting

        stage.setScene(new Scene(new StackPane(canvas)));
        stage.setTitle("Play Ground");
        stage.show();
    }

    /* setup */
    private void setup() {
        client = new Client("localhost", 5000, this);

        while (currentPlayerId == -1) {
            System.out.println("Waiting for player id from server...");
        }

        // build array of players
        players = new ArrayList<>();
        for (int i = 0; i <= currentPlayerId; i++) {
            RocketUI player = new RocketUI(WIDTH / 2, HEIGHT + 100, PLAYER_SIZE, PLAYER_IMG, gc);
            players.add(player);
        }

        shots = new HashMap<Integer, ShotUI>();
        enemies = new HashMap<Integer, EnemyUI>();
    }

    /* run graphicscontext */
    private void run(GraphicsContext gc) {
        gc.setFill(Color.grayRgb(20));
        gc.fillRect(0, 0, WIDTH, HEIGHT);
        gc.setTextAlign(TextAlignment.CENTER);
        gc.setFont(Font.font(20));
        gc.setFill(Color.WHITE);
        gc.fillText("Score: " + score, 60, 20);

        if(gameOver) {
            gc.setFont(Font.font(35));
            gc.setFill(Color.YELLOW);
            gc.fillText("Game Over \n Your Score is: " + score, WIDTH / 2, HEIGHT /2.5);
        // return;
        }
        // univ.forEach(Universe::draw);

        // player.update(); /* for explosion logic */
        for (int i = 0; i < players.size(); i++) {
            if (i != currentPlayerId) {
                RocketUI ally = players.get(i);
                ally.draw();
            }
        }

        if (!gameOver) {
            RocketUI player = players.get(currentPlayerId);
            player.draw();
            player.posX = (int) mouseX;
            player.posY = (int) mouseY;
            client.sendPosition(currentPlayerId, player.posX, player.posY);
        }

        // Bombs.stream().peek(Rocket::update).peek(Rocket::draw).forEach(e -> {
        // if(player.colide(e) && !player.exploding) {
        // player.explode();
        // }
        // });

        // /* old code for shots arraylist */
        // for (int i = shots.size() - 1; i >=0 ; i--) {
        // Shot shot = shots.get(i);
        // if(shot.posY < 0 || shot.toRemove) {
        // shots.remove(i);
        // continue;
        // }
        // shot.update();
        // shot.draw();
        // // for (Bomb bomb : Bombs) {
        // // if(shot.colide(bomb) && !bomb.exploding) {
        // // score++;
        // // bomb.explode();
        // // shot.toRemove = true;
        // // }
        // // }
        // }

        // for (int i = Bombs.size() - 1; i >= 0; i--){
        // if(Bombs.get(i).destroyed) {
        // Bombs.set(i, newBomb());
        // }
        // }

        // gameOver = player.destroyed;
        // if(RAND.nextInt(10) > 2) {
        // univ.add(new Universe());
        // }
        // for (int i = 0; i < univ.size(); i++) {
        // if(univ.get(i).posY > HEIGHT)
        // univ.remove(i);
        // }

        /* Traversing Shots map */
        try {
            Set set = shots.entrySet();
            Iterator iter = set.iterator();
            while (iter.hasNext()) {
                Map.Entry entry = (Map.Entry) iter.next();
                int id = (int) entry.getKey();
                ShotUI shot = (ShotUI) entry.getValue();
                shot.draw();
            }
        } catch (ConcurrentModificationException ex) {
            // System.out.println("Concurrent!");
        }

        /* Traversing Enemies map */
        try {
            Set set = enemies.entrySet();
            Iterator iter = set.iterator();
            while (iter.hasNext()) {
                Map.Entry entry = (Map.Entry) iter.next();
                int id = (int) entry.getKey();
                EnemyUI enemy = (EnemyUI) entry.getValue();
                enemy.draw();
            }
        } catch (ConcurrentModificationException ex) {
            // System.out.println("Concurrent!");
        }

    }

    public void setCurrentPlayerId(int playerId) {
        currentPlayerId = playerId;
    }

    public void addPlayer() {
        RocketUI player = new RocketUI(WIDTH / 2, HEIGHT - PLAYER_SIZE, PLAYER_SIZE, PLAYER_IMG, gc);
        players.add(player);
    }

    public RocketUI getPlayer(int id) {
        return players.get(id);
    }

    public void updatePlayer(int id, int xPos, int yPos) {
        RocketUI player = players.get(id);
        player.posX = xPos;
        player.posY = yPos;
    }

    public void removePlayer(int playerId) {
        // RocketUI player = players.remove(playerId);
        // if (player != null) {
        // // System.out.printf("Removed shot: %d %d %d\n", shotId, shot.posX,
        // shot.posY);
        // player.draw();
        // }
    }

    public void addShot(int shotId, int xPos, int yPos) {
        ShotUI newShot = new ShotUI(xPos, yPos, -1, gc);
        shots.put(shotId, newShot);
    }

    public void updateShot(int shotId, int xPos, int yPos) {
        ShotUI shot = shots.get(shotId);
        if (shot != null) {
            shot.posX = xPos;
            shot.posY = yPos;
        }
    }

    public void removeShot(int shotId) {
        ShotUI shot = shots.remove(shotId);
        if (shot != null) {
            // System.out.printf("Removed shot: %d %d %d\n", shotId, shot.posX, shot.posY);
            shot.draw();
        }
    }

    public void addEnemy(int id, int xPos, int yPos) {
        EnemyUI newEnemy = new EnemyUI(xPos, yPos, ENEMY_SIZE, BOMBS_IMG[RAND.nextInt(BOMBS_IMG.length)], gc);
        enemies.put(id, newEnemy);
    }

    public void updateEnemy(int enemyId, int xPos, int yPos) {
        EnemyUI enemy = enemies.get(enemyId);
        if (enemy != null) {
            enemy.posX = xPos;
            enemy.posY = yPos;
        }
    }

    public void removeEnemy(int enemyId) {
        EnemyUI enemy = enemies.remove(enemyId);
        if (enemy != null) {
            // System.out.printf("Removed shot: %d %d %d\n", shotId, shot.posX, shot.posY);
            enemy.draw();
        }
    }

    public void incrementScore() {
        score += 1;
        // System.out.printf("Score: %d\n", score);
    }

    public void makeGameOver() {
        gameOver = true;
    }

    // public class Rocket {

    // int posX, posY, size;
    // // boolean exploding, destroyed;
    // Image img;
    // // int explosionStep = 0;

    // public Rocket(int posX, int posY, int size, Image image) {
    // this.posX = posX;
    // this.posY = posY;
    // this.size = size;
    // img = image;
    // }

    // public Shot shoot() {
    // return new Shot(posX + size / 2 - Shot.size / 2, posY - Shot.size);
    // }

    // // public void update() {
    // // if(exploding) explosionStep++;
    // // destroyed = explosionStep > EXPLOSION_STEPS;
    // // }

    // public void draw() {
    // // if(exploding) {
    // // gc.drawImage(EXPLOSION_IMG, explosionStep % EXPLOSION_COL * EXPLOSION_W,
    // (explosionStep / EXPLOSION_ROWS) * EXPLOSION_H + 1,
    // // EXPLOSION_W, EXPLOSION_H,
    // // posX, posY, size, size);
    // // }
    // // else {
    // gc.drawImage(img, posX, posY, size, size);
    // // }
    // }

    // // public boolean colide(Rocket other) {
    // // int d = distance(this.posX + size / 2, this.posY + size /2,
    // // other.posX + other.size / 2, other.posY + other.size / 2);
    // // return d < other.size / 2 + this.size / 2 ;
    // // }

    // // public void explode() {
    // // exploding = true;
    // // explosionStep = -1;
    // // }

    // }

    // //bullets
    // public class Shot {

    // public boolean toRemove;

    // int posX, posY, speed = 20;
    // static final int size = 6;

    // public Shot(int posX, int posY) {
    // this.posX = posX;
    // this.posY = posY;
    // }

    // public void update() {
    // posY-=speed;
    // }

    // public void draw() {
    // // gc.setFill(Color.RED);
    // gc.setFill(Color.YELLOWGREEN);
    // gc.fillRect(posX-5, posY-10, size+10, size+30);
    // // if (score >=50 && score<=70 || score>=120) {
    // // gc.setFill(Color.YELLOWGREEN);
    // // speed = 50;
    // // gc.fillRect(posX-5, posY-10, size+10, size+30);
    // // } else {
    // // gc.fillOval(posX, posY, size, size);
    // // }
    // // gc.fillOval(posX, posY, size, size);
    // }

    // // public boolean collide(Rocket Rocket) {
    // // int distance = distance(this.posX + size / 2, this.posY + size / 2,
    // // Rocket.posX + Rocket.size / 2, Rocket.posY + Rocket.size / 2);
    // // return distance < Rocket.size / 2 + size / 2;
    // // }
    // }
}