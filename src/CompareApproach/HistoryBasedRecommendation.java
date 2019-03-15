package CompareApproach;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import DataPreprocess.DatabaseConnect;
import DataPreprocess.participle;
import DataPreprocess.proAPIMsg;
import Frame.ClusterMsg;
import Frame.DocMsg;
import Frame.FileData;
import Frame.SimilarityResult;
import GetCommentInProject.DocFileDeal;
import TFIDF.DocumentParser;
import TFIDF.TfIdfMain;
import mianRun.CustomizedAPIExtract;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import ComparedApproachFrame.APIScore;
import ComparedApproachFrame.HistoryBasedSimilarityResult;
import ComparedApproachTFIDF.HistoryBasedDocumentParser;

public class HistoryBasedRecommendation {

	// public static void main(String[] args) throws IOException, SQLException {
	// // In the end, we get the TOPK related descriptions and the scores of all API
	// // that appear in them.
	//
	// String APILibraryName = "comparedeasymock";
	// String writeFilePath = "I:\\DATA\\Final\\CompareExperiment\\Result\\result" +
	// APILibraryName + ".txt";
	//
	// List<List<APIScore>> ALLHistoryBasedTopKAPI = getFinalHistoryAPI(10.0,
	// APILibraryName, writeFilePath);
	// System.out.println(ALLHistoryBasedTopKAPI.get(0).get(2).getApi() + " "
	// + ALLHistoryBasedTopKAPI.get(0).get(2).getScore()+" ");
	//
	// }

	public static void getFinalHistoryAPI(double topK, String APILibraryName, String writeFilePathRoot)
			throws IOException, SQLException {
		// This method is used to calculate the final recommended APIs and their copies,
		// as described in Formula 5 of the paper.
		
		String writeFilePathHistory = writeFilePathRoot +"\\"+ "resultHistory"+ APILibraryName+ ".txt";

		List<List<HistoryBasedSimilarityResult>> resultSummary = CalculateHistoryScore(APILibraryName);
				
		List<List<APIScore>> ALLHistoryBasedTopKAPI = new LinkedList<List<APIScore>>();

		for (List<HistoryBasedSimilarityResult> resultList : resultSummary) {

			List<String> APIList = new LinkedList<String>();

			for (int i = 1; i < topK; i++) {// The reason for starting from 1 is that 0 tend to be themselves.
				String[] APIs = resultList.get(i).getAPILibraryData().getAPI().split(" ");
				for (String API : APIs) {
					APIList.add(API);
				}
			}

			ArrayList<Map.Entry<String, Integer>> sortedAPIlist = new proAPIMsg().getAPIFrequency(APIList);
			List<APIScore> HistoryBasedTopKAPI = new LinkedList<APIScore>();
			for (int i = 0; i < sortedAPIlist.size(); i++) {
				// System.out.println(sortedAPIlist.get(i).getKey() + ": " +
				// sortedAPIlist.get(i).getValue());
				APIScore topkAPI = new APIScore();
				topkAPI.setApi(sortedAPIlist.get(i).getKey());
				topkAPI.setScore((sortedAPIlist.get(i).getValue() / topK));
				HistoryBasedTopKAPI.add(topkAPI);
			}

			// Divide the maximum value of data into
			// normalization.
			List<APIScore> HistoryBasedTopKAPINormalized = new LinkedList<APIScore>();

			for (int i = 0; i < HistoryBasedTopKAPI.size(); i++) {
				APIScore fianlNormalizedScore = new APIScore();
				fianlNormalizedScore.setApi(HistoryBasedTopKAPI.get(i).getApi());
				fianlNormalizedScore
						.setScore(HistoryBasedTopKAPI.get(i).getScore() / HistoryBasedTopKAPI.get(0).getScore());
				HistoryBasedTopKAPINormalized.add(fianlNormalizedScore);
			}

			// for(int i=0;i<HistoryBasedTopKAPI.size();i++) {
			// System.out.println(HistoryBasedTopKAPI.get(i).getApi()+"
			// "+HistoryBasedTopKAPI.get(i).getScore());
			// }
			ALLHistoryBasedTopKAPI.add(HistoryBasedTopKAPINormalized);

//			List<FileData> APIdataList = new LinkedList<FileData>();
//			for (int i = 0; i < ALLHistoryBasedTopKAPI.size(); i++) {
//				String sql = "select * from " + APILibraryName + " where ID = '  " + i + 1 + " ' ";// orginalapi
//
//				DatabaseConnect dbConn = new DatabaseConnect();
//
//				if (dbConn.findAPIMsgData(sql).getAPI() != null) {
//					APIdataList.add(dbConn.findAPIMsgData(sql)); // get the dataList
//				}
//				System.out.println(APIdataList.size());
//
//				String query = APIdataList.get(i).getAnnotation();
//				String correctAPI = APIdataList.get(i).getAPI();
//
//				new DatabaseConnect().insertunifyRecommendResultIntoDatabase(i + 1, query, correctAPI,
//						resultAPILibraryName, HistoryBasedTopKAPINormalized);
//
//			}
		}

		BufferedWriter write;
		write = new BufferedWriter(
				new OutputStreamWriter(new FileOutputStream(new File(writeFilePathHistory), true), "UTF-8"));
		
		for (List<APIScore> APIScore : ALLHistoryBasedTopKAPI) {
			for (APIScore score : APIScore) {
				
				write.append(score.getApi()+"*"+score.getScore());
				write.append(" ");
			}
			write.append("\n");
			
		}
		write.close();

		
	}

	// The first step is to complete the similarity calculation between the
	// description and the summary.then calculate History Score

	private static List<List<HistoryBasedSimilarityResult>> CalculateHistoryScore(String APILibraryName)
			throws IOException, SQLException {

		List<FileData> APIdataList = new LinkedList<FileData>();
		DatabaseConnect dbConn = new DatabaseConnect();

		for (int i = 1; i <= 800; i++) {

			String sql = "select * from " + APILibraryName + " where ID = '  " + i + " ' ";// orginalapi

			if (dbConn.findAPIMsgData(sql).getAPI() != null) {
				APIdataList.add(dbConn.findAPIMsgData(sql)); // get the dataList
			}
		}

		List<List<HistoryBasedSimilarityResult>> resultSummary = new LinkedList<List<HistoryBasedSimilarityResult>>();

		for (int i = 0; i < APIdataList.size(); i++) { // cm.size()
			resultSummary.add(
					getresultScore(APIdataList.get(i).getAnnotation(), APIdataList.get(i).getAPI(), APILibraryName));
		}

		return resultSummary;
	}

	private static List<HistoryBasedSimilarityResult> getresultScore(String query, String correctAPI,
			String DataSetName) throws IOException, SQLException {
		DatabaseConnect dbconn = new DatabaseConnect();
		List<HistoryBasedSimilarityResult> resultSummary = new LinkedList<HistoryBasedSimilarityResult>();

		HistoryBasedDocumentParser dpSummary = new HistoryBasedDocumentParser();
		dpSummary.parseFiles(query, dbconn, DataSetName);
		dpSummary.tfIdfCalculator(); // calculates tfidf
		resultSummary = dpSummary.getCosineSimilarityCompared(query, correctAPI); // calculated cosine
																					// similarity ]

		return resultSummary;

	}

}
