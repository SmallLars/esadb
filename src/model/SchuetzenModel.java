package model;

import javax.swing.ComboBoxModel;
import javax.swing.event.ListDataListener;

import controller.Controller;

public class SchuetzenModel implements ComboBoxModel<Schuetze> {
	private Object selected;
	private Controller controller;

	public SchuetzenModel(Controller controller) {
		this.selected = null;
		this.controller = controller;
	}

	@Override
	public Schuetze getElementAt(int index) {
		if (index == 0) return null;
		return controller.getSchuetzen()[index - 1];
	}

	@Override
	public int getSize() {
		return controller.getSchuetzen().length + 1;
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
