package gui;

// for colors
import java.awt.Color;
import java.awt.Font;

public class settings {
	public static int binaryWidth = 128;
	public static int binaryBoxWidth = 15;
	public static int fileBufferSize = 512; // 0.5 kilobyte
	public static Color bitSurroundingRectsColor = new Color(255, 0, 0);
	public static Color bitSurroundingRectsBorderColor = new Color(255, 0, 0, 55);
	public static Color trueBitColor = new Color(32, 32, 32);
	public static Color falseBitColor = new Color(220, 220, 220);
	public static Color bitBorderColor = new Color(0, 0, 0);
	public static Font infoHoverFont = new Font("Courier New", Font.BOLD, 20);
	public static Color textBackgroundColor = new Color(255, 255, 255);
	public static Color textBorderColor = new Color(0, 0, 0);
}
