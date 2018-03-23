package index;

import java.util.LinkedList;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexableField;

public class MainIndex {

	public static void main(String[] args) {		

		/*
		 * Creation of a Document referencing to README.md (NOTE: as default, the program
		 * "is located" at GAvI-Project/ (so, nothing is to being added to reach this file) and of a
		 * Document referencing to "Lucene Useful Links.txt" file, stored under "other" folder
		 */
		Index generalIndex = Index.getIndex();
		
		generalIndex.addDocument("", "README.md");
		generalIndex.addDocument("other/", "Lucene Useful Links.txt");

		System.out.println("Documents in index: " + generalIndex.getSize());
		/*
		 * Erasing of the index
		 */
		//generalIndex.eraseIndex();
		
		/*
		 * This index is rebooted here and a new document is added
		 */
		generalIndex.addDocument("doc/", "prova.txt");
		
		System.out.println("Documents in index after erasing and readding: " + generalIndex.getSize());
		Document d;
		for (int i=0; i<generalIndex.getSize(); i++) {
			d = generalIndex.getDocument(i);
			System.out.println("Path of document n° " + i + ": " + d.get("path"));
			System.out.println("Name of document n° " + i + ": " + d.get("name"));
			System.out.println("Content of document n° " + i + ": " + d.get("content"));
		}
		
	}
}
