package view;


import java.awt.Component;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;


@SuppressWarnings("serial")
public class ResultTableCellRenderer extends DefaultTableCellRenderer {

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
		super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

		if (value instanceof Float) {
			setHorizontalAlignment(RIGHT);
			setText(String.format("%.1f", (Float) value));
		} else {
			setHorizontalAlignment(LEFT);
			setText(value.toString());
		}

		return this;
	}
}