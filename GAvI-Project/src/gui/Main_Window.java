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
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.LinkedList;
import java.awt.event.ActionEvent;
import java.awt.Color;
import java.awt.Dimension;
import javax.swing.JTable;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import java.awt.Toolkit;
import java.awt.Font;
import javax.swing.JTextArea;
import javax.swing.JList;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;


public class Main_Window {

	private JFrame frame = null;
	private JTextField textField = null;
	JScrollPane scrollPane = null;
	private JTable resultsTable;
	private JTable fileTable = null;
	private JTextField editDistanceText = null;
	private Index generalIndex = null;
	private JTable chronologyTable=null;
	private LinkedList<String> chronology=new LinkedList<String>();
	private static int editdistance=0;
	
	
	
	
	// Launch the application
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

	
	 // Create the application.
	 
	public Main_Window() {
		initialize();
	}

	
	 // Initialize the contents of the frame.
	 
	private void initialize() {
		generalIndex = Index.getIndex();
		
		
		frame = new JFrame();
		
		frame.getContentPane().setBackground(Color.gray);
		
		
		frame.setSize(1200, 800);
		
		
		frame.setExtendedState(frame.getExtendedState() | JFrame.MAXIMIZED_BOTH);
		
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setTitle("GaVi Project");	
		frame.getContentPane().setLayout(null);
		
		// label Project's name
		JLabel title = new JLabel("NAME");
		title.setFont(new Font("Verdana", Font.BOLD, 17));
		title.setBounds(266, 113, 77, 25);
		frame.getContentPane().add(title);
		
		//text to search
		textField = new JTextField();
		textField.setBounds(70, 149, 418, 20);
		frame.getContentPane().add(textField);
		textField.setColumns(10);
		
		//box model 
		JComboBox<String> modelbox = new JComboBox<String>();
		modelbox.setBounds(70, 11, 156, 20);
		modelbox.setMaximumRowCount(5);
		modelbox.setModel(new DefaultComboBoxModel<String>(new String[] { "Vector Space Model", "Boolean Model", "Probabilistic(BM25) Model", "Fuzzy Model"}));
		modelbox.setSelectedIndex(0);
		modelbox.setToolTipText("Preferenze");
		frame.getContentPane().add(modelbox);
		
		//button to search
		JButton search = new JButton("Search");
		search.setBounds(498, 148, 89, 23);
		frame.getContentPane().add(search);
		
		//button to delete all file add
		JButton delete = new JButton();
		delete.setBounds(1122, 183, 52, 52);
		ImageIcon deleteIcon = new ImageIcon(new ImageIcon("media/icons/empty_index.png").getImage().getScaledInstance(35, 35, 0));
		delete.setIcon(deleteIcon);
		delete.setMargin (new Insets (0, 0, 0, 0));
		frame.getContentPane().add(delete);
		
		//button to add new files
		JButton add = new JButton();
		add.setBounds(1122, 246, 52, 52);
        ImageIcon addIcon = new ImageIcon(new ImageIcon("media/icons/add_file.png").getImage().getScaledInstance(35, 35, 0));
		add.setIcon(addIcon);
		add.setMargin (new Insets (0, 0, 0, 0));
		frame.getContentPane().add(add);
		
		//button to remove all file selecting
		JButton remove = new JButton();
		remove.setBounds(1122, 309, 52, 52);
		ImageIcon removeIcon = new ImageIcon(new ImageIcon("media/icons/remove_file.png").getImage().getScaledInstance(35, 35, 0));
		remove.setIcon(removeIcon);
		remove.setMargin (new Insets (0, 0, 0, 0));
		frame.getContentPane().add(remove);
		
		//table for results 
		resultsTable = new JTable();
		resultsTable.setBounds(70, 234, 418, 405);
        DefaultTableModel resultsModel = (DefaultTableModel) resultsTable.getModel();
        resultsModel.addColumn("File");
        resultsModel.addColumn("Score");
        
       	frame.getContentPane().add(resultsTable);
	
		//table to view files adding
		fileTable = new JTable();
		fileTable.setBounds(753, 183, 359, 540);
		DefaultTableModel tableModel=(DefaultTableModel) fileTable.getModel();	
		//scrollPane= new JScrollPane(fileTable, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		//fileTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		frame.getContentPane().add(fileTable);
		
		//box of button to optimizations
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
		
		//button for user helping
		JButton Help = new JButton("HELP");
		Help.setBounds(254, 10, 89, 23);
		frame.getContentPane().add(Help);
		
		//some label to define tables
		JLabel lblResults = new JLabel("RESULTS");
		lblResults.setFont(new Font("Verdana", Font.BOLD, 17));
		lblResults.setBounds(70, 204, 108, 14);
		frame.getContentPane().add(lblResults);
		
		JLabel lblDocumnets = new JLabel("Documents");
		lblDocumnets.setFont(new Font("Verdana", Font.BOLD, 17));
		lblDocumnets.setBounds(753, 147, 156, 25);
		frame.getContentPane().add(lblDocumnets);
		
		//button chronology
		JButton btnChronology = new JButton("Chronology");
		btnChronology.setBounds(373, 10, 136, 23);
		frame.getContentPane().add(btnChronology);
		
		JButton btnSaveIndex = new JButton("save");
		
		btnSaveIndex .setBounds(1122, 372, 52, 52);
		frame.getContentPane().add(btnSaveIndex );
		
		JButton btnloadIndex = new JButton("load");
		
		
		btnloadIndex.setBounds(1122, 435, 52, 52);
		frame.getContentPane().add(btnloadIndex);
		
					
		//frame Chronology
		JFrame ChronoPane=new JFrame("Chronology");
		ChronoPane.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		
		chronologyTable= new JTable();
		DefaultTableModel chronologyTableModel=(DefaultTableModel) chronologyTable.getModel();
		 chronologyTableModel.setRowCount(10);
		 chronologyTableModel.setColumnCount(1);
		 tableModel.addColumn("Search");
		 
		
	//Search: searching after press button "Search"
		
		search.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				String queryStr = textField.getText();
				
				if(editDistance.isSelected()==true) {
					
					editdistance=checkeditDistance(editDistanceText.getText());
					System.out.println(editdistance);
				}
	  
				if(queryStr.isEmpty()==false) {
					
			 //LinkList to save Chronology
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
				LinkedList<Hit> results = generalIndex.submitQuery(queryStr, fields, modelUsed, false);
				if (results != null) {
					for(Hit result : results) {
						resultsModel.addRow(new Object[] {result.getDocPath()+result.getDocName(), result.getScore()});
					}
				}
			}
				}
		});
		
	// help for users
		Help.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				 				    
				JOptionPane.showMessageDialog(frame,	"Boolean Model: Using of Logic Operator (AND, OR, NOT) \"(Lucene OR Information Retrieval) AND Boolean Model\" \n"
													+	"Fuzzy Model: More flexible than Boolean Model, using of \"~\" operator to mark tolerance on terms \"United Stat~\" \n"
													+	"Vector Space Model: Based on a Vector rapresentation of query and documents, can be used Fuzzy and Boolean operators \n"
													+ 	"Probabilistic(BM25): Use of BM25 ranking function to retrieve documents given a query\n\n"
													+ 	"Query composition: You can declare in which fields search (name or content) writing field name in query \"name:Salomè content:Give me Iokanaan's head\"");
			}
		});
		
		
	// Add new file in the table Documents
	add.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				JFileChooser fileC=new JFileChooser();
				fileC.setMultiSelectionEnabled(true);
				fileC.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
				fileC.showOpenDialog(frame);
				
							
				File filesSelected[] = fileC.getSelectedFiles();   			
				LinkedList<String> paths = new LinkedList<String>();
				
				for (File doc : filesSelected) {
					
					if(doc.isDirectory()) {
						subfolders(doc,tableModel);
				        }
				        												
					if(doc.getAbsolutePath().endsWith(".txt")) {
					generalIndex.addDocument(doc.getAbsolutePath());
					int separatorIndex = doc.getPath().lastIndexOf(File.separator);
					String path = "";
					if (separatorIndex != -1) {
						path = doc.getPath().substring(0, separatorIndex+1);
					}
										
					if(!paths.contains(path)){
						paths.add(path);
						tableModel.setRowCount(tableModel.getRowCount()+1);
						tableModel.setValueAt(path, tableModel.getRowCount()-1, 0);
						
						
					}
					tableModel.setRowCount(tableModel.getRowCount()+1);
					tableModel.setValueAt("..." + doc.getPath().substring(separatorIndex+1, doc.getPath().length()), tableModel.getRowCount()-1, 0);
					
					}
					
				}
			}
		});
	
	
	//remove files selected 
	remove.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent arg0) {
				tableModel.removeRow(fileTable.getSelectedRow());
			}
	});
		
	// Delete All Row in the table "Documents"
	delete.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent arg0) {
			
			tableModel.setRowCount(0);
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
			    int y = (int) ((dimension.getHeight() - 215) / 2);
			    
			ChronoPane.setBounds(x, y, 300, 215);
			int i;
			for(i=0; i< chronologyTableModel.getRowCount();i++) {
				
				if(chronology.size()>i)
				chronologyTableModel.setValueAt(chronology.get(i),i,0);
				
			}
		}
	});
	
	//load index
	btnloadIndex.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent arg0) {
			FileReader fileReader=null;
			JFileChooser fileC=new JFileChooser();
			fileC.setMultiSelectionEnabled(true);
			fileC.setFileSelectionMode(JFileChooser.FILES_ONLY);
			fileC.showOpenDialog(frame);
			fileC.getSelectedFile();
			BufferedReader buffer = null;
			
			int reply =JOptionPane.showConfirmDialog(null,"Do you want load :"+ fileC.getSelectedFile()+ "?", "Attention", JOptionPane.YES_NO_OPTION);
			//aggiungi il file nella tabella documenti
			
			if(reply==JOptionPane.YES_OPTION) {
			
				File file[]=fileC.getSelectedFiles();
				if(file.length==1) {
			       System.out.println(file[0].getAbsolutePath());
			       generalIndex.loadIndex(file[0].getAbsolutePath());
			       
			       	}		
				else {
					JOptionPane.showMessageDialog(frame,"Only one file can be load \n");
				}
					
				}
				}
		
	});
	
	//Save index on tempIndex.ser
	btnSaveIndex.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			
			JFileChooser fileSave = new JFileChooser();
			 fileSave.setDialogTitle("Specify a file to save");   
			 
			int userSelection =  fileSave.showSaveDialog(frame);
			
			if (userSelection == JFileChooser.APPROVE_OPTION) {
				
			    File fileToSave =  fileSave.getSelectedFile();
			    
			    try {
					fileToSave.createNewFile();
					generalIndex.saveIndex(fileToSave.getAbsolutePath());
					
					/*PrintWriter p=new PrintWriter(fileToSave);
					for(int i=0;tableModel.getRowCount()>i;i++) {
					p.print(tableModel.getValueAt(i, 0));
					p.println("\n");}
					p.close();*/
					
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			    
			    System.out.println("Save as file: " + fileToSave.getAbsolutePath());
			}
		}
	});
	
	}
	
	public int checkeditDistance(String text) {
		int number=0;
		char n;
		if(text.length()==1) {
			n=text.charAt(0);
			
				
			if(Character.isDigit(n)==true)
			number= Integer.parseInt(String.valueOf(n));
			else {
				JOptionPane.showMessageDialog(frame,"Error in edit distance text box \n"
                                                  + "Only numbers from 1 to 4 in are accepted	");
				return 2;
				}
			if(number<5 && number>0)
				return number;
			else {
				JOptionPane.showMessageDialog(frame,"Error in edit distance text box \n"
                                                   + "Only numbers from 1 to 4 in are accepted	");
				
				return 2;
		}
			}
			
		JOptionPane.showMessageDialog(frame,"Error in edit distance text box \n"
                                          + "Only numbers from 1 to 4 in are accepted	");
			return 2;
		
	}
	
	
	public static int getEditdistance() {
		return editdistance;
	}


	// function to decide if add all or some or no subfolders
	public void subfolders (File Directory,DefaultTableModel tableModel) {
		
		File files[];
		LinkedList<String> paths = new LinkedList<String>();
								
		files=Directory.listFiles();
		
				for (File f : files) {
								if(f.isDirectory())	{
									int reply = JOptionPane.showConfirmDialog(null,"Do you want add all subfolders of :  "+ f.getPath() + "?", "Attention", JOptionPane.YES_NO_OPTION);
							        if (reply == JOptionPane.YES_OPTION) {
							         this.subfolders(f, tableModel);
									
								}
								}
					if(f.getAbsolutePath().endsWith(".txt")) {
						generalIndex.addDocument(f.getAbsolutePath());
						int separatorIndex = f.getPath().lastIndexOf(File.separator);
						String path = "";
						if (separatorIndex != -1) {
							path = f.getPath().substring(0, separatorIndex+1);
							
							}
						if(!paths.contains(path)){
							paths.add(path);
							tableModel.setRowCount(tableModel.getRowCount()+1);
							tableModel.setValueAt(path, tableModel.getRowCount()-1, 0);
							
							//System.out.println("\n" + paths.getLast());
							
							}
						tableModel.setRowCount(tableModel.getRowCount()+1);
						tableModel.setValueAt("..." + f.getPath().substring(separatorIndex+1, f.getPath().length()), tableModel.getRowCount()-1, 0);
						
					}
					
			}
					 
				
			if(Directory.getAbsolutePath().endsWith(".txt")) {
			int separatorIndex = Directory.getPath().lastIndexOf(File.separator);
			String path = "";
			if (separatorIndex != -1) {
				path = Directory.getPath().substring(0, separatorIndex+1);
			}
			
			
			if(!paths.contains(path)){
				paths.add(path);
				tableModel.setRowCount(tableModel.getRowCount()+1);
				tableModel.setValueAt(path, tableModel.getRowCount()-1, 0);
				
				//System.out.println("\n" + paths.getLast());
				
			}
			tableModel.setRowCount(tableModel.getRowCount()+1);
			tableModel.setValueAt("..." + Directory.getPath().substring(separatorIndex+1, Directory.getPath().length()), tableModel.getRowCount()-1, 0);
			
		}
		
		
		
		
	}
}
