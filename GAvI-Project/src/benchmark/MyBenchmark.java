package benchmark;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;

import org.apache.lucene.document.Document;

import index.Index;
import irModels.BM25;
import irModels.Model;

public class MyBenchmark {

	//
	Model model;
	String fileDocumentsPaths;
	String queriesPath;
	ArrayList<LinkedList<String>> results;
	
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
		 * E.g. results.get(3) contains a list of rilevants documents for query number 4
		 */
		results = new ArrayList<LinkedList<String>>(); 
		
		//Execute every query on documents and load results
		for (String query: queries) {
			LinkedList<String> docNames = new LinkedList<>();
			for (Document doc: index.submitQuery(query, ll, model)) {
				docNames.add(doc.get("name"));
			}
				results.add(docNames);
		}
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
					fw.append(results.get(i).get(j)+"\n");
				}
			}
			fw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Create and load a list of expected results.
	 * It works only for file formatted as LISA.REL
	 * Don't ask how it works
	 * @param fileResults file of expected results.
	 */
	public ArrayList<LinkedList<String>> realDocsRelevance(String fileResults) {
		ArrayList<LinkedList<String>> expected = new ArrayList<LinkedList<String>>();
		LinkedList<String> rel = new LinkedList<String>();
		String appo = "";
		try {
			File f = new File(fileResults);
			FileReader fr = new FileReader(f);
			BufferedReader br = new BufferedReader(fr);
			String line;
			int query_num = 1;
			int prev_num = 1;
			
			for (line = br.readLine(); line != null; line = br.readLine()) {
				if (line.contains("Query")) {
					query_num = Integer.parseInt(line.substring(line.indexOf(" ")+1, line.length()));
				}
				else if (line.contains("Refs"));
				else if (line.length() > 0) {
					appo = line.substring(0,line.indexOf(" "));
					if (prev_num != query_num) {
						expected.add(rel);
						rel = new LinkedList<>();
						prev_num = query_num;
					}
					if (!rel.contains(appo))
						rel.add(appo);
					String line2 = line.substring(0);
					while (appo.length() > 0 ) {
						if (!(line2.indexOf(" ") < 0)) {
							appo = line2.substring(0,line2.indexOf(" "));
							line2 = line2.substring(line2.indexOf(" ")+1,line2.length());
							if (!rel.contains(appo))
								rel.add(appo);
						}
						else if (!line2.equals("-1")) {
							rel.add(line2);
							line2 = "-1";
						}
						else
							break;
					}
				}
			}
			br.close();
		} catch (Exception e) {
			// TODO: handle exception
			System.err.println(e);
		}
		return expected;
	}

	/**
	 * Get intersection between relevance document from benchmark and 
	 * results of our system.
	 * @return intersection between relevance document from benchmark and results of our system.
	 */
	public ArrayList<LinkedList<String>> getIntersection() {
		ArrayList<LinkedList<String>> relevants = realDocsRelevance("GAvI-Project/benchmark/lisa/LISA.REL");
		ArrayList<LinkedList<String>> intersect = new ArrayList<LinkedList<String>>(); 
		
		for (int i = 0; i < Math.min(results.size(),relevants.size()); i++) {
			intersect.add(new LinkedList<String>());
			for (int j = 0; j < Math.min(results.get(i).size(), relevants.get(i).size()); j++) {
				if ( results.get(i).size() < relevants.get(i).size()) {
					if ( relevants.get(i).contains(results.get(i).get(j)))
						intersect.get(i).add(results.get(i).get(j));
				}
				else {
					if ( results.get(i).contains(relevants.get(i).get(j)))
						intersect.get(i).add(relevants.get(i).get(j));
				}	
			}
		}
		
		return intersect;
	}
	
	public static void main(String[] args) {
		
		//Use example
		MyBenchmark mb = new MyBenchmark(new BM25(),"benchmarkDocs.ser","GAvI-Project/benchmark/lisa/Query/");
		mb.executeBenchmark();
		
		mb.saveResults("resBM25.save");
		System.out.println(mb.realDocsRelevance("GAvI-Project/benchmark/lisa/LISA.REL"));
		System.out.println(mb.getIntersection());
	}
	
}
