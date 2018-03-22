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
	protected LinkedList<Document> documents = null;
	
	private DocumentSet() {
		
	}
	
	/* getDocumentSet
	 * This method is used to generate the unique instance of the class or to return it if it exists
	 * @return ds, the pointer to the only allowed class instance
	 */
	public DocumentSet getDocumentSet() {
		if(ds == null) {
			ds = new DocumentSet();
			resetSet();
		}
		return ds;
	}
	
	/* resetSet
	 * Erase documents allocating a new LinkedList
	 */
	public void resetSet() {
		documents = new LinkedList<Document>();
	}
	
	/* addDocument
	 * This method is used to add a document to the list, document refers to our Document type
	 */
	public void addDocument(Document doc) {
		documents.add(doc);
	}
	
	/* getDocument
	 * Returns a document giving corresponding index
	 */
	public Document getDocument(int index) {
		return documents.get(index);
	}
	
	/* removeDocument
	 * This method removes a document from LinkedList, given its index
	 */
	public void removeDocument(int index) {
		documents.remove(index);
	}
	
	/* getSize()
	 * returns number of documents stored in the LinkedList
	 */
	public int getSize() {
		return documents.size();
	}
	
}
