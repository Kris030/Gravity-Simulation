package hu.bacskai.school.gravity;

import java.awt.geom.AffineTransform;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.text.NumberFormat;
import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.Font;

// import static hu.bacskai.school.gravity.Utils.rad;

public class Rendering {

	// private static BufferedImage img;
	static void initRendering() {
		// int w = 800, h = 600;
		// img = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
		// int[] rgbRaster = ((DataBufferInt) img.getRaster().getDataBuffer()).getData();

        // for (int y = 0; y < h; y++)
        //     for (int x = 0; x < w; x++)
		// 		rgbRaster[(w * y) + x] = Color.HSBtoRGB((float) x / w, (float) y / h, 1);
		
	}

	static long frames = 0;
	static double ls;
	private static void preRender() {

		if (GravityMain.getSPassed() - ls >= 1) {
			ls = GravityMain.getSPassed();
			frames = 0;
		}
		frames++;
	}

    static void render(double d) {
		preRender();

		BufferStrategy bs = GravityMain.canvas.getBufferStrategy();
    	if (bs == null) {
    		GravityMain.canvas.createBufferStrategy(2);
    		return;
    	}

		float w = GravityMain.canvas.getWidth(), h = GravityMain.canvas.getHeight();
		int wc = (int) Math.ceil(w), hc = (int) Math.ceil(h);

    	Graphics2D g = (Graphics2D) bs.getDrawGraphics();
		AffineTransform ot = g.getTransform();
		
		AffineTransform at = new AffineTransform(ot);
		at.scale(1, -1);
		at.translate(0, -h);
		
    	g.setColor(Color.darkGray);
    	g.fillRect(0, 0, wc, hc);

		g.setTransform(at);

    	Utils.setRenderingHints(g);
    	for (GameObject go : Simulation.gos) {
			g.translate(go.x, go.y);
			go.draw(g);
			g.setTransform(at);
		}

		g.translate(10, 10);
		g.scale(1, -1);

		drawHUD(g);

    	bs.show();
    	g.dispose();
    }

	static double fps;
	private static void drawHUD(Graphics2D g) {
		if (frames % 5 == 0)
			fps = frames / (GravityMain.getSPassed() - ls);
		
		g.setColor(Color.black);
		g.setFont(new Font("Fira Code", Font.BOLD, 30));
		g.drawString("FPS: " + 
			String.format("%.3f", (fps)),
			0, 0
		);

	}
    
}
