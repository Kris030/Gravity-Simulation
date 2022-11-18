package hu.bacskai.school.gravity;

import java.awt.geom.AffineTransform;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.Font;

import static hu.bacskai.school.gravity.Utils.rad;

public class Rendering {

	private static BufferedImage img;
	static void initRendering() {
		int w = 800, h = 600;
		img = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
		int[] rgbRaster = ((DataBufferInt) img.getRaster().getDataBuffer()).getData();

        for (int y = 0; y < h; y++)
            for (int x = 0; x < w; x++)
				rgbRaster[(w * y) + x] = Color.HSBtoRGB((float) x / w, (float) y / h, 1);
		
	}

	static int frame = 0;
	static double rot = 0;
    static void render(double d) {
		BufferStrategy bs = GravityMain.canvas.getBufferStrategy();
    	if (bs == null) {
    		GravityMain.canvas.createBufferStrategy(2);
    		return;
    	}

		float w = GravityMain.canvas.getWidth(), h = GravityMain.canvas.getHeight();
		int wc = (int) Math.ceil(w), hc = (int) Math.ceil(h);

    	Graphics2D g = (Graphics2D) bs.getDrawGraphics();
		// AffineTransform ot = g.getTransform();
		// AffineTransform at = (AffineTransform) ot.clone();
		
    	g.setColor(Color.darkGray);
    	g.fillRect(0, 0, wc, hc);
		
		// g.translate(w / 2, h / 2);
		// g.rotate(rad(rot));
		g.scale(1, -1);

		g.drawImage(img, 0, 0, null);

    	/*
        Utils.setRenderingHints(g);
    	for (GameObject go : Simulation.gos) {
			// g.setTransform(at);
			// g.translate(go.x, go.y);
			go.draw(g);
		}

		g.setTransform(ot);
		g.setColor(new Color(0, 120, 180));
		g.setFont(new Font("Fira Code", Font.BOLD, 40));
		g.drawString("FPS: " + GravityMain.getCurrentFPS(), 10, GravityMain.canvas.getHeight() - 10);
    	*/
    	bs.show();
    	g.dispose();
    }
    
}
