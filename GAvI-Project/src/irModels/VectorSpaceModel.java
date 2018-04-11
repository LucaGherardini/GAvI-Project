package irModels;

import org.apache.lucene.search.similarities.ClassicSimilarity;
import org.apache.lucene.search.similarities.Similarity;

public class VectorSpaceModel extends SimilarityModel{
	
	public Similarity getSimilarity() {
		System.out.println("Creating a Vector Space Model Similarity");
		return new ClassicSimilarity();
	}

}
