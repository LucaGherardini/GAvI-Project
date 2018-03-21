package irModels;

import java.io.IOException;
import java.util.LinkedList;

/*
 * Testing class for models
 * @author luca
 */
public class MainModel {

	public static void main(String[] args) throws IOException {
		LinkedList<String> documentiProva = new LinkedList<String>();

		String textDocument = "title";
		String newTry = "different title";
		String lastTry = "title is different";
		String lastLastTry = "title";
		//String title = "title";
		//doc.add(new TextField("title", title, Field.Store.YES));
		documentiProva.add(textDocument);
		documentiProva.add(newTry);
		documentiProva.add(lastTry);
		documentiProva.add(lastLastTry);
		
		String query = "title";
		LinkedList<String> risultati;
		
		Model m = new BooleanModel();
		
		risultati = m.query("title", false, true, documentiProva);
		
		System.out.println("Risultati per query " + query + ": \n" + risultati.toString());
	}

}
