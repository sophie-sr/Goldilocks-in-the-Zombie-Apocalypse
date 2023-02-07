/* Game.java
 * Space Invaders Main Program
 *
 */

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.io.IOException;
import java.util.ArrayList;

public class Game extends Canvas {

	private BufferStrategy strategy; // take advantage of accelerated graphics
	private boolean waitingForKeyPress = true; // true if game held up until
												// a key is pressed
	private boolean leftPressed = false; // true if left arrow key currently pressed
	private boolean rightPressed = false; // true if right arrow key currently pressed
	private boolean downPressed = false;
	private boolean upPressed = false; // true if up arrow currently pressed
	private int health = 120;
	
	private boolean gameRunning = true;
	private ArrayList entities = new ArrayList(); // list of entities
	private ArrayList sprites = new ArrayList();
	private Entity ship; // the ship
	private double moveSpeed = 500; // hor. vel. of ship (px/s)
	private long lastFire = 0; // time last shot fired
	private long firingInterval = 1000; // interval between shots (ms)
	private Entity alien3;
	private Entity alien;
	private Entity alien2;
	private int screen = 0;
	private BufferedImage background;
	private String message = ""; // message to display while waiting
									// for a key press
	private int gameLevel = 1;
	int[][] map;
	
	int [][] level1 = { { 2, 0, 0, 1, 1, 1, 0, 0, 1, 1 }, 
						{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
						{ 0, 0, 0, 0, 0, 0, 0, 1, 1, 0 }, 
						{ 1, 1, 1, 0, 0, 0, 0, 0, 1, 0 }, 
						{ 1, 1, 1, 0, 0, 0, 0, 0, 0, 0 },
						{ 1, 1, 1, 0, 0, 0, 0, 1, 1, 0 }};

	int [][] level2 = { { 0, 0, 0, 0, 1, 1, 0, 0, 0, 2 }, 
						{ 1, 1, 0, 0, 0, 0, 0, 0, 0, 0 },
						{ 1, 1, 1, 0, 0, 0, 0, 1, 1, 0 }, 
						{ 1, 1, 0, 0, 0, 0, 0, 0, 0, 0 }, 
						{ 0, 0, 1, 1, 0, 0, 1, 1, 1, 0 },
						{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 }};
	
	int [][] level3 = { { 0, 0, 0, 0, 1, 1, 0, 0, 1, 1 }, 
						{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
						{ 1, 1, 1, 1, 1, 1, 0, 0, 0, 0 }, 
						{ 0, 0, 0, 0, 0, 0, 0, 0, 1, 1 }, 
						{ 1, 1, 0, 0, 0, 0, 0, 0, 0, 0 },
						{ 1, 2, 0, 0, 0, 0, 1, 1, 1, 1 }};


	/*
	 * Construct our game and set it running.
	 */
	public Game() {
		// create a frame to contain game
		JFrame container = new JFrame("Goldilocks: In the Zombie Apocalypse");

		// get hold the content of the frame
		JPanel panel = (JPanel) container.getContentPane();

		// set up the resolution of the game
		panel.setPreferredSize(new Dimension(1000, 600));
		panel.setLayout(null);

		// ((RootPaneContainer) panel).setContentPane(TileMap.draw(null));

		// set up canvas size (this) and add to frame
		setBounds(0, 0, 1000, 600);
		panel.add(this);

		// Tell AWT not to bother repainting canvas since that will
		// be done using graphics acceleration
		setIgnoreRepaint(true);

		// make the window visible
		container.pack();
		container.setResizable(false);
		container.setVisible(true);

		// if user closes window, shutdown game and jre
		container.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			} // windowClosing
		});

		// add key listener to this canvas
		addKeyListener(new KeyInputHandler());

		// request focus so key events are handled by this canvas
		requestFocus();

		// create buffer strategy to take advantage of accelerated graphics
		createBufferStrategy(2);
		strategy = getBufferStrategy();

		// initialize entities
		initEntities();

