import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.*;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;

@SuppressWarnings("serial")
public class GameCourt extends JPanel {
	
	private cannon Cannon;
	private ArrayList<bullet> bullets;
	private ArrayList<boulder> boulders;


	// state of the game
	public boolean playing = true;
	public boolean levelCompleted = false;
	
	// parameters for GameCourt
	// these two variables are updated every millisecond
	private JLabel status;
	private JLabel scoreboard;
	private JLabel stageNumber;
	
	// dimensions of the GUI
	public static final int COURT_WIDTH = 400;
	public static final int COURT_HEIGHT = 430;
	
	// NOTE: The top of the court is 0 height and the bottom is 430.
	// NOTE: The left side of the court is 0 and the right side is 400
	
	// velocity for objects
	public static final int BULLET_VY = 10;
	public static final int CANNON_VX = 5;
	public static final int BOULDER_VY = -17;
	public static final int BOULDER_VX = 5;
	
	// interval for timer (milliseconds)
	public static final int INTERVAL = 10;
	
	@SuppressWarnings("unused")
	private int score;
	private int level;
	
	// constructor for GameCourt
	public GameCourt(JLabel status, JLabel scoreboard, JLabel stageNumber) {
		setBorder(BorderFactory.createLineBorder(Color.BLACK));

		/**
		 * calls the BoulderCreator function every 5 seconds
		 */
		Timer boulderMaker = new Timer(5000, new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// creates the boulder and sets the velocities
				BoulderCreator();
			}
		});

		/**
		 * Calls the go function, checks for bullet interactions, checks for user movements for the cannon,
		 * and checks to see if the boulder changes health levels at an interval of 10 milliseconds
		 */
		Timer check = new Timer(INTERVAL, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	if(playing) {
            		GO();
            		bulletInteractions();
            		boulderBreak();
					CannonMovement();
				}
            }
        });

		/**
		 * updates the bullet and boulder positions every 100th of a second
		 */
		Timer Movement = new Timer(100, new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				BulletMovement();
				BoulderMovement();
			}
		});

		/**
		 * stops the creation of boulders after a set amount of time. The creation of boulders is not handled here, nor
		 * is the restarting of the creation of boulders.
		 */
		Timer boulderHalt = new Timer((20000)* Math.max((int)(.5 * getLevel()), 1), new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				boulderMaker.stop();
				}
			});

		/**
		 * checks to see if a level has been completed. If so, the level is incremented up one and it is switched to
		 * level incomplete
		 */
		Timer newLevelCheck = new Timer(10, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("the level is completed : "+ getLevelCompleted());
				if(getLevelCompleted()) {
					levelIncrement();
					System.out.println("level has increased: " + getLevel());
				}
				if(!boulderMaker.isRunning()) {
					boulderRestart(boulderMaker);
				}
			}
		});
		
		// used for keys
		setFocusable(true);

		/**
		 * If the left arrow key is pressed, the cannon moves left; if the right arrow key is pressed, the cannon moves
		 * right. If either the left or right key are released, set the velocity to 0.
		 */
		addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_LEFT) {
	                Cannon.setVx(-CANNON_VX);
	            } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
	                Cannon.setVx(CANNON_VX);
	            } 
			}
	        public void keyReleased(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_RIGHT) {
					Cannon.setVx(0);
				}
	         }
		});

		/**
		 * Creates bullets at the press of the space bar and adds it to the arrayList bullets. The bullets take on the
		 * initial position of the cannon.
		 */
		addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode() == KeyEvent.VK_SPACE) {
					bullet shot = new bullet(COURT_WIDTH, COURT_HEIGHT);
					shot.setPx(Cannon.getPx());
					shot.setPy(Cannon.getPy());
					bullets.add(shot);
				}
			}
		});

		/**
		 * starts all of the timers
		 */
		 check.start();
		 boulderMaker.start();
		 Movement.start();
		 boulderHalt.start();
		 newLevelCheck.start();

		/**
		 * initializations
		 */
		this.status = status;
		this.scoreboard = scoreboard;
		this.stageNumber = stageNumber;
		this.score = 0;
		this.level = 1;
		this.levelCompleted = false;
	}

	/**
	 * resets the game to its initial state
	 */
	public void reset() {
		System.out.println("should restart");
//-------------------------------------------INIT VARIABLES------------------------------------------//
		playing = true;
		score = 0;
		level = 1;
		levelCompleted = false;
//-------------------------------------------GAME OBJECTS--------------------------------------------//
		bullets = new ArrayList<>();
		boulders = new ArrayList<>();
		Cannon = new cannon(COURT_WIDTH, COURT_HEIGHT);
//-------------------------------------------J-LABELS-----------------------------------------------//
		scoreboard.setText("0");
		stageNumber.setText("Level: " + level);
		status.setText("Good Luck");
		requestFocusInWindow();
	}

	
	
