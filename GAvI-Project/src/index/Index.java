package index;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
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
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;

import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.RAMDirectory;

import irModels.Model;

/*
 * This class implements an Index. This Index allows to being manipulated by user, 
 * who decide which Documents/Directories adding/removing to it
 * 
 * @warning documents will be split following their directory, so an index contains abstract
 * directories, each of them has a path, that is the same for all documents of that directory
 * erasing a "Directory" means erasing all documents with a specific pattern in their path field
 * 
 * TODO and if Index wouldn't be a static class but it contains a List of Index instances? So, 
 * managing directories would be easier (no need to recognize path of all documents)
 * 
 * Static approach:
 * 	- Intuitive and easy to represent (a static index)
 * 	- Maybe more complex procedures to remove directories (recognize of a path between all documents)
 * 
 * List approach:
 * 	- Easy to remove directories (an entry of the List is an index on a specific directory) but the
 * 		instance remains pending (removing an instance to the List doesn't deallocate it, the pointer
 * 		could be still there! Risk of working with indexes not contained in the general index)
 * 
 * 	- More tricky procedure to scan index (List contains indexes, for each index we must get document,
 * 		look for something, going to the next index in the List...)
 * 
 * 	- The "lone" instance after removing an Index from the list could allow a faster readding of the
 * 		index to the general index (but only if NOTHING has changed!), but this strategy requires
 * 		great attention to indexes that we are using and memory leak is possible (indexes are 
 *		stored dinamically on RAM!)
 * 
 */
public class Index {
	
	/*
	 * A singleton to have an unique index, reachable by each part of the program and equals for 
	 * everybody
	 */
	private static Index uniqueIndex = null;
	
	private static Analyzer stdAnalyzer = null; 
	private static Directory dirIndex = null;
	private static IndexWriterConfig iwConfig = null; 
	private static IndexWriter inWriter = null; 
	private static IndexReader inReader = null;
	private static IndexSearcher inSearcher = null;

	/*
	 * This is what makes a singleton... single!
	 */
	private Index() {
		startIndex();
	}
	
	/* getIndex
	 * This method makes the Index class a singleton, allocating uniqueIndex as the only instance of
	 * this class, and returning it
	 */
	public static Index getIndex() {
		if(uniqueIndex == null) {
			uniqueIndex = new Index();
		}
		return uniqueIndex;
	}
	
	/* eraseIndex
	 * This method remove references to the previous uniqueIndex and close tools. 
	 * Then it makes uniqueIndex to being a new Index, reallocating new tools
	 * This is the fastest and easiest way to "clear" totally an index from its entries
	 */
	protected void eraseIndex() {
		resetIndex();
		startIndex();
	}
	
	/* resetIndex
	 * This method close tools that are closable (analyzer and directory)
	 */
	private void resetIndex() {
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
	
	/* startIndex
	 * A method used to allocate all tools of the Index
	 */
	private void startIndex() {
		stdAnalyzer = new StandardAnalyzer();
		dirIndex = new RAMDirectory();
		iwConfig = new IndexWriterConfig(stdAnalyzer);
		
		try {
			inWriter = new IndexWriter(dirIndex, iwConfig);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		try {
			inReader = DirectoryReader.open(inWriter);
			inSearcher = new IndexSearcher(inReader);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/* addDocument
	 * This method is used to create and to add a document to the index
	 * @param path is the string containing path to the file
	 * @param name is the string containing name of the file
	 * path+name gives position of the file on filesystem, they are submitted separately to allow
	 * a slimmer creation of the Document
	 */
	public void addDocument(String path, String name) {
		Document doc = new Document();
		
		BufferedReader buffer = null;
		try{
			buffer = new BufferedReader(new FileReader(path+name));
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
			inSearcher = new IndexSearcher(inReader);
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
			inSearcher = new IndexSearcher(inReader);
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
	
	public void submitQuery(String query, String[] fields, Model m) {

		QueryParser parser = null;
		LinkedList<Query> queries = new LinkedList<Query>();
		
		for (String field : fields) {
			parser = new QueryParser(field, stdAnalyzer);
			try {
				queries.add(parser.parse(query));
			} catch (ParseException e1) {
				e1.printStackTrace();
			}
		}
		
		
		LinkedList<TopDocs> results = new LinkedList<TopDocs>();
		LinkedList<ScoreDoc[]> hits = new LinkedList<ScoreDoc[]>();
		
		System.out.println("Printing query: " + queries.toString() + "\n");
		
		System.out.println("Printing documents in index: ");
		for (int i = 0; i < getSize(); i++) {
			System.out.println("Document " + i + ": " + getDocument(i).get("name"));
		}
		
		/*try {
			inSearcher = new IndexSearcher(inReader);
		}catch(Exception e) {
			e.printStackTrace();
		}*/
		
		int totalHits = 0;
		
		for (Query q : queries) {
			try {
				results.add(inSearcher.search(q, 5));
				hits.add(results.get(results.size()-1).scoreDocs);
			} catch (IOException e) {
				e.printStackTrace();
			}
			totalHits += results.get(results.size()-1).totalHits;
		}
		
		System.out.println(totalHits + " total matching documents");
		
		Document doc;
		try {
			for (ScoreDoc[] k : hits) {
				for ( int i = 0; i<k.length ; i++) {
					doc = inSearcher.doc(k[i].doc);
					System.out.println("Document " + doc.get("path") + doc.get("name"));
					//Document doc = searcher.doc(hits[i].doc);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		//return null;
	}
}