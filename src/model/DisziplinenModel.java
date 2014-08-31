package model;

import javax.swing.ComboBoxModel;
import javax.swing.event.ListDataListener;

import controller.Controller;

public class DisziplinenModel implements ComboBoxModel<Disziplin> {
	private Object selected;
	private Controller controller;

	public DisziplinenModel(Controller controller) {
		this.selected = null;
		this.controller = controller;
	}

	@Override
	public Disziplin getElementAt(int index) {
		if (index == 0) return null;
		return controller.getDisziplinen()[index - 1];
	}

	@Override
	public int getSize() {
		return controller.getDisziplinen().length + 1;
	}

	@Override
	public Object getSelectedItem() {
		return selected;
	}

	@Override
	public void setSelectedItem(Object item) {
		if (controller.contains(item)) {
			selected = item;
		}
	}

	@Override
	public void addListDataListener(ListDataListener l) {
		// TODO Auto-generated method stub
	}

	@Override
	public void removeListDataListener(ListDataListener l) {
		// TODO Auto-generated method stub
	}
}
