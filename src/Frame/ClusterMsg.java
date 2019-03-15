package Frame;

import java.util.List;

public class ClusterMsg {

	private int ClusterID;
	private String ClusterMethodName;
	private String ClusterParameter;
	private List<String> ClusterAnnotation;
	private String ClusterAPI;
	
	
	public ClusterMsg() {};
	
	public ClusterMsg(int clusterID, String clusterMethodName, String clusterParameter, List<String> clusterAnnotation,
			String clusterAPI) {
		
		ClusterID = clusterID;
		ClusterMethodName = clusterMethodName;
		ClusterParameter = clusterParameter;
		ClusterAnnotation = clusterAnnotation;
		ClusterAPI = clusterAPI;
	}
	
	public int getClusterID() {
		return ClusterID;
	}
	public void setClusterID(int clusterID) {
		ClusterID = clusterID;
	}
	public String getClusterMethodName() {
		return ClusterMethodName;
	}
	public void setClusterMethodName(String clusterMethodName) {
		ClusterMethodName = clusterMethodName;
	}
	public String getClusterParameter() {
		return ClusterParameter;
	}
	public void setClusterParameter(String clusterParameter) {
		ClusterParameter = clusterParameter;
	}
	public List<String> getClusterAnnotation() {
		return ClusterAnnotation;
	}
	public void setClusterAnnotation(List<String> clusterAnnotation) {
		ClusterAnnotation = clusterAnnotation;
	}
	public String getClusterAPI() {
		return ClusterAPI;
	}
	public void setClusterAPI(String clusterAPI) {
		ClusterAPI = clusterAPI;
	}
	
	
	
	
}
