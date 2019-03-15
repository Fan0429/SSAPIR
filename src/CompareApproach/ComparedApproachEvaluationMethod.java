package CompareApproach;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

import ComparedApproachFrame.ComparedOneQueryResultMsg;
import ComparedApproachFrame.ComparedResultMsg;
import DataPreprocess.DatabaseConnect;
import Frame.OneQueryResultMsg;
import net.sf.morph.transform.copiers.dsl.A;

public class ComparedApproachEvaluationMethod {

	public static void main(String[] args) {
		
		String ResultLibrary="resultComparedlog4j";
		getResult(ResultLibrary);
		CalculateAccuracyTop1();
		CalculateAccuracyTop5();
		CalculateAccuracyTop10();
		CalculateAccuracyTop15();
	}
	private static List<ComparedOneQueryResultMsg> QueryResultList = new LinkedList<ComparedOneQueryResultMsg>();
	
	public static void getResult(String ResultLibrary) {

		String sql = " select * from "+ResultLibrary;
		List<List<String>> result = new LinkedList<List<String>>();

		result = new DatabaseConnect().getRseult(sql);

		for (List<String> oneResult : result) {
			ComparedOneQueryResultMsg queryResult = new ComparedOneQueryResultMsg();
			queryResult.setQuery(oneResult.get(0));
			queryResult.setCorrectAPI(oneResult.get(1).replace("&", ""));
			queryResult.setResult1(splitResult(oneResult.get(2)));
			queryResult.setResult2(splitResult(oneResult.get(3)));
			queryResult.setResult3(splitResult(oneResult.get(4)));
			queryResult.setResult4(splitResult(oneResult.get(5)));
			queryResult.setResult5(splitResult(oneResult.get(6)));
			queryResult.setResult6(splitResult(oneResult.get(7)));
			queryResult.setResult7(splitResult(oneResult.get(8)));
			queryResult.setResult8(splitResult(oneResult.get(9)));
			queryResult.setResult9(splitResult(oneResult.get(10)));
			queryResult.setResult10(splitResult(oneResult.get(11)));
			queryResult.setResult11(splitResult(oneResult.get(12)));
			queryResult.setResult12(splitResult(oneResult.get(13)));
			queryResult.setResult13(splitResult(oneResult.get(14)));
			queryResult.setResult14(splitResult(oneResult.get(15)));
			queryResult.setResult15(splitResult(oneResult.get(16)));
			QueryResultList.add(queryResult);

		}
	}

	public static ComparedResultMsg splitResult(String result) {
		String[] tempResult = result.split("\\*");

		ComparedResultMsg comparedResultMsg = new ComparedResultMsg();
		comparedResultMsg.setSimilarity(Double.valueOf(tempResult[0]));
		comparedResultMsg.setAPIs(tempResult[1]);
		

		return comparedResultMsg;
	}
	
	

	public static double CalculateAccuracyTop1() {//We should change this to automatically generate TOPK results, rather than manually delete them.
		List<Double> Accuracy =  new LinkedList<Double>();
		double precision = 0.0;
		
		for(ComparedOneQueryResultMsg result: QueryResultList) {
			String[] correctAPIs = result.getCorrectAPI().split(" ");
			for(String correctAPI:correctAPIs) {
				correctAPI = correctAPI.substring(correctAPI.indexOf("#")+1);
				//System.out.println(correctAPI + "    "+ result.getResult1().getAPIs());
				if(result.getResult1().getAPIs().contains(correctAPI)) {
					Accuracy.add(1.0);
					break;
					}
				else {
					Accuracy.add(0.0);
				}
			}
		}
		
		 for(double temp :Accuracy) {
			 precision=precision+temp;
			
		 }
		 System.out.println(precision/Accuracy.size());
		 
		return precision/Accuracy.size();
	
		
	}
	
	public static double CalculateAccuracyTop5() {//We should change this to automatically generate TOPK results, rather than manually delete them.
		List<Double> Accuracy =  new LinkedList<Double>();
		double precision = 0.0;
		for(ComparedOneQueryResultMsg result: QueryResultList) {
			String[] correctAPIs = result.getCorrectAPI().split(" ");

			for(String correctAPI:correctAPIs) {
				correctAPI = correctAPI.substring(correctAPI.indexOf("#")+1);
				if(result.getResult1().getAPIs().contains(correctAPI)) {
					Accuracy.add(1.0);
					break;
					}
				else if(result.getResult2().getAPIs().contains(correctAPI)) {
					Accuracy.add(1.0);
					break;
					}
				else if(result.getResult3().getAPIs().contains(correctAPI)) {
					Accuracy.add(1.0);
					break;
					}
				else if(result.getResult4().getAPIs().contains(correctAPI)) {
					Accuracy.add(1.0);
					break;
					}
				else if(result.getResult5().getAPIs().contains(correctAPI)) {
					Accuracy.add(1.0);
					break;
					}
				else {
					Accuracy.add(0.0);
				}
			}
		}
		
		 for(double temp :Accuracy) {
			 precision=precision+temp;
			
		 }
		 System.out.println(precision/Accuracy.size());
		 
		return precision/Accuracy.size();
	
		
	}
	
