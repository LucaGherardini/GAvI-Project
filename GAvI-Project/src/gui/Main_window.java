package gui;

import java.awt.Font;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.store.Directory;

import listeners.Main_listeners;

/*
 * Classe finestra principale.
 * 	-Logo applicazione
 * 	-Barra ricerca
 * 	-Bottone avvio ricerca
 * 	-Impostazioni ed altre funzioni
 */
public class Main_window extends JFrame {

	//Index e analyzer usati per la ricerca
	private StandardAnalyzer analyzer;
	private Directory index;
	
	//Componenti
	private JLabel logo; 		//Logo stile google
	private JTextField query;	//Dove inserire query (barra ricerca)
	private JButton cerca;		//Bottone cerca
	private JLabel numHits;		//Label dove scrivo quanti trovati
	private JLabel results;		//Risultati trovati
	private JComboBox<String> tolerant; //Modalità tolerant
		private String edit_distance;
		private String qgrams;
	
	//Pannelli
	JPanel main;				//Pannello logo e second panel
	JPanel second;				//Pannello ricerca e bottone ok
	
	public Main_window(StandardAnalyzer analyzer, Directory index) {
		
		this.analyzer = analyzer;
		this.index = index;
		
		//Settaggio componenti
		logo = new JLabel("Lucene");
			logo.setFont( new Font(null, Font.PLAIN, 50));
		cerca = new JButton("Cerca");
		query = new JTextField(null,20);
		numHits = new JLabel();
		results = new JLabel();
		tolerant = new JComboBox<String>();
			edit_distance = "Edit Distance";
			tolerant.addItem(edit_distance);
			qgrams = "Q-Grams overlap";
			tolerant.addItem(qgrams);
			tolerant.addItem("No Tolerant Retrival");
		
		
		//Settaggio pannelli
		main = new JPanel();
		second = new JPanel();
		
		//Settaggio Layout
		main.setLayout(new BoxLayout(main, BoxLayout.Y_AXIS));
		
		//Aggiunta componenti ai pannelli
		main.add(logo);
		main.add(second);
		second.add(query);
		second.add(cerca);
		second.add(tolerant);
		main.add(numHits);
		main.add(results);
		
		add(main);
		
		//Aggiunta listeners
		this.addWindowListener(new Main_listeners(this));
		cerca.addActionListener(new Main_listeners(this));
		query.addKeyListener(new Main_listeners(this));
		cerca.addKeyListener(new Main_listeners(this));
		
	}
	
	public JTextField getQuery() {
		return query;
	}
	
	public JButton getCerca() {
		return cerca;
	}
	
	public JComboBox<String> getTolerant() {
		return tolerant;
	}
		public String getEdit_distance() {
			return edit_distance;
		}
		public String getQgrams() {
			return qgrams;
		}
	
	public JLabel getNumHits() {
		return numHits;
	}
	
	public JLabel getResults() {
		return results;
	}
	
	public StandardAnalyzer getAnalyzer() {
		return analyzer;
	}
	
	public Directory getIndex() {
		return index;
	}
	
	public void aggiornaValori() {
		numHits.setText("goo");
	}
}
