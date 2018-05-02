package irModels;

import java.util.LinkedList;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.queryparser.flexible.core.QueryNodeException;
import org.apache.lucene.queryparser.flexible.standard.StandardQueryParser;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.BooleanQuery.Builder;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.similarities.BooleanSimilarity;
import org.apache.lucene.search.similarities.Similarity;

public class BooleanModel extends Model{
	
	public Query getQueryParsed(String query, LinkedList<String> fields, StandardAnalyzer analyzer) {
		
		StandardQueryParser queryParser = null;
		Query q = null;
		Builder finalQuery = new BooleanQuery.Builder();
		queryParser = new StandardQueryParser(analyzer);
		
		for(String field : fields) {
			try {
				q = queryParser.parse(query, field);
			} catch (QueryNodeException e) {
				e.printStackTrace();
			}
			
			/*
			 * Using all "MUST" occurs is equivalent to "AND" operator. Using SHOULD is equivalent to "OR" 
			 * operator between queries
			 */			
			finalQuery.add(q, BooleanClause.Occur.SHOULD); 
		}
		
		return finalQuery.build();
	}

	@Override
	public Similarity getSimilarity() {
		System.out.println("Creating a Boolean Model Similarity");
		return new BooleanSimilarity();
	}
	
	/*public ArrayList<String> parser(String query, boolean stemming, boolean stopWordsRemoving){
		ArrayList<String> queryParsed = TextOperations.tokenization(query);
		if (stemming) {
			queryParsed = TextOperations.stemming(queryParsed);
		}
		if (stopWordsRemoving) {
			queryParsed = TextOperations.removeStopWords(queryParsed);
		}
		return queryParsed;
	}
	*/
}