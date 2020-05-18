import java.awt.Graphics;
import java.awt.Color;
import javax.swing.*; 

public class boulder extends GameObject {

	//each boulder is given an initial random health between 1 and 100
	private int health; 

	//both height and width will be determined by the health of the boulder
	private int height;

	private int width;

	//x and y are the coordinates of the boulder on the panel
	
	public static final int INIT_POSX = 0; 
	public static final int INIT_POSY = 200; 
	public static final int INIT_VX = 0; 
	public static final int INIT_VY = 0;
	public static int HEIGHT = 20;
	public static int WIDTH = 20;
	
	
	
	//constructor
	public boulder(int courtWidth, int courtHeight) {
  
  	//super the constructor

		super(INIT_VX, INIT_VY, INIT_POSX,
				  INIT_POSY, HEIGHT, WIDTH,
				  courtWidth, courtHeight);

		int newHealth = (int)(Math.random() * 100);
		this.setHealth(newHealth);

		if(this.getHealth() >= 75) {
			this.setHeight(100);
			this.setWidth(100);
			this.width = 100;
			this.height = 100;
		}
		else if(this.getHealth() >= 50) {
			this.setHeight(75);
			this.setWidth(75);
			this.width = 75;
			this.height = 75;
		}
		else {
			this.setHeight(60);
			this.setWidth(60);
			this.width = 60;
			this.height = 60;
		}
	}


	//health setter
	public void setHealth(int desiredHealth){
		this.health = desiredHealth;
	}
	
	//health getter!
	public int getHealth() {
		return this.health;
	}



	@Override
	public void draw(Graphics g) {
		if(this.health >= 75) {
			g.setColor(Color.RED);
			g.fillOval(this.getPx() - this.width / 2, this.getPy() - this.height / 2, this.width, this.height);
			g.setColor(Color.BLUE);
			g.fillOval(this.getPx() , this.getPy() , this.width / 3, this.height / 3);
		}
		else if (this.health >= 50) {
			g.setColor(Color.GREEN);
			g.fillOval(this.getPx() - this.width / 2, this.getPy() - this.height / 2, this.width, this.height);
			g.setColor(Color.BLUE);
			g.fillOval(this.getPx(), this.getPy(), this.width / 3, this.height / 3);
		}
		else {
			g.setColor(Color.BLUE);
			g.fillOval(this.getPx() - this.width / 2, this.getPy() - this.height / 2, this.width, this.height);
			g.setColor(Color.RED);
			g.fillOval(this.getPx(), this.getPy(), this.width / 3, this.height / 3);
		}
	}
}
