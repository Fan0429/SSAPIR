package ComparedApproachFrame;

public class APIScore {

	
	private String api;
	private double score;
	
	public APIScore() {};
	
	public APIScore(String api, double score) {
		super();
		this.api = api;
		this.score = score;
	}
	public String getApi() {
		return api;
	}
	public void setApi(String api) {
		this.api = api;
	}
	public double getScore() {
		return score;
	}
	public void setScore(double score) {
		this.score = score;
	}
	
	
}
