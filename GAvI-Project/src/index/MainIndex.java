package index;

import irModels.BM25;
import irModels.BooleanModel;
import irModels.FuzzyModel;
import irModels.VectorSpaceModel;
import java.util.LinkedList;

public class MainIndex {

	public static void main(String[] args) {	
		
		BooleanModel bm = new BooleanModel();
		Index generalIndex = Index.getIndex(bm.getSimilarity());
		LinkedList<Hit> results;		
		
		generalIndex.addDocument("README.md");
		generalIndex.addDocument("other/Lucene Useful Links.txt");
		generalIndex.addDocument("other/test.txt");
		generalIndex.addDocument("other/GenericTextFile.txt");
		generalIndex.addDocument("other/USA.txt");
		
		
		/*
		 * Try query "United States Park" and query "United States": same results, different ranking!
		 */
		String query = "I'm looking for Parks in united states AND Lucene documents";
		//String query = "American States";
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

		results = generalIndex.submitQuery(query, fields, bm, true);		
		
		for (Hit res : results) {
			System.out.println(res.getDocPath() + res.getDocName() + ": " + res.getScore());
		}
		
		System.out.println("************************************************");
		
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
		
		FuzzyModel fm = new FuzzyModel();
		
		generalIndex.setSimilarity(fm.getSimilarity(), true);
		
		results = generalIndex.submitQuery(query, fields, fm, true);
		
		for (Hit res : results) {
			System.out.println(res.getDocPath() + res.getDocName() + ": " + res.getScore());
		}
		
		System.out.println("************************************************");
		
		VectorSpaceModel vsm = new VectorSpaceModel();
		
		generalIndex.setSimilarity(vsm.getSimilarity(), true);
		
		results = generalIndex.submitQuery(query, fields, vsm, true);
		
		for (Hit res : results) {
			System.out.println(res.getDocPath() + res.getDocName() + ": " + res.getScore());
		}
		
		System.out.println("************************************************");
		
		BM25 bm25 = new BM25();
		
		generalIndex.setSimilarity(bm25.getSimilarity(), true);
		
		results = generalIndex.submitQuery(query, fields, bm25, true);
		
		for (Hit res : results) {
			System.out.println(res.getDocPath() + res.getDocName() + ": " + res.getScore());
		}
	}
}
