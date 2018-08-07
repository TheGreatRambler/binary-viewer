package gui;

//event libs
import java.awt.event.*;

public class onClickMenuItems implements ActionListener, ItemListener {
	public void actionPerformed(ActionEvent e) {
        //...Get information from the action event...
        //...Display it in the text area...
		String command = e.getActionCommand();
		switch (command) {
			case "about":
				gui.aboutFrame.setVisible(true);
				break;
			case "new_file":
				newFile.run(e);
				break;
			case "open_file":
				openFile.run(e);
				break;
			case "save_file":
				saveFile.run(e);
				break;
			case "quit":
				System.exit(0);
				break;
			case "page_left":
				gui.drawingLocation.flipPage(0);
				break;
			case "page_right":
				gui.drawingLocation.flipPage(1);
				break;
			default:
				System.out.println("Incorrect gui command");
				break;
		}
    }

    public void itemStateChanged(ItemEvent e) {
        //...Get information from the item event...
        //...Display it in the text area...
    }
}
