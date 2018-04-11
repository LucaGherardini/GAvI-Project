package index;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.util.LinkedList;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;

import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;

import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.queryparser.flexible.core.QueryNodeException;
import org.apache.lucene.queryparser.flexible.standard.StandardQueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.similarities.ClassicSimilarity;
import org.apache.lucene.search.similarities.Similarity;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;

import irModels.BM25;
import irModels.Model;
import irModels.VectorSpaceModel;

/*
 * This class implements an Index. This Index allows to being manipulated by user, 
 * who decide which Documents/Directories adding/removing to it
 * 
 * @warning documents will be split following their directory, so an index contains abstract
 * directories, each of them has a path, that is the same for all documents of that directory
 * erasing a "Directory" means erasing all documents with a specific pattern in their path field
 * 
 */
public class Index{

	/*
	 * A singleton to have an unique index, reachable by each part of the program and equals for 
	 * everybody
	 */
	private static Index uniqueIndex = null;
	
	private static StandardAnalyzer stdAnalyzer = null; 
	private static Directory dirIndex = null;
	private static IndexWriterConfig iwConfig = null; 
	private static IndexWriter inWriter = null; 
	private static IndexReader inReader = null;
	private static IndexSearcher inSearcher = null;
	private static Similarity simUsed = null;
	
	private Index() {
		startIndex();
	}
	
	/* getIndex
	 * This method makes the Index class a singleton, allocating uniqueIndex as the only instance of
	 * this class, and returning it
	 */
	public static Index getIndex() {
		return getIndex(new VectorSpaceModel().getSimilarity());
	}
	
	public static Index getIndex(Similarity sim) {
		if(uniqueIndex == null) {
			simUsed = sim;
			uniqueIndex = new Index();
		}
		return uniqueIndex;
	}
	
