package index;

public class Hit {
	private String docPath;
	private String docName;  
    private float score;  
    
    Hit(String docPath, String docName, float score){ 
    	this.docPath = docPath;
        this.docName = docName;
        this.score = score;
    }  
    
    public String getDocPath() {
    	return docPath;
    }
    
    public String getDocName() {
    	return docName;
    }
    
    public float getScore() {
    	return score;
    }
}
