package irModels;

import java.util.LinkedList;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.queryparser.flexible.core.QueryNodeException;
import org.apache.lucene.queryparser.flexible.standard.StandardQueryParser;
import org.apache.lucene.queryparser.flexible.standard.config.StandardQueryConfigHandler.Operator;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.BooleanQuery.Builder;
import org.apache.lucene.search.FuzzyQuery;
import org.apache.lucene.search.MultiPhraseQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.similarities.BM25Similarity;
import org.apache.lucene.search.similarities.BooleanSimilarity;
import org.apache.lucene.search.similarities.ClassicSimilarity;
import org.apache.lucene.search.similarities.MultiSimilarity;
import org.apache.lucene.search.similarities.Similarity;
import org.apache.lucene.search.similarities.SimilarityBase;

public class FuzzyModel extends Model {

	@Override
	public Query getQueryParsed(String query, LinkedList<String> fields, StandardAnalyzer analyzer) {
		
		int maxEdits = 2;	// This will be replaced with something like maxEdits = getEditDistance();
		
		Builder b = new BooleanQuery.Builder();
		String[] terms = query.split(" "); // Splitting query terms to process it
		
		query = "";
		
		for (String term : terms) {
			// Logic operators are not modified by adding of ~
			if(!term.equals("AND") && !term.equals("OR") && !term.equals("NOT")) {
				term = term.replaceAll("~", "") + "~" + maxEdits;
			}
			query += term +" "; // Re-adding of terms to query string after addition of ~
		}
		//System.out.println("Query after pre-processing: " + query);
		
		/*
		for (String field : fields) {
			FuzzyQuery fq = new FuzzyQuery(new Term(field, query), 0);
			b.add(fq, BooleanClause.Occur.SHOULD);
		}
		*/
		
		//query = b.build().toString().replaceAll("~0", "");
		
		StandardQueryParser qp = new StandardQueryParser();
		//b = new Builder();
		
		for (String field : fields) {
			try {
				b.add(qp.parse(query, field), BooleanClause.Occur.SHOULD);
			} catch (QueryNodeException e) {
				e.printStackTrace();
			}
		}
		
		/*
		 * To create a Fuzzy Query, we used a MultiFieldQueryParser, that allows more flexibility on query
		 * formulation (for example, specific multi fields on which search)
		 */
		//MultiFieldQueryParser queryParser = new MultiFieldQueryParser(fields.toArray(new String[fields.size()]), analyzer);
		//queryParser.setDefaultOperator(QueryParser.Operator.AND);	
		
		/*
		StandardQueryParser queryParser = new StandardQueryParser(analyzer);
		System.out.println("QUERY: " + query);
		query = query.toLowerCase();
		System.out.println("query: " + query);
		String[] terms = query.split(" ");
		for (String field : fields) {
			for (String term : terms){
				try {
					//if(!term.equals("AND") && !term.equals("OR") && !term.equals("NOT")) {
						//b.add(queryParser.parse(term), BooleanClause.Occur.MUST);
						//b.add(queryParser.parse(term.replaceAll("~", "") + "~"), BooleanClause.Occur.MUST);
						term = term.replaceAll("~", "") + "~";
					/*}else if(term.equals("AND")) {
					queryParser.setDefaultOperator(Operator.AND);
					}else if(term.equals("OR")) {
						queryParser.setDefaultOperator(Operator.OR);
					}*/
					//q = queryParser.parse(term, field);
				/*} catch (ParseException e) {
					e.printStackTrace();
					System.err.println("Error in parsing query term: " + term);*/
				/*} catch (QueryNodeException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				b.add(q, BooleanClause.Occur.SHOULD);
			}
		}
		*/
        
		/*String processedQuery = "";
		String[] terms = query.split(" ");
		
		for (String term : terms) {
			processedQuery += term.replaceAll("~", "") + "~ ";
		}
		
		System.out.println("Processed query: " + processedQuery);
		
		try {
			q = queryParser.parse(processedQuery);
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
