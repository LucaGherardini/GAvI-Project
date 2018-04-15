package irModels;

import java.util.LinkedList;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.similarities.BM25Similarity;
import org.apache.lucene.search.similarities.Similarity;

public class BM25 extends Model {

	@Override
	public Query getQueryParsed(String query, LinkedList<String> fields, StandardAnalyzer analyzer) {
		MultiFieldQueryParser queryParser = new MultiFieldQueryParser(fields.toArray(new String[fields.size()]), analyzer);
		Query q = null;
		
		try {
			q = queryParser.parse(query);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return q;
	}
	
	
	public Similarity getSimilarity() {
		System.out.println("Creating a BM25 Similarity");
		return new BM25Similarity(); 	// BM25 with these default values: k1 = 1.2 b = 0.75
										// BM25Similarity(float k1, float b)
	}

}
