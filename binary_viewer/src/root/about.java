package root;

import java.awt.Dimension;
import java.io.IOException;

import javax.swing.*;

//commons io
import org.apache.commons.io.FileUtils;

import gui.makeClickableHyperlink;
import helpers.help;

public class about extends JFrame {
	private static final long serialVersionUID = 1L;

	public void start() {
		setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		setPreferredSize(new Dimension(300, 220));
		pack();
		setAlwaysOnTop(true);
		setLocationRelativeTo(null);
		JTabbedPane infoPane = new JTabbedPane();
		
		JEditorPane program = new JEditorPane();
		program.setContentType("text/html");
		program.setEditable(false);
		try {
			program.setText(FileUtils.readFileToString(help.getResource("programInfo.html"), "UTF-8"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//program.setVerticalAlignment(SwingConstants.TOP);
		program.addHyperlinkListener(new makeClickableHyperlink());
		
		JEditorPane libsUsed = new JEditorPane();
		libsUsed.setContentType("text/html");
		libsUsed.setEditable(false);
		try {
			libsUsed.setText(FileUtils.readFileToString(help.getResource("libsUsed.html"), "UTF-8"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//libsUsed.setVerticalAlignment(SwingConstants.TOP);
		libsUsed.addHyperlinkListener(new makeClickableHyperlink());
		
		infoPane.addTab("Program", program);
		infoPane.addTab("Libraries used", libsUsed);
		getContentPane().add(infoPane);
		
		//addWindowListener(new WindowListener() {
			
		//});
	}
}
