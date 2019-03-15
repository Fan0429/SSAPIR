package DataPreprocess;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import Cluster.Cluster;
import ComparedApproachFrame.APIDocMsg;
import ComparedApproachFrame.APIScore;
import Frame.ClusterMsg;
import Frame.FileData;
import Frame.SimilarityResult;
import Frame.questionMessage;

public class DatabaseConnect {

	private static final String url = "jdbc:mysql://127.0.0.1/api";
	private static final String driveName = "com.mysql.jdbc.Driver";
	private static final String user = "root";
	private static final String password = "Fan,shangnanda!";

	public Connection conn = null;
	public PreparedStatement pst = null;
	public ResultSet rs = null;
	public Statement stmt = null;

	public DatabaseConnect() {
		try {

			Class.forName(driveName); // 指定连接类型
			conn = DriverManager.getConnection(url, user, password);// 获取连接

			if (!conn.isClosed()) {
				System.out.println("Succeeded connecting to the Database!");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void closePreparedStatement() {
		try {
			this.pst.close();
			this.conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void closeResultSet() {
		try {
			this.rs.close();
			this.stmt.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	
	public void insertunifyRecommendResultIntoDatabase(int count, String query, String correctAPI, String resultName,
			List<APIScore> unifyRecommendScore) throws SQLException {
		String sql = "insert into " + resultName + " values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		pst = conn.prepareStatement(sql);
		pst.setInt(1, count);
		pst.setString(2, query);
		pst.setString(3, correctAPI);
		pst.setString(4,
				String.valueOf(unifyRecommendScore.get(0).getScore()) + "*" + unifyRecommendScore.get(0).getApi());
		pst.setString(5,
				String.valueOf(unifyRecommendScore.get(1).getScore()) + "*" + unifyRecommendScore.get(1).getApi());
		pst.setString(6,
				String.valueOf(unifyRecommendScore.get(2).getScore()) + "*" + unifyRecommendScore.get(2).getApi());
		pst.setString(7,
				String.valueOf(unifyRecommendScore.get(3).getScore()) + "*" + unifyRecommendScore.get(3).getApi());
		pst.setString(8,
				String.valueOf(unifyRecommendScore.get(4).getScore()) + "*" + unifyRecommendScore.get(4).getApi());
		pst.setString(9,
				String.valueOf(unifyRecommendScore.get(5).getScore()) + "*" + unifyRecommendScore.get(5).getApi());
		pst.setString(10,
				String.valueOf(unifyRecommendScore.get(6).getScore()) + "*" + unifyRecommendScore.get(6).getApi());
		pst.setString(11,
				String.valueOf(unifyRecommendScore.get(7).getScore()) + "*" + unifyRecommendScore.get(7).getApi());
		pst.setString(12,
				String.valueOf(unifyRecommendScore.get(8).getScore()) + "*" + unifyRecommendScore.get(8).getApi());
		pst.setString(13,
				String.valueOf(unifyRecommendScore.get(9).getScore()) + "*" + unifyRecommendScore.get(9).getApi());
		pst.setString(14,
				String.valueOf(unifyRecommendScore.get(10).getScore()) + "*" + unifyRecommendScore.get(10).getApi());
		pst.setString(15,
				String.valueOf(unifyRecommendScore.get(11).getScore()) + "*" + unifyRecommendScore.get(11).getApi());
		pst.setString(16,
				String.valueOf(unifyRecommendScore.get(12).getScore()) + "*" + unifyRecommendScore.get(12).getApi());
		pst.setString(17,
				String.valueOf(unifyRecommendScore.get(13).getScore()) + "*" + unifyRecommendScore.get(13).getApi());
		pst.setString(18,
				String.valueOf(unifyRecommendScore.get(14).getScore()) + "*" + unifyRecommendScore.get(14).getApi());
		pst.setString(19, "");
		pst.executeUpdate();

		this.pst.close();
		this.conn.close();
	}

	public void insertResultToDatabase(String query, String correctAPI, List<SimilarityResult> SimilarityResultList,
			int count, String resultName) throws SQLException {

		String sql = "insert into " + resultName + " values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

		pst = conn.prepareStatement(sql);
		pst.setInt(1, count);
		pst.setString(2, query);
		pst.setString(3, correctAPI);
		pst.setString(4,
				SimilarityResultList.get(0).getSimilarity() + "*"
						+ SimilarityResultList.get(0).getClusterMsg().getClusterAnnotation() + "*"
						+ SimilarityResultList.get(0).getClusterMsg().getClusterAPI());
		pst.setString(5,
				SimilarityResultList.get(1).getSimilarity() + "*"
						+ SimilarityResultList.get(1).getClusterMsg().getClusterAnnotation() + "*"
						+ SimilarityResultList.get(1).getClusterMsg().getClusterAPI());
		pst.setString(6,
				SimilarityResultList.get(2).getSimilarity() + "*"
						+ SimilarityResultList.get(2).getClusterMsg().getClusterAnnotation() + "*"
						+ SimilarityResultList.get(2).getClusterMsg().getClusterAPI());
		pst.setString(7,
				SimilarityResultList.get(3).getSimilarity() + "*"
						+ SimilarityResultList.get(3).getClusterMsg().getClusterAnnotation() + "*"
						+ SimilarityResultList.get(3).getClusterMsg().getClusterAPI());
		pst.setString(8,
				SimilarityResultList.get(4).getSimilarity() + "*"
						+ SimilarityResultList.get(4).getClusterMsg().getClusterAnnotation() + "*"
						+ SimilarityResultList.get(4).getClusterMsg().getClusterAPI());
		pst.setString(9,
				SimilarityResultList.get(5).getSimilarity() + "*"
						+ SimilarityResultList.get(5).getClusterMsg().getClusterAnnotation() + "*"
						+ SimilarityResultList.get(5).getClusterMsg().getClusterAPI());
		pst.setString(10,
				SimilarityResultList.get(6).getSimilarity() + "*"
						+ SimilarityResultList.get(6).getClusterMsg().getClusterAnnotation() + "*"
						+ SimilarityResultList.get(6).getClusterMsg().getClusterAPI());
		pst.setString(11,
				SimilarityResultList.get(7).getSimilarity() + "*"
						+ SimilarityResultList.get(7).getClusterMsg().getClusterAnnotation() + "*"
						+ SimilarityResultList.get(7).getClusterMsg().getClusterAPI());
		pst.setString(12,
				SimilarityResultList.get(8).getSimilarity() + "*"
						+ SimilarityResultList.get(8).getClusterMsg().getClusterAnnotation() + "*"
						+ SimilarityResultList.get(8).getClusterMsg().getClusterAPI());
		pst.setString(13,
				SimilarityResultList.get(9).getSimilarity() + "*"
						+ SimilarityResultList.get(9).getClusterMsg().getClusterAnnotation() + "*"
						+ SimilarityResultList.get(9).getClusterMsg().getClusterAPI());
		pst.setString(14,
				SimilarityResultList.get(10).getSimilarity() + "*"
						+ SimilarityResultList.get(10).getClusterMsg().getClusterAnnotation() + "*"
						+ SimilarityResultList.get(10).getClusterMsg().getClusterAPI());
		pst.setString(15,
				SimilarityResultList.get(11).getSimilarity() + "*"
						+ SimilarityResultList.get(11).getClusterMsg().getClusterAnnotation() + "*"
						+ SimilarityResultList.get(11).getClusterMsg().getClusterAPI());
		pst.setString(16,
				SimilarityResultList.get(12).getSimilarity() + "*"
						+ SimilarityResultList.get(12).getClusterMsg().getClusterAnnotation() + "*"
						+ SimilarityResultList.get(12).getClusterMsg().getClusterAPI());
		pst.setString(17,
				SimilarityResultList.get(13).getSimilarity() + "*"
						+ SimilarityResultList.get(13).getClusterMsg().getClusterAnnotation() + "*"
						+ SimilarityResultList.get(13).getClusterMsg().getClusterAPI());
		pst.setString(18,
				SimilarityResultList.get(14).getSimilarity() + "*"
						+ SimilarityResultList.get(14).getClusterMsg().getClusterAnnotation() + "*"
						+ SimilarityResultList.get(14).getClusterMsg().getClusterAPI());
		pst.setString(19,
				SimilarityResultList.get(15).getSimilarity() + "*"
						+ SimilarityResultList.get(15).getClusterMsg().getClusterAnnotation() + "*"
						+ SimilarityResultList.get(15).getClusterMsg().getClusterAPI());

		pst.executeUpdate();

		this.pst.close();
		this.conn.close();
	}

	public List<List<String>> getRseult(String sql) {
		List<List<String>> result = new LinkedList<List<String>>();

		try {
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);
			while (rs.next()) {
				List<String> tempResult = new LinkedList<String>();
				tempResult.add(rs.getString(2));
				tempResult.add(rs.getString(3));
				tempResult.add(rs.getString(4));
				tempResult.add(rs.getString(5));
				tempResult.add(rs.getString(6));
				tempResult.add(rs.getString(7));
				tempResult.add(rs.getString(8));
				tempResult.add(rs.getString(9));
				tempResult.add(rs.getString(10));
				tempResult.add(rs.getString(11));
				tempResult.add(rs.getString(12));
				tempResult.add(rs.getString(13));
				tempResult.add(rs.getString(14));
				tempResult.add(rs.getString(15));
				tempResult.add(rs.getString(16));
				tempResult.add(rs.getString(17));
				tempResult.add(rs.getString(18));
				tempResult.add(rs.getString(19));

				result.add(tempResult);

			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return result;
	}

	public void addJsonData(String sql, List<questionMessage> questionMessageList) {

		for (questionMessage qm : questionMessageList) {
			try {
				pst = conn.prepareStatement(sql); // 准备执行语句
				pst.setString(1, qm.getQuestionId());
				pst.setString(2, qm.getQusetion());

				pst.setString(3, qm.getApi());
				pst.setString(4, qm.getRelevance());
				pst.executeUpdate();

			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

	}

	public List<APIDocMsg> getAPIDocMsgDescription(List<APIDocMsg> termsDocsArray, List<String> allTerms, String sql)
			throws SQLException {
		stmt = conn.createStatement();
		rs = stmt.executeQuery(sql);
		while (rs.next()) {
			APIDocMsg APIDocMsgData = new APIDocMsg();
			String Description = rs.getString(5);

			if (Description == "" || Description == null) {
				continue;
			}
			String[] temp = Description.split(" ");
			for (String tempStr : temp) {
				if (!allTerms.contains(tempStr)) {
					if (!tempStr.equals("")) {
						allTerms.add(tempStr);
					}
				}
			}
			APIDocMsgData.setID(rs.getInt(1));
			APIDocMsgData.setClassName(rs.getString(2));
			APIDocMsgData.setMethodName(rs.getString(3));
			APIDocMsgData.setParameterInfo(rs.getString(4));
			APIDocMsgData.setMethodDescription(Description);
			APIDocMsgData.setModifierType(rs.getString(6));

			termsDocsArray.add(APIDocMsgData);

		}
		return termsDocsArray;
	}

	public List<FileData> getAPILibraryAnnotation(List<FileData> termsDocsArray, List<String> allTerms, String sql)
			throws SQLException {

		stmt = conn.createStatement();
		rs = stmt.executeQuery(sql);
		while (rs.next()) {
			FileData APILibraryData = new FileData();
			String annotaion = rs.getString(4);
			if (annotaion == "" || annotaion == null) {
				continue;
			}

			String[] temp = annotaion.split(" ");
			for (String tempStr : temp) {
				if (!allTerms.contains(tempStr)) {
					if (!tempStr.equals("")) {
						allTerms.add(tempStr);
					}
				}
			}

			APILibraryData.setID(rs.getInt(1));
			APILibraryData.setMethodName(rs.getString(2));
			APILibraryData.setParameterName(rs.getString(3));
			APILibraryData.setAnnotation(annotaion);// annotaion
			APILibraryData.setAPI(rs.getString(5));
			termsDocsArray.add(APILibraryData);
		}

		return termsDocsArray;

	}

	public List<ClusterMsg> getClusterAnnotation(List<ClusterMsg> termsDocsArray, List<String> allTerms, String sql) {

		try {
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);
			while (rs.next()) {

				ClusterMsg clusterMsg = new ClusterMsg();

				String Annotation = rs.getString(4);

				if (Annotation == "" || Annotation == null) {
					continue;
				}
				String[] Annotations = Annotation.split("&");

				for (String str : Annotations) {
					String[] temp = str.split(" ");
					for (String tempStr : temp) {
						if (!allTerms.contains(tempStr)) {
							if (!tempStr.equals("")) {
								allTerms.add(tempStr);
							}
						}
					}
				} // end for

				List<String> temp = new LinkedList<String>();

				for (String str : Annotations) {
					String[] temp2 = str.split(" ");
					for (String tempStr : temp2) {
						if (!tempStr.equals("")) {
							temp.add(tempStr);
						}
					}

				}
				clusterMsg.setClusterID(rs.getInt(1));

				clusterMsg.setClusterMethodName(rs.getString(2));

				clusterMsg.setClusterParameter(rs.getString(3));
				clusterMsg.setClusterAnnotation(temp);

				clusterMsg.setClusterAPI(rs.getString(5));
				termsDocsArray.add(clusterMsg);

			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return termsDocsArray;

	}

	public FileData findAPIMsgData(String sql) {
		FileData fd = new FileData();
		try {
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);

			while (rs.next()) {
				if (rs.getString("API") != null) {
					fd.setAnnotation(rs.getString("Annotation"));
					fd.setAPI(rs.getString("API"));
					fd.setMethodName(rs.getString("MethodName"));
					fd.setParameterName(rs.getString("ParameterName"));
					fd.setID(rs.getInt("ID"));
					return fd;
				}
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return fd;
	}

	public void participleData(String sql, String tableName) {
		// 好像用不了
		try {
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);
			while (rs.next()) {
				String annotation = rs.getString("Annotation");
				annotation = new participle().participleLine(annotation);

				String sql2 = "update " + tableName + " set Annotation = " + "\"" + annotation + "\"" + "where ID = "
						+ rs.getInt("ID");
				System.out.println(sql2);
				stmt.executeUpdate(sql2);
				;

			}

			stmt.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public List<ClusterMsg> findClusterData(String sql) {
		List<ClusterMsg> ClusterMsgList = new LinkedList<ClusterMsg>();
		try {
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);
			while (rs.next()) {
				List<String> temp = new LinkedList<String>();
				String Annotation = rs.getString("ClusterAnnotation");
				temp.add(Annotation);
				ClusterMsg cm = new ClusterMsg(rs.getInt("ClusterID"), rs.getString("ClusterMethodName"),
						rs.getString("ClusterParameterName"), temp, rs.getString("ClusterAPI"));
				ClusterMsgList.add(cm);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return ClusterMsgList;
	}

	public List<questionMessage> findQuestionData(String sql) {
		List<questionMessage> questionList = new LinkedList<questionMessage>();
		try {
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);

			while (rs.next()) {
				questionMessage qm = new questionMessage(rs.getString("questionID"), rs.getString("question"),
						rs.getString("API"), rs.getString("relevance"));
				questionList.add(qm);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return questionList;
	}

	public void writeClusterToDatabase(List<Cluster> clusters, List<FileData> SpringdataList, String sql) {

		int count = 0;
		int count0 = 0;

		for (Cluster cl : clusters) {
			count0++;
			System.out.println(count0);
			try {
				pst = conn.prepareStatement(sql); // 准备执行语句
				pst.setInt(1, count0);

			} catch (SQLException e) {
				e.printStackTrace();
			}

			List<Integer> tempDps = cl.getDataPoints();
			String methodName = "", parameterName = "", annotation = "", API = "";
			for (int tempdp : tempDps) {
				methodName = methodName + SpringdataList.get(tempdp).getMethodName() + "&";
				parameterName = parameterName + SpringdataList.get(tempdp).getParameterName() + "&";
				annotation = annotation + SpringdataList.get(tempdp).getAnnotation() + "&";
				API = API + SpringdataList.get(tempdp).getAPI() + "&";

			}

			try {
				pst.setString(2, methodName);
				pst.setString(3, parameterName);
				pst.setString(4, annotation);
				pst.setString(5, API);
				pst.executeUpdate();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

	}

}
