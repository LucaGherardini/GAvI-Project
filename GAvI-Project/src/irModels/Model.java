package irModels;

import java.util.LinkedList;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.similarities.Similarity;

import index.Index;

/**
 * @author luca
 *
 * This class is the common ancestor of all Models implemented. The 
 */
public abstract class Model{
	
	/*
	 * query
	 * This method is a short call to method submitQuery of Index class. It allows a slim call
	 */
	public void query(String query, boolean stemming, boolean stopWordsRemoving, LinkedList<String> fields) {
		Index i = Index.getIndex();
		i.submitQuery(query, fields, this);
	}
	
	public abstract Query getQueryParsed(String query, LinkedList<String> fields, StandardAnalyzer analyzer);
	
	//public abstract ArrayList<String> parser(String query, boolean stemming, boolean stopWordsRemoving);
	
	/*
	 * parseQuery
	 * Given a query, each model has its own way to represent it (a Boolean model could, for example, recognize
	 * keywords, as AND/OR/NOT, then use it to answer to the query). This method uses TextOperation class to parse
	 * the query.
	 * @param stemming is used to activate "stemming" feature or not
	 * @param stopWordsRemoving is used to activate "stop words removing" feature or not
	 */
	//public abstract LinkedList<Query> parseQuery(String query, boolean stemming, boolean stopWordsRemoving, String[] fields, Analyzer stdAnalyzer);
	
	/*
	 * executeQuery
	 * Given the query string (parsed) and an ArrayList containing documents on which executing query, this 
	 * method will returns an array containing results (which order and number will depend on model).
	 * 
	 * to parse text of the documents, and to ensure a compatibility between query and text, the same parsing
	 * operations are made on both
	 * 
	 * the ranking function is assumed to be applied in this method (and it's a great thing, because, for example,
	 * boolean model doesn't have a ranking function (weights = {0,1}) so we don't declare an useless method in 
	 * this way
	 * 
	 * @return List of resulting documents
	 */
	//protected abstract LinkedList<Document> executeQuery(Query query, boolean stemming, boolean stopWordsRemoving);

	public abstract Similarity getSimilarity();
	
}
