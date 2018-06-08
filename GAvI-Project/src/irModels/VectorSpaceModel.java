package irModels;

import java.util.LinkedList;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.flexible.core.QueryNodeException;
import org.apache.lucene.queryparser.flexible.standard.StandardQueryParser;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.similarities.ClassicSimilarity;
import org.apache.lucene.search.similarities.Similarity;

public class VectorSpaceModel extends Model{

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
		System.out.println("Creating a Vector Space Model Similarity");
		return new ClassicSimilarity();
	}

}
