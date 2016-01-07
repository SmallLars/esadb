package model;


public enum Gender {
	ANY("beliebig"),
	MALE("männlich"),
	FEMALE("weiblich");

	private String name;

	private Gender(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return name;
	}
}