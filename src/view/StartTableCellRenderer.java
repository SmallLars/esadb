package view;


import java.awt.Component;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

import model.Start;


@SuppressWarnings("serial")
public class StartTableCellRenderer extends DefaultTableCellRenderer {

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
		super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

		if (value instanceof Float) {
			setHorizontalAlignment(RIGHT);
			setIcon(null);
			setText(String.format("%.1f", (Float) value));
			setForeground(null);
			return this;
		}

		if (value instanceof Start) {
			setHorizontalAlignment(LEFT);
			setIcon(null);
			setText(value.toString());
			setForeground(null);
			return this;
		}

		return this;
	}
}