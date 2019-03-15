/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package TFIDF;

import java.util.List;

import Frame.ClusterMsg;
import Frame.FileData;

//<editor-fold defaultstate="collapsed" desc="TFIDF calculator">
/**
 * Class to calculate TfIdf of term.
 * @author Mubin Shrestha
 */
public class TfIdf {
    
    //<editor-fold defaultstate="collapsed" desc="TF Calculator">
    /**
     * Calculated the tf of term termToCheck
     * @param totalterms : Array of all the words under processing document
     * @param termToCheck : term of which tf is to be calculated.
     * @return tf(term frequency) of term termToCheck
     */
    public double tfCalculator(List<String> totalterms, String termToCheck) {
    	
        double count = 0;  //to count the overall occurrence of the term termToCheck
        for (String s : totalterms) {
            if (s.equalsIgnoreCase(termToCheck)) {
                count++;
            }
        }
        return count / totalterms.size();
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Idf Calculator">
    /**
     * Calculated idf of term termToCheck
     * @param allTerms : all the terms of all the documents
     * @param termToCheck
     * @return idf(inverse document frequency) score
     */
    public double idfCalculator(List<ClusterMsg> allTerms, String termToCheck) {
        double count = 0;
        for (ClusterMsg APILibraryData : allTerms) {
        	List<String> ss = APILibraryData.getClusterAnnotation();
            for (String s : ss) {
            	
                if (s.equalsIgnoreCase(termToCheck)) {
                    count++;
                    break;
                }
            }
        }
     
        return Math.log(allTerms.size() / count);
    }
//</editor-fold>
}
//</editor-fold>
