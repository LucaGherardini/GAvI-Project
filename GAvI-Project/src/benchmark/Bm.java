package benchmark;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;

import org.apache.lucene.search.FuzzyQuery;

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

	public void executeBenchmark() {

		//Reading queries from file
		ArrayList<String> queries = ReadQueries();

		System.out.println("Loading index with " + fileDocumentsPaths);

		LoadIndex();


		ArrayList<LinkedList<String>> documentsExpected = ReadDocumentsExpected();

		ArrayList<LinkedList<String>> documentsRetrieved = RetrieveDocuments(queries);

		ArrayList<LinkedList<String>> intersect = MakeIntersection(documentsExpected, documentsRetrieved);

		saveResults("resFuz.save", intersect);

		ArrayList<Double> precision = getPrecision(intersect, documentsRetrieved);
		ArrayList<Double> recall = getRecall(intersect, documentsExpected);

		int i=0;
		for (Double rec: recall)
			System.out.println("Recall query "+(++i)+": " + rec);

		i=0;
		for (Double rec: precision)
			System.out.println("Precision query "+(++i)+": " + rec);
	}

	public ArrayList<String> ReadQueries() {

		//This will load queries in file queryFile
		ArrayList<String> queries = new ArrayList<String>();

		try {
			File queryF = new File(queryFile);
			BufferedReader br = new BufferedReader(new FileReader(queryF));

			String line = "";
			String query = "";

			while ( (line=br.readLine()) != null) {
				if(line.length()>2) {
					query += line + " ";
				}

				if(line.endsWith("#")) {
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


	private void LoadIndex(){
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





	private ArrayList<LinkedList<String>> ReadDocumentsExpected() {
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

		return documentsExpected;
	}



	private ArrayList<LinkedList<String>> RetrieveDocuments(ArrayList<String> queries){
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



	private ArrayList<LinkedList<String>> MakeIntersection(ArrayList<LinkedList<String>> documentsExpected, ArrayList<LinkedList<String>> documentsRetrieved){
		ArrayList<LinkedList<String>> intersect = new ArrayList<LinkedList<String>>();

		LinkedList<String> intersection = null;

		for (int query = 0 ; query < documentsExpected.size(); query++) {
			intersection = new LinkedList<String>();
			for (int i = 0; i < documentsRetrieved.get(query).size(); i++) {
				for (int j = 0; j < documentsExpected.get(query).size(); j++) {
					if ( documentsExpected.get(query).get(j).equals(documentsRetrieved.get(query).get(i)) ){
						if (!intersection.contains(documentsExpected.get(query).get(j))){
							intersection.add(documentsExpected.get(query).get(j));
						}
					}
				}
			}
			System.out.println("Intersection for query " + (query+1) + ": " + intersection.toString());
			intersect.add(intersection);
		}
		return intersect;
	}



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



	public ArrayList<Double> getRecall(ArrayList<LinkedList<String>> intersect, ArrayList<LinkedList<String>> documentsExpected) {
		ArrayList<Double> recall = new ArrayList<Double>();

		for (LinkedList<String> intersection: intersect)
			recall.add(intersection.size()+0.0);
		//For each query get intersection.size / relevants.size
		//recall = |intersect|/|relevants|
		for (int i = 0; i < documentsExpected.size(); i++)
			if (documentsExpected.get(i).size() != 0)
				recall.set(i, recall.get(i)/documentsExpected.get(i).size());
			else 
				recall.set(i, 0.0);

		return recall;
	}



	public ArrayList<Double> getPrecision(ArrayList<LinkedList<String>> intersect, ArrayList<LinkedList<String>> documentsRetrieved) {
		ArrayList<Double> precision = new ArrayList<Double>();

		for (LinkedList<String> intersection: intersect) {
			precision.add(intersection.size()+0.0);
		}
		//For each query get intersection.size / relevants.size
		//precision = |intersect|/|result|
		for (int i = 0; i < documentsRetrieved.size(); i++)
			if (documentsRetrieved.get(i).size() != 0)
				precision.set(i, precision.get(i)/documentsRetrieved.get(i).size());
			else
				precision.set(i, 0.0);
		return precision;
	}





	public static void main (String[] args) {
		Bm bench = new Bm(new FuzzyModel(), "benchmarkDocs.ser", "benchmark/lisa/LISA.QUE", "benchmark/lisa/LISA.REL");
		bench.executeBenchmark();
	}


}
