package benchmark;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;

import org.apache.lucene.document.Document;

import index.Index;
import irModels.BooleanModel;
import irModels.Model;

public class MyBenchmark {

	Model model;
	String fileDocumentsPaths;
	String queriesPath;
	ArrayList<LinkedList<Document>> results;
	
	/**
	 * This class performs benchmark and save results.
	 * @param model model to use for performs benchmark
	 * @param fileDocumentsPaths every line of this textual file is the path of a document to add into index
	 * @param queriesPath this is the path of the directory that contains the queries
	 */
	public MyBenchmark(Model model, String fileDocumentsPaths, String queriesPath) {
		this.model = model;
		this.fileDocumentsPaths = fileDocumentsPaths;
		this.queriesPath = queriesPath;
		this.results = null;
	}
	
	/**
	 * Performs benchmark
	 */
	public void executeBenchmark() {
		
		//Create index
		Index index = Index.getIndex();
		index.setSimilarity(model.getSimilarity());
		index.loadIndex(fileDocumentsPaths);
		
		//Fields on which query works 
		LinkedList<String> ll = new LinkedList<>();
		ll.add("name");
		ll.add("content");
		
		//This will load with queries in dir queriesPath
		ArrayList<String> queries = new ArrayList<String>();
		
		//Loading of queries
		try {			
			
			File dir = new File(queriesPath);
			String[] files = dir.list();
			
			File f;
			FileReader fr = null;
			BufferedReader br = null;
			
			String line;
			String s = "";
			for (int i = 0; i < files.length; i++) {
				f = new File(queriesPath+files[i]);
				fr = new FileReader(f);
				br = new BufferedReader(fr);
				while ( (line = br.readLine()) != null) {
					s += line;
				}
				queries.add(s);
				s = "";
			}
			fr.close();
			br.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		/*
		 * Result of the benchmark.
		 * Every element of arraylist is the query and every element of
		 * linkedlist is a list of document that are rilevants for that query
		 * 
		 * E.g. results.get(3) contains a list of rilevants documents for query number 3
		 */
		results = new ArrayList<LinkedList<Document>>(); 
		
		//Execute every query on documents and load results
		for (String query: queries)
			results.add(index.submitQuery(query, ll, model));
	}
	
	/**
	 * Save results of benchmark on passed file.
	 * @param fileName name of file
	 */
	public void saveResults(String fileName) {
		
		if (results == null) {
			System.out.println("You must perform a benchmark first!");
			return;
		}
		
		/*
		 * Save results on a file.
		 * File will:
		 * "Relevants docs for query 3:
		 * 	1023.doc
		 * 	2032.doc
		 * 	Relevants docs for query 4:
		 * 	...
		 * 	...
		 * "
		 */
		try {
			File f = new File(fileName);
			FileWriter fw = new FileWriter(f);
			for (int i = 0; i < results.size(); i++) {
				fw.append("Relevants docs for query "+(i+1)+":\n");
				for (int j = 0; j < results.get(i).size(); j++) {
					fw.append(results.get(i).get(j).get("name")+"\n");
				}
			}
			fw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		
		//Use example
		MyBenchmark mb = new MyBenchmark(new BooleanModel(),"benchmarkDocs.ser","GAvI-Project/benchmark/lisa/Query/");
		mb.executeBenchmark();
		mb.saveResults("newRes.save");
	}
	
}
