package irModels;

import org.apache.lucene.search.similarities.BM25Similarity;
import org.apache.lucene.search.similarities.Similarity;

public class BM25 extends SimilarityModel {
	
	public Similarity getSimilarity() {
		System.out.println("Creating a BM25 Similarity");
		return new BM25Similarity(); 	// BM25 with these default values: k1 = 1.2 b = 0.75
										// BM25Similarity(float k1, float b)
	}

}
