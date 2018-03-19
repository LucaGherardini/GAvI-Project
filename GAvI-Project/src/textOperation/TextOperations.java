package textOperation;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.CharArraySet;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.tartarus.snowball.ext.PorterStemmer;

/**
 * Utility class for execute text operations.
 * This class provides:
 * 	Tokenization
 * 	Remove stopwords
 * 	Stemming
 * 
 * @author simone
 *
 */
public class TextOperations {
	
	public TextOperations() {
		
	}
	

	/**
	 * Tokenization of a text.
	 * 
	 * @param analyzer analyzer for stream
	 * @param content do tokenization on this
	 * @return list of tokens
	 */
	public ArrayList<String> tokenization(String content) {
		
		ArrayList<String> tokens = new ArrayList<String>(); //List of tokens to return
		
		//Set an empty stopWords set to prevent stopwords removing
		CharArraySet s = new CharArraySet(0, true);
		Analyzer analyzer = new StandardAnalyzer(s);
		
		//TokenStream enumerates the sequence of tokens
        TokenStream stream  = analyzer.tokenStream(null, new StringReader(content));
        
        //Take tokens from text and add it into arraylist
        try {
            stream.reset(); //This method is called by a consumer before it begins consumption using incrementToken()
            
            while(stream.incrementToken()) {
                tokens.add(stream.getAttribute(CharTermAttribute.class).toString());
            }
            stream.close();
        }
        catch(IOException e) {
            // not thrown 'cause we're using a string reader...
        }
		
		return tokens;
	}
	
	/**
	 * Remove stop words from tokens by using default stop set.
	 * 
	 * @param analyzer analyzer for stream
	 * @param content removing stopwords on this
	 * @return list of tokens without stopwords
	 */
	public ArrayList<String> removeStopWords(ArrayList<String> tokens) {
		
		ArrayList<String> result = new ArrayList<>();	//Result of removing
		CharArraySet stopWords = EnglishAnalyzer.getDefaultStopSet();	//Default stop words

		//For every tokens, this block check if it is a stopword and, in case, remove it
		for (String s: tokens) {
			if (!stopWords.contains(s))
				result.add(s);
		}
	    
	    return result;
	}
	
	/**
	 * Stemming of tokens
	 * 
	 * @param tokens list of tokens
	 * @return list of stemmed tokens
	 */
	public ArrayList<String> stemming(ArrayList<String> tokens) {
		
		ArrayList<String> result = new ArrayList<String>();	//Result of stemming
		PorterStemmer stemmer = new PorterStemmer();
		
		//This block do stem for every token and add it into result
		for (String s: tokens) {
			stemmer.setCurrent(s);
			if (stemmer.stem())
				result.add(stemmer.getCurrent());
		}
		
		return result;
	}
}
