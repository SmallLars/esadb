package main.java.model;

public enum TargetValue {
	SIZE_WIDTH(55000),
	SIZE_HEIGHT(55000),
	FEED(5),
	DIA_BLACK(11240),
	DIA_OUTSIDE(15440),
	DIA_INNER_TEN(500),
	RING_WIDTH(800),
	RING_MIN(1),
	RING_MAX(10),
	NUM_MAX(8),
	NUM_ANGLE(0),
	TYPE(0),
	FILL(0),
	SUSP_DIA(0),
	SUSP_DISTANCE(0),
	ZOOM_CENTER_X(0),
	ZOOM_CENTER_Y(0),
	ZOOM_LEVELS(5),
	OFFSET_X(0),
	OFFSET_Y(0),
	IMAGE(0);

	private int value;

	private TargetValue(int value) {
		this.value = value;
	}

	public int getStandardValue() { 
		return value;
	}
}