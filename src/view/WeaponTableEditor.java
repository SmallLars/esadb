package view;

import java.awt.Component;

import javax.swing.AbstractCellEditor;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.SpinnerNumberModel;
import javax.swing.table.TableCellEditor;


@SuppressWarnings("serial")
public class WeaponTableEditor extends AbstractCellEditor implements TableCellEditor {

	JComponent component = null;

	public WeaponTableEditor(JComponent component) {
		super();

		if (component instanceof JSpinner) {
			JSpinner spinner = (JSpinner) component;
			spinner.setModel(new SpinnerNumberModel(new Double(0), new Double(0), null, new Double(0.01)));
			JSpinner.NumberEditor editor = new JSpinner.NumberEditor(spinner, "0.000");
			spinner.setEditor(editor);
		}

		this.component = component;
	}

	@Override
	public Object getCellEditorValue() {
		if (component instanceof JSpinner) {
			return ((JSpinner) component).getValue();
		}
		if (component instanceof JComboBox) {
			return ((JComboBox<?>) component).getSelectedItem();
		}
		return null;
	}

	@Override
	public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int rowIndex, int vColIndex) {
		if (vColIndex == 2) ((JSpinner) component).setValue(value);
		if (vColIndex == 3) ((JComboBox<?>) component).setSelectedItem(value);
		return component;
	}

}