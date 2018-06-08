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
	public Query getQueryParsed(String query, LinkedList<String> fields, StandardAnalyzer analyzer) {
		
		int maxEdits = Main_Window.getEditdistance(); //This will be replaced with something like maxEdits = getEditDistance()
		maxEdits = 2;
		Builder b = new BooleanQuery.Builder();
		/*
		 * To create a Fuzzy Query, we used a MultiFieldQueryParser, that allows more flexibility on query
		 * formulation (for example, specific multi fields on which search)
		 */
		//MultiFieldQueryParser queryParser = new MultiFieldQueryParser(fields.toArray(new String[fields.size()]), analyzer);
		//queryParser.setDefaultOperator(QueryParser.Operator.AND);	
		
		
		StandardQueryParser queryParser = new StandardQueryParser(analyzer);
		try {
			query = queryParser.parse(query, "").toString();
		} catch (Exception e) {
			
		}
		query = query.replaceAll("[(),.+-:'-_]", "");
		query = query.trim().replaceAll(" +", " "); // Removes first and last white spaces, and substitute multiple white spaces with only one white space
		System.out.println("Query parsed: " + query);
		
		String[] terms = query.split(" ");
		
		query = "";
		for (String term : terms) {
			term = term.replaceAll("~", "") + "~" + maxEdits;
			query += term + " ";
		}
		
		//TODO cerca di cambiare questo in un MultiFieldQueryParser
		//MultiFieldQueryParser qp = new MultiFieldQueryParser(fields.toArray(new String[fields.size()]), analyzer);
		StandardQueryParser qp = new StandardQueryParser(analyzer);
		Query q = null;
		for (String field : fields) {
			try {
				b.add(qp.parse(query, field), BooleanClause.Occur.SHOULD);
				//q = qp.parse(query);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
        
		//return q;
		return b.build();
	}

	@Override
	public Similarity getSimilarity() {
		System.out.println("Creating a Fuzzy Model Similarity");
		return new ClassicSimilarity();
	}

}
