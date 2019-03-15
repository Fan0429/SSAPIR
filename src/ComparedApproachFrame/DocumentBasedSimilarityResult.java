package ComparedApproachFrame;

public class DocumentBasedSimilarityResult {

	private APIDocMsg APIDocMsgData;
	private  double similarity;
	
	
	public DocumentBasedSimilarityResult(APIDocMsg aPIDocMsgData, double similarity) {
		super();
		APIDocMsgData = aPIDocMsgData;
		this.similarity = similarity;
	}
	public APIDocMsg getAPIDocMsgData() {
		return APIDocMsgData;
	}
	public void setAPIDocMsgData(APIDocMsg aPIDocMsgData) {
		APIDocMsgData = aPIDocMsgData;
	}
	public double getSimilarity() {
		return similarity;
	}
	public void setSimilarity(double similarity) {
		this.similarity = similarity;
	}
	
	
}
