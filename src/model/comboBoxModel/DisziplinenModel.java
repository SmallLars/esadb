package model.comboBoxModel;

import java.util.List;

import model.Discipline;
import controller.Controller;

public class DisziplinenModel extends MyComboBoxModel<Discipline> {

	public DisziplinenModel(Controller controller) {
		super(controller);
	}

	@Override
	public List<Discipline> getList(Controller controller) {
		return controller.getDisziplinen();
	}
}