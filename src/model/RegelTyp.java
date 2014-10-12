package model;

public enum RegelTyp {
	R_1_10(ScheibeTyp.LUFTGEWEHR, WaffeTyp.LUFTDRUCK),
	R_1_35(ScheibeTyp.GEWEHR100M, WaffeTyp.KLEINKALIBER),
	R_1_40(ScheibeTyp.GEWEHR50M, WaffeTyp.KLEINKALIBER),
	R_2_10(ScheibeTyp.LUFTPISTOLE, WaffeTyp.LUFTDRUCK);
	
	private ScheibeTyp scheibe;
	private WaffeTyp waffe;
	
	RegelTyp(ScheibeTyp scheibe, WaffeTyp waffe) {
		this.scheibe = scheibe;
		this.waffe = waffe;
	}

	public ScheibeTyp getScheibe() {
		return scheibe;
	}

	public WaffeTyp getWaffe() {
		return waffe;
	}

	public float getValuebyRadius(double radius) {
		// TODO funktioniert eventuell nur für KK50M
		float v = (int) (110 - radius * 10 / scheibe.getRingBreite()) / 10f;
		if (v < 1) return 0.0f;
		if (v > 10.9) return 10.9f;
		return v;
	}

	public double getRadiusByValue(float value) {
		// TODO funktioniert eventuell nur für KK50M
		return Math.round(scheibe.getAussenRadius() + scheibe.getRingBreite() + waffe.getRadius() - value * scheibe.getRingBreite());
	}

	public boolean isInnenZehn(double radius) {
		return radius <= scheibe.getInnenZehnRadius() + waffe.getRadius();
	}

	public static RegelTyp getTypByGattung(String s) {
		switch (s) {
			case "1.10": return R_1_10;
			case "1.35": return R_1_35;
			case "1.40": return R_1_40;
			case "2.10": return R_2_10;
			default:  return R_1_40;
		}
	}
}
