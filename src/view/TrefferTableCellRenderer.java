package view;

import java.awt.Color;
import java.awt.Component;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;


@SuppressWarnings("serial")
public class TrefferTableCellRenderer extends DefaultTableCellRenderer {

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
		super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

		if (value instanceof Integer) {
			setHorizontalAlignment(RIGHT);
			setIcon(null);
			setText(String.format("%d  ", (Integer) value));
			setForeground(null);
			return this;
		}

		if (value instanceof Boolean) {
			setHorizontalAlignment(LEFT);
			setIcon(null);
			Boolean b = (Boolean) value;
			setText(b ? "  Probe" : "  Match");
			setForeground(b ? Color.decode("0x00A050") : Color.decode("0xC80000"));
			return this;
		}

		if (value instanceof Float) {
			setHorizontalAlignment(RIGHT);
			setIcon(null);
			setText(String.format("%.1f  ", (Float) value));
			setForeground(null);
			return this;
		}

		if (value instanceof String) {
	    	URL url = getClass().getResource("/" + (String) value + ".png");
			setHorizontalAlignment(CENTER);
			setIcon(url == null ? null : new ImageIcon(url));
			setText("");
			setForeground(null);
			return this;
		}

		return this;
	}
}