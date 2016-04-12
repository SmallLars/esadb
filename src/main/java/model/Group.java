package model;


import java.io.Serializable;


public class Group  implements Serializable, Comparable<Group> {
	private static final long serialVersionUID = 1L;

	private String name;
	private int from;
	private int to;
	private Gender gender;

	public Group(String name, int from, int to, Gender gender) {
		this.name = name;
		this.from = from;
		this.to = to;
		this.gender = gender;
	}

	@Override
	public String toString() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Gender getGender() {
		return gender;
	}

	public void setGender(Gender gender) {
		this.gender = gender;
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

	public boolean isMember(Member member, boolean forceMaleFemale) {
		switch (gender) {
			case ANY:
				if (forceMaleFemale) return false;
				break;
			case MALE:
				if (member.isFemale()) return false;
				break;
			case FEMALE:
				if (member.isMale()) return false;
				break;
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
	public int hashCode() {
		return to + from + gender.hashCode();
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof Group) {
			return compareTo((Group) o) == 0;
		}
		return false;
	}

	@Override
	public int compareTo(Group g) {
		if (g == this) return 0;
		if (g == null) return 1;

		if (to < g.to) return 1;
		if (to > g.to) return -1;

		if (from < g.from) return 1;
		if (from > g.from) return -1;

		return gender.compareTo(g.gender);
	}

	public static Group getStandardGroup(Gender gender, int year, String name) {
		Group g = new Group(gender.toString(), year - 201, year, gender);
		if (name != null) g.setName(name);
		return g;
	}
}