	/* startIndex
	 * A method used to allocate all tools of the Index
	 */
	private void startIndex() {
		stdAnalyzer = new StandardAnalyzer();
		dirIndex = new RAMDirectory();
		iwConfig = new IndexWriterConfig();
		iwConfig.setSimilarity(simUsed);
		
		try {
			inWriter = new IndexWriter(dirIndex, iwConfig);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		try {
			inReader = DirectoryReader.open(inWriter);
			inSearcher = new IndexSearcher(inReader);
			inSearcher.setSimilarity(simUsed);			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void setSimilarity(Similarity sim) {
		// TODO maybe we could auto-save current index, to load it after reset of Index
		simUsed = sim;
		resetIndex();
		// TODO maybe we could auto-load last index saved, to allow a better use of software
	}
	
	/* resetIndex
	 * This method remove references to the previous uniqueIndex and close tools. 
	 * Then it makes uniqueIndex to being a new Index, reallocating new tools
	 * This is the fastest and easiest way to "clear" totally an index from its entries
	 */	
	protected void resetIndex() {
		eraseIndex();
		startIndex();
	}
	
	/* eraseIndex
	 * This method close tools that are closable
	 */
	private void eraseIndex() {
		if(stdAnalyzer != null){
			stdAnalyzer.close();
		}
		if(dirIndex != null) {
			try {
				dirIndex.close();
			}catch(IOException e) {
				e.printStackTrace();
			}
		}
		if(inWriter != null) {
			try {
				inWriter.deleteAll();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	/* saveIndex 
	 * This method write all documents path to the target file, as clear text
	 */
	public void saveIndex(String saveFile) {
		if (getSize() == 0) {
			System.err.println("This index is empty, saving it is useless");
			return ;
		}
		
		PrintWriter fileWriter = null;
		
		try {
			fileWriter = new PrintWriter(saveFile);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.err.println("File " + saveFile + " doesn't exist");
		}
		
		for (int k = 0 ; k < this.getSize() ; k++) {
			fileWriter.println(getDocument(k).get("path") + getDocument(k).get("name"));
		}
		
		fileWriter.close();
		System.out.println("Saving successful to " + saveFile + "!");
	}
	
	public void loadIndex(String saveFile) {
		BufferedReader reader = null;
		
		try {
			reader = new BufferedReader(new FileReader(new File(saveFile)));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.err.println("File " + saveFile + " doesn't seem to exist, or some else error showed up. Loading aborted.");
			return ;
		}
		
		System.out.println("Erasing of the previous index...");
		resetIndex(); 
		
		String line = "";
		try {
			while ( (line = reader.readLine()) != null) {
				addDocument(line);
				System.out.println("Loaded in index " + line);
			}

			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
			return ;
		}
		
		System.out.println("Loading successful from " + saveFile + "!");
	}
	
	
	/* addDocument
	 * This method is used to create and to add a document to the index
	 * @param docPath is a concatenation of path and name of a document (for example "doc/Lucene.pdf")
	 */
	public void addDocument(String docPath) {
		Document doc = new Document();
		
		BufferedReader buffer = null;
		try{
			buffer = new BufferedReader(new FileReader(docPath));
		}catch(IOException e) {
			e.printStackTrace();
		}
		
		/*
		 * Reading of document and storing it in content String, using line String to check 
		 * consistency of each line read by BufferedReader
		 */
		String content = "";
		String line;
		try {
			while((line = buffer.readLine()) != null) {
				content += line + "\n";
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		//System.out.println("***Content read from "+ path + name + ": \n\n" + content + "\n");
		int separatorIndex = docPath.lastIndexOf("/");
		
		String path = "";
		
		/*
		 * Currently, relative path is used to add documents to index, so the "/" could be missing. In future, using
		 * gui, absolute path will be always declared, erasing this problem
		 */
		if (separatorIndex != -1) {
			path = docPath.substring(0, separatorIndex+1);
		}
		String name = docPath.substring(separatorIndex+1, docPath.length());
		/*
		 * Document properties are stored into Document type.
		 * @warning path field is not intended to be used for queries
		 */
		doc.add(new TextField("path", path, Field.Store.YES));
		doc.add(new TextField("name", name, Field.Store.YES));
		doc.add(new TextField("content", content, Field.Store.YES));
		
		try {
			inWriter.addDocument(doc);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		/*
		 * This updates comReader if index was modified (in this case, yes, because a new document is added)
		 */
		try {
			inReader = DirectoryReader.openIfChanged((DirectoryReader) inReader);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	/* getDocument
	 * Returns a document giving corresponding index
	 */
	public Document getDocument(int index) {
		Document doc = null;
		try {
			doc = inReader.document(index);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return doc;
	}
	
	/* removeDocument
	 * This method removes a document from the index, given its position into the index
	 */
	public void removeDocument(int index) {
		try {
			inWriter.tryDeleteDocument(inReader, index);
			inReader = DirectoryReader.openIfChanged((DirectoryReader)inReader);
		}catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	/* getSize()
	 * returns number of documents stored in the index
	 */
	public int getSize() {
		return inWriter.numDocs();
	}
	
	public void submitQuery(String query, LinkedList<String> fields, Model m) {
		
		if(getSize() == 0) {
			System.err.println("No documents in index!");
			return ;
		}
		
		Query q = m.getQueryParsed(query, fields, stdAnalyzer);
		
		TopDocs results = null;
		ScoreDoc[] hits = null;
		
		System.out.println("Printing query: " + q.toString() + "\n");
		
		System.out.println("Printing documents in index: ");
		for (int i = 0; i < getSize(); i++) {
			System.out.println("Document " + i + ": " + getDocument(i).get("path") + getDocument(i).get("name"));
		}
		System.out.println("\n");
		
		/* Updating of IndexSearcher only if a request is submitted. The only way to updating a searcher, is to
		 * creating a new searcher bounded to current reader. This is cheap if we already have a reader
		 * available
		 */		
		try {
			inSearcher = new IndexSearcher(inReader);
			inSearcher.setSimilarity(simUsed);
		}catch(Exception e) {
			e.printStackTrace();
			System.err.println("\nError updating IndexSearcher. Trying to continue...");
		}
		
		try {
			results = inSearcher.search(q, getSize());
			hits = results.scoreDocs;
		} catch (IOException e) {
			e.printStackTrace();
			System.err.println("\nSomething goes wrong with your query... Quitting...");
			return ;
		}
		
		System.out.println(results.totalHits + " total matching documents");
		
		Document doc = null;
		try {
			for (int k=0 ; k < hits.length ; k++) {
					doc = inSearcher.doc(hits[k].doc);
					System.out.println("Document " + doc.get("path") + doc.get("name") + " with score: " + hits[k].score);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}