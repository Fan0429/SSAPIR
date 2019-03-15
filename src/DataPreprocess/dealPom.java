package DataPreprocess;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;


import jxl.Workbook;
import jxl.format.Alignment;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

public class dealPom {

	private List<String> jars = new LinkedList<String>();
	
//	public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException, RowsExceededException, WriteException {
//		
//		String fileRoot = "I:\\DATA\\TryFinal2\\pom";
//		
//		new dealPom().printJars(fileRoot,1);
//		new dealPom().printJars(fileRoot,3);
//		
//		
//	}

	public List<String> getFilelist(String fileRoot) {
		// get the first file list
		List<String> fileList = new LinkedList<String>();
		File root = new File(fileRoot);
		File[] files = root.listFiles();
		for (File file : files) {
			if (file.isFile()) {
				if (file.getAbsolutePath().endsWith(".xml")) {
					fileList.add(file.getAbsolutePath());

				}
			}
		}
		return fileList;
	}

	public void printJars(String fileRoot,int Id)
			throws ParserConfigurationException, SAXException, IOException, RowsExceededException, WriteException {

		
		getJarsNums(fileRoot,Id);
		ArrayList<Map.Entry<String, Integer>> list = new proAPIMsg().getAPIFrequency(jars);
		
		String WriteFilePath=fileRoot+"\\"+"JarMessage"+Id+".xls";
		WritableWorkbook book = Workbook.createWorkbook(new File(WriteFilePath));
		WritableSheet sheet = book.createSheet("sheet1", 0);
		System.out.println(list.size());
		for(int i=0;i<list.size();i++) {//irow,1lie
			Label label = new Label(0, i,list.get(i).getKey() + ": "+ list.get(i).getValue());
			sheet.addCell(label);
		}
		//生成格式 
		WritableCellFormat format= new WritableCellFormat();
		//垂直对齐
		format.setAlignment(Alignment.CENTRE);
		sheet.getSettings().setDefaultColumnWidth(20);
		book.write();
		book.close();
	}

	public void getJarsNums(String fileRoot,int Id) throws ParserConfigurationException, SAXException, IOException {

		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder();
		
		List<String> fileList = getFilelist(fileRoot);
		int count=0;
		
		for (String filepath : fileList) {

			Document document = db.parse(filepath);
			if (document.getElementsByTagName("dependencies").getLength() > 0) {
				
				NodeList dependencies = document.getElementsByTagName("dependencies");
				Node dependency = dependencies.item(0);

				NodeList dependencyChild = dependency.getChildNodes();

				for (int i = 0; i < dependencyChild.getLength(); i++) {
					Node groupId = dependencyChild.item(i);
					NodeList groupIdText = groupId.getChildNodes();
					if (groupIdText.getLength() > 0) {
						// System.out.println(groupIdText.item(1).getTextContent());
						jars.add(groupIdText.item(Id).getTextContent());
						count++;
					}
				}
			}
		}
		
	}

}
