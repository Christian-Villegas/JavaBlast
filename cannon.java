import apple.laf.JRSUIConstants;

import java.awt.Color;
import java.awt.Graphics;
import javax.swing.*;

public class cannon extends GameObject{
	
	public static final int SIZE = 40;
	public static final int INIT_VX = 0; 
	public static final int INIT_VY = 0; 
	
	public cannon(int courtWidth, int courtHeight) {
		super(INIT_VX, INIT_VY, courtWidth/2,
				courtHeight, SIZE, SIZE,
			  courtWidth, courtHeight);
		System.out.println(this.getPy());
	}

	@Override
	public void draw(Graphics g) {
		g.setColor(Color.RED);
		g.fillRect(this.getPx() -  (this.SIZE / 2), this.getPy() - (this.SIZE / 2), this.SIZE, this.SIZE);
		g.setColor((Color.BLUE));
		g.fillRect(this.getPx(), this.getPy(), this.SIZE/2, this.SIZE /2);
	}

	@Override
	public void setHealth(int i) {
	}

	public int getSize(){
		return this.SIZE;
	}

}
