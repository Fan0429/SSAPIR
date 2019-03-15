package APIExtract;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.internal.core.search.JavaSearchScope;


public class BuildFromProject {

	private IJavaProject sourceProj;
	private JavaSearchScope searchScope;
	private List<IType> classes;
	
	private String savePath;
	
	public BuildFromProject(IJavaProject proj, String saveroot){
		
		
		this.sourceProj = proj;//这个proj是后来初始化callHierarchy需要用到的参数
		this.savePath = saveroot + proj.getElementName() + "/";
		
		CallGraphGlobalSettings.ProjName = proj.getElementName();
		
		this.classes = new ArrayList();
		try {
			this.searchScope = new JavaSearchScope();
			this.searchScope.add(this.sourceProj);



			ExtractAllClasses();//到这里就是类了
			for (IType type : this.classes){
				new BuildFromOneClass(type, this.searchScope, this.savePath);
			}
		} catch (JavaModelException e) {
			e.printStackTrace();
		}
		
		
	}
	
	private void ExtractAllClasses() throws JavaModelException{

		IPackageFragment[] packages = this.sourceProj.getPackageFragments();
		for (IPackageFragment pack : packages){
			if (!(pack.getKind() == IPackageFragmentRoot.K_SOURCE)){ continue; }
			ICompilationUnit[] units = pack.getCompilationUnits();
			for (ICompilationUnit unit : units){
				IType[] types = unit.getAllTypes();
				this.classes.addAll(Arrays.asList(types));
			}
		}
	}
}
