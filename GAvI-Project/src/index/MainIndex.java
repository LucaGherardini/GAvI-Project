package index;

import irModels.BooleanModel;
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
		generalIndex.addDocument("doc/", "prova.txt");
		
		Document d;
		for (int i=0; i<generalIndex.getSize(); i++) {
			d = generalIndex.getDocument(i);
			//System.out.println("Path of document n° " + i + ": " + d.get("path"));
			//System.out.println("Name of document n° " + i + ": " + d.get("name"));
			//System.out.println("Content of document n° " + i + ": " + d.get("content"));
		}
		
		String query = "Ciao";
		BooleanModel bm = new BooleanModel();
		//bm.query(query, false, true);
		 

		generalIndex.submitQuery(bm.parseQuery(query, false, false));
		
	}
}
