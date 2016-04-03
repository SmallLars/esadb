package model;


import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

import controller.Controller;


public class Team extends Result {
	private static final long serialVersionUID = 2L;

	private String name;
	private Discipline disziplin;
	private Group group;
	private List<Single> member;

	public Team(Discipline disziplin, Group group) {
		this.name = "Mannschaft";
		this.disziplin = disziplin;
		this.group = group;
		this.member = new ArrayList<Single>();
	}

	@Override
	public String toString() {
		return name;
	}

	@Override
	public int hashCode() {
		return name.hashCode() + disziplin.hashCode() + group.hashCode() + member.hashCode();
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof Team) {
			return compareTo((Team) o) == 0;
		}
		return false;
	}

	@Override
	public int compareTo(Result s) {
		int c;

		// Zuerst nach Disziplin sortieren
		c = disziplin.compareTo(s.getDisziplin());
		if (c != 0) return c;

		// Sortierung nach Gruppen
		c = getGroup(true).compareTo(s.getGroup(true));
		if (c != 0) return c;

		// Mannschaften kommen vor Einzel
		if (s instanceof Single) return -1;

		// Zwei Mannschaften werden nun verglichen
		Team t = (Team) s;

		// 1. Endergebnis vergleichen
		c = (int) (t.getResult(false) * 10) - (int) (getResult(false) * 10);
		if (c != 0) return c;

		// 2. Serien rückwärts vergleichen
		for (int i = -1; true; i--) {
			int value = 0;
			int myvalue = 0;
			for (Single m : t.member) value += (int) (m.getSerie(false, i) * 10);
			for (Single m : member) myvalue += (int) (m.getSerie(false, i) * 10);
			if (value == 0 && myvalue == 0) break;
			c = value - myvalue;
			if (c != 0) return c;
		}

		int count = 0;
		int mycount = 0;

		// 3. Höchste Zahl der 10er, dann 9er, .... dann 1er
		// Bei unterschiedlichen Scheiben werden zuerst die höchsten Ringe,
		// dann die zweithöchsten Ringe usw. summiert und verglichen.
		int maxNum = 0;
		for (Single m : t.member) if (m.getMaxNumber() > maxNum) maxNum = m.getMaxNumber();
		for (Single m : member) if (m.getMaxNumber() > maxNum) maxNum = m.getMaxNumber();

		for (int i = maxNum; i >=0; i--) {
			count = 0;
			mycount = 0;
			for (Single m : t.member) {
				int checkNum =  m.getMaxNumber() == maxNum ? i : m.getMaxNumber() - (maxNum - i);
				count += m.getNumberCount(false, checkNum);
			}
			for (Single m : member) {
				int checkNum =  m.getMaxNumber() == maxNum ? i : m.getMaxNumber() - (maxNum - i);
				mycount += m.getNumberCount(false, checkNum);
			}
			c = count - mycount;
			if (c != 0) return c;
		}

		// 4. Höchste Zahl der InnenZehner
		count = 0;
		mycount = 0;
		for (Single m : t.member) count += (int) (m.getInnerCount(false));
		for (Single m : member) mycount += (int) (m.getInnerCount(false));
		return count - mycount;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String getName() {
		return name;
	}

	public void setDisziplin(Discipline disziplin) {
		this.disziplin = disziplin;
	}

	@Override
	public Discipline getDisziplin() {
		return disziplin;
	}

	public void setGroup(Group group) {
		this.group = group;
	}

	@Override
	public Group getGroup(boolean useSettings) {
		SettingsModel settings = Controller.get().getConfig();

		if (useSettings && !settings.getValue("ResultListGroup", true)) {
			if (!settings.getValue("ResultListGender", true)) {
				return Group.getStandardGroup(Gender.ANY, settings.getYear(), null);
			}

			return Group.getStandardGroup(group.getGender(), settings.getYear(), null);
		}

		if (useSettings && !settings.getValue("ResultListGender", true)) {
			if (group.getGender() == Gender.ANY) return group;

			Group g = new Group(group.toString() + " - männlich/weiblich", group.getFrom(), group.getTo(), Gender.ANY);
			// Versuch eine vorhandene Unisex-Gruppe zu finden um Gruppennamen wie "Herren - männlich/weiblich" zu vermeiden
			for (Group gc : settings.getGroups()) {
				if (gc.equals(g)) return gc;
			}

			// Leider keine Unisex-Gruppe gefunden :(
			return g;
		}

		return group;
	}

	@Override
	public float getResult(boolean probe) {
		float summe = 0;
		for (Single s : member) {
			summe += s.getResult(probe);
		}
		return summe;
	}

	public void addMember(Single single) {
		member.add(single);
	}

	public boolean contains(Single single) {
		return member.contains(single);
	}

	public Member[] getMember() {
		return member.toArray(new Member[0]);
	}

	public boolean removeMember(Single single) {
		return member.remove(single);
	}

	@Override
	public int lineCount() {
		return member.size() + 1;
	}

	@Override
	public void draw(Graphics2D g, int platz, int fontsize, int lineheight, boolean overline) {
		GraphicsString gs = new GraphicsString(g, fontsize, false, true, Color.BLACK);
		gs.drawStringRight(String.format("%d.", platz), 450, lineheight);
		gs.drawStringLeft(String.format("%s", name), 475, lineheight);
		gs.drawStringRight(String.format("%.1f", getResult(false)), 1600, lineheight);

		gs.setBold(false);
		int y = lineheight;
		for (Single m : member) {
			y += lineheight;
			gs.drawStringLeft(String.format("%s", m.getName()), 475, y);
			gs.drawStringRight(String.format("%.1f", m.getResult(false)), 1300, y);
		}

		g.drawLine(1350, 10, 1350, lineCount() * lineheight + 10);
		if (overline) g.drawLine(350, 10, 1650, 10);
	}
}