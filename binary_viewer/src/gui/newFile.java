package gui;

import java.awt.event.ActionEvent;

import javax.swing.JOptionPane;

public class newFile {
	public static void run(ActionEvent e) {
		String numOfBytes = (String) JOptionPane.showInputDialog(gui.frame, "Numbver of bytes", "Create new", JOptionPane.PLAIN_MESSAGE, null, null, 1024);
		gui.drawingLocation.createNew(Integer.parseInt(numOfBytes));
	}
}
