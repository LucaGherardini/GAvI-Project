package irModels;

import java.util.LinkedList;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.similarities.ClassicSimilarity;
import org.apache.lucene.search.similarities.Similarity;

public class VectorSpaceModel extends Model{

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
		System.out.println("Creating a Vector Space Model Similarity");
		return new ClassicSimilarity();
	}

}
