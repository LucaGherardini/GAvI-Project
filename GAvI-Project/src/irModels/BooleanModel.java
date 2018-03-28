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
import org.apache.lucene.util.Version;

public class BooleanModel extends Model{
	
	public void query(String query, boolean stemming, boolean stopWordsRemoving, LinkedList<String> fields){
		Index i = Index.getIndex();
		i.submitQuery(query, fields, this);
	}
	
	public Query getQueryParsed(String query, LinkedList<String> fields, StandardAnalyzer analyzer) {
		
		StandardQueryParser queryParser = null;
		Query q = null;
		Builder finalQuery = new BooleanQuery.Builder();
		
		for(String field : fields) {
			queryParser = new StandardQueryParser(analyzer);
			try {
				q = queryParser.parse(query, field);
			} catch (QueryNodeException e) {
				// TODO Auto-generated catch block
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
	
	/*@Override
	public LinkedList<Query> parseQuery(String query, boolean stemming, boolean stopWordsRemoving, String[] fields, Analyzer stdAnalyzer) {
		
		//ArrayList<String> queryParsed = parser(query, stemming, stopWordsRemoving);
		LinkedList<Query> pQueries = new LinkedList<Query>();
		
		
		for (String field : fields) {
		parser = new QueryParser(field, stdAnalyzer);
		try {
			queries.add(parser.parse(query));
		} catch (ParseException e1) {
			e1.printStackTrace();
		}
	}
		
		PhraseQuery pq = null;
		Builder booleanQueryBuilder = null; 
		
		for (String field : fields) {
			booleanQueryBuilder = new BooleanQuery.Builder();
			//pq = new PhraseQuery(0, field, query);
		    booleanQueryBuilder.add(new TermQuery(new Term(field, query)), BooleanClause.Occur.MUST);
		    pQueries.add(booleanQueryBuilder.build());
		}
		
		//BooleanQuery booleanQuery = booleanQueryBuilder.build();
			try {
				queries.add(parser.parse(query));
			} catch (ParseException e1) {
				e1.printStackTrace();
			}
		
		// TODO create a class "EditDistance" with a method called getDistance() that returns the number of
		// distance admittable to accept a boolean query
		
		//for (String field : fields)
		//= new PhraseQuery(0, "name", queryParsed.toString());
		//MultiPhraseQuery mpQ;

		Builder booleanQueryBuilder = new BooleanQuery.Builder();
			    booleanQueryBuilder.add(pq, BooleanClause.Occur.MUST);
		BooleanQuery booleanQuery = booleanQueryBuilder.build();
		
		return pQueries;
	}
	*/
	
	/*@Override
	protected LinkedList<Document> executeQuery(Query query, boolean stemming, boolean stopWordsRemoving){
		LinkedList<Document> results;
		Query q = null;

		for (String d : documents) {
			q = parseQuery(d, stemming, stopWordsRemoving);
			if(computeSimilarity(query, q)) {
				results.add(d);
			}
		
		Index generalIndex = Index.getIndex();
		//generalIndex.submitQuery(q);
		//results = generalIndex.submitQuery(q);
		
		return null;
	}
	*/
	
	
	/*@Override
	protected Boolean computeSimilarity(Query booleanQuery, Query textDocument) {
		BooleanQuery bq = (BooleanQuery) booleanQuery;
		
		BooleanQuery parsedText = (BooleanQuery) textDocument;
		return bq.equals(parsedText);
	}
	*/
}