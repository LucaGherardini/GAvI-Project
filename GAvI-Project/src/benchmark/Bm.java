package benchmark;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;

import org.apache.lucene.search.FuzzyQuery;

import com.panayotis.gnuplot.JavaPlot;

import index.Hit;
import index.Index;
import irModels.BM25;
import irModels.BooleanModel;
import irModels.FuzzyModel;
import irModels.Model;
import irModels.VectorSpaceModel;

public class Bm {

	Model model;
	String fileDocumentsPaths;
	String queryFile;
	String docExpected;
	Index generalIndex;
	LinkedList<String> ll;

	/**
	 * 
	 * @param model model on which execute benchmark
	 * @param fileDocumentsPaths in this file, every line is a path of a document on which execute query
	 * @param queryFile this is lisa.que file
	 * @param docExpected in this file, for every query, there is a list of relevant documents for it
	 */
	public Bm(Model model, String fileDocumentsPaths, String queryFile, String docExpected) {
		this.model = model;
		this.fileDocumentsPaths = fileDocumentsPaths;
		this.queryFile = queryFile;
		this.docExpected = docExpected;

		this.generalIndex = Index.getIndex(model.getSimilarity());

		ll = new LinkedList<String>();
		ll.add("name");
		ll.add("content");
	}

	/**
	 * This is the main method. 
	 * This method perform the benchmark.
	 * Documents are added in the index.
	 * Array with expected documents is created. (Documents that should be retrived)
 	 * Array with retrived documents is created. (Documents that are retrived)
 	 * Array with intersection between expected and retrived documents is created. (For calculating precision and recall)
	 */
	public void executeBenchmark() {

		//Reading queries from file
		ArrayList<String> queries = readQueries();

		System.out.println("Loading index with " + fileDocumentsPaths);

		loadIndex();

		ArrayList<LinkedList<String>> expectedDocuments = getExpectedDocuments();

		ArrayList<LinkedList<String>> retrievedDocuments = retrieveDocuments(queries);

		ArrayList<LinkedList<String>> intersect = getIntersection(expectedDocuments, retrievedDocuments);

		saveResults("resFuz.save", intersect);

		ArrayList<Double> precision = getPrecision(intersect, retrievedDocuments);
		ArrayList<Double> recall = getRecall(intersect, expectedDocuments);

		int i=0;
		for (Double rec: recall)
			System.out.println("Recall query "+(++i)+": " + rec);

		i=0;
		for (Double rec: precision)
			System.out.println("Precision query "+(++i)+": " + rec);
	}

	/**
	 * This method read query from lisa.que
	 * Queries are divide each other with a '#', so every query ends up with that. 
	 * @return list of query
	 */
	public ArrayList<String> readQueries() {

		//This will load queries in file queryFile
		ArrayList<String> queries = new ArrayList<String>();

		try {
			File queryF = new File(queryFile);
			BufferedReader br = new BufferedReader(new FileReader(queryF));

			String line = "";
			String query = "";

			while ( (line=br.readLine()) != null) {
				//So, if it isn't the line with query number
				if(line.length()>2) {
					query += line + " ";
				}

				//If is the last line of the query
				if(line.endsWith("#")) {
					//Add the line until '#'
					query = query.substring(0, query.length()-2);
					queries.add(query);
					query = "";
				}
			}
			br.close();
		}catch (Exception e) {
			e.printStackTrace();
		}
		return queries;
	}

	/**
	 * Load documents to retrival in to the index.
	 * Read every line of file (a line = a document) and add it to the index
	 */
	private void loadIndex(){
		String docPath = "";

		try {
			File docF = new File(fileDocumentsPaths);
			BufferedReader br = new BufferedReader(new FileReader(docF));

			while ( (docPath = br.readLine()) != null) {
				generalIndex.addDocument(docPath);
			}
			br.close();
		}catch(Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Get list of expected documents for every query from LISA.REL
	 * @return list of expected documents.
	 */
	private ArrayList<LinkedList<String>> getExpectedDocuments() {
		ArrayList<LinkedList<String>> expectedDocuments = new ArrayList<LinkedList<String>>();
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
					expectedDocuments.add(rel);
				}
				br.close();
			}
		} catch (Exception e) {
			System.err.println(e);
		}			

