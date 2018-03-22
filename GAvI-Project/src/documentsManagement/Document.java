package documentsManagement;

/*
 * This class implements our representation of a Document. A Document is a text file, characterized by a name (the
 * name of the representing file), by a path (path of file on disk)
 */
public class Document {
	protected final String name;
	protected final String path;
	
	public Document(String name, String path) {
		this.name = name;
		this.path = path;
	}
	
	public String getName() {
		return name;
	}
	
	public String getPath() {
		return path;
	}
}
