package Frame;

import java.util.LinkedList;
import java.util.List;

public class questionMessage {
	
	private String questionId;
	private String qusetion;
	private String api;
	private String relevance;
	
	
	questionMessage(){};
	
	public questionMessage( String questionId,String qusetion, String api, String relevance) {

		setQuestionId(questionId);
		setQusetion(qusetion);
		setApi(api);
		setRelevance(relevance);
		
	}
	public String getQusetion() {
		return qusetion;
	}
	public void setQusetion(String qusetion) {
		this.qusetion = qusetion;
	}
	public String getQuestionId() {
		return questionId;
	}
	public void setQuestionId(String questionId) {
		this.questionId = questionId;
	}
	public String getApi() {
		return api;
	}
	public void setApi(String api2) {
		this.api = api2;
	}
	public String getRelevance() {
		return relevance;
	}
	public void setRelevance(String relevance) {
		this.relevance = relevance;
	}
	
	
	
}
