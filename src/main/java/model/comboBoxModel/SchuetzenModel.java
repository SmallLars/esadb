package main.java.model.comboBoxModel;

import java.util.List;

import main.java.controller.Controller;
import main.java.model.Member;

public class SchuetzenModel extends MyComboBoxModel<Member> {

	public SchuetzenModel(Controller controller) {
		super(controller);
	}

	@Override
	public List<Member> getList(Controller controller) {
		return controller.getSchuetzen();
	}
}
