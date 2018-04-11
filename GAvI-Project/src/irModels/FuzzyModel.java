package irModels;

import java.util.LinkedList;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.BooleanQuery.Builder;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.similarities.ClassicSimilarity;
import org.apache.lucene.search.similarities.Similarity;

public class FuzzyModel extends Model {

	@Override
	public Query getQueryParsed(String query, LinkedList<String> fields, StandardAnalyzer analyzer) {
		
		//Query q = null;
		
		Builder b = new BooleanQuery.Builder();
		/*
		 * To create a Fuzzy Query, we used a MultiFieldQueryParser, that allows more flexibility on query
		 * formulation (for example, specific multi fields on which search)
		 */
		MultiFieldQueryParser queryParser = new MultiFieldQueryParser(fields.toArray(new String[fields.size()]), analyzer);
		queryParser.setDefaultOperator(QueryParser.Operator.AND);	
		
		String[] terms = query.split(" ");
        for (String term : terms){
            try {
				b.add(queryParser.parse(term.replaceAll("~", "") + "~"), BooleanClause.Occur.MUST);
			} catch (ParseException e) {
				e.printStackTrace();
			}
        }
        
        // Perform the search
		
		/*
		try {
			q = queryParser.parse(query);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		*/
		
		//return q;
        return b.build();
	}

	@Override
	public Similarity getSimilarity() {
		System.out.println("Creating a Fuzzy Model Similarity");
		return new ClassicSimilarity();
	}

}
