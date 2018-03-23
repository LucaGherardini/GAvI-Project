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
		generalIndex.addDocument("doc/", "test.txt");
		
		Document d;
		for (int i=0; i<generalIndex.getSize(); i++) {
			d = generalIndex.getDocument(i);
			//System.out.println("Path of document n° " + i + ": " + d.get("path"));
			//System.out.println("Name of document n° " + i + ": " + d.get("name"));
			//System.out.println("Content of document n° " + i + ": " + d.get("content"));
		}
		
		
		// This returns twice the same document, one for hit on "ciao", one for hit on "prova.txt"
		String query = "name:prova.txt OR content:test";
		BooleanModel bm = new BooleanModel();
		//bm.query(query, false, true);
		
		/*
		 * Fields on which query will work
		 */
		String[] fields = {"name", "content"};

		// QUESTO RENDE I MODELLI IMPLEMENTATI DA BUTTARE!
		/* TODO Maybe another field to submitQuery, called "Model" could let a query parsing suitable to Model 
		 * wanted
		 */
		generalIndex.submitQuery(query, fields, bm);
		
	}
}
