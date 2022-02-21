package personalfinance;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.event.*;
import java.awt.*;
 
 
public class Main {
	public static void main(String[] args) {
 /*
		try {
			String connectionUrl = "jdbc:derby:~/eclipse-workspace/personalfinance/aksjedatabase;create=true";
			Connection connection = DriverManager.getConnection(connectionUrl);
			Statement createAksjeStatement = connection.createStatement();
			createAksjeStatement.executeUpdate("create table Aksjer(aksjeNavn varchar(16), antall integer, verdi integer, primary key(aksjeNavn))");
			System.out.println("tabell ok");
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
*/
		DatabaseConnection dbCon = new DatabaseConnection();
	
		int sumAksjer = dbCon.sumAksjer();
		
		//definere hovedvindu og knapper
		//faner og paneler
		JFrame hovedvindu = new JFrame("Privatøkonomi");
		JTabbedPane faner = new JTabbedPane();
		JPanel faneAksjer = new JPanel();
		JPanel faneGjeld = new JPanel();
		JPanel faneOversikt = new JPanel();
 
		//lables
		JLabel sumVerdiAksjer = new JLabel("Aksjeverdi: " + sumAksjer);
		JLabel sumVerdiGjeld = new JLabel("sum gjeld");
		
		//knapper
		JButton leggTilAksje = new JButton("Legg til aksje");
		JButton skrivUtAksjeTabell = new JButton("Print tabell");
		JButton slettAksje = new JButton("Slett aksje");
		
		// tabeller
 
		String aksjeKolonner[] = {"Aksje","Antall","Verdi"};
		String aksjeData[][]= dbCon.skrivUtAksjeTabell();
 
		DefaultTableModel model = new DefaultTableModel(aksjeKolonner,0);
		JTable aksjeTabell = new JTable(model);
		for (int i = 0; i<aksjeData.length; i++) {
			model.addRow(aksjeData[i]);
		}
//		JTable aksjeTabell = new JTable(aksjeData,aksjeKolonner);
		aksjeTabell.setCellSelectionEnabled(true);
 
		JScrollPane aksjeSp = new JScrollPane(aksjeTabell);
		aksjeSp.setPreferredSize(new Dimension(400,400));
 
 
		//lage hovedvindu
		hovedvindu.add(faner);
		faner.setBounds(10,10,400,400);
		faner.add("Oversikt", faneOversikt);
		faner.add("Aksjer", faneAksjer);
		faner.add("Gjeld", faneGjeld);
		hovedvindu.setSize(440,460);
		hovedvindu.setResizable(false);
		hovedvindu.setLayout(null);
		hovedvindu.setVisible(true);
		hovedvindu.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		//fane oversikt
		faneOversikt.add(sumVerdiGjeld);
		faneOversikt.add(sumVerdiAksjer);
		
		//fane aksjer
		faneAksjer.add(leggTilAksje);
		faneAksjer.add(skrivUtAksjeTabell);
		faneAksjer.add(slettAksje);
		faneAksjer.add(aksjeSp);
 
		dbCon.hentAksje("TSLA");
		
		System.out.println(dbCon.sumAksjer());
 
		// lytter ny aksje
		leggTilAksje.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//leggTilAksje();
				String n = JOptionPane.showInputDialog("Aksjenavn");
				int p = Integer.parseInt(JOptionPane.showInputDialog("Pris"));
				int a = Integer.parseInt(JOptionPane.showInputDialog("Antall"));
				Aksje nyAksje = new Aksje();
				nyAksje.nyAksje(n, p, a);
				model.addRow(dbCon.hentAksje(n));
 
			}
		});
 
		// lytter print ut db til konsoll
		skrivUtAksjeTabell.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dbCon.skrivUtAksjeDatabase();
			}
		});
 
		// lytter til slett aksje
		slettAksje.addActionListener(new ActionListener() {
			public void actionPerformed (ActionEvent e) {
				String navn = JOptionPane.showInputDialog("Skriv navn på aksje du vil slette");
				if (dbCon.sokAksjeNavn(navn) == true) {
					dbCon.slettAksje(navn);
					for (int i = 0; i < model.getRowCount(); i++) {
						if (model.getValueAt(i, 0).equals(navn)) {
							model.removeRow(i);
							JOptionPane.showMessageDialog(hovedvindu, "Aksje " + navn + " er slettet.");
							break;
						}
					}
				}
				else {
					JOptionPane.showMessageDialog(hovedvindu, "Aksje finnes ikke");
				}
			}
		});	
	}
}