		return expectedDocuments;
	}

	/**
	 * This method retrive documents, so it performs benchmark.
	 * @param queries list of queries
	 * @return
	 */
	private ArrayList<LinkedList<String>> retrieveDocuments(ArrayList<String> queries){
		ArrayList<LinkedList<String>> documentsRetrieved = new ArrayList<LinkedList<String>>();
		LinkedList<String> results = null;
		LinkedList<Hit> indexResults = null;
		int query_num = 1;

		for(String query : queries) {
			results = new LinkedList<String>();

			indexResults = generalIndex.submitQuery(query, ll, model, false);
			for(Hit indRes : indexResults) {
				results.add(indRes.getDocName().substring(0, indRes.getDocName().lastIndexOf(".")));
			}
			System.out.println("Results for query " + query_num + ": " + results.toString());
			System.out.println("*****");
			query_num++;
			documentsRetrieved.add(results);
		}
		return documentsRetrieved;
	}

	/**
	 * Do intersection beetwen expected documents and retrived documents.
	 * @param expectedDocuments
	 * @param retrievedDocuments
	 * @return list of interesction
	 */
	private ArrayList<LinkedList<String>> getIntersection(ArrayList<LinkedList<String>> expectedDocuments, ArrayList<LinkedList<String>> retrievedDocuments){
		ArrayList<LinkedList<String>> intersect = new ArrayList<LinkedList<String>>();

		LinkedList<String> intersection = null;

		for (int query = 0 ; query < expectedDocuments.size(); query++) {
			intersection = new LinkedList<String>();
			for (int i = 0; i < retrievedDocuments.get(query).size(); i++) {
				for (int j = 0; j < expectedDocuments.get(query).size(); j++) {
					if ( expectedDocuments.get(query).get(j).equals(retrievedDocuments.get(query).get(i)) ){
						if (!intersection.contains(expectedDocuments.get(query).get(j))){
							intersection.add(expectedDocuments.get(query).get(j));
							
						}
					}
				}
			}
			System.out.println("Intersection for query " + (query+1) + ": " + intersection.toString());
			intersect.add(intersection);
		}
		return intersect;
	}

	/**
	 * Save intersection on a file.
	 * @param fileName
	 * @param intersect
	 */
	private void saveResults(String fileName, ArrayList<LinkedList<String>> intersect) {
		try {
			FileWriter fw = new FileWriter(new File(fileName));
			for (int i = 0; i < intersect.size(); i++) {
				fw.append("Docs intersected for query "+(i+1)+":\n");
				for (int j = 0; j < intersect.get(i).size(); j++) {
					fw.append(intersect.get(i).get(j)+"\n");

				}
			}
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Calculate recall
	 * @param intersect intersection beetwen expected and retrived documents
	 * @param expectedDocuments expected documents
	 * @return list of recall for every query
	 */
	public ArrayList<Double> getRecall(ArrayList<LinkedList<String>> intersect, ArrayList<LinkedList<String>> expectedDocuments) {
		ArrayList<Double> recall = new ArrayList<Double>();

		for (LinkedList<String> intersection: intersect)
			recall.add(intersection.size()+0.0);
		//For each query get intersection.size / relevants.size
		//recall = |intersect|/|relevants|
		for (int i = 0; i < expectedDocuments.size(); i++)
			if (expectedDocuments.get(i).size() != 0)
				recall.set(i, recall.get(i)/expectedDocuments.get(i).size());
			else 
				recall.set(i, 0.0);

		return recall;
	}


	/**
	 * Calculate precision
	 * @param intersect intersection beetwen expected and retrived documents
	 * @param retrievedDocuments retrived documents
	 * @return list of precision for every query
	 */
	public ArrayList<Double> getPrecision(ArrayList<LinkedList<String>> intersect, ArrayList<LinkedList<String>> retrievedDocuments) {
		ArrayList<Double> precision = new ArrayList<Double>();

		for (LinkedList<String> intersection: intersect) {
			precision.add(intersection.size()+0.0);
		}
		//For each query get intersection.size / relevants.size
		//precision = |intersect|/|result|
		for (int i = 0; i < retrievedDocuments.size(); i++) {
			if (retrievedDocuments.get(i).size() != 0)
				precision.set(i, precision.get(i)/retrievedDocuments.get(i).size());
			else
				precision.set(i, 0.0);
			}
		return precision;
	}

	/**
	 * Calculate recall
	 * @param precision list of precision for every query
	 * @param recall list of recall for every query
	 * @return f-measure
	 */
	public ArrayList<Double> getFMeasure(ArrayList<Double> precision, ArrayList<Double> recall) {
		ArrayList<Double> fmeasure = new ArrayList<Double>();
		for (int i = 0; i < precision.size(); i++)
			if ( (precision.get(i)+recall.get(i)) != 0 )
				fmeasure.add(i, ( (2*precision.get(i)*recall.get(i))/(precision.get(i)+recall.get(i)) ) );
			else
				fmeasure.add(i,0.0);
		return fmeasure;
	}
	
	/**
	 * Calculate E-measure
	 * @param precision list of precision for every query
	 * @param recall list of recall for every query
	 * @param b parameter b for e-measure 
	 * @return emeasure
	 */
	public ArrayList<Double> getEMeasure(ArrayList<Double> precision, ArrayList<Double> recall, double b) {
		ArrayList<Double> emeasure = new ArrayList<Double>();
		
		for (int i = 0; i < precision.size(); i++)
			emeasure.add(i, ( 1-( (1+Math.pow(b, 2))/( (Math.pow(b, 2)/recall.get(i) + (1/precision.get(i)) ) ))));
		return emeasure;
	}
	
	/**
	 * Save measure (f or e) on a file. 
	 * @param measure mesure to save
	 * @param fileName file name
	 */
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
	
	public static void main (String[] args) {
		/*
		Bm bench = new Bm(new FuzzyModel(), "benchmarkDocs.ser", "benchmark/lisa/LISA.QUE", "benchmark/lisa/LISA.REL");
		bench.executeBenchmark();
		
		ArrayList<LinkedList<String>> expectedDocuments = bench.getExpectedDocuments();
		ArrayList<LinkedList<String>> retrivedDocuments = bench.retrieveDocuments(bench.readQueries());

		ArrayList<LinkedList<String>> intersect = bench.getIntersection(expectedDocuments, retrivedDocuments);
		ArrayList<Double> precision = bench.getPrecision(intersect, retrivedDocuments);
		ArrayList<Double> recall = bench.getRecall(intersect, expectedDocuments);
		
		ArrayList<Double> e_measure = bench.getEMeasure(precision, recall, 0.5);
		ArrayList<Double> f_measure = bench.getFMeasure(precision, recall);
		
		bench.saveMeasure(e_measure, "emeasure.dat");
		bench.saveMeasure(f_measure, "fmeasure.dat");
		*/
		JavaPlot p = new JavaPlot();
		double [][] points = new double[10][1];
		double value = 0.0;
		for (int i = 0; i < 10; i++)
			points[i][0] = value++;
		
		p.addPlot(points);
		p.plot();
		
	}



}
