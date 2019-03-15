package mianRun;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import DataPreprocess.addClasspath;
import GetCommentInProject.DocFileDeal;
import GetCommentInProject.FileDeal;
import GetCommentInProject.compaireDocAPI;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;


public class methods {

	public void generateFile(String fileroot) throws IOException {
		// when we give the file root ,we can generate the .project file and the
		// classpath file.

		// you may need to fix the addClasspath method line 219 to choose the fileroot
		// type
		System.out.println("generating :");
		new addClasspath().createFile(fileroot);
		new addClasspath().addSrc(fileroot);
		new addClasspath().addProjectName(fileroot);
		System.out.println("generate OVER:");
	}

	public void getAnnotation(String fileRoot, String writeAnoFilespath) throws IOException {
		// these three line is to get the annotation,and may have some bugs ,the bond
		// out of index
		System.out.println("geting annotation:");
		new DocFileDeal().writeAnoFile(fileRoot, writeAnoFilespath);
		System.out.println("Deleting:");
		new FileDeal().fileDelete(writeAnoFilespath);
		System.out.println("geting annotation OVER");
		// when here we get all the anno in all project in writeAnoFilespath
	}

	
	public void compaireTwo(String fileRoot,String APIroot, String DocRoot, String finalFilepath) throws IOException {
		// this is the method to compair the .java file's method both hava annotation
		// and the APIs ,save in three txt ,one is thirdPartFinally,one is JreFianlly ,one is Finally
		System.out.println("compairing:");
		String excelFilePath =finalFilepath+"\\"+"ProMsg";
		Map m = new HashMap();
		m = new compaireDocAPI().compaireTwo1(APIroot, DocRoot, finalFilepath,excelFilePath);
		//new getProjectMessage().getProMessage1(fileRoot, excelFilePath,m);
		System.out.println("compairing OVER");
		
	}


	public void splitOneFile(String finalFilepath)
			throws IOException {
		// this is the method to split the finally.txt to APIS.txt and the
		// annotation.txt and the methodName.txt
		System.out.println("spliting:");
		
		String jreSplitPath = finalFilepath+"\\"+"jreAPI"+"\\"+"jrefianlly.txt";
		String thirdPartSplitPath = finalFilepath+"\\"+"thirdpartAPI"+"\\"+"thirdpartfianlly.txt";
		finalFilepath=finalFilepath+"\\"+"fianlly.txt";
		
		new DocFileDeal().splitOneFile2(finalFilepath);
		new DocFileDeal().splitOneFile2(jreSplitPath);
		new DocFileDeal().splitOneFile2(thirdPartSplitPath);
		
		System.out.println("spliting OVER");
	}



}
