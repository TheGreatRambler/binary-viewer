package root;

import gui.gui;

public class start {
	public static void main(String[] args) {
		// node to run: run with -Dsun.java2d.d3d=false if not working on older windows systems
		// https://superuser.com/questions/388918/java-swing-over-remote-desktop-strange-weird-gui-squashing
		gui.createGui();
	}
}
