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
import java.util.List;
import java.util.ArrayList;

public class PlayGround extends Application {
    static final Image PLAYER_IMG = new Image("./images/player.png");
    private static final int WIDTH = 800;
    private static final int HEIGHT = 600;
    private static final int PLAYER_SIZE = 60;
    private static final int MAX_SHOTS = 1000;

    private GraphicsContext gc;

    // Rocket player;
    List<Rocket> players;
    List<Shot> shots;
    int currentPlayerId = -1;
    private double mouseX;
    private double mouseY;

    /* network */
    protected Client client;


    /* start */
    public void start(Stage stage) throws Exception {
        Canvas canvas = new Canvas(WIDTH, HEIGHT);
        gc = canvas.getGraphicsContext2D();

        setup();

        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(5), e -> run(gc)));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
        
        canvas.setCursor(Cursor.MOVE);
        canvas.setOnMouseMoved(e -> {mouseX = e.getX(); mouseY = e.getY();});
        canvas.setOnMouseClicked(e -> {
            Rocket player = players.get(currentPlayerId);
			if(shots.size() < MAX_SHOTS) {
                Shot newShot = player.shoot();
                shots.add(newShot);
                client.sendNewShotMessage(newShot.posX, newShot.posY);
            }
			// if(gameOver) { 
			// 	gameOver = false;
			// 	setup();
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

        while(currentPlayerId == -1) {
            System.out.println("Waiting for player id from server...");
        }

        // build array of players
        players = new ArrayList<>();
        for(int i = 0; i <= currentPlayerId; i++) {
            Rocket player = new Rocket(WIDTH / 2, HEIGHT - PLAYER_SIZE, PLAYER_SIZE, PLAYER_IMG, gc);
            players.add(player);
        }

        shots = new ArrayList<>();

        // todo: create new threads to get other player positions and draw
    }

    /* run graphicscontext */
	private void run(GraphicsContext gc) {
		gc.setFill(Color.grayRgb(20));
		gc.fillRect(0, 0, WIDTH, HEIGHT);
		gc.setTextAlign(TextAlignment.CENTER);
		gc.setFont(Font.font(20));
		gc.setFill(Color.WHITE);
		// gc.fillText("Score: " + score, 60,  20);
	
		
		// if(gameOver) {
		// 	gc.setFont(Font.font(35));
		// 	gc.setFill(Color.YELLOW);
		// 	gc.fillText("Game Over \n Your Score is: " + score + " \n Click to play again", WIDTH / 2, HEIGHT /2.5);
		// //	return;
		// }
		// univ.forEach(Universe::draw);
	
        // player.update(); /* for explosion logic */
        for(int i = 0; i < players.size(); i++) {
            if(i != currentPlayerId) {
                Rocket ally = players.get(i);
                ally.draw();
            }
        }
        
        Rocket player = players.get(currentPlayerId);
		player.draw();
        player.posX = (int) mouseX;
        player.posY = (int) mouseY;
        client.sendPosition(currentPlayerId, player.posX, player.posY);
		
		// Bombs.stream().peek(Rocket::update).peek(Rocket::draw).forEach(e -> {
		// 	if(player.colide(e) && !player.exploding) {
		// 		player.explode();
		// 	}
		// });
		
		
		for (int i = shots.size() - 1; i >=0 ; i--) {
			Shot shot = shots.get(i);
			if(shot.posY < 0 || shot.toRemove)  { 
				shots.remove(i);
				continue;
			}
			shot.update();
			shot.draw();
			// for (Bomb bomb : Bombs) {
			// 	if(shot.colide(bomb) && !bomb.exploding) {
			// 		score++;
			// 		bomb.explode();
			// 		shot.toRemove = true;
			// 	}
			// }
		}
		
		// for (int i = Bombs.size() - 1; i >= 0; i--){  
		// 	if(Bombs.get(i).destroyed)  {
		// 		Bombs.set(i, newBomb());
		// 	}
		// }
	
		// gameOver = player.destroyed;
		// if(RAND.nextInt(10) > 2) {
		// 	univ.add(new Universe());
		// }
		// for (int i = 0; i < univ.size(); i++) {
		// 	if(univ.get(i).posY > HEIGHT)
		// 		univ.remove(i);
		// }
    }
    
    public void setCurrentPlayerId(int playerId) {
        currentPlayerId = playerId;
    }

    public void addPlayer() {
        Rocket player = new Rocket(WIDTH / 2, HEIGHT - PLAYER_SIZE, PLAYER_SIZE, PLAYER_IMG, gc);
        players.add(player);
    }

    public Rocket getPlayer(int id) {
        return players.get(id);
    }

    public void updatePlayer(int id, int xPos, int yPos) {
        Rocket player = players.get(id);
        player.posX = xPos;
        player.posY = yPos;
    }

    public void addShot(int xPos, int yPos) {
        Shot newShot = new Shot(xPos, yPos, gc);
        shots.add(newShot);
    }

    // public class Rocket {

    //     int posX, posY, size;
    //     // boolean exploding, destroyed;
    //     Image img;
    //     // int explosionStep = 0;
        
    //     public Rocket(int posX, int posY, int size,  Image image) {
    //         this.posX = posX;
    //         this.posY = posY;
    //         this.size = size;
    //         img = image;
    //     }
        
    //     public Shot shoot() {
    //         return new Shot(posX + size / 2 - Shot.size / 2, posY - Shot.size);
    //     }
    
    //     // public void update() {
    //     //     if(exploding) explosionStep++;
    //     //     destroyed = explosionStep > EXPLOSION_STEPS;
    //     // }
        
    //     public void draw() {
    //         // if(exploding) {
    //         //     gc.drawImage(EXPLOSION_IMG, explosionStep % EXPLOSION_COL * EXPLOSION_W, (explosionStep / EXPLOSION_ROWS) * EXPLOSION_H + 1,
    //         //             EXPLOSION_W, EXPLOSION_H,
    //         //             posX, posY, size, size);
    //         // }
    //         // else {
    //             gc.drawImage(img, posX, posY, size, size);
    //         // }
    //     }
    
    //     // public boolean colide(Rocket other) {
    //     //     int d = distance(this.posX + size / 2, this.posY + size /2, 
    //     //                     other.posX + other.size / 2, other.posY + other.size / 2);
    //     //     return d < other.size / 2 + this.size / 2 ;
    //     // }
        
    //     // public void explode() {
    //     //     exploding = true;
    //     //     explosionStep = -1;
    //     // }
    
    // }

    // //bullets
	// public class Shot {
		
	// 	public boolean toRemove;

	// 	int posX, posY, speed = 20;
	// 	static final int size = 6;
			
	// 	public Shot(int posX, int posY) {
	// 		this.posX = posX;
	// 		this.posY = posY;
	// 	}

	// 	public void update() {
	// 		posY-=speed;
	// 	}
		

	// 	public void draw() {
    //         // gc.setFill(Color.RED);
    //         gc.setFill(Color.YELLOWGREEN);
    //         gc.fillRect(posX-5, posY-10, size+10, size+30);
	// 		// if (score >=50 && score<=70 || score>=120) {
	// 		// 	gc.setFill(Color.YELLOWGREEN);
	// 		// 	speed = 50;
	// 		// 	gc.fillRect(posX-5, posY-10, size+10, size+30);
	// 		// } else {
	// 		// gc.fillOval(posX, posY, size, size);
    //         // }
    //         // gc.fillOval(posX, posY, size, size);
	// 	}
		
	// 	// public boolean collide(Rocket Rocket) {
	// 	// 	int distance = distance(this.posX + size / 2, this.posY + size / 2, 
	// 	// 			Rocket.posX + Rocket.size / 2, Rocket.posY + Rocket.size / 2);
	// 	// 	return distance  < Rocket.size / 2 + size / 2;
	// 	// }
	// }
}