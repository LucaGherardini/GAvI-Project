package documentsManagement;

import java.io.IOException;
import java.util.LinkedList;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;

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
	protected static StandardAnalyzer stdAnalyzer = null; 
	protected static Directory dirIndex = null;
	protected static IndexWriterConfig iwConfig = null; 
	protected static IndexWriter indexW = null; 
	
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
		
		stdAnalyzer.close();
		try {
			dirIndex.close();
		} catch (IOException e) {
			e.printStackTrace();
		}		

		stdAnalyzer = new StandardAnalyzer();
		dirIndex = new RAMDirectory();
		iwConfig = new IndexWriterConfig(stdAnalyzer);
		try {
			indexW = new IndexWriter(dirIndex, iwConfig);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	/* addDocument
	 * This method is used to add a document to the list, document refers to our Document type
	 */
	public void addDocumentFile(DocumentFile doc) {
		documentFiles.add(doc);
		
		try {
			indexW.addDocument(doc.getDocument());
		} catch (IOException e) {
			e.printStackTrace();
		}
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
		//TODO add method to remove specific document to index
	}
	
	/* getSize()
	 * returns number of documents stored in the LinkedList
	 */
	public int getSize() {
		return documentFiles.size();
	}
	
}
