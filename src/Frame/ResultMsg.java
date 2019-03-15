package Frame;

public class ResultMsg {

	private double Similarity;
	private String annotation;
	private String APIs;
	
	public ResultMsg() {};
	
	
	
	public ResultMsg(double similarity, String annotation, String aPIs) {
		super();
		Similarity = similarity;
		this.annotation = annotation;
		APIs = aPIs;
	}
	
	public double getSimilarity() {
		return Similarity;
	}
	public void setSimilarity(double similarity) {
		Similarity = similarity;
	}
	public String getAnnotation() {
		return annotation;
	}
	public void setAnnotation(String annotation) {
		this.annotation = annotation;
	}
	public String getAPIs() {
		return APIs;
	}
	public void setAPIs(String aPIs) {
		APIs = aPIs;
	}
	
}
