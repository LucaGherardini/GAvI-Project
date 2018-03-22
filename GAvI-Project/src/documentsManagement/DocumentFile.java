package documentsManagement;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.document.Field;

/*
 * This class implements our representation of a Document. A Document is a text file, characterized by a name (the
 * name of the representing file), by a path (path of file on disk)
 */
public class DocumentFile {
	protected final String name;
	protected final String path;
	protected final Document doc;
	
	public DocumentFile(String name, String path) {
		this.name = name;
		this.path = path;
		
		doc = createDocument(name, path);
		
	}
	
	private Document createDocument(String name, String path) {
		
		/*
		 * Creation of a BufferedReader to read document
		 */
		BufferedReader buffer = null;
		try{
			buffer = new BufferedReader(new FileReader(path+name));
		}catch(IOException e) {
			e.printStackTrace();
		}
		
		/*
		 * Reading of document and storing in content String
		 */
		String content = "";
		String line;
		try {
			while((line = buffer.readLine()) != null) {
				content += line + "\n";
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		System.out.println("Content read from "+ path + name + ":\n" + content);
		Document doc = new Document();
		
		/*
		StandardAnalyzer analyzer = new StandardAnalyzer();
		Directory index = new RAMDirectory();
		IndexWriterConfig config = new IndexWriterConfig(analyzer);
		IndexWriter w = new IndexWriter(index, config);
		*/
		
		doc.add(new TextField("title", name, Field.Store.YES));
		doc.add(new TextField("body", content, Field.Store.YES));
		
		return doc;
	}
	
	public String getName() {
		return name;
	}
	
	public String getPath() {
		return path;
	}
	
	public Document getDocument() {
		return doc;
	}
}
