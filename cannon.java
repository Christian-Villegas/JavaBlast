import apple.laf.JRSUIConstants;

import java.awt.Color;
import java.awt.Graphics;
import javax.swing.*;

public class cannon extends GameObject{
	
	public static final int SIZE = 40;
	public static final int INIT_VX = 0; 
	public static final int INIT_VY = 0; 
	
	public cannon(int courtWidth, int courtHeight) {
		super(INIT_VX, INIT_VY, (courtWidth / 2),
				courtHeight - (SIZE / 2), SIZE, SIZE,
			  courtWidth, courtHeight);
	}

	@Override
	public void draw(Graphics g) {
		//System.out.println("in the paint component, the cannon py is: " + this.getPy());
		g.setColor(Color.RED);
		g.fillRect(this.getPx() -  (this.SIZE / 2), this.getPy() - (this.SIZE / 2 ), this.SIZE, this.SIZE);
		g.drawLine(0, 430, 400, 430);
		g.drawLine(0, 410, 400, 410);
		g.setColor((Color.BLUE));
		g.fillRect(this.getPx(), this.getPy(), this.SIZE/2, this.SIZE /2);
	}

	@Override
	public void setHealth(int i) {
	}

}
