package listeners;

import java.awt.RenderingHints.Key;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;

import javax.swing.SwingUtilities;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.FuzzyQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;

import gui.Main_window;


/**
 * Implementazione ascoltatori per la finestra principale
 * @author simone
 *
 */
public class Main_listeners implements WindowListener, KeyListener, ActionListener  {

	Main_window source;
	
	public Main_listeners(Main_window source) {
		// TODO Auto-generated constructor stub
		this.source = source;
	}
	
	@Override
	public void windowActivated(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowClosed(WindowEvent e) {
		// TODO Auto-generated method stub
		System.exit(0);
	}

	@Override
	public void windowClosing(WindowEvent e) {
		// TODO Auto-generated method stub
		System.exit(0);
	}

	@Override
	public void windowDeactivated(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowDeiconified(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowIconified(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowOpened(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		/**
		 * All'avvio della ricerca.
		 * Prelevo il testo della query, cerco nei documenti,
		 * restituisco i risultati
		 */
		if (e.getSource().equals(source.getCerca())) {
			// TODO Auto-generated method stub
			
			//Prelevo testo di ricerca
	        String querystr = source.getQuery().getText().length() > 0 ? source.getQuery().getText() : "lucene";
	
	        // "Title" sarà il campo che viene cercato di defaulta
	        // se nessun'altro campo è specificato nella query
			try {
				try {
					Query q = null;
					if (source.getTolerant().getSelectedItem().equals(source.getEdit_distance()))
						q = new FuzzyQuery(new Term("title", querystr));
					else if (source.getTolerant().getSelectedItem().equals(source.getQgrams()))
							;
					else
						q = new QueryParser("title", source.getAnalyzer()).parse(querystr);
		
			        //Ricerca
			        int hitsPerPage = 10;
			        IndexReader reader = DirectoryReader.open(source.getIndex());
			        IndexSearcher searcher = new IndexSearcher(reader);
			        TopDocs docs = searcher.search(q, hitsPerPage);
			        ScoreDoc[] hits = docs.scoreDocs;
			
			        // Mostro i risultati
			        source.getNumHits().setText(hits.length + " risultati trovati.");
			        String s = "<html>";
			        for(int i=0;i<hits.length;++i) {
			            int docId = hits[i].doc;
			            Document d = searcher.doc(docId);
			            s = s + ((i + 1) + ". " + d.get("isbn") + "\t" + d.get("title")) + "<br>";
			        }
			        s = s + "</html>";
			        source.getResults().setText(s);
			        // reader can only be closed when there
			        // is no need to access the documents any more.
			        reader.close();
			} catch (ParseException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			} catch (IOException e2) {
				// TODO: handle exception
			}
		}
	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		//Nel caso in cui si preme Invio si invoca il metodo del bottone "cerca"
		if (e.getKeyCode() == KeyEvent.VK_ENTER) {
			actionPerformed(new ActionEvent(source.getCerca(), 1, "cerca"));
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	public void edit_distance(String s1, String s2) {
		
	}
	
}


