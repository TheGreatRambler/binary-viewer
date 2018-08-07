package gui;

//event libs
import java.awt.event.*;

//gui libs
import javax.swing.*;

//file libs
import java.io.File;

// thread stuff
import java.lang.Thread;

public class openFile {
	private static File oldFile;
	public static void run(ActionEvent e) {
		gui.drawingLocation.resetCompletely();
		JFileChooser chooseFile = new JFileChooser();
		chooseFile.setCurrentDirectory(new File(System.getProperty("user.home")));
		int result = chooseFile.showOpenDialog(gui.frame);
		if (result == JFileChooser.APPROVE_OPTION) {
		    // user selects a file
			File selectedFile = chooseFile.getSelectedFile();
			oldFile = selectedFile;
			Thread thread = new Thread(new getFileBits(selectedFile));
			thread.start();
		}
	}
	
	public static File getOldFile() {
		return oldFile;
	}
}
