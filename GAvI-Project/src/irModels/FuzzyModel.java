package irModels;

import java.util.LinkedList;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.Query;

import index.Index;

public class FuzzyModel extends Model {

	@Override
	public void query(String query, boolean stemming, boolean stopWordsRemoving, LinkedList<String> fields) {
		Index i = Index.getIndex();
		i.submitQuery(query, fields, this);
	}

	@Override
	public Query getQueryParsed(String query, LinkedList<String> fields, StandardAnalyzer analyzer) {
		
		Query q = null;
		
		/*
		 * To create a Fuzzy Query, we used a MultiFieldQueryParser, that allows more flexibility on query
		 * formulation (for example, specific multi fields on which search)
		 */
		MultiFieldQueryParser queryParser = new MultiFieldQueryParser(fields.toArray(new String[fields.size()]), analyzer);
			try {
				q = queryParser.parse(query);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		return q;
	}

}
