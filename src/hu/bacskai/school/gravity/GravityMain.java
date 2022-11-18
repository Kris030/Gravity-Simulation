package hu.bacskai.school.gravity;

import java.awt.event.MouseWheelListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.MouseAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.MouseEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.MouseInfo;
import java.awt.Canvas;

import javax.swing.JFrame;

public class GravityMain {

	private static void test() throws InterruptedException {
		final int TPS = 100, FPS = 120;
		final int Tick = 0, Render = 1;

		int ticks = 0, renders = 0;

		long tWait = Math.round(1_000_000_000.0 / TPS);
		long rWait = Math.round(1_000_000_000.0 / FPS);

		int nOP = Render;
		long now = System.nanoTime();
		long render_next = now;
		long tick_next = now;

		double secs = 5;
		// long start = now;
		long end = now + Math.round(secs * 1_000_000_000);

		////
		long nextOPT, margin = 750_000, s;
		////

		while (System.nanoTime() < end) {
			switch (nOP) {
				case Tick:
					System.out.println("Tick " + ticks++);
					// Thread.sleep(0, 1);

					now = System.nanoTime();
					tick_next = now + tWait;
					break;

				case Render:
					
					System.out.println("Render " + renders++);
					// Thread.sleep(0, 1);

					now = System.nanoTime();
					render_next = now + rWait;
					break;
			}

			// TODO: somehow prioritise ticks
			if (tick_next <= render_next) {
				nOP = Tick;
				nextOPT = tick_next;
			} else {
				nOP = Render;
				nextOPT = render_next;
			}

			now = System.nanoTime();
			if (nextOPT <= now)
				continue;
			
			s = nextOPT - now - margin;
			if (s > margin && s > 0)
				Utils.sleepNS(s);
			
			while (System.nanoTime() < nextOPT) {
				for (int i = 0; i < 100; i++) {
					
				}
			}
		}

		System.out.println("Overall TPS: " + ticks / secs + " tps vs expected: " + TPS);
		System.out.println("Overall FPS: " + renders / secs + " fps vs expected: " + FPS);
		System.out.println(tWait);
		System.out.println(rWait);
	}

	public static void main(String[] args) {
		try {
			test();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		if (System.currentTimeMillis() < 0) init();
	}
	
	private static void onClose() {
		running = false;
	}

	public static void afterLoop() {
		frame.dispose();
	}

	private static JFrame frame;
	static Canvas canvas;
	static boolean running;
	
	private static void init() {
		Input.mouse = new int[MouseInfo.getNumberOfButtons()];
		Input.keyboard = new int[0x020D];
		
		frame = new JFrame("Gravity Simulation");
		
		frame.setSize(800, 600);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		
		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				onClose();
			}
		});
		
		frame.setResizable(false);
		
		canvas = new Canvas();
		frame.add(canvas);
		
		canvas.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				int b = e.getButton();

				// is pressed bit unset?
				if ((Input.mouse[b] & 1) == 0)
					// set pressed bit and just pressed bit to true
					Input.mouse[b] |= 0b11;
				else // set just pressed bit to false
					Input.mouse[b] &= ~0b10;
			}
			public void mouseReleased(MouseEvent e) {
				// unset pressed and just pressed bits
				Input.mouse[e.getButton()] &= ~0b11;
			}
		});
		canvas.addMouseWheelListener(new MouseWheelListener() {
			public void mouseWheelMoved(MouseWheelEvent e) {
				Input.processedMouseWheel = false;
				Input.mouseWheelDelta += e.getScrollAmount() * e.getWheelRotation();
			}
		});
		
		canvas.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				int b = e.getKeyCode();

				// is pressed bit unset?
				if ((Input.keyboard[b] & 1) == 0)
					// set pressed bit and just pressed bit to true
					Input.keyboard[b] |= 0b11;
				else // set just pressed bit to false
					Input.keyboard[b] &= ~0b10;
			}
			public void keyReleased(KeyEvent e) {
				// unset pressed and just pressed bits
				Input.keyboard[e.getKeyCode()] &= ~0b11;
			}
		});
		
		frame.setVisible(true);
		canvas.requestFocus();
		
		Simulation.initSimulation();
		Rendering.initRendering();

		loop();
	}
	
	private static int currentFPS = -1;
	private static void loop() {
		final int targetTPS = 100, targetFPS = 30;

		/* V1
		// final double nsPTick = Math.round(1_000_000_000.0 / targetTPS);
		final double nsPFrame = Math.round(1_000_000_000.0 / targetFPS);
		
		running = true;
		while (running) {
			long then = System.nanoTime();
			long secEnd = then + 1_000_000_000;
			
			// 1. Tick until required TPS
			for (int t = 0; t < targetTPS; t++) {
				Input.processInput();
				Simulation.tick();
			}

			// 2. Render + afterprocess until
			long now = System.nanoTime();

			long estFrames = Math.round((secEnd - now) / nsPFrame);

			long f = 0;
			for (; now < secEnd && f < estFrames; f++) {
				Rendering.render(1);
				now = System.nanoTime();
			}
			currentFPS = (int) f;
			
			afterProcessing();
			
			now = System.nanoTime();
			// end of second
			if (now >= secEnd)
				continue;
				
			// reached target FPS => sleep
			long ns = secEnd - now;
			try {
				Utils.sleepNS(ns);
			} catch (InterruptedException e) {
				running = false;
				e.printStackTrace();
				System.err.println("Thread interrupted for some reason...");
			}
		}
		*/

		/* V2 */

		/* */

		long nsBTWtick = Math.round(1_000_000_000.0 / targetTPS);
		long nsBTWframe = Math.round(1_000_000_000.0 / targetFPS);

		// long start = System.nanoTime();
		
		long lastTick = System.nanoTime();
		long lastFrame = lastTick;

		// start one frame ahead
		long nextTick = lastTick + nsBTWtick;
		long nextFrame = lastFrame;

		
		final byte TICK = 0, RENDER = 1;
		byte next = RENDER;
		
		running = true;
		while (running) {
			if (next == TICK) {
				Input.processInput();
				Simulation.tick();
			} else
				Rendering.render(1);
			
			Input.afterProcessing();

			long now = System.nanoTime();

			if (next == TICK)
				nextTick = now + nsBTWtick;
			else
				nextFrame = now + nsBTWframe;
			
			long ns;
			if (nextTick <= nextFrame) {
				next = TICK;
				ns = nsBTWtick;
			} else {
				next = RENDER;
				ns = nsBTWframe;
			}

			try {
				Utils.sleepNS(ns);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		afterLoop();
	}

	public static int getCurrentFPS() {
		return currentFPS;
	}
}
