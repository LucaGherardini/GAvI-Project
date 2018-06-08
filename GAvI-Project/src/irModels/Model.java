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
		i.submitQuery(query, fields, this, true);
	}
	
	public abstract Query getQueryParsed(String query, LinkedList<String> fields, StandardAnalyzer analyzer);
	
	
	public abstract Similarity getSimilarity();
	
}
