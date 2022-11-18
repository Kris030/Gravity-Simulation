package hu.bacskai.school.gravity;

import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.Color;

import java.util.ArrayList;

public class Simulation {
	static ArrayList<GameObject> gos;
        	
	static void initSimulation() {
		gos = new ArrayList<>();
		
		gos.add(new GameObject() {
            
			public void tick() {
				if (Input.keyHeld(KeyEvent.VK_RIGHT))
					x += 1;
				if (Input.keyHeld(KeyEvent.VK_LEFT))
					x -= 1;
				
				if (Input.keyHeld(KeyEvent.VK_UP))
					y -= 1;
				if (Input.keyHeld(KeyEvent.VK_DOWN))
					y += 1;
			}
			public void draw(Graphics2D g) {
				g.setColor(Color.red);
				g.fillRoundRect((int) 25, (int) 25, 50, 50, 10, 10);
			}
		});
	}

    static void tick() {

		// TODO: remove
		// for (int i = 0; i < Input.keyboard.length; i++)
		// 	if (Input.keyPressed(i))
		// 		System.out.println(KeyEvent.getKeyText(i) + " was just pressed");

		if (Input.keyHeld(KeyEvent.VK_Q))
			GravityMain.running = false;


		if (Input.keyHeld(KeyEvent.VK_R))
			System.out.println(Rendering.rot += 1);

		for (GameObject g : gos)
			g.tick();
	}
}
