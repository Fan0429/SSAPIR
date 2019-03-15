package ComparedApproachFrame;

public class ComparedResultMsg {

	private double Similarity;
	private String APIs;
	
	public ComparedResultMsg() {};
	
	
	public ComparedResultMsg(double similarity, String aPIs) {
		super();
		Similarity = similarity;
		APIs = aPIs;
	}
	public double getSimilarity() {
		return Similarity;
	}
	public void setSimilarity(double similarity) {
		Similarity = similarity;
	}
	public String getAPIs() {
		return APIs;
	}
	public void setAPIs(String aPIs) {
		APIs = aPIs;
	}
	
	
}