//************************************************************************************//

	/**
	 * While the game is running, this function checks to see if there is ever an intersection between the
	 * cannon or any of the boulders in the boulders array. If there is, the playing variable is switched to false
	 * and the game is over.
	 */
	public void GO() {
		if(playing) {
			for(int i = 0; i < boulders.size(); i++) {
				// the intersects method needs work
				//System.out.println(Cannon.intersects(boulders.get(i)));
				if(Cannon.intersects(boulders.get(i))) {
					// if it intersects, then the game is over and a text will appear
					this.playing = false;
					status.setText("You lose.");
				}
			}
		}
	}
	
	
//************************************************************************************//


	/**
	 * While the game is running, this function redraws the cannons according the move function
 	 */
	public void CannonMovement() {
		if (playing) {
			Cannon.move();
            repaint();
        }
    }

	/**
	 * while the game is running, this function iterates through the bullets array and redraws each according to the
	 * move function.
	 */
	public void BulletMovement() {
		if(!bullets.isEmpty() && playing) {
			for(int i = 0; i < bullets.size(); i++) {
				if(!(bullets.get(i).getPy() <= 10)) {
					bullets.get(i).move();
				} else {
					bullets.remove(bullets.get(i));
					i--;
				}
			}
		}
	}
	

	
//************************************************************************************//

	/**
	 * while the game is running, this function creates new boulders and adds them to the boulders array list.
	 */
	public void BoulderCreator() {
		if(playing) { 
			boulder boulder = new boulder(COURT_WIDTH, COURT_HEIGHT);
			boulder.setVx(BOULDER_VX);
			boulder.setVy(BOULDER_VY);
			boulders.add(boulder);
		}
	}

	/**
	 * While the game is running, this function handles the boulders' reaction to hitting walls, and its movements
	 */
	void BoulderMovement () {
		if(!boulders.isEmpty() && this.playing) {
			for(int i = 0; i < boulders.size(); i++) {
				boulders.get(i).move();

				// right border
				if(boulders.get(i).getPx() + boulders.get(i).getWidth() / 2 >= COURT_WIDTH) {
					boulders.get(i).setVx(-BOULDER_VX);
					boulders.get(i).move();
				}
				//left border
				else if(boulders.get(i).getPx() <= 0 + (boulders.get(i).getWidth() / 2 )) {
					boulders.get(i).setVx(BOULDER_VX);
					boulders.get(i).move();
				}
 				//top border
				else if(boulders.get(i).getPy() <= 0 + (boulders.get(i).getHeight() / 2)) {
					boulders.get(i).setVy(-BOULDER_VY);
					boulders.get(i).move();
				}
				//bottom border
				else if(boulders.get(i).getPy() + (boulders.get(i).getHeight() / 2) + 30 >= COURT_HEIGHT) {
					boulders.get(i).setVy(BOULDER_VY);
					boulders.get(i).move();
				}

			}	
		}	
	}

	/**
	 * While the game is running, this function checks to see if the boulders change their healthLevel at any point.
	 * If they do, then the function creates an identical boulder to the one that just became smaller with the only
	 * difference of a negative x velocity. This boulder is added to the boulders array list.
	 */
	public void boulderBreak(){
		if(playing && boulders.size() > 0){
			for(int i = 0; i < boulders.size(); i++) {
				if (boulders.get(i).getBoulderBroke()) {
					boulders.get(i).setBoulderBroke(false);
					boulder brokenPiece = new boulder(COURT_WIDTH, COURT_HEIGHT);
					brokenPiece.setVx(-boulders.get(i).getVx());
					brokenPiece.setHealth(boulders.get(i).getHealth());
					brokenPiece.setPx(boulders.get(i).getPx());
					brokenPiece.setPy(boulders.get(i).getPy());
					if(boulders.get(i).getVy() > 0) {
						brokenPiece.setVy(boulders.get(i).getVy());
					}
					else{
						brokenPiece.setVy(-boulders.get(i).getVy());
						boulders.get(i).setVy(-boulders.get(i).getVy());
					}
					boulders.add(brokenPiece);
				}
			}
		}
	}

	/**
	 * While the game is running, this function checks to see if there is any interaction between bullets or boulders.
	 * If there is, the bullet is removed from bullets, and the boulder's health is taken down by 5. The player receives
	 * 5 points every time that a bullet interacts with a boulder and 10 points every time that a boulder is destroyed
	 * (removed from boulders).
	 */
	public void bulletInteractions() {
		if (playing) {
			for (int i = 0; i < bullets.size(); i++) {
				for (int j = 0; j < boulders.size(); j++) {
					if (i < bullets.size() && bullets.contains(bullets.get(i))) {
						bullet bullet = bullets.get(i);
						boulder boulder = boulders.get(j);
						if (bullet.intersects(boulder)) {
							bullets.remove(bullet);
							boulder.setHealth(boulder.getHealth() - 5);
							score += 5;
							scoreboard.setText(score + "");
							if(boulder.getHealth() <= 0){
								boulders.remove(boulder);
								score += 10;
								scoreboard.setText(score + "");
							}
						}
					}
				}
			}
		}
	}

	/**
	 * This is a specialized function that is made to restart the boulderMaker timer if the boulders on the screen have
	 * been reduced to 0. This indicates that the level has been completed.
	 * @param boulderMaker -- the boulderMaker timer
	 */
	public void boulderRestart(Timer boulderMaker){
		if(this.boulders.size() == 0 && this.playing){
			this.levelCompleted = true;
			System.out.println("boulder Restart was used and changed levelCompleted true");
			boulderMaker.start();
		}
	}

	/**
	 * While the game is running, this function is used to increase the level. Once the level increases,
	 * it is marked as incomplete.
	 */
	public void levelIncrement(){
		if(this.boulders.size() != 0 && this.playing && levelCompleted == true) {
			this.level++;
			this.levelCompleted = false;
			this.stageNumber.setText("Level: " + this.level);
		}
	}

//-------------------------------------------GETTER FUNCTIONS------------------------------------------//

	public int getScore(){
		return this.score;
	}

	public int getLevel(){
		return this.level;
	}

	public boolean getLevelCompleted(){
		return this.levelCompleted;
	}

	public boolean getPlaying(){
		return this.playing;
	}

//************************************************************************************//
	
	// paints the objects in the game
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Cannon.draw(g);
		
		if(!(bullets.isEmpty())) {
			for(bullet currentShot: bullets) {
				currentShot.draw(g);
			}
		}
		
		if(!(boulders.isEmpty())) {
			for(boulder Boulder: boulders) {
				Boulder.draw(g);
			}
		}
	}
	
	// dimensions for the game itself
	@Override
	public Dimension getPreferredSize() {
		return new Dimension(COURT_WIDTH, COURT_HEIGHT);
	}
}
