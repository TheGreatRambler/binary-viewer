package gui;

import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;

import javax.swing.JFileChooser;

import org.apache.commons.io.FileUtils;

public class saveFile {
	public static void run(ActionEvent e) {
		JFileChooser saveFile = new JFileChooser();
		String oldFile;
		try {
			oldFile = openFile.getOldFile().getAbsolutePath(); // errors happen if you try to save a file when no file exists
		} catch (NullPointerException varNotFound) {
			oldFile = System.getProperty("user.home") + File.separator + "new.txt";
		}
		String fileDirectoryToStart = oldFile.substring(0, oldFile.lastIndexOf(File.separator));
		saveFile.setCurrentDirectory(new File(fileDirectoryToStart));
		int result = saveFile.showSaveDialog(gui.frame);
		if (result == JFileChooser.APPROVE_OPTION) {
		    // user selects a directory
			File selectedDirectory = saveFile.getSelectedFile();
			byte[] bytesToSave = gui.drawingLocation.getEditedBytes();
			try {
				FileUtils.writeByteArrayToFile(selectedDirectory, bytesToSave);
			} catch (IOException e1) {
				System.out.println("Directory is invalid");
			}
		}
	}
}