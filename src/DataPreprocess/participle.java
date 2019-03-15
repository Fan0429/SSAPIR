package DataPreprocess;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

//import ComparedApproachFrame.PorterStemmer;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.ling.CoreAnnotations.LemmaAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TokensAnnotation;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.util.CoreMap;
import Frame.StanfordCoreNLPTokenizeComment;

public class participle {
	
//	public static void main(String[] args) throws IOException {
//		
//		String qurie = "happiness  is tests failed happys";
//		
//		participle p = new participle();
//		p.participleLine(qurie);
//		
//	}
/*	public String ComparedparticipleLine(String qurie) {
		List<String> temp = filterComment2(qurie);//tokenization
		
		
		String tempStr = "";
		
		for(String str :temp) {
			
			str=new PorterStemmer().porterstem(str);
			//System.out.println(tempStr);
			tempStr = tempStr+str+" ";
			
		}
		
		return tempStr;
		
	}
*/	
	public List<String> filterComment2(String comment) {
		
		
		StanfordCoreNLPTokenizeComment tokenize = new StanfordCoreNLPTokenizeComment(comment);
		List<String> tokenizedComment = tokenize.getTokens();
		return tokenizedComment;
	}
	
	
	
	public String  participleLine(String qurie) {
		
		List<String> temp = filterComment(qurie);
		
		String tempStr = "";
		
		for(String str :temp) {
			tempStr = tempStr +str +" ";
			tempStr=Stemming2(tempStr);
		}
		tempStr=tempStr.replace("@","");
		
		String[] temp2 = tempStr.split(" ");
		try {
			temp=removeStopWord(temp2);
			tempStr = "";
			for(String str :temp) {
				tempStr = tempStr +str +" ";
				
			}
			
			
		} catch (IOException e) {
			 
			e.printStackTrace();
		}
		return tempStr;
	}

//	public List<String> Stemming(String FilePath) {
//		
//		 List<String> StemmedWordList =new LinkedList<String>();
//		 
//			String[] a  = {"File\\a.txt"};
//			StemmedWordList= Stemmer.main(a);
//		 return StemmedWordList;
//	}
	
	public String Stemming2(String inputStr) {  
        Properties props = new Properties();  
        props.setProperty("annotators", "tokenize, ssplit, pos, lemma");  
        StanfordCoreNLP pipeline = new StanfordCoreNLP(props);  
  
        Annotation document = new Annotation(inputStr);  
        pipeline.annotate(document);  
        List<CoreMap> sentences = document.get(SentencesAnnotation.class);  
  
        String outputStr = "";  
        for (CoreMap sentence : sentences) {  
            // traversing the words in the current sentence  
            // a CoreLabel is a CoreMap with additional token-specific methods  
            for (CoreLabel token : sentence.get(TokensAnnotation.class)) {  
                String lema = token.get(LemmaAnnotation.class);  
                outputStr += lema+" ";  
            }  
  
        }  
        return outputStr;  
    }  
	
	
	public List<String> removeStopWord(String[] tokenizedComment) throws IOException {
		BufferedReader br = new BufferedReader(
				new InputStreamReader(new FileInputStream(new File("File\\English stop words.txt"))));
		String line = "";
		List<String> stopedWord = new LinkedList<String>();
		List<String> removedStopedWordCommon = new LinkedList<String>();
		while ((line = br.readLine()) != null) {
			stopedWord.add(line);
		}
		for (String str : tokenizedComment) {
			boolean flag = true;
			for (String word : stopedWord) {
				// System.out.println(word);
				if (str.equals(word)) {
					flag = false;
					break;
				}
			}
			if (flag) {
				removedStopedWordCommon.add(str);
			}
		}
		return removedStopedWordCommon;
	}

	
	public List<String> filterComment(String comment) {
		
		comment = comment.toLowerCase();
		StanfordCoreNLPTokenizeComment tokenize = new StanfordCoreNLPTokenizeComment(comment);
		List<String> tokenizedComment = tokenize.getTokens();
		// System.out.println(tokenizedComment);
		return tokenizedComment;
	}


	

}
