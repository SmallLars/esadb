package main.java.view;


import java.awt.event.ActionListener;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JButton;


@SuppressWarnings("serial")
public class IconButton extends JButton {

	public static final String LEFT   = "f";
	public static final String RIGHT  = "g";
	public static final String UP     = "h";
	public static final String DOWN   = "i";
	public static final String ADD    = "add";
	public static final String REMOVE = "rem";

	/**
	 * @wbp.parser.constructor
	 */
	public IconButton() {}

	public IconButton(String dir, String ac, ActionListener al) {
		URL url = getClass().getResource("/" + dir + ".png");
		setIcon(new ImageIcon(url));
		setActionCommand(ac);
		addActionListener(al);
	}
}