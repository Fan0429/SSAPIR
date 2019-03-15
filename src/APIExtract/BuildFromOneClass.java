package APIExtract;

import java.util.Arrays;
import java.util.List;

import org.eclipse.jdt.core.IAnnotation;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.internal.core.search.JavaSearchScope;


public class BuildFromOneClass {

	private IType sourceClass;
	private JavaSearchScope searchScope;
	private List<IMethod> methods;

	private String savePath;

	public BuildFromOneClass(IType type, JavaSearchScope scope, String saveroot) throws JavaModelException {
		this.sourceClass = type;
		this.searchScope = scope;
		this.savePath = saveroot;
		// PrintoutResult.PrintInScreen("In class " +
		// this.sourceClass.getElementName());
		// 这是文件的位置 PrintoutResult.PrintInScreen("In class: " +
		// this.sourceClass.getFullyQualifiedName();
		// 这个路径，它一个类是一个文件夹了已经，不是我们想要的
		// this.savePath = saveroot +
		// this.sourceClass.getFullyQualifiedName().replaceAll("\\.", "\\\\");
		// this.savePath += ".java\\";
	
		try {

			IMethod[] mets = this.sourceClass.getMethods();
			IAnnotation[] ia =  this.sourceClass.getAnnotations();
			for(IAnnotation a :ia ) {
				//System.out.println(a.getElementName());
			}
			if (mets != null) {
				this.methods = Arrays.asList(mets);
				for (IMethod met : this.methods) {
					System.out.println(met.getElementName()); 
					new MethodInvokeAPI(met, this.searchScope, this.savePath,this.sourceClass);
					// new MethodInvokeRelation(met, this.searchScope, this.savePath);
				}
			}
		} catch (JavaModelException e) {

			e.printStackTrace();
		}
	}
}
