package GetCommentInProject;

import java.util.ArrayList;
import java.util.List;

import Frame.StanfordCoreNLPTokenizeComment;

public class DocFilter {
	private boolean flag=true;
	public List<String> filterComment(String comment){
		String temp=filterCommentBeforeTokenization(comment);
		List<String> tokenizedComment=tokenizeComment(temp);
		return filterCommentAfterTokenization(tokenizedComment);
	}
	private String filterCommentBeforeTokenization(String comment){//filter before tokenization
		comment=comment.replace("}", "");
		comment=comment.replace("{", "");
		comment=comment.replace("`", "'");
		comment=comment.trim();
		if(comment.contains("/**")&&comment.contains("*/"))
			comment=comment.substring(comment.indexOf("/**")+3,comment.lastIndexOf("*/")).trim();
		comment=cutEachLine(comment);
		if(comment.matches("[^\\|]+(\\|[^\\|]+)+"))//..|..|..|..|.. 既然是生成comment，前提就要一定程度上保证comment是自然语言，而不是一堆的数学符号
			comment="";
		if(comment.startsWith("//TODO:"))//TODO:
			comment="";
		while((comment.startsWith("*")||comment.startsWith("/"))&&!comment.isEmpty())//* ...    //**************  ... 
			comment=comment.substring(1).trim();
		int index=comment.indexOf("<p/>");
		if(index>-1)
			comment=comment.substring(0,index);
		index=comment.indexOf("<br");
		if(index>-1)
			comment=comment.substring(0,index);
		return comment;
	}
	private String cutEachLine(String comment){
		String[]lines=comment.split("\n");
		String temp="";
		for(String line:lines){
			line=line.trim();
			while((line.startsWith("*")||line.startsWith("/"))&&!line.isEmpty())//* ...    //**************  ... 
				line=line.substring(1).trim();
			temp+=line+" ";
		}
		return temp.trim();
		
	}
	private List<String> cutFirstSentence(List<String> commentTokens){
		
		
		String[]keywords=new String[]{".","!","?"};
		boolean isCutted=false;
	    List<String> cuttedCommentTokens=new ArrayList<String>();
		for(String str:keywords){
			int index=commentTokens.indexOf(str);
			if(index>-1){
				cuttedCommentTokens=commentTokens.subList(0, index);
				isCutted=true;
				break;
			}	
		}
		if(!isCutted){
			cuttedCommentTokens.addAll(commentTokens);
		}
	    return cuttedCommentTokens;
		
	}
	
	private List<String> tokenizeComment(String comment){
		StanfordCoreNLPTokenizeComment tokenize=new StanfordCoreNLPTokenizeComment(comment);
		return tokenize.getTokens();
	}
	private List<String> filterCommentAfterTokenization(List<String> commentTokens){//首先做的处理
		List<String> filteredTokens=new ArrayList<String>();
		commentTokens=cutFirstSentence(commentTokens);
		while(!commentTokens.isEmpty()&&commentTokens.get(0).matches("<.+>"))//第一个token符合正则表达式<.+>	
			commentTokens.remove(0);
		if(commentTokens.size()>=2&&commentTokens.get(1).equals(":")){//TOKEN :
			commentTokens.remove(0);
			commentTokens.remove(0);
		}
		if(commentTokens.size()>=1){//将开头的token小写化
			String temp=commentTokens.get(0);
			if(Character.isUpperCase(temp.charAt(0)))
				commentTokens.set(0, temp.toLowerCase());
		}
		for(String token:commentTokens){
			if(token.startsWith("#"))
				token=token.substring(1);
			if(token.equals("``"))
				token="''";
			if(token.equals("`"))
				token="'";
//			if(token.matches("<!.+>"))
//					continue;
			if(token.matches("<a href.+"))
				token="<__HyperLink__>";
			if(token.matches("https?\\://.+"))
				token="<__HyperLink__>";
			if(token.matches("[A-Za-z]+/[A-Za-z]+")){	//Apply find/replace regexes to our WM template 
				filteredTokens.add(token.substring(0,token.indexOf("/")));
				filteredTokens.add("and");
				token=token.substring(token.indexOf("/")+1);
			}
			if(token.matches("<.+>")&&!token.equals("<__HyperLink__>"))//<script?../>//<img?src="https://www.codenameone.com/img/developer-guide/box-layout-x.png"?alt="Box?Layout?X"?/> 
				continue;
			filteredTokens.add(token);
		}
		return filteredTokens;
	}
	public boolean isFlag() {
		return flag;
	}
	public void setFlag(boolean flag) {
		this.flag = flag;
	}



	
	//Writes < tag > value < / tag > to output stream 
	

	
	//Hier werden die verschiedenen Moeglichen Werte erkannt und jenachdem wird mit der passenden Methode weitergefahren <br?/> EBNF : <br?/> string | number | dynamic | <NAME_3> ; 


}
