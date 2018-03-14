/**
 * 
 */
package irModels;

import java.util.ArrayList;

/**
 * @author luca
 * @param <E> is an abstract type, it will be replaced with the type Document (when it will be defined)
 *
 *
 * @subclasses will be static class, which could be used directly, without creating an instance (is useless 
 * creating an instance of something that doesn't need to have "own properties")
 */
public abstract class Model<E> {
	
	/*
	 * parseQuery
	 * Given a query, each model has its own way to represent it (a Boolean model could, for example, recognize
	 * keywords, as AND/OR/NOT, then use it to answer to the query). In what the query string will be transformed
	 * is yet to define (for now, Object is used to represent it)
	 */
	public abstract Object parseQuery(String query);
	
	/*
	 * executeQuery
	 * Given the query string and an ArrayList containing documents on which executing query, this method will
	 * return an array containing results (which order and number will depend on model)
	 * In this method we could expect that, for each document, will be provided a relevance value
	 * 
	 * the ranking function is assumed to be applied in this method (and is a great thing, because, for example,
	 * boolean model doesn't have ranking functions (weights 0/1) so we don't declare an useless method in this
	 * way
	 * 
	 * @param As seen for parseQuery, we could expect that String will not be a suitable type to store a query
	 * after parsing by the Model. 
	 */
	public abstract ArrayList<E> executeQuery(Object query, ArrayList<E> documents);

	/*
	 * computeSimilarity
	 * This function will be called by executeQuery to compute the similarity (or relevance, or weight) of 
	 * each document for that specific query. Relevance is declared float, but probably it will be changed to 
	 * Object type too (boolean model has 0/1 weights, vector could have real number weights, ...) so the
	 * correct type could be override by sub-classes.
	 */
	public abstract float computeSimilarity(Object query, Object Document);
	
	
	
}
