package irModels;

import java.util.ArrayList;
import java.util.LinkedList;
import textOperation.TextOperations;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.queryparser.flexible.core.QueryNodeException;
import org.apache.lucene.queryparser.flexible.standard.StandardQueryParser;
import org.apache.lucene.queryparser.xml.builders.BooleanQueryBuilder;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.BooleanQuery.Builder;
import org.apache.lucene.search.MultiPhraseQuery;

import index.Index;

import org.apache.lucene.search.PhraseQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.similarities.BM25Similarity;
import org.apache.lucene.search.similarities.Similarity;
import org.apache.lucene.search.similarities.TFIDFSimilarity;
import org.apache.lucene.util.Version;

public class BooleanModel extends Model{
	
	public Query getQueryParsed(String query, LinkedList<String> fields, StandardAnalyzer analyzer) {
		
		StandardQueryParser queryParser = null;
		Query q = null;
		Builder finalQuery = new BooleanQuery.Builder();
		
		for(String field : fields) {
			queryParser = new StandardQueryParser(analyzer);
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
		/* By default, Fuzzy and Boolean Model doesn't change similarity */
		return null;
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