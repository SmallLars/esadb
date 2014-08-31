package model;

import java.util.Vector;

import javax.swing.ComboBoxModel;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

import controller.Controller;

public class SchuetzenModel implements ComboBoxModel<Schuetze> {
	private Object selected;
	private Controller controller;
	private int length;
	private Vector<ListDataListener> listDataListener;

	public SchuetzenModel(Controller controller) {
		this.selected = null;
		this.controller = controller;
		this.length = controller.getSchuetzen().length + 1;
		listDataListener = new Vector<ListDataListener>();
	}

	@Override
	public Schuetze getElementAt(int index) {
		if (index == 0) return null;
		return controller.getSchuetzen()[index - 1];
	}

	@Override
	public int getSize() {
		return length;
	}

	@Override
	public Object getSelectedItem() {
		return selected;
	}

	@Override
	public void setSelectedItem(Object item) {
		if (item == null || controller.contains(item)) {
			selected = item;
		}
	}

	@Override
	public void addListDataListener(ListDataListener l) {
		listDataListener.add(l);
	}

	@Override
	public void removeListDataListener(ListDataListener l) {
		listDataListener.remove(l);
	}
	
	public void modelChanged() {
		for (ListDataListener l : listDataListener) {
			l.intervalRemoved(new ListDataEvent(controller, ListDataEvent.INTERVAL_REMOVED, 0, length - 1));
			length = controller.getSchuetzen().length + 1;
			l.intervalAdded(new ListDataEvent(controller, ListDataEvent.INTERVAL_ADDED, 0, length - 1));
		}
	}
}
