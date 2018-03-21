package irModels;

import java.util.LinkedList;

import org.apache.lucene.document.Document;
import org.apache.lucene.search.Query;

/**
 * @author luca
 *
 *
 * @subclasses will be static classes, which could be used directly, without creating an instance (is useless 
 * creating an instance of something that doesn't need to have "own properties")
 */
public abstract class Model{

	/*
	 * parseQuery
	 * Given a query, each model has its own way to represent it (a Boolean model could, for example, recognize
	 * keywords, as AND/OR/NOT, then use it to answer to the query). This method uses TextOperation class to parse
	 * the query.
	 * @param stemming is used to activate "stemming" feature or not
	 * @param stopWordsRemoving is used to activate "stop words removing" feature or not
	 */
	protected abstract Query parseQuery(String query,boolean stemming, boolean stopWordsRemoving);
	
	/*
	 * executeQuery
	 * Given the query string (parsed) and an ArrayList containing documents on which executing query, this 
	 * method will returns an array containing results (which order and number will depend on model).
	 * 
	 * the ranking function is assumed to be applied in this method (and it's a great thing, because, for example,
	 * boolean model doesn't have a ranking function (weights = {0,1}) so we don't declare an useless method in 
	 * this way
	 * 
	 * @return List of resulting documents
	 */
	protected abstract LinkedList<Document> executeQuery(Query query, LinkedList<Document> documents);

	/*
	 * computeSimilarity
	 * This function will be called by executeQuery to compute the similarity (or relevance, or weight) of 
	 * each document for that specific query. Relevance is declared Object (because each model has its own way
	 * to compute similarity, for example boolean model has 0/1 weights, vector could have real 
	 * number weights, ...) so the correct type could be override by sub-classes.
	 */
	protected abstract Object computeSimilarity(Query query, Document Document);
	
	
	
}
