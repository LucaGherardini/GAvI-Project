package irModels;

import java.util.ArrayList;
import java.util.LinkedList;

import textOperation.TextOperations;
import org.apache.lucene.document.Document;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.BooleanQuery.Builder;
import org.apache.lucene.search.PhraseQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;

public class BooleanModel extends Model{
	
	public BooleanModel() {
		
	}
	
	@Override
	protected Query parseQuery(String query, boolean stemming, boolean stopWordsRemoving) {
		
		ArrayList<String> queryParsed = TextOperations.tokenization(query);
		if (stemming) {
			queryParsed = TextOperations.stemming(queryParsed);
		}
		if (stopWordsRemoving) {
			queryParsed = TextOperations.removeStopWords(queryParsed);
		}
		
		// TODO create a class "EditDistance" with a method called getDistance() that returns the number of
		// distance admittable to accept a boolean query
		
		/*
		 * Create the PhraseQuery with list of Terms to search, with a generic field (for now) and without
		 * slop value (for now) (slop = jumps between words admitted)
		 */
		System.out.println("queryParsed: " + queryParsed.toString());
		PhraseQuery pq = new PhraseQuery("title", queryParsed.toString());
		
		/*
		 * Create a Builder, add query to it and build a BooleanQuery
		 */
		Builder booleanQueryBuilder = new BooleanQuery.Builder();
			    booleanQueryBuilder.add(pq, BooleanClause.Occur.MUST);
		BooleanQuery booleanQuery = booleanQueryBuilder.build();
		
		return booleanQuery;
	}
	
	@Override
	protected LinkedList<Document> executeQuery(Query query, LinkedList<Document> documents){
		LinkedList<Document> results = new LinkedList<Document>();
		
		/*
		 * For each document, compute the similarity using the booleanQuery in computeSimilarity, adding it to
		 * the list of results if similar to the query
		 */
		for (Document d : documents) {
			if(computeSimilarity(query, d)) {
				results.add(d);
			}
		}
		return results;
	}
	
	@Override
	// TODO maybe document should be represented in another way, to be similar to query form
	protected Boolean computeSimilarity(Query booleanQuery, Document document) {
		BooleanQuery bq = (BooleanQuery) booleanQuery;
		String value = document.get("title");
		System.out.println("document: " + document.getField("title"));
		System.out.println("value: " + value);
		System.out.println("boolean query: " + bq.toString());
		return bq.equals(document.getValues("title"));
	}
}