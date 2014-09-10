package model.comboBoxModel;

import java.util.List;

import model.Schuetze;
import controller.Controller;

public class SchuetzenModel extends MyComboBoxModel<Schuetze> {

	public SchuetzenModel(Controller controller) {
		super(controller);
	}

	@Override
	public List<Schuetze> getList(Controller controller) {
		return controller.getSchuetzen();
	}
}
