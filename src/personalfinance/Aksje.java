package personalfinance;

public class Aksje {
	 
	String aksjeNavn;
	int antall;
	int pris;
 
	public void nyAksje(String navn, int a, int p) {
		aksjeNavn = navn;
		antall = a;
		pris = p;
		int verdi = a * p;
		DatabaseConnection dbCon = new DatabaseConnection();
		dbCon.leggTilAksje(aksjeNavn, antall, verdi);
	}
 
 
}