package model.comboBoxModel;

import java.util.List;

import model.Disziplin;
import controller.Controller;

public class DisziplinenModel extends MyComboBoxModel<Disziplin> {

	public DisziplinenModel(Controller controller) {
		super(controller);
	}

	@Override
	public List<Disziplin> getList(Controller controller) {
		return controller.getDisziplinen();
	}
}