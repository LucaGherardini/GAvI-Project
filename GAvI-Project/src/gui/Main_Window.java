package gui;

import java.awt.EventQueue;
import java.awt.Insets;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import index.Hit;
import index.Index;
import irModels.BM25;
import irModels.BooleanModel;
import irModels.FuzzyModel;
import irModels.Model;
import irModels.VectorSpaceModel;

//import com.sun.scenario.effect.Filterable;

import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.awt.event.ActionEvent;
import java.awt.Color;
import javax.swing.JTable;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.swing.JTextArea;
import javax.swing.border.BevelBorder;
import javax.swing.SwingConstants;

public class Main_Window {

	private JFrame frame = null;
	private JTextField textField = null;
	//private JTable chronology = null;
	private JTable resultsTable;
	private JTable fileTable = null;
	private JTextField editDistanceText = null;
	private Index generalIndex = null;
	private JTable chronologyTable=null;
	private LinkedList chronology=new LinkedList();
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Main_Window window = new Main_Window();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Main_Window() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		generalIndex = Index.getIndex();
		
		frame = new JFrame();
		frame.getContentPane().setBackground(Color.CYAN);
		frame.setBounds(100, 100, 800, 420);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		
		JLabel title = new JLabel("NOME");
		title.setBounds(98, 48, 77, 25);
		frame.getContentPane().add(title);
		
		
		textField = new JTextField();
		textField.setBounds(10, 84, 205, 20);
		frame.getContentPane().add(textField);
		textField.setColumns(10);
		
		JComboBox<String> modelbox = new JComboBox<String>();
		modelbox.setMaximumRowCount(5);
		modelbox.setModel(new DefaultComboBoxModel<String>(new String[] {"Boolean Model", "Vector Space Model", "Probabilistic(BM25) Model", "Fuzzy Model"}));
		modelbox.setSelectedIndex(0);
		modelbox.setToolTipText("Preferenze");
		modelbox.setBounds(0, 0, 117, 20);
		frame.getContentPane().add(modelbox);
		
		JButton search = new JButton("Cerca");
		
		search.setBounds(225, 83, 89, 23);
		frame.getContentPane().add(search);
		
		JButton delete = new JButton();
		
		ImageIcon saveIcon = new ImageIcon(new ImageIcon("media/icons/save_index.png").getImage().getScaledInstance(35, 35, 0));
		delete.setIcon(saveIcon);
		delete.setMargin (new Insets (0, 0, 0, 0));
		delete.setBounds(493, 143, 35, 35);
		frame.getContentPane().add(delete);
		
		
		JButton add = new JButton();
        ImageIcon addIcon = new ImageIcon(new ImageIcon("media/icons/add_file.png").getImage().getScaledInstance(35, 35, 0));
		add.setIcon(addIcon);
		add.setMargin (new Insets (0, 0, 0, 0));
		add.setBounds(493, 183, 35, 35);
		frame.getContentPane().add(add);
		
		JButton remove = new JButton();
		ImageIcon removeIcon = new ImageIcon(new ImageIcon("media/icons/remove_file.png").getImage().getScaledInstance(35, 35, 0));
		remove.setIcon(removeIcon);
		remove.setMargin (new Insets (0, 0, 0, 0));
		remove.setBounds(493, 223, 35, 35);
		frame.getContentPane().add(remove);
		

		resultsTable = new JTable();
        resultsTable.setBounds(28, 162, 274, 188);
        DefaultTableModel resultsModel = (DefaultTableModel) resultsTable.getModel();
        resultsModel.addColumn("File");
        resultsModel.addColumn("Score");
		frame.getContentPane().add(resultsTable);
		
		fileTable = new JTable(0,0);
		fileTable.setBounds(347, 143, 136, 218);
		
		DefaultTableModel tableModel=(DefaultTableModel) fileTable.getModel();
		
		tableModel.addColumn("Files");
		//fileTable.setEnabled(false);		
		frame.getContentPane().add(fileTable);
		
		
		ButtonGroup opGroup = new ButtonGroup();		
		
		JRadioButton noOptimizations = new JRadioButton("No Optimizations");
		noOptimizations.setBounds(345, -1, 170, 23);
		noOptimizations.setSelected(true);
		opGroup.add(noOptimizations);
		frame.getContentPane().add(noOptimizations);
		
		JRadioButton editDistance = new JRadioButton("Edit Distance");
		editDistance.setBounds(345, 25, 170, 23);
		opGroup.add(editDistance);
		frame.getContentPane().add(editDistance);
		
		JRadioButton qGram = new JRadioButton("Q-Gram Overlap");
		qGram.setBounds(345, 51, 170, 23);
		opGroup.add(qGram);
		frame.getContentPane().add(qGram);
		
		editDistanceText = new JTextField();
		editDistanceText.setBounds(520, 26, 30, 20);
		frame.getContentPane().add(editDistanceText);
		editDistanceText.setColumns(10);
		
		JButton Help = new JButton("HELP");
		
		Help.setBounds(177, -1, 89, 23);
		frame.getContentPane().add(Help);
		
		JLabel lblResults = new JLabel("RESULTS");
		lblResults.setBounds(142, 129, 108, 14);
		frame.getContentPane().add(lblResults);
		
