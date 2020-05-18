import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
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
	public boolean playing = false;
	
	// parameters for GameCourt
	// these two variables are updated every millisecond
	private JLabel status;
	private JLabel scoreboard;
	
	// dimensions of the GUI
	public static final int COURT_WIDTH = 400;
	public static final int COURT_HEIGHT = 430;
	
	// NOTE: The top of the court is 0 height and the bottom is 430.
	// NOTE: The left side of the court is 0 and the right side is 400
	
	// velocity for objects
	public static final int BULLET_VY = 10;
	public static final int CANNON_VX = 5;
	public static final int BOULDER_VY = -6; 
	public static final int BOULDER_VX = 5;
	
	// interval for timer (milliseconds)
	public static final int INTERVAL = 10;
	
	@SuppressWarnings("unused")
	private int score;
	
	// constructor for GameCourt
	public GameCourt(JLabel status, JLabel scoreboard) {
		setBorder(BorderFactory.createLineBorder(Color.BLACK));
		
		// this is constanly going to check if the game is still going on
		Timer check = new Timer(INTERVAL, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	// once the game is opened it starts this
            	if(playing) {
            		// checks if i won or lost
            		GO();
            	}
            }
        });
		
		Timer cannonMover = new Timer(INTERVAL, new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// checks if the cannon has changed its velocity which indirectly 
				// changes its position
				CannonMovement();
			}
		});
		
		Timer shooter = new Timer(100, new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// checks if bullets are made
				ShooterCheck();
				// it will either move or delete a bullet based on its position
				BulletMovement();
				// it will move the boulders across the court until the health is gone
				BoulderMovement();
			}
		});
		
		Timer boulderMaker = new Timer(5000, new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// creates the boulder and sets the velocities
				BoulderCreator();
			}
		});
		
		// used for keys
		setFocusable(true);
		
		// needed to use keys to move cannon
		// if the keys are used, it will change the velocity to positive or negative
		// deoending on what key i pressed (it doesn't directly change the position)
		addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_LEFT) {
	                Cannon.setVx(-CANNON_VX);
	            } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
	                Cannon.setVx(CANNON_VX);
	            } 
			}

			// if i stop pressing the key, it will put the velocity back to 0
	        public void keyReleased(KeyEvent e) {
	            Cannon.setVx(0);
	         }
		});
		
		
		// starts all the timers when the game starts
		check.start();
		cannonMover.start();
		shooter.start();
		boulderMaker.start();
		
		this.status = status;
		this.scoreboard = scoreboard;
		this.score = 0;
	}
	
	// resets the game to its initial state
	public void reset() {
		playing = true;
		bullets = new ArrayList<>();
		boulders = new ArrayList<>();
		Cannon = new cannon(COURT_WIDTH, COURT_HEIGHT);
		score = 0;
		scoreboard.setText("0");
		status.setText("Good Luck");
		requestFocusInWindow();
	}
	
	
//************************************************************************************//

	// constantly checking whether i won or lost
	// meaning a method where it will check whether i hit the ball and lose
	// or i destroy every ball and i won
	public void GO() {
		if(playing) {
			for(int i = 0; i < boulders.size(); i++) {
				// the intersects method needs work
				if(Cannon.intersects(boulders.get(i))) {
					// if it intersects, then the game is over and a text will appear
					playing = false;
					status.setText("You lose.");
				}
			}
		}
	}
	
	
//************************************************************************************//
	
	// if playing, checks to see if the space bar is clicked
	// creates bullets
	public void ShooterCheck() {
		if(playing){ 
			addKeyListener(new KeyAdapter() {
				public void keyPressed(KeyEvent e) {
				if(e.getKeyCode() == KeyEvent.VK_SPACE) { 
						// if pressed, a new bullet is created
		            	bullet shot = new bullet(COURT_WIDTH, COURT_HEIGHT);
		            	// the position is set to the middle of the cannon
		            	shot.setPx(Cannon.getPx());
		            	shot.setPy(Cannon.getPy());
		            	// added to the arraylist of bullets
		            	bullets.add(shot);
					}
				}
			});
		 }
	}
	
	// if playing, then the cannon's position will be updated according to what i pressed
	public void CannonMovement() {
		if (playing) {
			Cannon.move();
			// i'm assuming the repaint method calls the draw method
			// the cannon technically never moves, all thats happening is it is repainted in
			// different positions whenever the key is pressed
            repaint();
        }
    }
		
	public void BulletMovement() {
		// if the array of bullets is not empty
		if(!bullets.isEmpty()) {
			// checks each bullet
			for(int i = 0; i < bullets.size(); i++) {
				// if the bullet is not at the very top, then move the bullet
				if(!(bullets.get(i).getPy() <= 10)) {
					bullets.get(i).move();
				// if the bullet is at the very top, delete the bullet
				} else {
					bullets.remove(bullets.get(i));
					// it will also go back an index for the arrray of bullets
					i--;
				}
			}
		}
	}
	

	
//************************************************************************************//
	
	// if playing then a new boulder will be created and added to the Array list of Boulders
	public void BoulderCreator() {
		if(playing) { 
			boulder boulder = new boulder(COURT_WIDTH, COURT_HEIGHT);
			boulder.setVx(BOULDER_VX);
			boulder.setVy(BOULDER_VY);
			//testing purposes
			System.out.println("the bolder health is: " + boulder.getHealth());
			boulders.add(boulder);
		}
	}
	
	void BoulderMovement () {
		if(!boulders.isEmpty()) {
			for(int i = 0; i < boulders.size(); i++) {
				// right border
				if(boulders.get(i).getPx() + boulders.get(i).getWidth() / 2 >= COURT_WIDTH) {
					boulders.get(i).setVx(-BOULDER_VX);
					boulders.get(i).move();
				}
				//left border
				else if(boulders.get(i).getPx() <= 0 +(boulders.get(i).getWidth() / 2 )) {
					System.out.println("hit left border");
					boulders.get(i).setVx(BOULDER_VX);
					boulders.get(i).move();
				}
 				//top border
				 else if(boulders.get(i).getPy() <= 0 + (boulders.get(i).getHeight() / 2)) {
					System.out.println("hit top border");
					boulders.get(i).setVy(-BOULDER_VY);
					boulders.get(i).move();
				}
				//bottom border
				 else if(boulders.get(i).getPy() + (boulders.get(i).getHeight() / 2) >= COURT_HEIGHT) {
					System.out.println("hit bottom border");
					System.out.println("status of boulder: height = " + boulders.get(i).getHeight()
							+ " y position = " + boulders.get(i).getPy());
					boulders.get(i).setVy(BOULDER_VY);
					boulders.get(i).move();
				}
				
				// i think this if statement should be before the rest but figure out health first
				if(!(boulders.get(i).getHealth() == 0)) {
					boulders.get(i).move();
				} else {
					boulders.remove(boulders.get(i));
					i = i - 1;
				}
			}	
		}	
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
