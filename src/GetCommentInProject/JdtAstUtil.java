package GetCommentInProject;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;

import Frame.StanfordCoreNLPTokenizeComment;




public class JdtAstUtil {
	/**
	 * get compilation unit of source code
	 * 
	 * @param javaFilePath
	 * @return CompilationUnit
	 */
	private static String sourceString;
	public static CompilationUnit getCompilationUnit(String javaFilePath) {
		byte[] input = null;
		try {
			BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(javaFilePath));
			input = new byte[bufferedInputStream.available()];
			bufferedInputStream.read(input);
			bufferedInputStream.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		setSourceString(new String(input));
		ASTParser astParser = ASTParser.newParser(AST.JLS8);
		astParser.setKind(ASTParser.K_COMPILATION_UNIT);
		astParser.setSource(new String(input).toCharArray());

		astParser.setSource(sourceString.toCharArray()); 
		CompilationUnit result = (CompilationUnit) (astParser.createAST(null));

		return result;
	}

	public String getOnelineDoc(String annotation) {//no use ,we use mrYan's code
		List<String> s = new LinkedList<String>();
//
//		annotation = "/**\r\n"
//				+ " * NTPServerTimeProvider is a physical time provider that uses external NTP servers to fetch timestamp\r\n"
//				+ " * periodically (this.a currently hardcoded as 1 minute).\r\n" + " */";
		
		annotation = annotation.substring(annotation.indexOf("/**") + 3, annotation.lastIndexOf("*/"));
		annotation = annotation.replace("*", "");

		StanfordCoreNLPTokenizeComment obj = new StanfordCoreNLPTokenizeComment(annotation);
		s=obj.getTokens();
		String str= "";
		for(int i = 0;i<s.size();i++) {
			str=str+s.get(i)+" ";
			if(s.get(i).equals(".")||s.get(i+1).contains("@"))
				break;
		}
		//System.out.println(str);
		return str;
	}
	
	
	public static String getSourceString() {
		return sourceString;
	}
	
	public static void setSourceString(String sourceString) {
		JdtAstUtil.sourceString = sourceString;
	}
	
}