		JLabel lblDocumnets = new JLabel("Documnets");
		lblDocumnets.setBounds(347, 107, 94, 25);
		frame.getContentPane().add(lblDocumnets);
		
		JButton btnChronology = new JButton("Chronology");
		
		btnChronology.setBounds(584, 83, 136, 23);
		frame.getContentPane().add(btnChronology);
		
		JButton btnClose = new JButton("X");
		btnClose.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frame.remove(chronologyTable);
				frame.repaint();
				
			}
		});
		btnClose.setHorizontalAlignment(SwingConstants.LEADING);
		btnClose.setBounds(730, 83, 39, 23);
		frame.getContentPane().add(btnClose);
		
		/*chronologyTable = new JTable();
		chronologyTable.setBounds(569, 143, 176, 207);
		frame.getContentPane().add(chronologyTable);*/
		
		
	//Search
		
		search.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				String queryStr = textField.getText();
				
	   /*LinkList to save Chronology*/
				chronology.addFirst(queryStr);
				
				frame.remove(chronologyTable);
				frame.repaint();
				
				
				System.out.println("Query string: " + queryStr);
				resultsModel.setRowCount(0);
				
				Model modelUsed = null;
				
				if(modelbox.getSelectedItem()=="Boolean Model") {
					System.out.println("Boolean");
					modelUsed = new BooleanModel();
				}
				
				if(modelbox.getSelectedItem()=="Vector Space Model") {
					System.out.println("Vector Space");
					modelUsed = new VectorSpaceModel();
				}
				
				if(modelbox.getSelectedItem()=="Probabilistic(BM25) Model") {
					System.out.println("Probabilistic(BM25)");
					modelUsed = new BM25();
				}
				
				if(modelbox.getSelectedItem()=="Fuzzy Model") {
					System.out.println("Fuzzy");
					modelUsed = new FuzzyModel();
				}	
				
				LinkedList<String> fields = new LinkedList<String>();
				
				if(queryStr.contains("name:")) {
					fields.add("name");
				}
				if(queryStr.contains("content:")) {
					fields.add("content");
				}
				if(fields.size() == 0) {
					fields.add("name");
					fields.add("content");
				}
				
				generalIndex.setSimilarity(modelUsed.getSimilarity());
				LinkedList<Hit> results = generalIndex.submitQuery(queryStr, fields, modelUsed);
				
				for(Hit result : results) {
					resultsModel.addRow(new Object[] {result.getDocPath()+result.getDocName(), result.getScore()});
				}
			}
		});
		
		Help.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				 				    
				JOptionPane.showMessageDialog(frame,	"Boolean Model: Using of Logic Operator (AND, OR, NOT) \"(Lucene OR Information Retrieval) AND Boolean Model\" \n"
													+	"Fuzzy Model: More flexible than Boolean Model, using of \"~\" operator to mark tolerance on terms \"United Stat~\" \n"
													+	"Vector Space Model: Based on a Vector rapresentation of query and documents, can be used Fuzzy and Boolean operators \n"
													+ 	"Probabilistic(BM25): Use of BM25 ranking function to retrieve documents given a query\n\n"
													+ 	"Query composition: You can declare in which fields search (name or content) writing field name in query \"name:Salomè content:Give me Iokanaan's head\"");
			}
		});
		
	// Add
	add.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				JFileChooser fileC=new JFileChooser();
				fileC.setMultiSelectionEnabled(true);
				fileC.showOpenDialog(frame);
				
				//File f=file.getSelectedFile();
				
				//se si vogliono selezionare piu file insieme (difficile implementazione forse non il modo migliore per piu file o directory)
				File filesSelected[] = fileC.getSelectedFiles();
				
				for (File f : filesSelected) {
					String nameFile = f.getAbsolutePath();
				
					if(nameFile.endsWith(".txt")) {
							tableModel.setRowCount(tableModel.getRowCount()+1);
							tableModel.setValueAt(f.getName(), tableModel.getRowCount()-1, 0);
							generalIndex.addDocument(nameFile);
					}
				}
							
			}
		});
	
	//remove

	remove.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent arg0) {
				tableModel.removeRow(fileTable.getSelectedRow());
			}
	});
		
	// Delete All Row
	delete.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent arg0) {
			if(tableModel.getRowCount() !=1) {
				for(int i=0 ; i < tableModel.getRowCount() ; i++) {
					tableModel.removeRow(i);	
				}
			}else {
				tableModel.removeRow(0);
			}
		}
		});
	
	//Chronology
	btnChronology.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent arg0) {
			chronologyTable = new JTable();
			chronologyTable.setBounds(569, 143, 176, 160);
			frame.getContentPane().add(chronologyTable);
			
			DefaultTableModel chronologyModel=(DefaultTableModel) chronologyTable.getModel();
			
			chronologyModel.addColumn("Chronology");
			chronologyModel.setRowCount(10);
			
			for(int i=0;i<chronologyModel.getRowCount();i++) {
				chronologyModel.setValueAt(chronology.get(i), i,0);			
				}
		
			
		      frame.repaint();
	
		}
	});
	}
}