		// start the game
		gameLoop();
	} // constructor

	/*
	 * initEntities input: none output: none purpose: Initialise the starting state
	 * of the ship and alien entities. Each entity will be added to the array of
	 * entities in the game.
	 */

	private void initEntities() {
		if (gameLevel == 1) {
			map = level1;
			ship = new ShipEntity(this, "sprites/ship.gif", 400, 510);
			alien = new AlienEntity(this, "sprites/enemy.png", 0, 100);
			alien2 = new Alien2Entity(this, "sprites/enemy.png", 900, 400);
			alien3 = new Alien3Entity(this, "sprites/enemy.png", 700, 300);
		} else if (gameLevel == 2) {
			map = level2;
			ship = new ShipEntity(this, "sprites/ship.gif", 20, 410);
			alien = new AlienEntity(this, "sprites/enemy.png", 0, 500);
			alien2 = new Alien2Entity(this, "sprites/enemy.png", 900, 300);
			alien3 = new Alien3Entity(this, "sprites/enemy.png", 900, 100);
		} else if (gameLevel == 3) {
			map = level3;
			ship = new ShipEntity(this, "sprites/ship.gif", 55, 10);
			alien = new AlienEntity(this, "sprites/enemy.png", 0, 100);
			alien2 = new Alien2Entity(this, "sprites/enemy.png", 700, 300);
			alien3 = new Alien3Entity(this, "sprites/enemy.png", 900, 400);
		}

		entities.add(ship);
		entities.add(alien);
		entities.add(alien2);
		entities.add(alien3);

		for (int i = 0; i < 6; i++) {
			for (int j = 0; j < 10; j++) {
				switch (map[i][j]) {
				case 1:
					Entity tree = new TreeEntity(this, "sprites/treee.png", (j * 100), (i * 100));
					entities.add(tree);
					break;
				case 2:
					Entity door = new DoorEntity(this, "sprites/doorr.gif", (j * 100), (i * 100));
					entities.add(door);
					break;
				}
			}
		}

	} // initEntities

	// Notification that the player has died.
	public void notifyDeath() {
		screen = 2;
		message = "You have died fortunately! Would you like to try again?";
		health -= 40;
		System.out.println(health);
		//if you die once, go back to level one regardless of level (shreya you can either keep or get rid of this,
		//whatever you want aha
		if(health == 0) {
			startGame();
			gameLevel = 1;
		}
		waitingForKeyPress = true;
	} // notifyDeath

	// Notification that the play has won or reached the door
	public void notifyWin() {
		if (gameLevel != 3) {
			screen = 3;
			message = "You won the level! Proceed to the next level?";
			gameLevel++;
		} else if (gameLevel == 3) {
			screen = 1;
			message = "Congratulations! You have completed all the levels! Play from the start?";
			gameLevel = 1;
		}
		waitingForKeyPress = true;
	} // notifyWin

	/* Attempt to fire. */
	public void fire() {
		// check that we've waited long enough to fire
		if ((System.currentTimeMillis() - lastFire) < firingInterval) {
			return;
		} // if

		// otherwise add a shot
		lastFire = System.currentTimeMillis();
		ShotEntity shot = new ShotEntity(this, "sprites/shot.gif", alien3.getX(), alien3.getY() + 50);
		entities.add(shot);
	} // fire

	/*
	 * gameLoop input: none output: none purpose: Main game loop. Runs throughout
	 * game play. Responsible for the following activities: - calculates speed of
	 * the game loop to update moves - moves the game entities - draws the screen
	 * contents (entities, text) - updates game events - checks input
	 */
	public void gameLoop() {
		long lastLoopTime = System.currentTimeMillis();

		// keep loop running until game ends
		while (gameRunning) {

			// calc. time since last update, will be used to calculate
			// entities movement
			long delta = System.currentTimeMillis() - lastLoopTime;
			lastLoopTime = System.currentTimeMillis();

			// get graphics context for the accelerated surface and make it black
			Graphics2D g = (Graphics2D) strategy.getDrawGraphics();
			if(gameLevel == 0) {
				Sprite backgroundimage = (SpriteStore.get()).getSprite("sprites/bg1.png");
				backgroundimage.draw(g, 0, 0);
			} else if (gameLevel == 1) {
				Sprite backgroundimage = (SpriteStore.get()).getSprite("sprites/bg2.png");
				backgroundimage.draw(g, 0, 0);
			} else {
				Sprite backgroundimage = (SpriteStore.get()).getSprite("sprites/bg3.png");
				backgroundimage.draw(g, 0, 0);
			}

			// move each entity
			if (!waitingForKeyPress) {
				for (int i = 0; i < entities.size(); i++) {
					Entity entity = (Entity) entities.get(i);
					entity.move(delta);
				} // for
			} // if

			// draw all entities
			for (int i = 0; i < entities.size(); i++) {
				Entity entity = (Entity) entities.get(i);
				entity.draw(g);
			} // for
			
			/*g.setColor(Color.white);
			g.fillRect(875, 555, 120, 40);
			
			g.setColor(Color.green);
			g.fillRect(875, 555, health, 40);
			
			g.setColor(Color.white);
			g.drawRect(875, 555, 120, 40);*/

			// brute force collisions, compare every entity
			// against every other entity. If any collisions
			// are detected notify both entities that it has
			// occurred
			for (int i = 0; i < entities.size(); i++) {
				for (int j = i + 1; j < entities.size(); j++) {
					Entity me = (Entity) entities.get(i);
					Entity them = (Entity) entities.get(j);
					
					if (me.collidesWith(them)) {
						me.collidedWith(them);
						them.collidedWith(me);
					} // if
				} // inner for
			} // outer for

			// if waiting for "any key press", draw message
			if (waitingForKeyPress) {
				// starting screen
				if (screen == 0) {
					try {
						background = ImageIO.read(this.getClass().getResource("sprites/start.png"));
					} catch (IOException e) {
					} // try catch

					g.drawImage(background, 0, 0, null);
				} // if

				// print win screen
				if (screen == 1) {
					try {
						background = ImageIO.read(this.getClass().getResource("sprites/win.png"));
					} catch (IOException e) {
					} // try catch

					g.drawImage(background, 0, 0, null);
				} // if

				// print lose screen
				if (screen == 2) {
					try {
						background = ImageIO.read(this.getClass().getResource("sprites/lose.png"));
					} catch (IOException e) {
					} // try catch

					g.drawImage(background, 0, 0, null);
				} // if

				// print between screen
				if (screen == 3) {
					try {
						background = ImageIO.read(this.getClass().getResource("sprites/between.png"));
					} catch (IOException e) {
					} // try catch

					g.drawImage(background, 0, 0, null);
				} // if
				
				if (screen == 4) {
					try {
						background = ImageIO.read(this.getClass().getResource("sprites/instructions.png"));
					} catch (IOException e) {
					} // try catch

					g.drawImage(background, 0, 0, null);
				}

				// set graphic values and draw message
				g.setColor(Color.white);
				g.setFont(new Font("Courier", Font.BOLD, 20));
				g.drawString(message, (1000 - g.getFontMetrics().stringWidth(message)) / 2, 475);
				g.drawString("Press any key", (1000 - g.getFontMetrics().stringWidth("Press any key")) / 2, 500);
			} // if

			// clear graphics and flip buffer
			g.dispose();
			strategy.show();

			// ship should not move without user input
			ship.setHorizontalMovement(0);
			ship.setVerticalMovement(0);

			// respond to user moving ship left and right
			if ((leftPressed) && (!rightPressed) && (!upPressed) && (!downPressed)) {
				ship.setHorizontalMovement(-moveSpeed);
			} else if ((rightPressed) && (!leftPressed) && (!upPressed) && (!downPressed)) {
				ship.setHorizontalMovement(moveSpeed);
			} else if ((upPressed) && (!downPressed)) {
				ship.setVerticalMovement(-moveSpeed);
			} else if ((downPressed) && (!upPressed)) {
				ship.setVerticalMovement(moveSpeed);
			} // else

			fire();

			// pause
			// try { Thread.sleep(100); } catch (Exception e) { }

			int i = 0;
			int k = 0;

			if (ship.getY() / 100 == 5) {
				i = 5;
			} else if (ship.getY() / 100 == 4) {
				i = 4;
			} else if (ship.getY() / 100 == 3) {
				i = 3;
			} else if (ship.getY() / 100 == 2) {
				i = 2;
			} else if (ship.getY() / 100 == 1) {
				i = 1;
			} else if (ship.getY() / 100 == 0) {
				i = 0;
			}
			if (ship.getX() / 100 == 9) {
				k = 9;
			} else if (ship.getX() / 100 == 8) {
				k = 8;
			} else if (ship.getX() / 100 == 7) {
				k = 7;
			} else if (ship.getX() / 100 == 6) {
				k = 6;
			} else if (ship.getX() / 100 == 5) {
				k = 5;
			} else if (ship.getX() / 100 == 4) {
				k = 4;
			} else if (ship.getX() / 100 == 3) {
				k = 3;
			} else if (ship.getX() / 100 == 2) {
				k = 2;
			} else if (ship.getX() / 100 == 1) {
				k = 1;
			} else if (ship.getX() / 100 == 0) {
				k = 0;
			}

			// left
			if ((ship.getX() % 100) - 5 >= 0) {
				if (map[i][(((ship.getX() - 5) / 100))] == 1 && ship.getHorizontalMovement() < 0) {
					ship.setHorizontalMovement(0);
				}
			}

			// right
			if ((ship.getX() % 100) + 75 >= 0) {
				if (map[i][(((ship.getX() + 60) / 100))] == 1 && ship.getHorizontalMovement() > 0) {
					ship.setHorizontalMovement(0);
				}
			}

			// down
			if ((ship.getY() % 100) + 75 >= 0) {
				if (map[(((ship.getY() + 80) / 100))][k] == 1 && ship.getVerticalMovement() > 0) {
					ship.setVerticalMovement(0);
				}
			}

			// up
			if ((ship.getY() % 100) - 5 <= 0) {
				if (map[(((ship.getY() - 5) / 100))][k] == 1 && ship.getVerticalMovement() < 0) {
					ship.setVerticalMovement(0);
				}
			}

			// door
			if (map[(ship.getY() + 75) / 100][(ship.getX() + 75) / 100] == 2) {
				entities.clear();
				initEntities();
				notifyWin();
			}

		} // while

	} // gameLoop

	/*
	 * startGame input: none output: none purpose: start a fresh game, clear old
	 * data
	 */
	private void startGame() {
		// clear out any existing entities and initalize a new set
		entities.clear();

		initEntities();
		
		health = 120;
		
		// blank out any keyboard settings that might exist
		leftPressed = false;
		rightPressed = false;
		upPressed = false;
		downPressed = false;
	} // startGame

	/*
	 * inner class KeyInputHandler handles keyboard input from the user
	 */
	private class KeyInputHandler extends KeyAdapter {

		private int pressCount = 1; // the number of key presses since
									// waiting for 'any' key press

		/*
		 * The following methods are required for any class that extends the abstract
		 * class KeyAdapter. They handle keyPressed, keyReleased and keyTyped events.
		 */
		public void keyPressed(KeyEvent e) {

			// if waiting for keypress to start game, do nothing
			if (waitingForKeyPress) {
				return;
			} // if

			// respond to move left, right or fire
			if (e.getKeyCode() == KeyEvent.VK_LEFT) {
				leftPressed = true;
			} // if

			if (e.getKeyCode() == KeyEvent.VK_DOWN) {
				downPressed = true;
			}

			if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
				rightPressed = true;
			} // if

			if (e.getKeyCode() == KeyEvent.VK_UP) {
				upPressed = true;
			} // if

		} // keyPressed

		public void keyReleased(KeyEvent e) {
			// if waiting for keypress to start game, do nothing
			if (waitingForKeyPress) {
				return;
			} // if

			// respond to move left, right or fire
			if (e.getKeyCode() == KeyEvent.VK_LEFT) {
				leftPressed = false;
			} // if

			if (e.getKeyCode() == KeyEvent.VK_DOWN) {
				downPressed = false;
			} // if

			if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
				rightPressed = false;
			} // if

			if (e.getKeyCode() == KeyEvent.VK_UP) {
				upPressed = false;
			} // if

		} // keyReleased

		public void keyTyped(KeyEvent e) {

			// if waiting for key press to start game
			if (waitingForKeyPress) {
				if (pressCount == 1) {
					waitingForKeyPress = false;
					startGame();
					pressCount = 0;
				} else {
					pressCount++;
				} // else
			} // if waitingForKeyPress

			// if escape is pressed, end game
			if (e.getKeyChar() == 27) {
				System.exit(0);
			} // if escape pressed

		} // keyTyped

	} // class KeyInputHandler

	/**
	 * Main Program
	 */
	public static void main(String[] args) {
		// instantiate this object
		new Game();
	} // main
} // Game