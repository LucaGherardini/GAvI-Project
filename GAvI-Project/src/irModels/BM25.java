package irModels;

import java.util.LinkedList;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.flexible.standard.StandardQueryParser;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.similarities.BM25Similarity;
import org.apache.lucene.search.similarities.Similarity;

public class BM25 extends Model {

	@Override
	public Query getQueryParsed(String query, LinkedList<String> fields, StandardAnalyzer analyzer) {
StandardQueryParser sqp = new StandardQueryParser(analyzer);
		
		Query q = null;
		String query_parsed = "";
		
		//This parsing is used to "pre-process" query string, removing stop words, doing stemming, ...
		try {
			query = sqp.parse(query, "").toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		//After parsing, all special character, added by query parser, are removed
		query = query.replaceAll("[()+-:]", "");
		query = query.trim().replaceAll(" +", " ");
		
		//Now, parsing is make on each field, creating the final query
		try {
			for(String field : fields) {
				query_parsed += sqp.parse(query, field).toString() + " ";
			}
			q = sqp.parse(query_parsed, "");
		} catch (Exception e) {
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
