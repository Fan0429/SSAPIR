package GetCommentInProject;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.dom.CompilationUnit;



public class getCommentFromOneFile {

	private List<String> javadocs=new ArrayList<String>();

	public getCommentFromOneFile(String filePath) {
		getComments(filePath);
	}
	
	
	private void getComments(String filePath) {
		
		CompilationUnit comp = JdtAstUtil.getCompilationUnit(filePath);
		String sourceString=JdtAstUtil.getSourceString();
		
		CommentVisiter visitor = new CommentVisiter(sourceString);
		System.out.println(filePath);
		comp.accept(visitor);
		setJavadocs(visitor.getTempString());
		//System.out.println(javadocs.size());
	}


	public List<String> getJavadocs() {
		return javadocs;
	}
	public void setJavadocs(List<String> javadocs) {
		this.javadocs = javadocs;
	}
	
	
}
