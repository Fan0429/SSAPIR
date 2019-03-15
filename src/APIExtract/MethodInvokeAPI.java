package APIExtract;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.plaf.synth.SynthSpinnerUI;

import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.IAnnotation;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaModel;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IMember;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.search.IJavaSearchConstants;
import org.eclipse.jdt.core.search.SearchPattern;
import org.eclipse.jdt.internal.core.search.JavaSearchScope;
import org.eclipse.jdt.internal.corext.callhierarchy.CallHierarchy;
import org.eclipse.jdt.internal.corext.callhierarchy.MethodWrapper;



public class MethodInvokeAPI {

	private String savePath;

	private IMethod targetMethod;
	private String targetMethodName;
	private CallHierarchy callHierarchy;
	private JavaSearchScope searchScope;

	private MethodWrapper[] callers;
	private MethodWrapper[] callees;

	private String[] paramNames;
	private String methodBody;

	private String fileName;

	public MethodInvokeAPI(IMethod method, JavaSearchScope scope, String saveroot, IType ClassFileName)
			throws JavaModelException {
		this.targetMethod = method;
		this.paramNames = this.targetMethod.getParameterNames();
		this.fileName = ClassFileName.getElementName()+".java";
		//System.out.println(ClassFileName.getElementName());
		this.savePath= saveroot;

		
		// this.getMethodSignature()
		// this.savePath = saveroot + "/" + this.getMethodSignature() + "/";
		// 感觉完全没必要在这里建文件啊
//		try {
//			File paramFile = new File(this.savePath + ClassFileName + ".java");
//			if (!paramFile.exists()) {
//				paramFile.getParentFile().mkdirs();
//				paramFile.createNewFile();
//			}
//			 File paramFile = new File(this.savePath + method.getElementName() +
//			 "_params.res");
//			 if (!paramFile.exists()) {
//			 paramFile.getParentFile().mkdirs();
//			 paramFile.createNewFile();
//			 }
//			 FileWriter fwriter = new FileWriter(paramFile);
//			 fwriter.append(new
//			 MethodInformation(this.targetMethod).getAllParamTypesForParamFile());
//			 fwriter.close();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
		
		
		// 这一段是筛选掉没有参数的方法，所以我们不需要，param参数
		// if (CallGraphGlobalSettings.SplitNoParameters
		// && (this.paramNames == null || this.paramNames.length <= 0)) {
		// return;
		// }

		// PrintoutResult.PrintInScreen("\tin method: " + method.getElementName());

		this.targetMethodName = this.targetMethod.getElementName();
		this.methodBody = this.targetMethod.getSource();
		// PrintoutResult.PrintInScreen(this.methodBody);
		// toStr 这个是输出的一类类似于路径的东西，从后往前是这样的
		// void removeSome() [in ListElementRemovalTest [in ListElementRemovalTest.java
		// [in test.java.org.ahocorasick.util [in src [in aho-corasick-master]]]]]
		String toStr = "\t\tto String:\n" + this.targetMethod.toString();
		// PrintoutResult.PrintOutResult(toStr);

		// .getSource()获取这个方法下的源代码
		String src = "\t\tget source:\n" + this.targetMethod.getSource();
		// PrintoutResult.PrintOutResult(src);

		// 这一段是获取每个方法的参数列表
		String paramInfo = "\t\tparam names:\n";
		if (this.paramNames.length > 0) {
			for (String p : this.paramNames) {
				paramInfo += "\t\t\t" + p + "\n";
			}
		} else {
			paramInfo += "\t\t\tno params\n";
		}
		// PrintoutResult.PrintOutResult(paramInfo);

		this.callHierarchy = CallHierarchy.getDefault();
		this.callHierarchy.setSearchScope(scope);

		getCalleeWrappers();

		try {
			sss2();
		} catch (IOException e) {
			e.printStackTrace();
		}
//		try {
//			sss();
//		
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
	}
 
	private void getCalleeWrappers() {
		MethodWrapper[] targets = this.callHierarchy.getCalleeRoots(new IMember[] { this.targetMethod });
		if (targets.length > 0) {
			try {
				// 这个方法调用了哪些方法，放在this.callees中
				this.callees = targets[0].getCalls(new NullProgressMonitor());
				 System.out.println("the callee's length is："+this.callees.length);
			} catch (Exception e) {
				System.err.println(targets[0].getName());
			}
		}
	}

	private void sss2() throws IOException {
		MethodWrapper[] calles = this.callees;
		
			File paramFile = new File(this.savePath+ this.fileName);
			System.out.println("the file name is:"+paramFile);
			if (!paramFile.exists()) {
				paramFile.getParentFile().mkdirs();
				paramFile.createNewFile();
			}
			FileWriter writerCalleInfo = new FileWriter(paramFile,true);
			//writerCalleInfo.append(calleInfo.getGoalSignature()+"\n");
			writerCalleInfo.append(this.targetMethod.getElementName()+" ");
	 
		
		if (calles != null) {
			for (MethodWrapper calle : calles) {
				if (calle.getMember() instanceof IMethod) {
					IMethod callMethod = (IMethod) calle.getMember();
					MethodInformation calleInfo = new MethodInformation(calle.getMember());

						String ss[] = calleInfo.writeMethodFileInfo().split("\n");
						writerCalleInfo.append(ss[1]+"#"+ss[0]+" ");

				}
			}
		}
		writerCalleInfo.append("\n");
		writerCalleInfo.close();
	}

	private String sss() throws IOException, JavaModelException {
		String callPath = this.savePath + "callees/";
		File callFold = new File(callPath);
		callFold.mkdirs();
		MethodWrapper[] calles = this.callees;
		if (calles != null) {
			for (MethodWrapper calle : calles) {
				if (calle.getMember() instanceof IMethod) {
					IMethod callMethod = (IMethod) calle.getMember();
					MethodInformation calleInfo = new MethodInformation(calle.getMember());

					File callfile = new File(callPath + calleInfo.getGoalSignature() + ". ");
					callfile.createNewFile();
					FileWriter writerCalleInfo = new FileWriter(callfile);
					writerCalleInfo.append(calleInfo.writeMethodFileInfo());
					// PrintoutResult.PrintInScreen("\t\tin Calle:\t" +
					// (callMethod).getElementName());

					// String[] p = this.getPassedParams(this.targetMethod, callMethod);
					//
					// writerCalleInfo.append(p.length + "\n");
					// for (String s : p) {
					// writerCalleInfo.append(s + "\n");
					// }
					
					writerCalleInfo.close();
				}
			}
		}

		return "";

	}

	private String getMethodSignature() {
		// String res = this.targetMethod.getElementName();
		String res = new MethodInformation(this.targetMethod).getGoalSignature();

		return res;
	}
}
