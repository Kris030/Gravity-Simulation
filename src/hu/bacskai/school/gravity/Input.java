package hu.bacskai.school.gravity;

import java.awt.Point;

public class Input {

	static void processInput() {
		Point l = GravityMain.canvas.getMousePosition();
		if (l == null)
			Input.mouseAway = true;
		else
			Input.mousePosition = l;
	}

	static void afterProcessing() {
		if (!processedMouseWheel) {
			mouseWheelDelta = 0;
			processedMouseWheel = true;
		}
	
		for (int k = 0; k < keyboard.length; k++)
			// set just pressed bit to false
			keyboard[k] &= ~0b10;
	
		for (int k = 0; k < mouse.length; k++)
			// set just pressed bit to false
			mouse[k] &= ~0b10;
	
	}

	/**
	 * 8. bit - being held
	 * 7. bit - just pressed
	 */
	static int[] keyboard;
	
	/**
	 * 8. bit - being held
	 * 7. bit - just pressed
	 */
	static int[] mouse;
	static boolean mouseAway;
	static Point mousePosition;
	
	static boolean processedMouseWheel = true;
	static double mouseWheelDelta;
	
	public static boolean keyPressed(int keyCode) {
		return (keyboard[keyCode] & 0b10) != 0;
	}
	public static boolean keyHeld(int keyCode) {
		return (keyboard[keyCode] & 0b1) != 0;
	}

	public static boolean mousePressed(int keyCode) {
		return (mouse[keyCode] & 0b10) != 0;
	}
	public static boolean mouseHeld(int keyCode) {
		return (mouse[keyCode] & 0b1) != 0;
	}
	
	public static boolean isMouseAway() {
		return mouseAway;
	}
	
	public static Point getMousePosition() {
		return mousePosition;
	}
	
	public static double getMouseWheelDelta() {
		return mouseWheelDelta;
	}
}
