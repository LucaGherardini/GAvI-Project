package gui;

import java.awt.EventQueue;
import java.awt.Insets;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import com.sun.scenario.effect.Filterable;

import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.io.File;
import java.awt.event.ActionEvent;
import java.awt.Color;
import javax.swing.JTable;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.swing.JTextArea;

public class Main_Windos1 {

	private JFrame frame;
	private JTextField textField;
	private JTable chronology;
	private JTable fileTable;
	private JTextField editDistanceText;
	private JTable table;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Main_Windos1 window = new Main_Windos1();
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
	public Main_Windos1() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.getContentPane().setBackground(Color.CYAN);
		frame.setBounds(100, 100, 550, 400);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JLabel title = new JLabel("NOME");
		title.setBounds(98, 48, 77, 25);
		frame.getContentPane().add(title);
		
		textField = new JTextField();
		textField.setBounds(10, 84, 205, 20);
		frame.getContentPane().add(textField);
		textField.setColumns(10);
		
		JComboBox modelbox = new JComboBox();
		modelbox.setMaximumRowCount(5);
		modelbox.setModel(new DefaultComboBoxModel(new String[] {"Boolean", "Vector Space", "Probabulistic", "Fuzzy"}));
		modelbox.setSelectedIndex(0);
		modelbox.setToolTipText("Preferenze");
		modelbox.setBounds(0, 0, 117, 20);
		frame.getContentPane().add(modelbox);
		
		JButton search = new JButton("Cerca");
		
		search.setBounds(225, 83, 89, 23);
		frame.getContentPane().add(search);
		
		JButton delete = new JButton();
		
		ImageIcon saveicon=new ImageIcon("media/icons/empy_index.png");
		delete.setIcon(saveicon);
		delete.setMargin (new Insets (0, 0, 0, 0));
		delete.setBounds(493, 143, 41, 23);
		frame.getContentPane().add(delete);
		
		
		JButton add = new JButton();
        ImageIcon addicon=new ImageIcon("media/icons/add_file.png");
		add.setIcon(addicon);
		add.setMargin (new Insets (0, 0, 0, 0));
		add.setBounds(493, 177, 41, 23);
		frame.getContentPane().add(add);
		
		JButton remove = new JButton();
		ImageIcon removeicon=new ImageIcon("media/icons/remove.png");
		remove.setIcon(removeicon);
		remove.setMargin (new Insets (0, 0, 0, 0));
		remove.setBounds(493, 211, 41, 23);
		frame.getContentPane().add(remove);
		

		chronology = new JTable();
        chronology.setBounds(28, 162, 274, 188);
		frame.getContentPane().add(chronology);
		
		fileTable = new JTable(0,0);
		fileTable.setBounds(347, 143, 136, 218);
		
		DefaultTableModel model=(DefaultTableModel) fileTable.getModel();
		
		model.addColumn("FILES");
		//fileTable.setEnabled(false);
		
		
		frame.getContentPane().add(fileTable);
		
		
				
		JRadioButton noOtimisation = new JRadioButton("No Otimisation");
		
		noOtimisation.setBounds(345, -1, 109, 23);
		frame.getContentPane().add(noOtimisation);
		
		JRadioButton editDistance = new JRadioButton("Edit Distance");
		editDistance.setBounds(345, 25, 109, 23);
		frame.getContentPane().add(editDistance);
		
		JRadioButton qGram = new JRadioButton("Q-Gram Overlap");
		qGram.setBounds(345, 51, 109, 23);
		frame.getContentPane().add(qGram);
		
		editDistanceText = new JTextField();
		editDistanceText.setBounds(472, 26, 52, 20);
		frame.getContentPane().add(editDistanceText);
		editDistanceText.setColumns(10);
		
		JButton Help = new JButton("HELP");
		
		Help.setBounds(177, -1, 89, 23);
		frame.getContentPane().add(Help);
		
			
		
		
	//JradioButton
		noOtimisation.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(noOtimisation.isSelected()) {
					editDistance.setSelected(false);
					qGram.setSelected(false);
				}
					
			}
		});
		
		editDistance.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(editDistance.isSelected()) {
					noOtimisation.setSelected(false);
					qGram.setSelected(false);
				}
					
			}
		});
		qGram.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(qGram.isSelected()) {
					editDistance.setSelected(false);
					noOtimisation.setSelected(false);
				}
					
			}
		});
		
	//Search
		
		search.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				System.out.println(textField.getText());
				
				if(modelbox.getSelectedItem()=="Boolean") {
					System.out.println("Boolean");
				}
				
				if(modelbox.getSelectedItem()=="Vector Space") {
					System.out.println("Vector Space");
				}
				
				if(modelbox.getSelectedItem()=="Probabulistic") {
					System.out.println("Probabulistic");
				}
				
				if(modelbox.getSelectedItem()=="Fuzzy") {
					System.out.println("Fuzzy");
				}		
			}
		});
		
	// Help **** Inserire gli esampi di stringhe se si possono Parsare*****
		Help.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				 				    
				JOptionPane.showMessageDialog(frame,"INSERIRE ESEMPI DI STRINGHE" );
			}
		});
		// Add
	add.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				JFileChooser file=new JFileChooser();
				int n=file.showOpenDialog(frame);
				
				
				File f=file.getSelectedFile();
				
				//se si vogliono selezionare piu file insieme (difficile implementazione forse non il modo migliore per piu file o directory)
				//File f[]=file.getSelectedFiles();
				
				String nameFile=f.getName().toLowerCase();
				
				
				if(nameFile.endsWith("txt")) {
				model.setRowCount(model.getRowCount()+1);
				model.setValueAt(f.getName(), model.getRowCount()-1, 0);
					
				
				}
							
			}
		});
	
	//remove

	remove.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent arg0) {
			
			
				model.removeRow(fileTable.getSelectedRow());
			}
		
	});
		
	// Delete All Row
	delete.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent arg0) {
			
			
			if(model.getRowCount() !=1) {
				for(int i=0;i< model.getRowCount();i++) {
					model.removeRow(0);
					
					
				}
				
				model.removeRow(0);
			}
		
		else
			model.removeRow(0);
			}
	});
	
	
	}
}
