package personalfinance;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
 
 
public class DatabaseConnection {
 
	
	private final String connectionUrl = "jdbc:derby:~/eclipse-workspace/personalfinance/aksjedatabase;create=true";
	

	
	boolean resultat;
 
	// metode for å legge til aksje
	public void leggTilAksje (String navn, int antall, int verdi) {
		try {
			Connection connection = DriverManager.getConnection(connectionUrl);
 
			Statement insertRowStatement = connection.createStatement();
			insertRowStatement.executeUpdate("INSERT INTO Aksjer(aksjeNavn, antall, verdi) values('"+navn+"', "+antall+", "+verdi+")");
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		hentAksje(navn);
	}
 
	//metode for å slette aksje
	public void slettAksje(String navn) {
		try {
			Connection connection = DriverManager.getConnection(connectionUrl);
 
			Statement removeRowStatement = connection.createStatement();
 
			ResultSet resultSet = removeRowStatement.executeQuery("SELECT aksjeNavn from Aksjer");
			while (resultSet.next()) {
				if (resultSet.getString("aksjeNavn").equals(navn)) {
					removeRowStatement.executeUpdate("DELETE FROM Aksjer WHERE aksjeNavn = '"+navn+"' ");
					break;
				}
			}
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	//metode for å returnere total verdi av aksjer
	public int sumAksjer() {
		int sum = 0;
		try {
			Connection connection = DriverManager.getConnection(connectionUrl);
			Statement sumAksjer = connection.createStatement();
			
			ResultSet resultSet = sumAksjer.executeQuery("SELECT verdi from Aksjer");
			
			while(resultSet.next()) {
				sum += Integer.parseInt(resultSet.getString("verdi"));
				
			}
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		
		return sum;
	}
 
	// søker om aksje finnes
	public boolean sokAksjeNavn(String input) {
		//boolean resultat = false;
 
		try {
 
		Connection connection = DriverManager.getConnection(connectionUrl);
		Statement sokNavn = connection.createStatement();
 
		ResultSet resultSet = sokNavn.executeQuery("SELECT * FROM Aksjer");
		while (resultSet.next()) {
			if (resultSet.getString("aksjeNavn").equals(input)) {
				resultat = true;
				break;
			}
			else {
				resultat = false;
			}
		}
	}
		catch (SQLException e) {
			e.printStackTrace();
		}
		return resultat;
	}
 
 
	// printer ut db i konsoll
	public void skrivUtAksjeDatabase () {
		try {
			//koble til db
			Connection connection = DriverManager.getConnection(connectionUrl);		
			System.out.println("Database content: \n");
 
			Statement getRowStatement = connection.createStatement();
			ResultSet resultSet = getRowStatement.executeQuery("SELECT * from Aksjer");
			while (resultSet.next()) {
				String navn = resultSet.getString("aksjeNavn");
				String ant = resultSet.getString("antall");
				String sum = resultSet.getString("verdi");
				System.out.println(navn + ", " + ant + ", " + sum);
			}
 
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
	}
 
	public int telleDatabaseKolonner() {
 
		int antallKolonner = 0;
 
		try {
		Connection connection = DriverManager.getConnection(connectionUrl);	
		Statement getRowStatement = connection.createStatement();
 
		ResultSet resultSet = getRowStatement.executeQuery("SELECT * from Aksjer");
		ResultSetMetaData rsmd = resultSet.getMetaData();
 
		antallKolonner = rsmd.getColumnCount();
 
		}
 
		catch (SQLException e) {
			e.printStackTrace();
		}
		return antallKolonner;
		}
 
	public int telleDatabaseRader() {
 
		int antallRader = 0;
 
		try {
		Connection connection = DriverManager.getConnection(connectionUrl);	
		Statement getRowStatement = connection.createStatement();
 
		ResultSet resultSet = getRowStatement.executeQuery("SELECT * from Aksjer");
 
		while (resultSet.next()) {
			antallRader++;
		}
 
		}
 
		catch (SQLException e) {
			e.printStackTrace();
		}
		return antallRader;
		}
 
	// hente ut spesifik aksje
	public String[] hentAksje(String n) {
		String[] aksje = new String[3];
		String navn = null;
		String verdi = null;
		String antall = null;
		try {
			Connection connection = DriverManager.getConnection(connectionUrl);
			Statement hentRowStatement = connection.createStatement();
			ResultSet resultSet = hentRowStatement.executeQuery("SELECT * from Aksjer");
			while (resultSet.next()) {
				if (resultSet.getString("aksjeNavn").equals(n)) {
					navn = resultSet.getString("aksjeNavn");
					verdi = resultSet.getString("verdi");
					antall = resultSet.getString("antall");
					aksje[0]=navn;
					aksje[1]=antall;
					aksje[2]=verdi;
					break;
				}
			}
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		return aksje;
	}
	public String[][] skrivUtAksjeTabell () {
		int kolonner = telleDatabaseKolonner();
		int rader = telleDatabaseRader();
		int i = 0;
		String aksjer[][] = new String[rader][kolonner];
		try {
			Connection connection = DriverManager.getConnection(connectionUrl);	
			Statement getRowStatement = connection.createStatement();
 
			ResultSet resultSet = getRowStatement.executeQuery("SELECT * from Aksjer");
 
			while (resultSet.next()) {
						aksjer[i][0] = resultSet.getString("aksjeNavn");
						aksjer[i][1] = resultSet.getString("antall");
						aksjer[i][2] = resultSet.getString("verdi");
						i++;
			}
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		return aksjer;
	}
}
