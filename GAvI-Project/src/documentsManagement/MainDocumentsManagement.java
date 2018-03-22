package documentsManagement;

public class MainDocumentsManagement {

	public static void main(String[] args) {
		DocumentSet dset = DocumentSet.getDocumentSet();
		
		DocumentFile df = new DocumentFile("README.md", "");
		dset.addDocumentFile(df);
	}
}
