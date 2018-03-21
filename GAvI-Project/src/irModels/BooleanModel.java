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
	
	public LinkedList<String> query(String query, boolean stemming, boolean stopWordsRemoving, LinkedList<String> documents){
		Query q = parseQuery(query, stemming, stopWordsRemoving);		
		return executeQuery(q, stemming, stopWordsRemoving, documents);
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
	protected LinkedList<String> executeQuery(Query query, boolean stemming, boolean stopWordsRemoving, LinkedList<String> documents){
		LinkedList<String> results = new LinkedList<String>();
		Query q = null;
		
		/*
		 * For each document, compute the similarity using the booleanQuery in computeSimilarity, adding it to
		 * the list of results if similar to the query
		 */
		for (String d : documents) {
			q = parseQuery(d, stemming, stopWordsRemoving);
			if(computeSimilarity(query, q)) {
				results.add(d);
			}
		}
		return results;
	}
	
	
	@Override
	protected Boolean computeSimilarity(Query booleanQuery, Query textDocument) {
		BooleanQuery bq = (BooleanQuery) booleanQuery;
		
		BooleanQuery parsedText = (BooleanQuery) textDocument;
		return bq.equals(parsedText);
	}
}