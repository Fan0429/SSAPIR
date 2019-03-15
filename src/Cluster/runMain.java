package Cluster;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import DataPreprocess.DatabaseConnect;
import Frame.FileData;

public class runMain {

	public static void main(String[] args) throws IOException {
//		String orginFile = "I:\\DATA\\Try5\\jrefianlly.txt";
//		new HierarchicalCluster().originalFileDeal(orginFile);
	
//		String FilePath = "I:\\DATA\\Try5\\springframework.txt";
//		String weiteFilePath = "I:\\DATA\\Try7\\Clustertest.txt";		
//														
//		double[][] sim = new simmetrics().CalSimMatrix(FilePath);
		
//		HierarchicalCluster hc = new HierarchicalCluster();
//		List<String> sentencelist = hc.getAPIList(FilePath);
//		
//		
//		List<Cluster> cluste_res1 = hc.starAnalysis(sim, 0.1);
//		hc.writeClusterToFile(weiteFilePath, cluste_res1,sentencelist);
		
		
		
		List<FileData> SpringdataList = new LinkedList<FileData>();
		DatabaseConnect dbConn = new DatabaseConnect();
		
		for(int i =0;i<1000;i++) {
			
			String sql = "select * from slf4j where ID = '  "+i+" ' ";//orginalapi
			
			if(dbConn.findAPIMsgData(sql).getAPI()!=null) {
				SpringdataList.add(dbConn.findAPIMsgData(sql)) ; // get the dataList 
			}
		}

    	
		String addSql = "insert into clusterslf4j(ClusterID,ClusterMethodName,ClusterParameterName,ClusterAnnotation,ClusterAPI)values(?,?,?,?,?)";
		
    	double[][] simMatrix = new simmetrics2().CalSimMatrix(SpringdataList); //get the sim matrix
    	
    	HierarchicalCluster hc = new HierarchicalCluster();
    	List<Cluster> cluste_res1 = hc.starAnalysis(simMatrix,0.5);
    	dbConn.writeClusterToDatabase(cluste_res1,SpringdataList,addSql);
    	dbConn.closePreparedStatement();
    	dbConn.closeResultSet();
    
    	

	}
}
