package model;


import java.io.Serializable;


public class Group  implements Serializable, Comparable<Group> {
	private static final long serialVersionUID = 1L;

	private String name;
	private int from;
	private int to;
	private boolean male;

	public Group(String name, int from, int to, boolean male) {
		this.name = name;
		this.from = from;
		this.to = to;
		this.male = male;
	}

	@Override
	public String toString() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isMale() {
		return male;
	}

	public void setMale(boolean male) {
		this.male = male;
	}

	public int getFrom() {
		return from;
	}

	public void setFrom(int from) {
		this.from = from;
	}

	public int getTo() {
		return to;
	}

	public void setTo(int to) {
		this.to = to;
	}

	public boolean isInGroup(Member member) {
		if (male != member.isMale()) {
			return false;
		}

		if (from > member.getBirthYear()) return false;
		if (to < member.getBirthYear()) return false;

		return true;
	}

	public void change(int value) {
		from += value;
		to += value;
	}

	@Override
	public int compareTo(Group o) {
		if (to < o.to) return 1;
		if (to > o.to) return -1;

		if (from < o.from) return 1;
		if (from > o.from) return -1;

		if (male != o.male) return male ? -1 : 1;

		return 0;
	}
}
