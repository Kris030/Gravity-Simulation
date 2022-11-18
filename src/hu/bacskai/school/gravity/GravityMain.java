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

	public static void main(String[] args) {
		init();
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

		startMS = System.currentTimeMillis();
		startNS = System.nanoTime();
		loop();
	}
	private static long startMS, startNS;
	private static double SDT;
	private static long NSDT;
	public static long getStart() {
		return startMS;
	}
	public static long getNSPassed() {
		return NSDT;
	}
	public static double getSPassed() {
		return SDT;
	}

	public static final int targetTPS = 100, targetFPS = 120;
	private static void loop() {

		/* V1
		// final double nsPTick = Math.round(1_000_000_000d / targetTPS);
		final double nsPFrame = Math.round(1_000_000_000d / targetFPS);
		
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

		long nsBTWtick = Math.round(1_000_000_000d / targetTPS);
		long nsBTWframe = Math.round(1_000_000_000d / targetFPS);

		final byte TICK = 0, RENDER = 1;
		// start with a render to show stuff earlier
		byte next = RENDER;
		
		// start one frame ahead
		long nextTick = 0, nextFrame = -1;
		long nextAction, now, wait;

		final long margin = 100_000;

		running = true;
		while (running) {
			
			now = System.nanoTime();
			if (next == TICK) {
				nextTick = now + nsBTWtick;

				Input.processInput();
				NSDT = now - startNS;
				SDT = NSDT / 1_000_000_000d;
				Simulation.tick();
				Input.afterProcessing();

			} else {
				nextFrame = now + nsBTWframe;

				Rendering.render(1);
			}
			
			if (nextTick <= nextFrame) {
				next = TICK;
				nextAction = nextTick;
			} else {
				next = RENDER;
				nextAction = nextFrame;
			}

			now = System.nanoTime();

			// if we're already behind, don't wait
			// TODO: skip the next frame if needed
			wait = nextAction - now;
			if (wait <= 0)
				continue;
			
			// if we have more time than the busy
			// wait margin do a regular sleep
			wait -= margin;
			if (wait > 0) {
				try {
					Utils.sleepNS(wait);
				} catch (InterruptedException e) {
					e.printStackTrace();
					break;
				}
			} else
				wait += margin;
			
			// do the rest of the waiting busily
			while (System.nanoTime() < nextAction);
		}

		afterLoop();
	}

}
