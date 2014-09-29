package view;

import java.awt.Color;
import java.awt.Component;

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

		if (value instanceof String) {
			setHorizontalAlignment(LEFT);
			setIcon(null);
			String s = (String) value;
			setText("  " + s);
			setForeground(s.equals("Probe") ? Color.decode("0x00A050") : Color.decode("0xC80000"));
			return this;
		}

		if (value instanceof Float) {
			setHorizontalAlignment(RIGHT);
			setIcon(null);
			setText(String.format("%.1f  ", (Float) value));
			setForeground(null);
			return this;
		}

		if (value instanceof ImageIcon) {
			setHorizontalAlignment(CENTER);
			setIcon((ImageIcon) value);
			setText("");
			setForeground(null);
			return this;
		}

		return this;
	}
}