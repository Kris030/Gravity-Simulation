package hu.bacskai.school.gravity;

import java.awt.Graphics2D;
import java.awt.Color;

public class Planet extends GameObject {

    double vx, vy, weight, r;

    public Planet(double x, double y, double vx, double vy, double weight, double r) {
        this.weight = weight;
        this.vx = vx;
        this.vy = vy;
        this.x = x;
        this.y = y;
        this.r = r;
    }

    public static Planet createWithDensity(double x, double y, double vx, double vy, double density, double weight, double exad) {
        double r = Math.pow(3 * weight / (4 * Math.PI * density), 1 / 3d) * exad;
        return new Planet(x, y, vx, vy, weight, r);
    }

    public void tick() {
        
        for (GameObject g : Simulation.gos) {
            if (g == this || !(g instanceof Planet))
            continue;
            
            Planet p = (Planet) g;
            
            double xx = p.x - x, yy = p.y - y;
            double dsq = xx * xx + yy * yy;
            double dist = Math.sqrt(dsq);
            
            double v = p.weight / dsq;

            double dx = xx / dist * v;
            double dy = yy / dist * v;

            vx += dx;
            vy += dy;
        }

        x += vx; y += vy;
    }

    public void draw(Graphics2D g) {
        float h = (GravityMain.getNSPassed() % 10_000_000_000l) / 10_000_000_000f;

        g.setColor(new Color(Color.HSBtoRGB(h, .5f, .85f)));
        g.fillOval((int) -r, (int) -r, (int) (r * 2), (int) (r * 2));
    }
    
}
