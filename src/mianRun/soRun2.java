package mianRun;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import DataPreprocess.DatabaseConnect;
import Frame.ClusterMsg;
import Frame.questionMessage;
import TFIDF.TfIdfMain;

public class soRun2 {

	public static void main(String[] args) throws IOException, SQLException {
		// String clusterName = "cluster2";
		// String sql= "select * from question";
		// DatabaseConnect dbconn = new DatabaseConnect();
		// List<questionMessage> qm =dbconn.findQuestionData(sql);
		//
		// for(int i=0;i<1;i++) { //qm.size()
		// System.out.println(qm.get(i).getQusetion());
		// new
		// TfIdfMain().getAnnotationScore(qm.get(i).getQusetion(),qm.get(i).getApi(),i+1,clusterName);
		// }

		String clusterName = "clusterslf4j";
		String sql = "select * from " + clusterName;
		String writeFilePath = "I:\\DATA\\Try12\\resultslf4j.txt";
		String resultName= "Resultslf4j";
		 
		DatabaseConnect dbconn = new DatabaseConnect();
		List<ClusterMsg> cm = dbconn.findClusterData(sql);

		for (int i = 0; i < cm.size(); i++) { // cm.size()
			//System.out.println(cm.get(i).getClusterAnnotation().get(0).replace("&", ""));
			new TfIdfMain().getAnnotationScore(cm.get(i).getClusterAnnotation().get(0).replace("&", ""),
					cm.get(i).getClusterAPI(), i + 1, clusterName,writeFilePath,resultName);
			
		}

	}
}
