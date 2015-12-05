package model.comboBoxModel;

import java.util.List;

import model.Member;
import controller.Controller;

public class SchuetzenModel extends MyComboBoxModel<Member> {

	public SchuetzenModel(Controller controller) {
		super(controller);
	}

	@Override
	public List<Member> getList(Controller controller) {
		return controller.getSchuetzen();
	}
}
