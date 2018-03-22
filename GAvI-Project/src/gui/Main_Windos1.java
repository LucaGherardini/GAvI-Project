package gui;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JTextArea;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JEditorPane;
import java.awt.ScrollPane;
import java.awt.Color;
import javax.swing.JPopupMenu;
import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JTable;
import java.awt.Dimension;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JToolBar;
import javax.swing.JRadioButton;

public class Main_Windos1 {

	private JFrame frame;
	private JTextField textField;
	private JTable chronology;
	private JTable file;
	private JTextField editDistanceText;

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
		frame.getContentPane().setBackground(Color.BLUE);
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
		modelbox.setBounds(98, 0, 93, 20);
		frame.getContentPane().add(modelbox);
		
		JButton search = new JButton("Cerca");
		
		search.setBounds(225, 83, 89, 23);
		frame.getContentPane().add(search);
		
		JButton refresh = new JButton("#");
		refresh.setBounds(493, 143, 41, 23);
		frame.getContentPane().add(refresh);
		
		JButton add = new JButton("+");
		
		add.setBounds(493, 177, 41, 23);
		frame.getContentPane().add(add);
		
		JButton remove = new JButton("-");
		remove.setBounds(493, 211, 41, 23);
		frame.getContentPane().add(remove);
		

		chronology = new JTable();
        chronology.setBounds(26, 162, 274, 188);
		frame.getContentPane().add(chronology);
		
		file = new JTable();
		file.setBounds(347, 143, 136, 218);
		frame.getContentPane().add(file);
		
				
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
	}
	
}
