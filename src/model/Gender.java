package model;


public enum Gender {
	ANY("männlich/weiblich"),
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