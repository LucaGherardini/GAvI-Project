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

import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.LinkedList;
import java.awt.event.ActionEvent;
import java.awt.Color;
import java.awt.Component;

import javax.swing.JTable;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
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
	private LinkedList<String> chronology=new LinkedList<String>();
	private JTable table;
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
		frame.setBounds(800, 600, 850, 500);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		frame.setTitle("GaVi Project");
		frame.setResizable(false);
		
		JLabel title = new JLabel("NOME");
		title.setBounds(91, 50, 77, 25);
		frame.getContentPane().add(title);
		
		
		textField = new JTextField();
		textField.setBounds(10, 96, 205, 20);
		frame.getContentPane().add(textField);
		textField.setColumns(10);
		
		JComboBox<String> modelbox = new JComboBox<String>();
		modelbox.setMaximumRowCount(5);
		modelbox.setModel(new DefaultComboBoxModel<String>(new String[] {"Boolean Model", "Vector Space Model", "Probabilistic(BM25) Model", "Fuzzy Model"}));
		modelbox.setSelectedIndex(0);
		modelbox.setToolTipText("Preferenze");
		modelbox.setBounds(0, 0, 156, 20);
		frame.getContentPane().add(modelbox);
		
		JButton search = new JButton("Cerca");
		
		search.setBounds(233, 95, 89, 23);
		frame.getContentPane().add(search);
		
		JButton delete = new JButton();
		
		ImageIcon saveIcon = new ImageIcon(new ImageIcon("media/icons/save_index.png").getImage().getScaledInstance(35, 35, 0));
		delete.setIcon(saveIcon);
		delete.setMargin (new Insets (0, 0, 0, 0));
		delete.setBounds(524, 158, 35, 35);
		frame.getContentPane().add(delete);
		
		
		JButton add = new JButton();
        ImageIcon addIcon = new ImageIcon(new ImageIcon("media/icons/add_file.png").getImage().getScaledInstance(35, 35, 0));
		add.setIcon(addIcon);
		add.setMargin (new Insets (0, 0, 0, 0));
		add.setBounds(524, 204, 35, 35);
		frame.getContentPane().add(add);
		
		JButton remove = new JButton();
		ImageIcon removeIcon = new ImageIcon(new ImageIcon("media/icons/remove_file.png").getImage().getScaledInstance(35, 35, 0));
		remove.setIcon(removeIcon);
		remove.setMargin (new Insets (0, 0, 0, 0));
		remove.setBounds(524, 250, 35, 35);
		frame.getContentPane().add(remove);
		

		resultsTable = new JTable();
        resultsTable.setBounds(22, 183, 274, 245);
        DefaultTableModel resultsModel = (DefaultTableModel) resultsTable.getModel();
        resultsModel.addColumn("File");
        resultsModel.addColumn("Score");
		frame.getContentPane().add(resultsTable);
		
		fileTable = new JTable(0,0);
		fileTable.setBounds(347, 158, 167, 270);
		
		DefaultTableModel tableModel=(DefaultTableModel) fileTable.getModel();
		
		tableModel.addColumn("Files");
		//fileTable.setEnabled(false);		
		frame.getContentPane().add(fileTable);
		
		
		ButtonGroup opGroup = new ButtonGroup();		
		
		JRadioButton noOptimizations = new JRadioButton("No Optimizations");
		noOptimizations.setBounds(367, -1, 170, 23);
		noOptimizations.setSelected(true);
		opGroup.add(noOptimizations);
		frame.getContentPane().add(noOptimizations);
		
		JRadioButton editDistance = new JRadioButton("Edit Distance");
		editDistance.setBounds(367, 25, 170, 23);
		opGroup.add(editDistance);
		frame.getContentPane().add(editDistance);
		
		JRadioButton qGram = new JRadioButton("Q-Gram Overlap");
		qGram.setBounds(367, 51, 170, 23);
		opGroup.add(qGram);
		frame.getContentPane().add(qGram);
		
		editDistanceText = new JTextField();
		editDistanceText.setBounds(543, 26, 39, 20);
		frame.getContentPane().add(editDistanceText);
		editDistanceText.setColumns(10);
		
		JButton Help = new JButton("HELP");
		
		Help.setBounds(218, -1, 89, 23);
		frame.getContentPane().add(Help);
		
		JLabel lblResults = new JLabel("RESULTS");
		lblResults.setBounds(121, 158, 108, 14);
		frame.getContentPane().add(lblResults);
		
		JLabel lblDocumnets = new JLabel("Documnets");
		lblDocumnets.setBounds(347, 122, 94, 25);
		frame.getContentPane().add(lblDocumnets);
		
		JButton btnChronology = new JButton("Chronology");
		
		btnChronology.setBounds(622, 83, 136, 23);
		frame.getContentPane().add(btnChronology);
		
		JButton btnClose = new JButton();
		
		ImageIcon deleteChro = new ImageIcon(new ImageIcon("media/icons/empy_index.png").getImage().getScaledInstance(39, 23, 0));
		btnClose.setIcon(deleteChro);
		btnClose.setMargin (new Insets (0, 0, 0, 0));
		btnClose.setHorizontalAlignment(SwingConstants.LEADING);
		btnClose.setBounds(768, 83, 39, 23);
		frame.getContentPane().add(btnClose);
		
		/*table = new JTable();
		table.setBounds(628, 172, 168, 206);
		frame.getContentPane().add(table);
		*/
		
		
	//Search
		
		search.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				String queryStr = textField.getText();
				
	  
				if(queryStr.isEmpty()==false) {
					
			 /*LinkList to save Chronology*/
					
				chronology.addFirst(queryStr);
				Component compo[]=frame.getComponents();
				if(compo.equals(chronologyTable)==false) {
				frame.remove(chronologyTable);
				frame.repaint();
				}
				
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
				
				generalIndex.setSimilarity(modelUsed.getSimilarity(),true);
				LinkedList<Hit> results = generalIndex.submitQuery(queryStr, fields, modelUsed);
				
				for(Hit result : results) {
					resultsModel.addRow(new Object[] {result.getDocPath()+result.getDocName(), result.getScore()});
				}
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
			chronologyTable.setBounds(622, 172, 176, 160);
			frame.getContentPane().add(chronologyTable);
			
			DefaultTableModel chronologyModel=(DefaultTableModel) chronologyTable.getModel();
			
			chronologyModel.addColumn("Chronology");
			chronologyModel.setRowCount(10);
			
			if(chronology.isEmpty()==false && chronology.getFirst()!= "") {
			for(int i=0;i<chronology.size();i++) {
				chronologyModel.setValueAt(chronology.get(i), i,0);			
				}
			}
			
		      frame.repaint();
	
		}
	});
	//Delete Chronology
	btnClose.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			Component compo[]=frame.getComponents();
			if(compo.equals(chronologyTable)==false) {
			frame.remove(chronologyTable);
			frame.repaint();
			
			if(chronology.isEmpty()==false)
			chronology.remove();
			}
		}
	});
	
	
	}
}
