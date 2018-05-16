package gui;

import java.awt.EventQueue;
import java.awt.Insets;


import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import com.sun.prism.Image;

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
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.io.File;

import java.util.LinkedList;
import java.awt.event.ActionEvent;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;

import javax.swing.JTable;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import java.awt.Toolkit;


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
		frame.getContentPane().setBackground(Color.BLACK);
		//frame.setSize(1200, 800);
		
		frame.setExtendedState(frame.getExtendedState() | JFrame.MAXIMIZED_BOTH);
		
		ImageIcon icon = new ImageIcon("media/icons/background.jpg");
		frame.setIconImage(icon.getImage().getScaledInstance(frame.getExtendedState(), frame.getExtendedState(),0));
		
		
		//frame.setIconImage(new ImageIcon("media/icons/backgound.jpg").getImage().getScaledInstance(frame.getExtendedState(), frame.getExtendedState(),0));
		//frame.setContentPane(new JLabel(new ImageIcon("media/icons/background.jpg").getImage().getScaledInstance(frame.getExtendedState(), frame.getExtendedState(),0)));
		/*Toolkit tk = Toolkit.getDefaultToolkit();
		int xSize = ((int) tk.getScreenSize().getWidth());
		int ySize = ((int) tk.getScreenSize().getHeight());
		frame.setSize(xSize,ySize);*/
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		frame.setTitle("GaVi Project");
		//frame.setResizable(false);
		
		JLabel title = new JLabel("NOME");
		title.setBounds(266, 113, 77, 25);
		frame.getContentPane().add(title);
		
		
		textField = new JTextField();
		textField.setBounds(70, 149, 418, 20);
		frame.getContentPane().add(textField);
		textField.setColumns(10);
		
		JComboBox<String> modelbox = new JComboBox<String>();
		modelbox.setMaximumRowCount(5);
		modelbox.setModel(new DefaultComboBoxModel<String>(new String[] {"Boolean Model", "Vector Space Model", "Probabilistic(BM25) Model", "Fuzzy Model"}));
		modelbox.setSelectedIndex(0);
		modelbox.setToolTipText("Preferenze");
		modelbox.setBounds(70, 11, 156, 20);
		frame.getContentPane().add(modelbox);
		
		JButton search = new JButton("Cerca");
		
		search.setBounds(498, 148, 89, 23);
		frame.getContentPane().add(search);
		
		JButton delete = new JButton();
		
		ImageIcon deleteIcon = new ImageIcon(new ImageIcon("media/icons/empty_index.png").getImage().getScaledInstance(35, 35, 0));
		delete.setIcon(deleteIcon);
		delete.setMargin (new Insets (0, 0, 0, 0));
		delete.setBounds(1122, 183, 52, 52);
		frame.getContentPane().add(delete);
		
		
		JButton add = new JButton();
        ImageIcon addIcon = new ImageIcon(new ImageIcon("media/icons/add_file.png").getImage().getScaledInstance(35, 35, 0));
		add.setIcon(addIcon);
		add.setMargin (new Insets (0, 0, 0, 0));
		add.setBounds(1122, 246, 52, 52);
		frame.getContentPane().add(add);
		
		JButton remove = new JButton();
		ImageIcon removeIcon = new ImageIcon(new ImageIcon("media/icons/remove_file.png").getImage().getScaledInstance(35, 35, 0));
		remove.setIcon(removeIcon);
		remove.setMargin (new Insets (0, 0, 0, 0));
		remove.setBounds(1122, 309, 52, 52);
		frame.getContentPane().add(remove);
		

		resultsTable = new JTable();
        resultsTable.setBounds(70, 234, 418, 405);
        DefaultTableModel resultsModel = (DefaultTableModel) resultsTable.getModel();
        resultsModel.addColumn("File");
        resultsModel.addColumn("Score");
       
		frame.getContentPane().add(resultsTable);
	
		
		fileTable = new JTable();
		fileTable.setBounds(753, 183, 359, 540);
		
		DefaultTableModel tableModel=(DefaultTableModel) fileTable.getModel();
		
		//tableModel.addColumn("Files");
		//fileTable.setEnabled(false);		
		frame.getContentPane().add(fileTable);
		
		
		ButtonGroup opGroup = new ButtonGroup();		
		
		JRadioButton noOptimizations = new JRadioButton("No Optimizations");
		noOptimizations.setBounds(753, 10, 170, 23);
		noOptimizations.setSelected(true);
		opGroup.add(noOptimizations);
		frame.getContentPane().add(noOptimizations);
		
		JRadioButton editDistance = new JRadioButton("Edit Distance");
		editDistance.setBounds(753, 36, 170, 23);
		opGroup.add(editDistance);
		frame.getContentPane().add(editDistance);
		
		JRadioButton qGram = new JRadioButton("Q-Gram Overlap");
		qGram.setBounds(753, 62, 170, 23);
		opGroup.add(qGram);
		frame.getContentPane().add(qGram);
		
		editDistanceText = new JTextField();
		editDistanceText.setBounds(929, 37, 39, 20);
		frame.getContentPane().add(editDistanceText);
		editDistanceText.setColumns(10);
		
		JButton Help = new JButton("HELP");
		
		Help.setBounds(254, 10, 89, 23);
		frame.getContentPane().add(Help);
		
		JLabel lblResults = new JLabel("RESULTS");
		lblResults.setBounds(70, 204, 108, 14);
		frame.getContentPane().add(lblResults);
		
		JLabel lblDocumnets = new JLabel("Documents");
		lblDocumnets.setBounds(753, 147, 94, 25);
		frame.getContentPane().add(lblDocumnets);
		
		JButton btnChronology = new JButton("Chronology");
		
		btnChronology.setBounds(373, 10, 136, 23);
		frame.getContentPane().add(btnChronology);
		
		
		
		
		
		//ImageIcon deleteChro = new ImageIcon(new ImageIcon("media/icons/empy_index.png").getImage().getScaledInstance(39, 23, 0));
		
		JFrame ChronoPane=new JFrame("Chronology");
		ChronoPane.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		
		chronologyTable= new JTable();
		DefaultTableModel chronologyTableModel=(DefaultTableModel) chronologyTable.getModel();
		 chronologyTableModel.setRowCount(10);
		 chronologyTableModel.setColumnCount(1);
		 tableModel.addColumn("Search");
		 
		
	//Search
		
		search.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				String queryStr = textField.getText();
				
	  
				if(queryStr.isEmpty()==false) {
					
			 /*LinkList to save Chronology*/
					
				chronology.addFirst(queryStr);
				
				
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
				
		generalIndex.setSimilarity(modelUsed.getSimilarity(), true);
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
				fileC.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
				fileC.showOpenDialog(frame);
				
							
				File filesSelected[] = fileC.getSelectedFiles();
				
				File files[];
				
				
				LinkedList<String> paths = new LinkedList<String>();		
				for (File doc : filesSelected) {
					
				
					if(doc.isDirectory()) {
						files=doc.listFiles();
						for (File doci : files) {
							if(doci.getAbsolutePath().endsWith("txt")) {
								int separatorIndex = doci.getPath().lastIndexOf(File.separator);
								String path = "";
								if (separatorIndex != -1) {
									path = doci.getPath().substring(0, separatorIndex+1);
									
									}
								if(!paths.contains(path)){
									paths.add(path);
									tableModel.setRowCount(tableModel.getRowCount()+1);
									tableModel.setValueAt(path, tableModel.getRowCount()-1, 0);
									//System.out.println("\n" + paths.getLast());
									
									}
								tableModel.setRowCount(tableModel.getRowCount()+1);
								tableModel.setValueAt("..." + doci.getPath().substring(separatorIndex+1, doci.getPath().length()), tableModel.getRowCount()-1, 0);
					   }
					}
				 }
					
					if(doc.getAbsolutePath().endsWith("txt")) {
					int separatorIndex = doc.getPath().lastIndexOf(File.separator);
					String path = "";
					if (separatorIndex != -1) {
						path = doc.getPath().substring(0, separatorIndex+1);
					}
					
					
					if(!paths.contains(path)){
						paths.add(path);
						tableModel.setRowCount(tableModel.getRowCount()+1);
						tableModel.setValueAt(path, tableModel.getRowCount()-1, 0);
						//System.out.println("\n" + paths.getLast());
						
					}
					tableModel.setRowCount(tableModel.getRowCount()+1);
					tableModel.setValueAt("..." + doc.getPath().substring(separatorIndex+1, doc.getPath().length()), tableModel.getRowCount()-1, 0);
				}
				}
				
				/*
				for (File f : filesSelected) {
					String nameFile = f.getAbsolutePath();
				
					if(nameFile.endsWith(".txt")) {
							tableModel.setRowCount(tableModel.getRowCount()+1);
							tableModel.setValueAt(f.getName(), tableModel.getRowCount()-1, 0);
							generalIndex.addDocument(nameFile);
					}
				}*/
							
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
			
			tableModel.setRowCount(0);
	/*if(tableModel.getRowCount() !=1) {
				for(int i=0 ; i < tableModel.getRowCount() ; i++) {
					tableModel.removeRow(tableModel.);	
				}
			}else {
				tableModel.removeRow(0);
			}*/
		}
		});
	
	
	//Chronology
	btnChronology.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent arg0) {
			ChronoPane.setVisible(true);
			ChronoPane.getContentPane().add(chronologyTable);
			ChronoPane.setAlwaysOnTop(true);
			
			//in the center
			 Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
			 
			    int x = (int) ((dimension.getWidth() - 300) / 2);
			    int y = (int) ((dimension.getHeight() - 200) / 2);
			    
			ChronoPane.setBounds(x, y, 300, 200);
			int i;
			for(i=0; i< chronologyTableModel.getRowCount();i++) {
				
				if(chronology.size()>i)
				chronologyTableModel.setValueAt(chronology.get(i),i,0);
				
			}
			
			
		}
	});
	
	
	}
}
