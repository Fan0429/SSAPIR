package GetCommentInProject;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.print.Doc;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.MethodDeclaration;

public class CommentVisiter extends ASTVisitor {

	private List<String> tempString = new LinkedList<String>();
	String source;

	public CommentVisiter(String source) {
		super();
		this.source = source;
	}

	@Override
	public boolean visit(MethodDeclaration node) {
		// we can get the javadoc and the method name
		if (node.getJavadoc() != null) {
			int start = node.getStartPosition();
			int end = start + node.getLength();
			String comment = source.substring(start, end);
			if ((comment.indexOf("/**") >= 0) && (comment.lastIndexOf("*/") > 1)) {
				comment = comment.substring(comment.indexOf("/**") + 3, comment.lastIndexOf("*/") - 1);
				comment = comment.replace("*", "").trim();
			}

			// 在这之前还是要把分词什么的都做了啊，在这里的comment就应该是处理好了的
			List<String> oneLineComment = new LinkedList<String>();
			oneLineComment = new DocFilter().filterComment(comment);

			String s = "";
			s = node.parameters().toString();
			s = s.substring(s.indexOf("[") + 1, s.lastIndexOf("]"));
			s = s.replace("[", "%");
			s = s.replace("]", "&");
			s = "{" + s + "}";
			s = s.replace(" ", "*");

			comment = node.getName().toString() + s + "#" + " ";
			//System.out.println(comment);

			for (String str : oneLineComment) {
				comment = comment + str + " ";
			}

			tempString.add(comment);
		}

		return true;
	}

	public List<String> getTempString() {
		return tempString;
	}

	public void setTempString(List<String> tempString) {
		this.tempString = tempString;
	}

}
