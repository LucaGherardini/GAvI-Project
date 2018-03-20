package irModels;

import java.util.ArrayList;
import textOperation.TextOperations;
import org.apache.lucene.document.Document;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.BooleanQuery.Builder;
import org.apache.lucene.search.Query;

public class BooleanModel extends Model{
	
	@Override
	public Query parseQuery(String query, boolean stemming, boolean stopWordsRemoving) {
		
		ArrayList<String> queryParsed = TextOperations.tokenization(query);
		if (stemming) {
			queryParsed = TextOperations.stemming(queryParsed);
		}
		if (stopWordsRemoving) {
			queryParsed = TextOperations.removeStopWords(queryParsed);
		}
		
		/*
		 * Cast the queryParsed ArrayList<String> to a Query object
		 */
		Query q = (Query)queryParsed.clone();
		
		return q;
	}
	
	@Override
	public ArrayList<Document> executeQuery(Query query, ArrayList<Document> documents){
		ArrayList<Document> results = new ArrayList<Document>();
		
		/*
		 * Create a Builder, add query to it and build a BooleanQuery
		 */
		Builder booleanQueryBuilder = new BooleanQuery.Builder();
			    booleanQueryBuilder.add(query, BooleanClause.Occur.MUST);
		BooleanQuery booleanQuery = booleanQueryBuilder.build();
		
		/*
		 * For each document, compute the similarity using the booleanQuery in computeSimilarity, adding it to
		 * the list of results if similar to the query
		 */
		for (Document d : documents) {
			if(computeSimilarity(booleanQuery, d)) {
				results.add(d);
			}
		}
		return null;
	}
	
	@Override
	// TODO maybe document should be represented in another way, to be similar to query form
	public Boolean computeSimilarity(Query booleanQuery, Document document) {
		return booleanQuery.equals(document);
	}
}