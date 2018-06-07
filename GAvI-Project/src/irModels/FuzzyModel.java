package irModels;

import java.util.LinkedList;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
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
		
		Builder b = new BooleanQuery.Builder();
		/*
		 * To create a Fuzzy Query, we used a MultiFieldQueryParser, that allows more flexibility on query
		 * formulation (for example, specific multi fields on which search)
		 */
		//MultiFieldQueryParser queryParser = new MultiFieldQueryParser(fields.toArray(new String[fields.size()]), analyzer);
		//queryParser.setDefaultOperator(QueryParser.Operator.AND);	
		
		String[] terms = query.split(" ");
		query = "";
		for (String term : terms) {
			if( !term.contains("AND") && !term.contains("OR") && !term.contains("NOT")) {
				term = term.replaceAll("~", "") + "~" + maxEdits;
			}
			query += term + " ";
		}
		
		StandardQueryParser qp = new StandardQueryParser();
		for (String field : fields) {
			try {
				b.add(qp.parse(query, field), BooleanClause.Occur.SHOULD);
			} catch (QueryNodeException e) {
				e.printStackTrace();
			}
		}
        
		return b.build();
	}

	@Override
	public Similarity getSimilarity() {
		System.out.println("Creating a Fuzzy Model Similarity");
		return new ClassicSimilarity();
	}

}
