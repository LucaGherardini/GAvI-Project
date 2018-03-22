package index;

public class MainIndex {

	public static void main(String[] args) {		

		/*
		 * Creation of a Document referencing to README.md (NOTE: as default, the program
		 * "is located" at GAvI-Project/ (so, nothing is to being added to reach this file) and of a
		 * Document referencing to "Lucene Useful Links.txt" file, stored under "other" folder
		 */
		Index.addDocument("", "README.md");
		Index.addDocument("other/", "Lucene Useful Links.txt");

		System.out.println("Documents in index: " + Index.getSize());
		/*
		 * Erasing of the index
		 */
		Index.eraseIndex();
		
		/*
		 * This index is rebooted here and a new document is added
		 */
		Index.addDocument("doc/", "prova.txt");
		
		System.out.println("Documents in index after erasing and readding: " + Index.getSize());
		
	}
}
