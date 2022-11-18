package hu.bacskai.school.gravity;

import java.awt.Graphics2D;

public abstract class GameObject {
	
	double x, y;
	
	public abstract void tick();
	public abstract void draw(Graphics2D g);

	public double getX() {
		return x;
	}
	
	public double getY() {
		return y;
	}
}
