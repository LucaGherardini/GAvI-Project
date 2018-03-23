package irModels;

import java.util.ArrayList;
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
	 * query
	 * This method is the "engine" that make a Model working: it call parsing function on query string, using the
	 * same parameters (stemming and stopWordsRemoving) both for query parsing that for document text parsing, to
	 * ensure the same results, starting from the same text
	 * @return a list of the results
	 */
	public abstract /*LinkedList<Document>*/ void query(String query, boolean stemming, boolean stopWordsRemoving);
	
	/*
	 * parseQuery
	 * Given a query, each model has its own way to represent it (a Boolean model could, for example, recognize
	 * keywords, as AND/OR/NOT, then use it to answer to the query). This method uses TextOperation class to parse
	 * the query.
	 * @param stemming is used to activate "stemming" feature or not
	 * @param stopWordsRemoving is used to activate "stop words removing" feature or not
	 */
	public abstract Query parseQuery(String query, boolean stemming, boolean stopWordsRemoving);
	
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
	protected abstract LinkedList<Document> executeQuery(Query query, boolean stemming, boolean stopWordsRemoving);

	/*
	 * computeSimilarity
	 * This function will be called by executeQuery to compute the similarity (or relevance, or weight) of 
	 * each document for that specific query. Relevance is declared Object (because each model has its own way
	 * to compute similarity, for example boolean model has 0/1 weights, vector could have real 
	 * number weights, ...) so the correct type could be override by sub-classes.
	 * @param first Query object is the query parsed and stored in the suitable sub-class of Query
	 * @param Query textDocument is the document text, parsed and passed as a query to enable. Parsing is made by
	 * executeQuery to reduce parameters required by this method (on other side, textDocument would be a String
	 * and stemming and stopWordsRemoving boolean variables would be needed)
	 */
	protected abstract Object computeSimilarity(Query query, Query textDocument);
	
	
	
}
