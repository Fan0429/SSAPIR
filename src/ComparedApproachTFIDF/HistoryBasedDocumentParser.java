/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ComparedApproachTFIDF;

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

import ComparedApproachFrame.HistoryBasedSimilarityResult;

import java.util.Scanner;
import java.util.Set;
import java.util.TreeMap;

import DataPreprocess.DatabaseConnect;
import Frame.ClusterMsg;
import Frame.FileData;
import Frame.SimilarityResult;
import TFIDF.CosineSimilarity;
import TFIDF.TfIdf;

public class HistoryBasedDocumentParser {

	// This variable will hold all terms of each document in an array.
	private List<FileData> termsDocsArray = new ArrayList<FileData>();
	private List<String> allTerms = new ArrayList<String>(); // to hold all terms
	private List<double[]> tfidfDocsVector = new ArrayList<double[]>();
	private List<HistoryBasedSimilarityResult> SimilarityResultListComparedSummary = new LinkedList<HistoryBasedSimilarityResult>();

	public static List<HistoryBasedSimilarityResult> sortByValueDescending(List<HistoryBasedSimilarityResult> ResultList ) {
		
	for (int i = 0 ; i < ResultList.size()-1; i++) {
		for ( int j = 0 ; j <ResultList.size()-1-i; j++) {
			if(ResultList.get(j).getSimilarity()<ResultList.get(j+1).getSimilarity()) {
				double temps = ResultList.get(j).getSimilarity();
				FileData tempC = ResultList.get(j).getAPILibraryData();
				
				ResultList.get(j).setSimilarity(ResultList.get(j+1).getSimilarity());
				ResultList.get(j).setAPILibraryData(ResultList.get(j+1).getAPILibraryData());
				
				ResultList.get(j+1).setSimilarity(temps);
				ResultList.get(j+1).setAPILibraryData(tempC);
				
			}
		}
		
	}	
		return ResultList;

	}


	public List<FileData> parseFiles(String query, DatabaseConnect dbconn, String APILibraryName) throws SQLException {
		
		String sql = "select * from " + APILibraryName;
		

		// these three line is put the query to the final termsDocsArray to calculate
		// the similarity
		FileData APIFileData = new FileData();
		APIFileData.setAnnotation(query);
		
		termsDocsArray.add(APIFileData);
		termsDocsArray = dbconn.getAPILibraryAnnotation(termsDocsArray, allTerms, sql);

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

		for (FileData docTermsArray : termsDocsArray) {

			double[] tfidfvectors = new double[allTerms.size()];
			int count = 0;
			for (String terms : allTerms) {
				tf = new HistoryBasedTfIdf().tfCalculator(docTermsArray.getAnnotation(), terms);
				idf = new HistoryBasedTfIdf().idfCalculator(termsDocsArray, terms);
				tfidf = tf * idf;

				tfidfvectors[count] = tfidf;
				count++;

			}

			tfidfDocsVector.add(tfidfvectors); // storing document vectors;
		}
	}


	
	
	
	public  List<HistoryBasedSimilarityResult> getCosineSimilarityCompared(String query,String correctAPI) throws IOException {
		
		List<Double> temp = new LinkedList<Double>() ;
		for (int i = 1; i < tfidfDocsVector.size(); i++) {
			double similarity = new CosineSimilarity().cosineSimilarity(tfidfDocsVector.get(0), tfidfDocsVector.get(i));
			temp.add(similarity);
			FileData APILibraryData = termsDocsArray.get(i);
			
			HistoryBasedSimilarityResult similarityResult = new HistoryBasedSimilarityResult(APILibraryData, similarity);
			SimilarityResultListComparedSummary.add(similarityResult);

		}
		

		SimilarityResultListComparedSummary = sortByValueDescending(SimilarityResultListComparedSummary);
		// after this the cosineSimilarity is calculated over
	
		
//		String writeFilePath = "I:\\DATA\\Final\\CompareExperiment\\Result\\test.txt";
//		BufferedWriter write;
//		write = new BufferedWriter(
//				new OutputStreamWriter(new FileOutputStream(new File(writeFilePath), true), "UTF-8"));
//	
//		for(double dd:temp) {
//			write.append(String.valueOf(dd));write.append("\n");
//		}
//		write.close();
		
		
		
		return SimilarityResultListComparedSummary;
		
	}
	
}
