package view;

import java.awt.Component;
import java.awt.event.MouseEvent;
import java.util.EventObject;

import javax.swing.AbstractCellEditor;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JSpinner;
import javax.swing.JSpinner.NumberEditor;
import javax.swing.JTable;
import javax.swing.SpinnerNumberModel;
import javax.swing.table.TableCellEditor;


@SuppressWarnings("serial")
public class TableEditor extends AbstractCellEditor implements TableCellEditor {

	JComponent component = null;

	public TableEditor(JComponent component, int version) {
		super();
		
		if (component instanceof JSpinner) {
			JSpinner spinner = (JSpinner) component;

			switch (version) {
				case 0:
					spinner.setModel(new SpinnerNumberModel(new Integer(1900), new Integer(0), null, new Integer(1)));
					spinner.setEditor(new NumberEditor(spinner, "0000"));
					break;
				case 1:
					spinner.setModel(new SpinnerNumberModel(new Double(0), new Double(0), null, new Double(0.01)));
					spinner.setEditor(new NumberEditor(spinner, "0.000"));
					break;
			}
		}

		this.component = component;
	}

    @Override
    public boolean isCellEditable(EventObject e) {
        if (super.isCellEditable(e)) {
            if (e instanceof MouseEvent) {
                MouseEvent me = (MouseEvent) e;
                return me.getClickCount() >= 2;
            }
        }
        return false;
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
		if (component instanceof JSpinner) {
			((JSpinner) component).setValue(value);
		}
		if (component instanceof JComboBox) {
			((JComboBox<?>) component).setSelectedItem(value);
		}
		return component;
	}

}