	public static double CalculateAccuracyTop10() {//We should change this to automatically generate TOPK results, rather than manually delete them.
		List<Double> Accuracy =  new LinkedList<Double>();
		double precision = 0.0;
		for(ComparedOneQueryResultMsg result: QueryResultList) {
			String[] correctAPIs = result.getCorrectAPI().split(" ");
			
			
			for(String correctAPI:correctAPIs) {
				correctAPI = correctAPI.substring(correctAPI.indexOf("#")+1);
				if(result.getResult1().getAPIs().contains(correctAPI)) {
				
					Accuracy.add(1.0);
					break;
					}
				else if(result.getResult2().getAPIs().contains(correctAPI)) {
					Accuracy.add(1.0);
					break;
					}
				else if(result.getResult3().getAPIs().contains(correctAPI)) {
					Accuracy.add(1.0);
					break;
					}
				else if(result.getResult4().getAPIs().contains(correctAPI)) {
					Accuracy.add(1.0);
					break;
					}
				else if(result.getResult5().getAPIs().contains(correctAPI)) {
					Accuracy.add(1.0);
					break;
					}
				else if(result.getResult6().getAPIs().contains(correctAPI)) {
					Accuracy.add(1.0);
					break;
					}
				else if(result.getResult7().getAPIs().contains(correctAPI)) {
					Accuracy.add(1.0);
					break;
					}
				else if(result.getResult8().getAPIs().contains(correctAPI)) {
					Accuracy.add(1.0);
					break;
					}
				else if(result.getResult9().getAPIs().contains(correctAPI)) {
					Accuracy.add(1.0);
					break;
					}
				else if(result.getResult10().getAPIs().contains(correctAPI)) {
					Accuracy.add(1.0);
					break;
					}
				else {
					Accuracy.add(0.0);
				}
			}
		}
		
		 for(double temp :Accuracy) {
			 precision=precision+temp;
			
		 }
		 System.out.println(precision/Accuracy.size());
		 
		return precision/Accuracy.size();
	
		
	}
	
	public static double CalculateAccuracyTop15() {//We should change this to automatically generate TOPK results, rather than manually delete them.
		List<Double> Accuracy =  new LinkedList<Double>();
		double precision = 0.0;
		for(ComparedOneQueryResultMsg result: QueryResultList) {
			String[] correctAPIs = result.getCorrectAPI().split(" ");
			
			
			for(String correctAPI:correctAPIs) {
				correctAPI = correctAPI.substring(correctAPI.indexOf("#")+1);
				if(result.getResult1().getAPIs().contains(correctAPI)) {
					Accuracy.add(1.0);
					break;
					}
				else if(result.getResult2().getAPIs().contains(correctAPI)) {
					Accuracy.add(1.0);
					break;
					}
				else if(result.getResult3().getAPIs().contains(correctAPI)) {
					Accuracy.add(1.0);
					break;
					}
				else if(result.getResult4().getAPIs().contains(correctAPI)) {
					Accuracy.add(1.0);
					break;
					}
				else if(result.getResult5().getAPIs().contains(correctAPI)) {
					Accuracy.add(1.0);
					break;
					}
				else if(result.getResult6().getAPIs().contains(correctAPI)) {
					Accuracy.add(1.0);
					break;
					}
				else if(result.getResult7().getAPIs().contains(correctAPI)) {
					Accuracy.add(1.0);
					break;
					}
				else if(result.getResult8().getAPIs().contains(correctAPI)) {
					Accuracy.add(1.0);
					break;
					}
				else if(result.getResult9().getAPIs().contains(correctAPI)) {
					Accuracy.add(1.0);
					break;
					}
				else if(result.getResult10().getAPIs().contains(correctAPI)) {
					Accuracy.add(1.0);
					break;
					}
				else if(result.getResult11().getAPIs().contains(correctAPI)) {
					Accuracy.add(1.0);
					break;
					}
				else if(result.getResult12().getAPIs().contains(correctAPI)) {
					Accuracy.add(1.0);
					break;
					}
				else if(result.getResult13().getAPIs().contains(correctAPI)) {
					Accuracy.add(1.0);
					break;
					}
				else if(result.getResult14().getAPIs().contains(correctAPI)) {
					Accuracy.add(1.0);
					break;
					}
				else if(result.getResult15().getAPIs().contains(correctAPI)) {
					Accuracy.add(1.0);
					break;
					}
			
				else {
					Accuracy.add(0.0);
				}
			}
		}
		
		 for(double temp :Accuracy) {
			 precision=precision+temp;
			
		 }
		 System.out.println(precision/Accuracy.size());
		 
		return precision/Accuracy.size();
	
		
	}

	

}
