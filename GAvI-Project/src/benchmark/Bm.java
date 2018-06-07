package benchmark;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.LinkedList;

import org.apache.lucene.search.FuzzyQuery;

import index.Hit;
import index.Index;
import irModels.BooleanModel;
import irModels.FuzzyModel;
import irModels.Model;
import irModels.VectorSpaceModel;

public class Bm {
	
	Model model;
	String fileDocumentsPaths;
	String queryFile;
	String docExpected;
	ArrayList<LinkedList<String>> results;
	
	
	public Bm(Model model, String fileDocumentsPaths, String queryFile, String docExpected) {
		this.model = model;
		this.fileDocumentsPaths = fileDocumentsPaths;
		this.queryFile = queryFile;
		this.results = null;
		this.docExpected = docExpected;
	}
	
	public void executeBenchmark() {
		
		//Create index
				Index index = Index.getIndex();
				index.setSimilarity(model.getSimilarity(), false);
				//index.loadIndex(fileDocumentsPaths);
				
				//Fields on which query works 
				LinkedList<String> ll = new LinkedList<String>();
				ll.add("name");
				ll.add("content");
				
				//This will load with queries in dir queriesPath
				ArrayList<String> queries = new ArrayList<String>();
				
			try {
				File queryF = new File(queryFile);
				
				BufferedReader br = new BufferedReader(new FileReader(queryF));
				
				String line = "";
				String query = "";
				int query_num = 1;
				
				while ( (line=br.readLine()) != null) {
					if(line.length()>2) {
						query += line + " ";
					}
					if(line.endsWith("#")) {
						query = query.substring(0, query.length()-2);
						queries.add(query);
						System.out.println("Query #" + query_num + ": " + query);
						query_num++;
						query = "";
					}
				}
			}catch (Exception e) {
				e.printStackTrace();
			}
			
			
			System.out.println("Loading index with " + fileDocumentsPaths);
			
			try {
				File docF = new File(fileDocumentsPaths);
				BufferedReader br = new BufferedReader(new FileReader(docF));
				
				String docPath = "";
				
				while ( (docPath = br.readLine()) != null) {
					index.addDocument(docPath);
				}
				
			}catch(Exception e) {
				e.printStackTrace();
			}
			
			ArrayList<LinkedList<String>> documentsExpected = new ArrayList<LinkedList<String>>();
			LinkedList<String> rel = null;	
			String line = "";
			int query_num = 1;
			
			try {
				BufferedReader br = new BufferedReader(new FileReader(new File(docExpected)));
				
				while( (line=br.readLine()) != null) {
					rel = new LinkedList<String>();
					
					if (!line.contains("Refs") &&  !line.contains("Query") && line.length() > 0) {
						boolean terminator = false;
						while( !terminator ){ 
							String [] docs = line.split(" ");
						
							for (String doc : docs) {
								int num = Integer.parseInt(doc);
								if (num != -1) {
									rel.add(doc);
								} else {
									terminator = true;
									break;
								}
							}
							
							if(!terminator) {
								line = br.readLine();
							}
						}
						System.out.println("Documents expected for query " + query_num + ": " + rel.toString());
						query_num++;
						documentsExpected.add(rel);
					}
				}
				br.close();
			} catch (Exception e) {
				System.err.println(e);
			}			
			
			ArrayList<LinkedList<String>> documentsRetrieved = new ArrayList<LinkedList<String>>();
			LinkedList<String> results = null;
			LinkedList<Hit> indexResults = null;
			query_num = 1;
			
			for(String query : queries) {
				results = new LinkedList<String>();
				
				System.out.println("*****");
				System.out.println("Query " + query_num + " is: " + query);
				indexResults = index.submitQuery(query, ll, model, true);
				for(Hit indRes : indexResults) {
					results.add(indRes.getDocName().substring(0, indRes.getDocName().lastIndexOf(".")-1));
				}
				System.out.println("Results for query " + query_num + ": " + results.toString() + "\n*****");
				query_num++;
				documentsRetrieved.add(results);
			}
	}
	
	public static void main (String[] args) {
		Index generalIndex = Index.getIndex();
		Bm bench = new Bm(new BooleanModel(), "benchmarkDocs.ser", "benchmark/lisa/LISA.QUE", "benchmark/lisa/LISA.REL");
		bench.executeBenchmark();
	}
	

}
