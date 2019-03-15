package APIExtract;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;



public class BuildFromWorkbench {

	private IWorkspace workspace; 
    private IWorkspaceRoot workroot;
    private List<IJavaProject> javaProjs;
    
    private String savepath_root;
    
    public BuildFromWorkbench(String rootpath){
    	this.workspace = ResourcesPlugin.getWorkspace();
    	this.workroot = workspace.getRoot();
    	this.savepath_root = rootpath;
    	//这个就是筛选判断一个项目是不是JAVA项目，并把项目proj和保存路径传进去
    	this.javaProjs = new ArrayList();
    	try {
			ExtractJavaProjects();
			for (IJavaProject proj : this.javaProjs){
				new BuildFromProject(proj, this.savepath_root);
			}
			
			
		} catch (CoreException e) {
			e.printStackTrace();
		}
    }

    private void ExtractJavaProjects() throws CoreException{
    	IProject[] projs = this.workroot.getProjects();
    	for (IProject proj : projs){
    		if (proj.isNatureEnabled("org.eclipse.jdt.core.javanature")){
    			this.javaProjs.add(JavaCore.create(proj));
    		}
    	}
    }
}
