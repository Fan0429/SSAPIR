package Cluster;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import Frame.FileData;
import uk.ac.shef.wit.simmetrics.similaritymetrics.AbstractStringMetric;
import uk.ac.shef.wit.simmetrics.similaritymetrics.Levenshtein;

public class simmetrics2 {

	public double[][] CalSimMatrix(List<FileData> fileData) throws IOException {

		List<String> nameList = new LinkedList<String>();
		List<String> parameterNameList = new LinkedList<String>();
		List<String> annotationList = new LinkedList<String>();
		List<String> APIList = new LinkedList<String>();
		

		for (int i = 0; i < fileData.size(); i++) {
			nameList.add(fileData.get(i).getMethodName());
			parameterNameList.add(fileData.get(i).getParameterName());//并没有考虑参数在里面
			annotationList.add(fileData.get(i).getAnnotation());	
			APIList.add(fileData.get(i).getAPI());
		}
		
		
		double[][] sim = new double[nameList.size()][nameList.size()];

		for (int i = 0; i < nameList.size(); i++) {
			for (int j = 0; j < nameList.size(); j++) {
				String[] names = new String[2];
				names[0] = nameList.get(i);
				names[1] = nameList.get(j);
				String[] APIs = new String[2];
				APIs[0] = APIList.get(i);
				APIs[1] = APIList.get(j);

				
				
				sim[i][j] = calculateAPIsSimilarity(APIs) * 0.5	+ calculateNameSimilarity(names) * 0.5;
				
			}
		}
		
		return sim;
	}

	public void printSimMatrix(double[][] Matrix) {
		for(int i =0;i<Matrix.length;i++) {
			for(int j =0;j<Matrix.length;j++) {
				System.out.println(Matrix[i][j]);
			}
		}
	}
	
	private static float calculate() {
		return 0;}
	private static float calculateAPIsSimilarity(String[] APIs) {

		if (APIs.length != 2) {// the expected num is 2 ,we compair them and get the score
			throw new IllegalArgumentException("Expected number of arguments: " + 2);
		}
		if (APIs[0] == null || APIs[1] == null) {
			return 0.0f;
		}

		HashSet set = new HashSet();
		
		String[] API0 = APIs[0].split(" ");
		String[] API1 = APIs[1].split(" ");//注意在这里你对API进行了删减？
		for (String s : API0) {
//			s=s.substring(0, s.lastIndexOf("#"));
//			s=s.substring(0,s.lastIndexOf("."));
			if(s.contains("(")) {
				s= s.substring(0,s.indexOf("("));
				set.add(s);
			}
			
		}
		for (String s : API1) {
//			s=s.substring(0, s.lastIndexOf("#"));
//			s=s.substring(0,s.lastIndexOf("."));
			if(s.contains("(")) {
				s= s.substring(0,s.indexOf("("));
				set.add(s);
			}
		}

		float count = (float) (API0.length + API1.length - set.size()) / (float) (set.size());

		return count;

	}

	private static float calculateNameSimilarity(String[] names) {

		if (names.length != 2) {// the expected num is 2 ,we compair them and get the score
			throw new IllegalArgumentException("Expected number of arguments: " + 2);
		}
		if (names[0] == null || names[1] == null) {
			return 0.0f;
		}

		String name1 = names[0].toLowerCase();
		String name2 = names[1].toLowerCase();

		name1 = name1.replaceAll("[^\\dA-Za-z ]", "");
		name2 = name2.replaceAll("[^\\dA-Za-z ]", "");

		AbstractStringMetric metric = new Levenshtein();// here is the argiuism
		return metric.getSimilarity(name1, name2);
	}

}
