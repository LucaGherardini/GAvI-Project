package Image;

import java.awt.BorderLayout;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class Paint_Graphics  extends JFrame{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public Paint_Graphics(String pathGraphic) {
		//super("Test Panel");
		this.setSize(800,800);

		try { 
		File file = new File(pathGraphic); 
		BufferedImage image = ImageIO.read(file);
		JLabel label = new JLabel(new ImageIcon(image));
		JScrollPane pane = new JScrollPane(label);
		this.getContentPane().add(pane, BorderLayout.CENTER);
		}
		catch (Exception e) {
		this.getContentPane().add(new JTextArea(e.getMessage()));
		}

		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE );
		this.setVisible(true);
		}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new  Paint_Graphics("C:\\Users\\Utente\\Desktop\\screenshot\\Cattura.PNG");
	}

}
