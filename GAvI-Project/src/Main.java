import java.io.IOException;
import java.util.ArrayList;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
//import org.apache.lucene.benchmark.byTask.tasks.NewLocaleTask;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;

import gui.Main_window;
import textOperation.TextOperations;

public class Main {

	public static void main(String[] args) throws IOException, ParseException {
		// TODO Auto-generated method stub
		
		//Analizzatore, serve per tokenizzare i testi
        StandardAnalyzer analyzer = new StandardAnalyzer();

        // Creazione indice
        Directory index = new RAMDirectory();

        IndexWriterConfig config = new IndexWriterConfig(analyzer);

        IndexWriter w = new IndexWriter(index, config);
        //Aggiunta documenti di prova
        addDoc(w, "Lucene in Action", "193398817");
        addDoc(w, "Lucene for Dummies", "55320055Z");
        addDoc(w, "Managing Gigabytes", "55063554A");
        addDoc(w, "The Art of Computer Science", "9900333X");
        w.close();
        
        tryTextOperations("Two cats are enough under these beds?");

        /*
		Main_window main = new Main_window(analyzer, index);
		main.setSize(500, 500);
		main.setVisible(true);
		*/
        
        }

    private static void addDoc(IndexWriter w, String title, String isbn) throws IOException {
        //Nota, textfield è usato se si vuole tokenizzare
    	//		stringfield se non lo si vuole   	
    	Document doc = new Document();
        doc.add(new TextField("title", title, Field.Store.YES));
        doc.add(new StringField("isbn", isbn, Field.Store.YES));
        w.addDocument(doc);
    }
    
    private static void tryTextOperations(String phrase) {
    	
        ArrayList<String> tokens = TextOperations.tokenization(phrase);
        ArrayList<String> stop = TextOperations.removeStopWords(tokens);
        ArrayList<String> stem = TextOperations.stemming(stop);
        System.out.println("Frase: "+phrase);
        System.out.println("Tokens: "+tokens);
        System.out.println("Removed StopWords: " + stop);
        System.out.println("Stem: "+stem);
        
    }
		
}
