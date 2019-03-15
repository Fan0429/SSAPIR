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

import ComparedApproachFrame.APIDocMsg;
import ComparedApproachFrame.DocumentBasedSimilarityResult;
import ComparedApproachFrame.HistoryBasedSimilarityResult;

import java.util.Scanner;
import java.util.Set;
import java.util.TreeMap;

import DataPreprocess.DatabaseConnect;
import TFIDF.CosineSimilarity;

public class DocumentBasedDocumentParser {

	// This variable will hold all terms of each document in an array.
	private List<APIDocMsg> termsDocsArray = new ArrayList<APIDocMsg>();
	private List<String> allTerms = new ArrayList<String>(); // to hold all terms
	private List<double[]> tfidfDocsVector = new ArrayList<double[]>();
	private List<DocumentBasedSimilarityResult> SimilarityResultListComparedSummary = new LinkedList<DocumentBasedSimilarityResult>();

	public static List<DocumentBasedSimilarityResult> sortByValueDescending(List<DocumentBasedSimilarityResult> ResultList ) {
		
	for (int i = 0 ; i < ResultList.size()-1; i++) {
		for ( int j = 0 ; j <ResultList.size()-1-i; j++) {
			if(ResultList.get(j).getSimilarity()<ResultList.get(j+1).getSimilarity()) {
				double temps = ResultList.get(j).getSimilarity();
				APIDocMsg tempC = ResultList.get(j).getAPIDocMsgData();
				
				ResultList.get(j).setSimilarity(ResultList.get(j+1).getSimilarity());
				ResultList.get(j).setAPIDocMsgData(ResultList.get(j+1).getAPIDocMsgData());
				
				ResultList.get(j+1).setSimilarity(temps);
				ResultList.get(j+1).setAPIDocMsgData(tempC);
				
			}
		}
		
	}	
		return ResultList;

	}


	public List<APIDocMsg> parseFiles(String query, DatabaseConnect dbconn, String APIDocumentLibraryName) throws SQLException {
		
		String sql = "select * from " + APIDocumentLibraryName;
		

		// these three line is put the query to the final termsDocsArray to calculate
		// the similarity
		APIDocMsg APIDocMsgData = new APIDocMsg();
		APIDocMsgData.setMethodDescription(query);
		
		termsDocsArray.add(APIDocMsgData);
		termsDocsArray = dbconn.getAPIDocMsgDescription(termsDocsArray, allTerms, sql);

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

		for (APIDocMsg docTermsArray : termsDocsArray) {

			double[] tfidfvectors = new double[allTerms.size()];
			int count = 0;
			for (String terms : allTerms) {
				tf = new DocumentBasedTfIdf().tfCalculator(docTermsArray.getMethodDescription(), terms);
				idf = new DocumentBasedTfIdf().idfCalculator(termsDocsArray, terms);
				tfidf = tf * idf;

				tfidfvectors[count] = tfidf;
				count++;

			}

			tfidfDocsVector.add(tfidfvectors); // storing document vectors;
		}
	}


	
	
	
	public  List<DocumentBasedSimilarityResult> getCosineSimilarityCompared( String query,String correctAPI) throws IOException {
		List<Double> temp = new LinkedList<Double>() ;
		
		for (int i = 1; i < tfidfDocsVector.size(); i++) {
			double similarity = new CosineSimilarity().cosineSimilarity(tfidfDocsVector.get(0), tfidfDocsVector.get(i));
			APIDocMsg APILibraryData = termsDocsArray.get(i);
			temp.add(similarity);
			DocumentBasedSimilarityResult similarityResult = new DocumentBasedSimilarityResult(APILibraryData, similarity);
			SimilarityResultListComparedSummary.add(similarityResult);

		}
		
		//我认为，应该两个加在一起之后，再排序会比较好！！！！懂么，现在排序， 你还要定位，难道不是很麻烦
		//注意，还有一点，他对推荐出来的API方法，做了一个加权，就是他推荐的不是API序列，而是一个单个的API，这里你没有选
		SimilarityResultListComparedSummary = sortByValueDescending(SimilarityResultListComparedSummary);
		// after this the cosineSimilarity is calculated over
		
		
// 这里有0是没问题的，没有出现NAN说明NAN是在上面出现的
		
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
