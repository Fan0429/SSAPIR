/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package TFIDF;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeMap;

import DataPreprocess.DatabaseConnect;
import Frame.ClusterMsg;
import Frame.SimilarityResult;

public class DocumentParser {

	// This variable will hold all terms of each document in an array.
	private List<ClusterMsg> termsDocsArray = new ArrayList<ClusterMsg>();
	private List<String> allTerms = new ArrayList<String>(); // to hold all terms
	private List<double[]> tfidfDocsVector = new ArrayList<double[]>();
	private List<SimilarityResult> SimilarityResultListOurs = new LinkedList<SimilarityResult>();


	public static List<SimilarityResult> sortByValueDescending(List<SimilarityResult> ResultList ) {
		
	for (int i = 0 ; i < ResultList.size()-1; i++) {
		for ( int j = 0 ; j <ResultList.size()-1-i; j++) {
			if(ResultList.get(j).getSimilarity()<ResultList.get(j+1).getSimilarity()) {
				double temps = ResultList.get(j).getSimilarity();
				ClusterMsg tempC = ResultList.get(j).getClusterMsg();
				
				ResultList.get(j).setSimilarity(ResultList.get(j+1).getSimilarity());
				ResultList.get(j).setClusterMsg(ResultList.get(j+1).getClusterMsg());
				
				ResultList.get(j+1).setSimilarity(temps);
				ResultList.get(j+1).setClusterMsg(tempC);
				
			}
		}
		
	}
		
		return ResultList;
		
		
		
	}


	public List<ClusterMsg> parseFiles(String query, DatabaseConnect dbconn, String clusterName) {
		String[] list = query.split(" ");
		List<String> temp = new LinkedList<String>();
		for (String str : list) {
			if (!str.equals("")) {
				temp.add(str);
			}
		}

		// these three line is put the query to the final termsDocsArray to calculate
		// the similarity
		ClusterMsg queryAnno = new ClusterMsg();
		queryAnno.setClusterAnnotation(temp);
		termsDocsArray.add(queryAnno);

		String sql = "select * from " + clusterName;

		termsDocsArray = dbconn.getClusterAnnotation(termsDocsArray, allTerms, sql);

		return termsDocsArray;

	}

	/**
	 * Method to create termVector according to its tfidf score.
	 */
	public void tfIdfCalculator() {

		double tf; // term frequency
		double idf; // inverse document frequency
		double tfidf; // term requency inverse document frequency

		for (int i = 0; i < termsDocsArray.size(); i++) {
			// System.out.println(termsDocsArray.get(i).getClusterAnnotation());

		}

		for (ClusterMsg docTermsArray : termsDocsArray) {

			double[] tfidfvectors = new double[allTerms.size()];
			int count = 0;
			for (String terms : allTerms) {
				tf = new TfIdf().tfCalculator(docTermsArray.getClusterAnnotation(), terms);
				idf = new TfIdf().idfCalculator(termsDocsArray, terms);
				tfidf = tf * idf;

				tfidfvectors[count] = tfidf;
				count++;

			}

			tfidfDocsVector.add(tfidfvectors); // storing document vectors;
		}
	}

	/**
	 * Method to calculate cosine similarity between all the documents.
	 * 
	 * @throws IOException
	 * @throws SQLException 
	 * @throws FileNotFoundException
	 * @throws UnsupportedEncodingException
	 */

	public int getCosineSimilarityOurs(int count, String writeFilePath, String query,String correctAPI,String resultName) throws IOException, SQLException {
     // this is our approach to get the cosineSimilarity method 
		for (int i = 1; i < tfidfDocsVector.size(); i++) {
			double similarity = new CosineSimilarity().cosineSimilarity(tfidfDocsVector.get(0), tfidfDocsVector.get(i));
			ClusterMsg clusterMsg = termsDocsArray.get(i);
			
			SimilarityResult similarityResult = new SimilarityResult(clusterMsg, similarity);
			SimilarityResultListOurs.add(similarityResult);

		}
		SimilarityResultListOurs = sortByValueDescending(SimilarityResultListOurs);

		BufferedWriter write;
		write = new BufferedWriter(
				new OutputStreamWriter(new FileOutputStream(new File(writeFilePath), true), "UTF-8"));
		write.append(query+"--------" + count);
		write.append("\n");
		for (int i = 0; i < 16; i++) {
			SimilarityResult result =  SimilarityResultListOurs.get(i);
			write.append(result.getClusterMsg().getClusterID() + "---" + result.getSimilarity()+"---"+result.getClusterMsg().getClusterAPI().replace("&",""));
			write.append("\n");
		}
		write.close();
		
		
		new DatabaseConnect().insertResultToDatabase(query,correctAPI,SimilarityResultListOurs,count,resultName);
		
		
		return count;
	}

	
	
	
	

}
