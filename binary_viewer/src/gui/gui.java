package gui;

import javax.imageio.ImageIO;
// gui libs
import javax.swing.*;

import root.about;
import helpers.help;

import java.awt.*;

//event libs
//import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

// jhexview
import tv.porst.jhexview.JHexView;

public class gui {
	public static JFrame frame;
	public static JProgressBar fileLoadingProgress;
	public static ButtonGroup bitToDraw;
	public static binaryDisplay drawingLocation = new binaryDisplay();
	public static JHexView hexView = new JHexView();
	public static about aboutFrame = new about();
	
	private static void setDefaultFonts() {
		//Font programFont = new Font("Courier", Font.PLAIN, 20);
		//UIManager.put("MenuBar.font", programFont);
		//UIManager.put("MenuItem.font", programFont);
		//UIManager.put("Menu.font", programFont);
		/*
		UIManager.put("Button.font", programFont);
		UIManager.put("ToggleButton.font", programFont);
		UIManager.put("RadioButton.font", programFont);
		UIManager.put("CheckBox.font", programFont);
		UIManager.put("ColorChooser.font", programFont);
		UIManager.put("ComboBox.font", programFont);
		UIManager.put("Label.font", programFont);
		UIManager.put("List.font", programFont);
		UIManager.put("MenuBar.font", programFont);
		UIManager.put("MenuItem.font", programFont);
		UIManager.put("RadioButtonMenuItem.font", programFont);
		UIManager.put("CheckBoxMenuItem.font", programFont);
		UIManager.put("Menu.font", programFont);
		UIManager.put("PopupMenu.font", programFont);
		UIManager.put("OptionPane.font", programFont);
		UIManager.put("Panel.font", programFont);
		UIManager.put("ProgressBar.font", programFont);
		UIManager.put("ScrollPane.font", programFont);
		UIManager.put("Viewport.font", programFont);
		UIManager.put("TabbedPane.font", programFont);
		UIManager.put("Table.font", programFont);
		UIManager.put("TableHeader.font", programFont);
		UIManager.put("TextField.font", programFont);
		UIManager.put("PasswordField.font", programFont);
		UIManager.put("TextArea.font", programFont);
		UIManager.put("TextPane.font", programFont);
		UIManager.put("EditorPane.font", programFont);
		UIManager.put("TitledBorder.font", programFont);
		UIManager.put("ToolBar.font", programFont);
		UIManager.put("ToolTip.font", programFont);
		UIManager.put("Tree.font", programFont);
		*/
	}
	
	private static void setNativeTheme() {
	    try {
	        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
	    } catch(Exception ex) {
	        ex.printStackTrace();
	    }
	}

	private static void setup() {
		setDefaultFonts();
		setNativeTheme();
	}
	
	public static void createGui() {
		setup();
		aboutFrame.start();
		
		frame = new JFrame("Binary viewer");
	    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
	    frame.setLocationRelativeTo(null);
	    frame.setResizable(true);
	    frame.setUndecorated(false); // dont change!
	    frame.setIconImage((new ImageIcon(ClassLoader.getSystemResource("bitLogo.png"))).getImage());
	    
	    //Creating the MenuBar and adding components
        JMenuBar mb = new JMenuBar();
        
        JMenu fileMenu = new JMenu("File");
        mb.add(fileMenu);
        
        JMenu helpMenu = new JMenu("Help");
        
        JMenuItem aboutMenu = new JMenuItem("About");
        aboutMenu.setActionCommand("about");
        aboutMenu.addActionListener(new onClickMenuItems());
        helpMenu.add(aboutMenu);
        
        mb.add(helpMenu);
        
        JMenuItem newFileMenu = new JMenuItem("New file");
        newFileMenu.setActionCommand("new_file");
        newFileMenu.addActionListener(new onClickMenuItems());
        fileMenu.add(newFileMenu);
        
        JMenuItem openFileMenu = new JMenuItem("Open");
        openFileMenu.setActionCommand("open_file");
        openFileMenu.addActionListener(new onClickMenuItems());
        fileMenu.add(openFileMenu);
        
        JMenuItem saveAsFileMenu = new JMenuItem("Save as");
        saveAsFileMenu.setActionCommand("save_file");
        saveAsFileMenu.addActionListener(new onClickMenuItems());
        fileMenu.add(saveAsFileMenu);
        
        JMenuItem quitFileMenu = new JMenuItem("Quit");
        quitFileMenu.setActionCommand("quit");
        quitFileMenu.addActionListener(new onClickMenuItems());
        fileMenu.add(quitFileMenu);
        
        frame.getContentPane().add(BorderLayout.NORTH, mb);
        
        JTabbedPane rightInfoPane = new JTabbedPane();
        
        JPanel paintBitMenu = new JPanel();
        paintBitMenu.setLayout(new BoxLayout(paintBitMenu, BoxLayout.Y_AXIS));
        
        JLabel paintBitLabel = new JLabel("Paint bit");
        bitToDraw = new ButtonGroup();
        JRadioButton trueRadioButton = new JRadioButton("1", true);
        trueRadioButton.setActionCommand("bitPaintTrue");
        JRadioButton falseRadioButton = new JRadioButton("0");
        falseRadioButton.setActionCommand("bitPaintFalse");
        bitToDraw.add(trueRadioButton);
        bitToDraw.add(falseRadioButton);
        
        JLabel pageChangeLabel = new JLabel("Change page");
        //GridLayout pageChangeButtons = new GridLayout(0, 2);
        
        JButton arrowLeft = new JButton();
        arrowLeft.setActionCommand("page_left");
        arrowLeft.setToolTipText("Page left");
        BufferedImage pageLeftImg = null;
		try {
			pageLeftImg = ImageIO.read(help.getResource("arrow-left.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        arrowLeft.setIcon(new ImageIcon(pageLeftImg, "Page left"));
        arrowLeft.addActionListener(new onClickMenuItems());
        //pageChangeButtons.addLayoutComponent("arrow_left", arrowLeft);
        
        JButton arrowRight = new JButton();
        arrowRight.setActionCommand("page_right");
        arrowRight.setToolTipText("Page right");
        BufferedImage pageRightImg = null;
		try {
			pageRightImg = ImageIO.read(help.getResource("arrow-right.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        arrowRight.setIcon(new ImageIcon(pageRightImg, "Page right"));
        arrowRight.addActionListener(new onClickMenuItems());
        //pageChangeButtons.addLayoutComponent("arrow_right", arrowRight);
        
        paintBitMenu.add(paintBitLabel);
        paintBitMenu.add(trueRadioButton);
        paintBitMenu.add(falseRadioButton);
        paintBitMenu.add(pageChangeLabel);
        paintBitMenu.add(arrowLeft);
        paintBitMenu.add(arrowRight);
        
        rightInfoPane.addTab("Paint settings", paintBitMenu);
        
        frame.getContentPane().add(BorderLayout.EAST, rightInfoPane);
        
        JTabbedPane editPane = new JTabbedPane();
        drawingLocation.setup();
        //canvas.setSize(400, 400);
        //frame.add(drawingLocation);
        editPane.addTab("Bits", drawingLocation);
        editPane.addTab("Hex", hexView);
        frame.add(editPane);
	    
	    //JButton button = new JButton("Press");
	    //frame.getContentPane().add(button);
	    frame.setVisible(true);
	}
}
