package Model;

public enum Status {
	INIT(0),
	SPERREN(1),
	ENTSPERREN(2),
	START(3),
	STOP(4),
	FREI(5),
	UNKNOWN(6),
	DATA(7),
	WERTUNG(8),
	PROBE(9);

    public final int code;
    
    Status(int code) {
    	this.code = code;
    }
    
    public String getCode() {
    	return "" + code;
    }
}