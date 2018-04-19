package benchmark;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;

import org.apache.lucene.document.Document;

import index.Hit;
import index.Index;
import irModels.BM25;
import irModels.Model;

public class MyBenchmark {

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
		LinkedList<String> ll = new LinkedList<String>();
		ll.add("name");
		ll.add("content");
		
		//This will load with queries in dir queriesPath
		ArrayList<String> queries = new ArrayList<String>();
		
		//Loading of queries
		try {			
			
			File dir = new File(queriesPath);
			String[] files = dir.list(); //TODO use this to create directory loading into index
			
			BufferedReader br = null;
			
			String line;
			String s = "";
			for (int i = 0; i < files.length; i++) {
				br = new BufferedReader(new FileReader(new File(queriesPath+files[i])));
				while ( (line = br.readLine()) != null) {
					s += line;
				}
				queries.add(s);
				s = "";
			}
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
		 * results is an ArrayList in which each element is a LinkedList of documents returned by index
		 * 
		 * E.g. results.get(3) contains a list of relevant documents for query number 4
		 */
		results = new ArrayList<LinkedList<String>>(); 
		System.out.println("Getting results from index");
		//Execute every query on documents and load results
		for (String query: queries) {
			LinkedList<String> docNames = new LinkedList<String>();
			for (Hit result: index.submitQuery(query, ll, model)) {
				String res = result.getDocName();
				int point = res.indexOf(".doc");
				docNames.add(res.substring(0, point-1));
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
		 * File will be(f.e.):
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
		String appo = "";
		BufferedReader br = null;		
		LinkedList<String> rel = null;
		
		try {
			br = new BufferedReader(new FileReader(new File(fileResults)));
			String line;
			int query_num = 1;
			int prev_num = -1;
			
			while( (line=br.readLine()) != null) {
				rel = new LinkedList<String>();
				
				if (line.contains("Query")) {
					query_num = Integer.parseInt(line.substring(line.indexOf(" ")+1, line.length()));
					System.out.println("Reading docs expected for query " + query_num);
				} else if (!line.contains("Refs") && line.length() > 0) {
					boolean terminator = false;
					while( !terminator ){ 
						String [] docs = line.split(" ");
						LinkedList<Integer> docNum = new LinkedList<Integer>();
					
						for (String doc : docs) {
							int num = Integer.parseInt(doc);
							if (num != -1) {
								docNum.add(num);
							} else {
								terminator = true;
							}
						}
					
						for (Integer k : docNum) {
							rel.add(k.toString());
						}
						if(!terminator) {
							line = br.readLine();
						}
					}
					
					expected.add(rel);
					
					
					/*
					appo = line.substring(0, line.indexOf(" "));
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
					*/
				}
			}
			br.close();
		} catch (Exception e) {
			System.err.println(e);
		}
		
		/*
		System.out.println("Checking that \"expected\" is load...");
		for (int q = 0; q < expected.size(); q++) {
			System.out.print(q + ": ");
			for (int r = 0; r < expected.get(q).size(); r++) {
				System.out.print(expected.get(q).get(r) + " | ");
			}
			System.out.println("");
		}
		*/
		
		return expected;
	}

	/**
	 * Get intersection between expected documents from benchmark and 
	 * results of our system.
	 * @return intersection between relevance document from benchmark and results of our system.
	 */
	public ArrayList<LinkedList<String>> getIntersection() {
		ArrayList<LinkedList<String>> relevants = realDocsRelevance("benchmark/lisa/LISA.REL");
		ArrayList<LinkedList<String>> intersect = new ArrayList<LinkedList<String>>(); 
		
		System.out.println("Results size: " + results.size());
		System.out.println("Relevants size: " + relevants.size());
		
		LinkedList<String> intersection = null;
		
		for (int query = 0 ; query < results.size(); query++) {
			System.out.println("Making matching on query " + query);
			intersection = new LinkedList<String>();
			System.out.println("res size " + results.get(query).size());
			for (int i = 0; i < results.get(query).size(); i++) {
					for (int j = 0; j < relevants.get(query).size(); j++) {
						if ( relevants.get(query).get(j).equals(results.get(query).get(i))) {
							intersection.add(relevants.get(query).get(j));
						}
					}
			}
			intersect.add(intersection);
		}
			/*
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
			*/
		
		return intersect;
	}
	
	public static void main(String[] args) {
		
		//Use example
		MyBenchmark mb = new MyBenchmark(new BM25(),"benchmarkDocs.ser","benchmark/lisa/Query/");
		mb.executeBenchmark();
		
		mb.saveResults("resBM25.save");
		
		System.out.println("Printing results for each query...");
		int queryNum = 1;
		
		for (LinkedList<String> queryResult : mb.realDocsRelevance("benchmark/lisa/LISA.REL")) {
			System.out.println("Query n° " + queryNum + " results: " + queryResult.toString());
			queryNum++;
		}
		
		queryNum = 1;
		System.out.println("Printing intersection between retrieved and expected documents...");
		for (LinkedList<String> intersection : mb.getIntersection()) {
			System.out.println("For Query " + queryNum + ", intersections: " + intersection.toString());
			queryNum++;
		}
	}
	
}
