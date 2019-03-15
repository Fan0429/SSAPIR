package DataPreprocess;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.protobuf.Field;

import Frame.questionMessage;


public class getJsonFile {

//	public static void main(String[] args) {
//		String filePath = "I:\\DATA\\Try8\\experiment_questions.json";
//		getJson(filePath);
//	}

	public static void getJson(String filePath) {
		List<questionMessage> questionMessageList= new LinkedList<questionMessage>();
		
		try {

			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(new File(filePath)))) ;
			String line = null;
			while((line = br.readLine())!=null) {
				JSONObject jsonObject = new JSONObject(line);

				 String api= "";
				 String relevance="";
				 for(int i= 0;i<jsonObject.getJSONArray("reviews").length();i++) {
					 
					 String temp =jsonObject.getJSONArray("reviews").getJSONObject(i).getString("canonicalName");
					 String[] tempArray=temp.replace(", ", ",").split(" ");
					 temp=tempArray[1];
					 temp=temp.substring(0, temp.indexOf("("));
					 
					 if(temp.contains("$")) {
						 temp=temp.replace("$", ".");
						 tempArray=temp.split("\\.");
						 temp="";
						 for(int j= 0 ;j<tempArray.length-1;j++) {
							 
							 if(j==tempArray.length-2) {
								 temp=temp+"#"+tempArray[j];
							 }else if(j==0){
								 temp=temp+tempArray[j];
							 }else {
								 temp=temp+"."+tempArray[j];
							 }
						 }
					 }
					 tempArray=temp.split("\\.");//split是按照正则表达式来判定的
					 temp="";
					 for(int j= 0 ;j<tempArray.length;j++) {
						 
						 if(j==tempArray.length-1) {
							 temp=temp+"#"+tempArray[j];
						 }else if(j==0){
							 temp=temp+tempArray[j];
						 }else {
							 temp=temp+"."+tempArray[j];
						 }
					 }
					 api=api+temp+" ";
					 relevance=relevance+jsonObject.getJSONArray("reviews").getJSONObject(i).getString("relevance")+" ";
					 //System.out.println(jsonObject.getJSONArray("reviews").getJSONObject(i).getString("relevance"));
					 
				 }
				 String question = jsonObject.getString ("question");
				 question = new participle().participleLine(question);
				 question=question.replace("?", "").replace("-lsb-", "").replace("-rsb-", "").replace("-rrb-", "").replace("-lrb-", "");
				 
				 questionMessage qm = new questionMessage(jsonObject.getString ("stackOverflowQuestionId"),question,api,relevance);
				 if(qm.getApi().length()>0) {
					 questionMessageList.add(qm);
				 }
				 
			}
	
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		String sql= "insert into question values(?,?,?,?)";
		DatabaseConnect dbConn = new DatabaseConnect();
		dbConn.addJsonData(sql,questionMessageList);
		dbConn.closePreparedStatement();
		
		
		
	}
}
