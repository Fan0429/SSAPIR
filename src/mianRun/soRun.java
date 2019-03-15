package mianRun;

import java.io.IOException;


import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;


public class soRun {
				
	private static String fileroot = "H:\\JAVA\\ALLdata\\AiData\\1";
//	private static String APIRoot = "I:\\DATA\\Final\\CRAPI\\data\\callGraph";
//	private static String AnoRoot = "I:\\DATA\\Final\\CRAPI\\data\\annotation";
//	private static String  finalFileRoot = "I:\\DATA\\Final\\CRAPI\\data";
//	
	
	public static void main(String[] args) throws IOException, RowsExceededException, WriteException {

		new methods().generateFile(fileroot);	
		//get APIs
		//new methods().getAnnotation(fileroot, AnoRoot);
		//new methods().compaireTwo(fileroot, APIRoot, AnoRoot, finalFileRoot);
		
		//new CustomizedAPIExtract().APIExtract();

	}
	
	
	
}
