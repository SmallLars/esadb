package model;


import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;


public class Team extends Start {
	private static final long serialVersionUID = 2L;

	private Discipline disziplin;
	private Group group;
	private List<Single> member;

	public Team(Discipline disziplin, Group group) {
		this.disziplin = disziplin;
		this.group = group;
		member = new ArrayList<Single>();
	}

	@Override
	public int compareTo(Start s) {
		int c;

		// Zuerst nach Disziplin sortieren
		c = disziplin.compareTo(s.getDisziplin());
		if (c != 0) return c;

		// Sortierung nach Gruppen
		c = getGroup().compareTo(s.getGroup());
		if (c != 0) return c;

		// Mannschaften kommen vor Einzel
		if (s instanceof Single) return -1;

		// Zwei Mannschaften werden nun verglichen
		Team t = (Team) s;

		// 1. Endergebnis vergleichen
		c = (int) (t.getResult() * 10) - (int) (getResult() * 10);
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
	public Group getGroup() {
		return group;
	}

	public void addMember(Single single) {
		member.add(single);
	}

	public Member[] getMember() {
		return member.toArray(new Member[0]);
	}

	public boolean removeMember(Single single) {
		return member.remove(single);
	}

	@Override
	public int lineCount() {
		return member.size() + 2;
	}

	@Override
	public void draw(Graphics2D g, int platz) {
		// TODO Auto-generated method stub

	}

	private float getResult() {
		float summe = 0;
		for (Single s : member) {
			summe += s.getResult(false);
		}
		return summe;
	}

}