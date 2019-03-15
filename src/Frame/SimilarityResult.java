package Frame;

public class SimilarityResult {
	
	private ClusterMsg clusterMsg ;
	private  double similarity;
	
	
	public SimilarityResult(ClusterMsg clusterMsg, double similarity) {
		super();
		this.clusterMsg = clusterMsg;
		this.similarity = similarity;
	}
	
	public ClusterMsg getClusterMsg() {
		return clusterMsg;
	}
	public void setClusterMsg(ClusterMsg clusterMsg) {
		this.clusterMsg = clusterMsg;
	}
	public double getSimilarity() {
		return similarity;
	}
	public void setSimilarity(double similarity) {
		this.similarity = similarity;
	}
	
}
