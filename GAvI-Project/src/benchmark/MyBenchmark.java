package benchmark;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Scanner;

import index.Hit;
import index.Index;
import irModels.FuzzyModel;
import irModels.Model;
import irModels.VectorSpaceModel;

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
		index.setSimilarity(model.getSimilarity(), false);
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
			int query_num = 1;
			for (int i = 0; i < files.length; i++) {
				String s = "";
				br = new BufferedReader(new FileReader(new File(queriesPath+(i+1)+".que")));
				while ( (line = br.readLine()) != null) {
					s += line;
				}
				queries.add(s);
				System.out.println("Query collected #" + query_num + ": " + s);
				query_num++;
			}
			br.close();
		} catch (Exception e) {
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
		int query_num = 1;
		//Execute every query on documents and load results
		for (String query: queries) {
			LinkedList<String> docNames = new LinkedList<String>();
			for (Hit result: index.submitQuery(query, ll, model, true)) {
				String res = result.getDocName();
				int point = res.indexOf(".doc");
				docNames.add(res.substring(0, point-1));
				System.out.print(res.substring(0, point-1) + ", ");
			}
			System.out.print("Printing results obtained from index for query " + query_num + ": " + docNames);
			query_num++;
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
			e.printStackTrace();
		}
	}
	
	/**
	 * Create and load a list of expected results.
	 * It works only for file formatted as LISA.REL
	 * @param fileResults file of expected results.
	 */
	public ArrayList<LinkedList<String>> expectedDocsRelevance(String fileResults) {
		ArrayList<LinkedList<String>> expected = new ArrayList<LinkedList<String>>();	
		BufferedReader br = null;		
		LinkedList<String> rel = null;
		
		try {
			br = new BufferedReader(new FileReader(new File(fileResults)));
			String line;
			int query_num = 1;
			
			while( (line=br.readLine()) != null) {
				rel = new LinkedList<String>();
				
				if (!line.contains("Refs") &&  !line.contains("Query") && line.length() > 0) {
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
								break;
							}
						}
					
						for (Integer k : docNum) {
							rel.add(k.toString());
						}
						if(!terminator) {
							line = br.readLine();
						}
					}
					System.out.println("Documents expected for query " + query_num + ": " + rel.toString());
					query_num++;
					expected.add(rel);
				}
			}
			br.close();
		} catch (Exception e) {
			System.err.println(e);
		}
		
		return expected;
	}

	/**
	 * Get intersection between expected documents from benchmark and 
	 * results of our system.
	 * @return intersection between relevance document from benchmark and results of our system.
	 */
	public ArrayList<LinkedList<String>> getIntersection(String relevance) {
		ArrayList<LinkedList<String>> relevants = expectedDocsRelevance(relevance+"benchmark/lisa/LISA.REL");
		ArrayList<LinkedList<String>> intersect = new ArrayList<LinkedList<String>>(); 
		
		System.out.println("Results size: " + results.size());
		System.out.println("Relevants size: " + relevants.size());
		
		LinkedList<String> intersection = null;
		
		for (int query = 0 ; query < results.size(); query++) {
			System.out.println("Making matching on query " + query);
			intersection = new LinkedList<String>();
			for (int i = 0; i < results.get(query).size(); i++) {
					for (int j = 0; j < relevants.get(query).size(); j++) {
						if ( relevants.get(query).get(j).equals(results.get(query).get(i))) {
							if (!intersection.contains(relevants.get(query).get(j))){
								intersection.add(relevants.get(query).get(j));
							}
							relevants.get(query).remove(relevants.get(query).get(j));
							results.get(query).remove(results.get(query).get(i));
						}
					}
			}
			intersect.add(intersection);
		}
		return intersect;
	}
	
	public ArrayList<Double> getRecall(ArrayList<LinkedList<String>> intersection, ArrayList<LinkedList<String>> relevants) {
		ArrayList<Double> recall = new ArrayList<Double>();
		//List of size of each intersection for each query
		for (LinkedList<String> inter: intersection)
			recall.add(inter.size()+0.0);
		//For each query get intersection.size / relevants.size
			//recall = |intersect|/|relevants|
		for (int i = 0; i < relevants.size(); i++)
			if (relevants.get(i).size() != 0)
				recall.set(i, recall.get(i)/relevants.get(i).size());
			else 
				recall.set(i, 0.0);
		return recall;
	}
	
	public ArrayList<Double> getPrecision(ArrayList<LinkedList<String>> intersection) {
		ArrayList<Double> precision = new ArrayList<Double>();
		//List of size of each intersection for each query
		for (LinkedList<String> inter: intersection) {
			precision.add(inter.size()+0.0);
		}
		//For each query get intersection.size / relevants.size
			//precision = |intersect|/|result|
		for (int i = 0; i < results.size(); i++)
			if (results.get(i).size() != 0)
				precision.set(i, precision.get(i)/results.get(i).size());
			else
				precision.set(i, 0.0);
		return precision;
	}
		
	public ArrayList<Double> getFMeasure(ArrayList<Double> precision, ArrayList<Double> recall) {
		ArrayList<Double> fmeasure = new ArrayList<Double>();
		for (int i = 0; i < precision.size(); i++)
			if ( (precision.get(i)+recall.get(i)) != 0 )
				fmeasure.add(i, ( (2*precision.get(i)*recall.get(i))/(precision.get(i)+recall.get(i)) ) );
			else
				fmeasure.add(i,0.0);
		return fmeasure;
	}
	
	public ArrayList<Double> getEMeasure(ArrayList<Double> precision, ArrayList<Double> recall, double b) {
		ArrayList<Double> emeasure = new ArrayList<Double>();
		
		for (int i = 0; i < precision.size(); i++)
			emeasure.add(i, ( 1-( (1+Math.pow(b, 2))/( (Math.pow(b, 2)/recall.get(i) + (1/precision.get(i)) ) ))));
		return emeasure;
	}
	
	public void saveMeasure(ArrayList<Double> measure, String fileName) {
		try {
			File f = new File(fileName);
			FileWriter fw = new FileWriter(f);
			for (int i = measure.size()-1; i >= 0; i-- )
				fw.append( measure.get(i).toString()+"\n" );
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
			
	}
	
	public static void main(String[] args) {
		
		String simone = "";
		//simone = "GAvI-Project/";
		
		//Use example
		//MyBenchmark mb = new MyBenchmark(new FuzzyModel(),"benchmarkDocs.ser","benchmark/lisa/Query/");
		MyBenchmark mb = new MyBenchmark(new VectorSpaceModel(),"benchmarkDocs.ser",simone+"benchmark/lisa/Query/");
		mb.executeBenchmark();
		
		mb.saveResults("resFuz.save");
		
		System.out.println("Printing results for each query...");
		int queryNum = 1;
		
		ArrayList<LinkedList<String>> relevants = mb.expectedDocsRelevance(simone+"benchmark/lisa/LISA.REL");
		
		queryNum = 1;
		System.out.println("Printing intersection between retrieved and expected documents...");
		ArrayList<LinkedList<String>> intersect = mb.getIntersection(simone);
		for (LinkedList<String> intersection : intersect) {
			System.out.println("For Query " + queryNum + ", intersections: " + intersection.toString());
			queryNum++;
		}
		
		ArrayList<Double> precision = mb.getPrecision(intersect);
		ArrayList<Double> recall = mb.getRecall(intersect, relevants);
		
		int i=0;
		for (Double rec: recall)
			System.out.println("Recall query "+(++i)+": " + rec);
		
		i=0;
		for (Double rec: precision)
			System.out.println("Precision query "+(++i)+": " + rec);
	
		mb.saveMeasure(mb.getFMeasure(precision, recall), "fmeasure.txt");
		mb.saveMeasure(mb.getEMeasure(precision, recall,0.5), "emeasure.txt");
	
		mb.saveMeasure(precision, "precision.txt");
		mb.saveMeasure(recall, "recall.txt");
	}
	
}
