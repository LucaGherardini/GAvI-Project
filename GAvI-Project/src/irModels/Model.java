package irModels;

import java.util.LinkedList;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.similarities.Similarity;

import index.Index;

/**
 * @author luca
 *
 * This class is the common ancestor of all Models implemented.
 */
public abstract class Model{
	
	/**
	 * query(String query,LinkedList<String> fields, boolean verbose)
	 * This method is a short call to method submitQuery of Index class. It allows a slim call, passing
	 * model to use (itself), fields on which apply the query, query string and verbose, that toggle if index
	 * has to print the parsed query and the results on terminal.
	 */
	public void query(String query, LinkedList<String> fields, boolean verbose) {
		Index i = Index.getIndex();
		i.submitQuery(query, fields, this, verbose);
	}
	
	/**
	 * getQueryParsed(String query, LinkedList<String> fields, StandardAnalyzer analyzer)
	 * This is how a model process query string to obtain a Query object suitable to its structure.
	 */
	public abstract Query getQueryParsed(String query, LinkedList<String> fields, StandardAnalyzer analyzer);
	
	/**
	 * getSimilarity()
	 * This is a way to provide to the index the similarity to use for a particular model. This element influences
	 * ranking function
	 */
	public abstract Similarity getSimilarity();
	
}
