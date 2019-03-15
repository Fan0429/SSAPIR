/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package TFIDF;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;

import DataPreprocess.DatabaseConnect;




public class TfIdfMain {
    
	public void getAnnotationScore(String query,String API ,int count,String clusterName,String writeFilePath, String resultName) throws IOException, SQLException{
       
   
		DocumentParser dp = new DocumentParser();
        DatabaseConnect dbconn = new DatabaseConnect();
        
        
        dp.parseFiles(query,dbconn,clusterName);
        dp.tfIdfCalculator(); //calculates tfidf
        dp.getCosineSimilarityOurs(count,writeFilePath,query,API,resultName); //calculated cosine similarity ]
        
        
        
	}
  
    
    
}
