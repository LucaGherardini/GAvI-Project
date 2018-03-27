package index;

import irModels.BooleanModel;
import irModels.FuzzyModel;
import textOperation.TextOperations;

import java.util.LinkedList;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexableField;
import org.apache.lucene.search.Query;

public class MainIndex {

	public static void main(String[] args) {		

		Index generalIndex = Index.getIndex();
		
		/*
		 * Creation of a Document referencing to README.md (NOTE: as default, the program
		 * "is located" at GAvI-Project/ (so, nothing is to being added to reach this file) 
		 */
		generalIndex.addDocument("", "README.md");
		generalIndex.addDocument("other/", "Lucene Useful Links.txt");
		generalIndex.addDocument("other/", "prova.txt");
		generalIndex.addDocument("other/", "test.txt");
		generalIndex.addDocument("other/", "GenericTextFile.txt");
		generalIndex.addDocument("other/", "USA.txt");
		
		Document d;
		for (int i=0; i<generalIndex.getSize(); i++) {
			d = generalIndex.getDocument(i);
			//System.out.println("Path of document n° " + i + ": " + d.get("path"));
			//System.out.println("Name of document n° " + i + ": " + d.get("name"));
			//System.out.println("Content of document n° " + i + ": " + d.get("content"));
		}
		
		
		String query = "name:test.txt OR content:Lucene";
		BooleanModel bm = new BooleanModel();
		
		
		/*
		 * Fields on which query will work, passed to inform index in which fields it must search
		 */
		LinkedList<String> fields = new LinkedList<String>();// = {"name", "content"};
		
		
		if(query.contains("name")) {
			fields.add("name");
		}
		
		if(query.contains("content")) {
			fields.add("content");
		}
		
		if(fields.size() == 0) {
			fields.add("name");
			fields.add("content");
		}

		generalIndex.submitQuery(query, fields, bm);
		
		System.out.println("************************************************");
		
		/*
		 * Try query "United States Park" and query "United States": same results, different ranking!
		 */
		query = "United States Park";
		
		fields = new LinkedList<String>();// = {"name", "content"};
		
		if(query.contains("name")) {
			fields.add("name");
		}
		
		if(query.contains("content")) {
			fields.add("content");
		}
		
		if(fields.size() == 0) {
			fields.add("name");
			fields.add("content");
		}
		
		FuzzyModel fm = new FuzzyModel();
		
		generalIndex.submitQuery(query, fields, fm);
	}
}
