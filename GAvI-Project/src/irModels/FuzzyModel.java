package irModels;

import java.util.LinkedList;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.flexible.core.QueryNodeException;
import org.apache.lucene.queryparser.flexible.standard.StandardQueryParser;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.BooleanQuery.Builder;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.similarities.ClassicSimilarity;
import org.apache.lucene.search.similarities.Similarity;

import gui.Main_Window;

public class FuzzyModel extends Model {

	@Override
	/*
	 * (non-Javadoc)
	 * @see irModels.Model#getQueryParsed(java.lang.String, java.util.LinkedList, org.apache.lucene.analysis.standard.StandardAnalyzer)
	 */
	public Query getQueryParsed(String query, LinkedList<String> fields, StandardAnalyzer analyzer) {
		
		int maxEdits = Main_Window.getEditdistance();
		Builder b = new BooleanQuery.Builder();		
		
		/*
		 * First parsing is to remove stopwords, make stemming, removing digits, ...
		 */
		StandardQueryParser queryParser = new StandardQueryParser(analyzer);
		try {
			query = queryParser.parse(query, "").toString();
		} catch (Exception e) {
			
		}
		
		//From the query parsed, all special letters used by query parsed are removed, and eventual exceeding white spaces are removed too
		query = query.replaceAll("[()+-:]", "");
		query = query.trim().replaceAll(" +", " "); // Removes first and last white spaces, and substitute multiple white spaces with only one white space
		
		String[] terms = query.split(" ");
		
		query = "";
		//For each "Token", this is followed by a ~ and a value representing maxEdits
		for (String term : terms) {
			term = term.replaceAll("~", "") + "~" + maxEdits;
			query += term + " ";
		}
		
		//Query is re-parsed, this time on each field, and built using a BooleanQuery.Builder
		String query_parsed = "";
		Query q = null;
		try {
			for (String field : fields) {
				query_parsed += queryParser.parse(query, field).toString() + " ";
			}
			q = queryParser.parse(query_parsed, "");
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
		return new ClassicSimilarity();
	}
}
