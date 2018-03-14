package src;

import java.io.IOException;
import org.apache.lucene.analysis.*;
import org.apache.lucene.analysis.standard.*;
import org.apache.lucene.document.*;
import org.apache.lucene.index.*;
import org.apache.lucene.queryparser.classic.*;
import org.apache.lucene.search.*;
import org.apache.lucene.store.*;

/*
 * This simple piece of code is a test to ensure you have all well configured to work with Lucene
 */

public class LuceneTest{

	public static void main(String args[]){
		System.out.println("Starting with the test");
		
		System.out.println("Creating a document");
		Document doc = new Document();
		doc.add(new TextField("title", "Lucene in action", Field.Store.YES));
		doc.add(new StringField("isbn", "193398817" , Field.Store.YES));
		
		System.out.println("Creating an analyzer and an associated directory");
		Analyzer analyzer = new StandardAnalyzer();		
		Directory index = new RAMDirectory();
		
		System.out.println("Building an index");
		IndexWriterConfig config = new IndexWriterConfig(analyzer);
		IndexWriter w = null;
		
		try{
		w = new IndexWriter(index, config);
		}catch(IOException e) {
			e.printStackTrace();
		}
		
		System.out.println("Adding an example document to the index");
		try {
		w.addDocument(doc);
		w.close();
		}catch(IOException e) {
			e.printStackTrace();
		}
		
		System.out.println("Building and launching a mock query");
		QueryParser parser=new QueryParser("test", analyzer);
		
		try {
		Query q = parser.parse("test");
		}catch(ParseException e) {
			e.printStackTrace();
		}
		
		System.out.println("Reading a directory and searching in it");
		DirectoryReader reader = null;
		try {
		reader = DirectoryReader.open(index);
		}catch(IOException e) {
			e.printStackTrace();
		}
		IndexSearcher searcher = new IndexSearcher(reader);
		System.out.println("Test finished, if no errors showed up, you're ok!");
	}
}