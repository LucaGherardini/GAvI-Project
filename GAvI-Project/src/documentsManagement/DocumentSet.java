package documentsManagement;

import java.util.LinkedList;

/*
 * This class creates a Document Set, composed by our Document type. We choosen to make it a singleton class to allow
 * only one instance at time, so, in each point of the program, we can easily get this with a call to "getDocumentSet"
 * and eventually reset it with resetSet method
 * 
 * @param ds is a static pointer to DocumentSet to allow the singleton, in fact it is used to create the first (and 
 * unique) instance of the class, and it's used to refer to the same instance by each point in the program, and for
 * erase documents contained in it too
 */
public class DocumentSet {
	protected static DocumentSet ds = null;
	protected static LinkedList<DocumentFile> documentFiles = null;
	
	private DocumentSet() {
		
	}
	
	/* getDocumentSet
	 * This method is used to generate the unique instance of the class or to return it if it exists
	 * @return ds, the pointer to the only allowed class instance
	 */
	public static DocumentSet getDocumentSet() {
		if(ds == null) {
			ds = new DocumentSet();
			resetSet();
		}
		return ds;
	}
	
	/* resetSet
	 * Erase documents allocating a new LinkedList
	 */
	public static void resetSet() {
		documentFiles = new LinkedList<DocumentFile>();
	}
	
	/* addDocument
	 * This method is used to add a document to the list, document refers to our Document type
	 */
	public void addDocumentFile(DocumentFile doc) {
		documentFiles.add(doc);
	}
	
	/* getDocument
	 * Returns a document giving corresponding index
	 */
	public DocumentFile getDocumentFile(int index) {
		return documentFiles.get(index);
	}
	
	/* removeDocument
	 * This method removes a document from LinkedList, given its index
	 */
	public void removeDocumentFile(int index) {
		documentFiles.remove(index);
	}
	
	/* getSize()
	 * returns number of documents stored in the LinkedList
	 */
	public int getSize() {
		return documentFiles.size();
	}
	
}
