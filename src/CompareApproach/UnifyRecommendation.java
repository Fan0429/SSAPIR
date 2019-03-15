package CompareApproach;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import ComparedApproachFrame.APIDocMsg;
import ComparedApproachFrame.APIScore;
import DataPreprocess.DatabaseConnect;
import Frame.FileData;
import Frame.SimilarityResult;

public class UnifyRecommendation {


	
	
	public static void main(String[] args) throws IOException, SQLException {

		String APILibraryName = "comparedeasymock";
		String APIDocumentLibraryName = "doccomparedeasymock";
		String resultName = "resultComparedeasymock";
		String writeFilePathRoot = "I:\\DATA\\Final\\CompareExperiment\\Result\\Easymock";

		// zhe liang ge da xiao yi ding shi yi yang de
		 new HistoryBasedRecommendation().getFinalHistoryAPI(10.0, APILibraryName,writeFilePathRoot);
		 new DocumentBasedRecommendation().getFinalDocumentAPI(APILibraryName,APIDocumentLibraryName,writeFilePathRoot);

		List<List<APIScore>> ALLHistoryBasedTopKAPI = new LinkedList<List<APIScore>>();
		List<List<APIScore>> ALLDocumentBasedTopKAPI = new LinkedList<List<APIScore>>();

		String ReadFilePathHistory = writeFilePathRoot + "\\" + "resultHistory" + APILibraryName + ".txt";
		String ReadFilePathDocument = writeFilePathRoot + "\\" + "resultDocument" + APILibraryName + ".txt";

		BufferedReader br = new BufferedReader(
				new InputStreamReader(new FileInputStream(new File(ReadFilePathHistory))));
		String line = "";
		while ((line = br.readLine()) != null) {

			String[] HistoryBasedTopKAPI = line.split(" ");
			List<APIScore> TopKAPI = new LinkedList<APIScore>();
			for (String TopKAPITemp : HistoryBasedTopKAPI) {
				APIScore API = new APIScore();
				String[] APITemp = TopKAPITemp.split("\\*");
				API.setApi(APITemp[0]);
				API.setScore(Double.parseDouble(APITemp[1]));
				TopKAPI.add(API);
			}
			ALLHistoryBasedTopKAPI.add(TopKAPI);
		}
		br.close();

		br = new BufferedReader(new InputStreamReader(new FileInputStream(new File(ReadFilePathDocument))));
		line = "";
		while ((line = br.readLine()) != null) {

			String[] DocumentBasedTopKAPI = line.split(" ");
			List<APIScore> TopKAPI = new LinkedList<APIScore>();
			for (String TopKAPITemp : DocumentBasedTopKAPI) {
				APIScore API = new APIScore();
				String[] APITemp = TopKAPITemp.split("\\*");
				API.setApi(APITemp[0]);
				API.setScore(Double.parseDouble(APITemp[1]));
				TopKAPI.add(API);
			}
			ALLDocumentBasedTopKAPI.add(TopKAPI);
		}
		br.close();
		System.out.println(ALLHistoryBasedTopKAPI.size());
		System.out.println(ALLDocumentBasedTopKAPI.size());

		int dataSize = ALLHistoryBasedTopKAPI.size();

		List<FileData> APIdataList = new LinkedList<FileData>();
		for (int i = 0; i < dataSize; i++) {

			String sql = "select * from " + APILibraryName + " where ID = ' " + (i + 1) + " ' ";// orginalapi
			System.out.println(sql);
			DatabaseConnect dbConn = new DatabaseConnect();

			if (dbConn.findAPIMsgData(sql).getAPI() != null) {
				APIdataList.add(dbConn.findAPIMsgData(sql)); // get the dataList
			}

			String query = APIdataList.get(i).getAnnotation();
			String correctAPI = APIdataList.get(i).getAPI();

			List<APIScore> unifyRecommendScore = new LinkedList<APIScore>();

			for (int j = 0; j < ALLDocumentBasedTopKAPI.get(i).size(); j++) {
				for (int k = 0; k < ALLHistoryBasedTopKAPI.get(i).size(); k++) {
					// System.out.println("asdasdasdasd");
					if (ALLHistoryBasedTopKAPI.get(i).get(k).getApi()
							.contains(ALLDocumentBasedTopKAPI.get(i).get(j).getApi())) {
						APIScore oneScore = new APIScore();
						oneScore.setApi(ALLDocumentBasedTopKAPI.get(i).get(j).getApi());
						double score = 0.556 * ALLHistoryBasedTopKAPI.get(i).get(k).getScore()
								+ 0.704 * ALLDocumentBasedTopKAPI.get(i).get(j).getScore();
						oneScore.setScore(score);
						unifyRecommendScore.add(oneScore);
						// System.out.println(ALLHistoryBasedTopKAPI.get(i).get(k).getApi());
						// System.out.println(ALLDocumentBasedTopAPI.get(i).get(j).getApi());
						break;
					}
					if (k == ALLHistoryBasedTopKAPI.get(i).size() - 1) {// if comes to the end
						APIScore oneScore = new APIScore();
						oneScore.setApi(ALLDocumentBasedTopKAPI.get(i).get(j).getApi());
						oneScore.setScore(ALLDocumentBasedTopKAPI.get(i).get(j).getScore() * 0.704);

						unifyRecommendScore.add(oneScore);
					}
				}

			}

			unifyRecommendScore = unifyRecommendScoreSort(unifyRecommendScore);

			new DatabaseConnect().insertunifyRecommendResultIntoDatabase(i + 1, query, correctAPI, resultName,unifyRecommendScore);

		}

	}

	public static List<APIScore> unifyRecommendScoreSort(List<APIScore> unifyRecommendScore) {

		for (int i = 0; i < unifyRecommendScore.size() - 1; i++) {
			for (int j = 0; j < unifyRecommendScore.size() - 1 - i; j++) {
				if (unifyRecommendScore.get(j).getScore() < unifyRecommendScore.get(j + 1).getScore()) {
					double temps = unifyRecommendScore.get(j).getScore();
					String tempA = unifyRecommendScore.get(j).getApi();

					unifyRecommendScore.get(j).setScore(unifyRecommendScore.get(j + 1).getScore());
					unifyRecommendScore.get(j).setApi(unifyRecommendScore.get(j + 1).getApi());

					unifyRecommendScore.get(j + 1).setScore(temps);
					unifyRecommendScore.get(j + 1).setApi(tempA);
				}
			}
		}

		return unifyRecommendScore;

	}

}
