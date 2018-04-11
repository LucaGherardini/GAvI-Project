package index;

import irModels.BM25;
import irModels.BooleanModel;
import irModels.FuzzyModel;
import irModels.VectorSpaceModel;
import textOperation.TextOperations;

import java.util.LinkedList;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexableField;
import org.apache.lucene.search.Query;

public class MainIndex {

	public static void main(String[] args) {		

		Index generalIndex = Index.getIndex();
		
		/*
		generalIndex.addDocument("README.md");
		generalIndex.addDocument("other/Lucene Useful Links.txt");
		generalIndex.addDocument("other/test.txt");
		generalIndex.addDocument("other/GenericTextFile.txt");
		generalIndex.addDocument("other/USA.txt");
		generalIndex.saveIndex("savedIndex.ser");
		*/		
		
		BooleanModel bm = new BooleanModel();
		generalIndex.setSimilarity(bm.getSimilarity());
		generalIndex.loadIndex("savedIndex.ser");
		
		String query = "United States Park";
		/*
		 * Fields on which query will work, passed to inform index in which fields it must search
		 */
		LinkedList<String> fields = new LinkedList<String>();// = {"name", "content"};
		
		
		if(query.contains("name")) {
			fields.add("name");
		}
		
		if(query.contains("content")) {
			fields.add("content");
		}
		
		if(fields.size() == 0) {
			fields.add("name");
			fields.add("content");
		}

		generalIndex.submitQuery(query, fields, bm);
		
		System.out.println("************************************************");
		
		/*
		 * Try query "United States Park" and query "United States": same results, different ranking!
		 */
		//query = "Parks in united states";
		/*
		fields = new LinkedList<String>();// = {"name", "content"};
		
		if(query.contains("name")) {
			fields.add("name");
		}
		
		if(query.contains("content")) {
			fields.add("content");
		}
		
		if(fields.size() == 0) {
			fields.add("name");
			fields.add("content");
		}
		*/
		//query = "Unit~ Stat~ Par~";
		
		FuzzyModel fm = new FuzzyModel();
		generalIndex.setSimilarity(fm.getSimilarity());
		generalIndex.loadIndex("savedIndex.ser");
		
		generalIndex.submitQuery(query, fields, fm);
		
		System.out.println("************************************************");
		
		System.err.println("Warning, starting with testing of VectorSpaceModel");
		
		VectorSpaceModel vsm = new VectorSpaceModel();
		
		generalIndex.setSimilarity(vsm.getSimilarity());
		generalIndex.loadIndex("savedIndex.ser");
		
		generalIndex.submitQuery(query, fields, vsm);
		
		System.out.println("************************************************");
		
		System.err.println("Warning, starting with testing of BM25");
		
		BM25 bm25 = new BM25();
		
		generalIndex.setSimilarity(bm25.getSimilarity());
		generalIndex.loadIndex("savedIndex.ser");
		generalIndex.submitQuery(query, fields, bm25);
	}
}
