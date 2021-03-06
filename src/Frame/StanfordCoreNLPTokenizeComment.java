package Frame;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.ling.CoreAnnotations.LemmaAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.PartOfSpeechAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TextAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TokensAnnotation;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.util.CoreMap;

public class StanfordCoreNLPTokenizeComment {
	private List<String> tokens=new ArrayList<String>();
	
	public List<String> getTokens() {
		return tokens;
	}

	public void setTokens(List<String> tokens) {
		this.tokens = tokens;
	}
	public StanfordCoreNLPTokenizeComment(String comment){
		tokenizeComment(comment);
	}

	private List<String> tokenizeComment(String comment){
		
			Properties props = new Properties();
	     //   props.setProperty("annotators", "tokenize, ssplit, pos, lemma, ner, parse, dcoref");
//	        props.setProperty("annotators", "tokenize,ssplit,pos,lemma");
			
			props.setProperty("annotators", "tokenize,ssplit");
	        StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
	        // read some text in the text variable
//	        String comment = "She went to America last week.";// Add your text here!
	        // create an empty Annotation just with the given text
	        Annotation document = new Annotation(comment);

	        // run all Annotators on this text
	        pipeline.annotate(document);
	        // these are all the sentences in this document
	        // a CoreMap is essentially a Map that uses class objects as keys and
	        // has values with custom types
	        List<CoreMap> sentences = document.get(SentencesAnnotation.class);
	        List<String> words = new ArrayList<>();

	        for (CoreMap sentence : sentences) {
	            // traversing the words in the current sentence
	            // a CoreLabel is a CoreMap with additional token-specific methods
	            for (CoreLabel token : sentence.get(TokensAnnotation.class)) {
	                // this is the text of the token
	                String word = token.get(TextAnnotation.class);
	                words.add(word);
	            }
	        }

	        this.tokens.addAll(words);
	        return words;
	}
	

}
