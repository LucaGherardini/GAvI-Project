package irModels;


import java.util.LinkedList;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.queryparser.flexible.standard.StandardQueryParser;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.similarities.ClassicSimilarity;
import org.apache.lucene.search.similarities.Similarity;

import gui.Main_Window;


public class VectorSpaceModel extends Model{

	@Override
	/*
	 * (non-Javadoc)
	 * @see irModels.Model#getQueryParsed(java.lang.String, java.util.LinkedList, org.apache.lucene.analysis.standard.StandardAnalyzer)
	 */
	public Query getQueryParsed(String query, LinkedList<String> fields, StandardAnalyzer analyzer) {

		int maxEdits = Main_Window.getEditdistance();
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
		
		String[] terms = query.split(" ");
		
		query = "";
		//For each "Token", this is followed by a ~ and a value representing maxEdits
		for (String term : terms) {
			term = term.replaceAll("~", "") + "~" + maxEdits;
			query += term + " ";
		}
		
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
	
	@Override
	/*
	 * (non-Javadoc)
	 * @see irModels.Model#getSimilarity()
	 */
	public Similarity getSimilarity() {
		//Classic similarity is tf-idf similarity
		return new ClassicSimilarity();
	}

}
