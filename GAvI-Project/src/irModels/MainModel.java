package irModels;

import java.io.IOException;
import java.util.LinkedList;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.search.Query;

/*
 * Testing class for models
 * @author luca
 */
public class MainModel {

	public static void main(String[] args) throws IOException {
		LinkedList<Document> documentiProva = new LinkedList<Document>();

		Document doc = new Document();
		String title = "Io sono un titolo";
		doc.add(new TextField("Ciao", title, Field.Store.YES));
		documentiProva.add(doc);
	
		String query = "Ciao";
		LinkedList<Document> risultati;
		
		BooleanModel bm = new BooleanModel();
		Query q = bm.parseQuery(query, true, true);
		risultati = bm.executeQuery(q, documentiProva);
		
		
		System.out.println("Risultati per query " + query + ": \n" + risultati.toString() );
	}

}
