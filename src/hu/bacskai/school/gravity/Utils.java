package hu.bacskai.school.gravity;

import java.awt.Graphics2D;
import java.awt.Graphics;

import static java.awt.RenderingHints.*;

public class Utils {
    
    private Utils() {}

    public static void setRenderingHints(Graphics g) {
        Graphics2D gg = (Graphics2D) g;
        gg.setRenderingHint(KEY_ALPHA_INTERPOLATION, VALUE_ALPHA_INTERPOLATION_QUALITY);
        gg.setRenderingHint(KEY_TEXT_ANTIALIASING, VALUE_TEXT_ANTIALIAS_ON);
        gg.setRenderingHint(KEY_ANTIALIASING, VALUE_ANTIALIAS_ON);
        gg.setRenderingHint(KEY_RENDERING, VALUE_RENDER_QUALITY);
    }

    public static void sleepNS(long nanos) throws InterruptedException {
        long ms = nanos / 1_000_000;
        nanos %= 1_000_000;
        Thread.sleep(ms, (int) nanos);
    }

    public static double rad(double deg) {
        return deg * Math.PI / 180;
    }

    public static double deg(double rad) {
        return rad * 180 / Math.PI ;
    }
}
