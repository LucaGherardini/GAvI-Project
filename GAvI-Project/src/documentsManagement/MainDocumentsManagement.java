package documentsManagement;

public class MainDocumentsManagement {

	public static void main(String[] args) {
		DocumentSet dset = DocumentSet.getDocumentSet();
		
		/*
		 * Creating of a DocumentFile referencing to README.md (NOTE: as default, the program
		 * "is located" at GAvI-Project/ (so, nothing is to being added to reach this file)
		 */
		DocumentFile readme = new DocumentFile("README.md", "");
		
		DocumentFile luceneLinks = new DocumentFile("Lucene Useful Links.txt", "other/");
		
		/*
		 * Adding of DocumentFiles to the DocumentSet
		 */
		dset.addDocumentFile(readme);
		dset.addDocumentFile(luceneLinks);
	}
}
