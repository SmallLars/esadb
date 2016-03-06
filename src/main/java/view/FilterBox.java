package main.java.view;


import java.util.Arrays;
import java.util.Vector;

import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;


@SuppressWarnings("serial")
public class FilterBox extends JComboBox<Object> {

	public FilterBox(String noFilter, Object[] items) {
		this(noFilter, new Vector<Object>(Arrays.asList(items)));		
	}

	public FilterBox(String noFilter, Vector<Object> items) {
		items.insertElementAt(noFilter, 0);
		setModel(new DefaultComboBoxModel<Object>(items));
	}

	public boolean doFilter() {
		return getSelectedIndex() > 0;
	}
}