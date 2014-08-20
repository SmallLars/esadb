package Model;
import java.io.Serializable;
import java.util.regex.Pattern;


public class Treffer implements Serializable, Comparable<Treffer> {
	private static final long serialVersionUID = 1L;

	private boolean probe;
	private int nummer;
	private float wert;
	private String lage;
	private double x;
	private double y;
	private double r;
	private double phi;
	private int zeit;

	/*
		PN1 : "51$34$0708023-0004$0$0$1$1$ 0$ 9.4$g$ 1244$ 289     $ 1277.13$  13.079$ 8"
		PN2 : "51$34$0708023-0004$0$0$1$2$ 0$ 8.6$k$ 1306$ 1343    $ 1873.31$    45.8$ 9"
		MN1 : "51$34$0708023-0004$0$0$1$1$-1$ 7.8$k$ 1655$ 1872    $ 2498.68$  48.521$25"
		MN2 : "51$34$0708023-0004$0$0$1$2$-1$ 8.7$f$-1794$-17.99999$ 1794.09$ 180.575$26"
		      " A$ B$           C$D$E$F$G$ H$   I$J$    K$        L$       M$       N$ O"
		
		A = WettkampfID
		B = DisziplinID
		C = PassNr
		D = 
		E = 
		F = Linie
		G = SchussNr
		H = Wertung : Ja(-1), Nein(0)
		I = Ringwert
		J = Trefferlage     f, g, h, i, j, k, l, m, R
		K = X_Pos
		L = Y_Pos
		M = R_Pos
		N = Phi_pos
		O = Relative Zeit in Sekunden
	 */
    public Treffer(String raw) {
    	String sa[] = raw.replace('"', '$').substring(1).split(Pattern.quote("$"));
   		nummer = Integer.parseInt(sa[6].trim());
   		probe = (sa[7].equals("0") ? true : false);
   		wert = Float.parseFloat(sa[8].trim());
   		lage = sa[9].trim();
   		x = Double.parseDouble(sa[10].trim());
   		y = Double.parseDouble(sa[11].trim());
   		r = Double.parseDouble(sa[12].trim());
   		phi = Double.parseDouble(sa[13].trim());
   		zeit = Integer.parseInt(sa[14].trim());
    }

    boolean isProbe() {
    	return probe;
    }

    public int getNummer() {
    	return nummer;
    }

    public float getWert() {
    	return wert;
    }

    public boolean isInnenZehner() {
    	//return lage.equals("R"); <- Gibt leider keine Auskunft :(
    	return r <= 530;
    }

    public String getLage() {
    	return lage;
    }
    
    public double getX() {
    	return x;
    }
    
    public double getY() {
    	return y;
    }

    public double getR() {
    	return r;
    }
    
    public double getPhi() {
    	return phi;
    }

    public int getZeit() {
    	return zeit;
    }

    @Override
    public String toString() {
    	return "" + wert;
    }

	@Override
	public int compareTo(Treffer t) {
		return nummer - t.nummer;
	}
}