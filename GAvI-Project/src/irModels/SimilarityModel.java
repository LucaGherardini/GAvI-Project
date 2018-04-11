package irModels;

import org.apache.lucene.search.similarities.Similarity;

public abstract class SimilarityModel {

	public abstract Similarity getSimilarity();
}
