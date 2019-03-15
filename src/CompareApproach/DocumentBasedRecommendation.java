package CompareApproach;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import ComparedApproachFrame.APIDocMsg;
import ComparedApproachFrame.DocumentBasedSimilarityResult;
import ComparedApproachFrame.APIScore;
import ComparedApproachFrame.HistoryBasedSimilarityResult;
import ComparedApproachTFIDF.DocumentBasedDocumentParser;
import DataPreprocess.DatabaseConnect;
import DataPreprocess.participle;
import Frame.DocMsg;
import Frame.FileData;
import GetCommentInProject.DocFileDeal;
import mianRun.CustomizedAPIExtract;

public class DocumentBasedRecommendation {

//	public static void main(String[] args) throws IOException, SQLException {
//		String APILibraryName = "comparedeasymock";
//		String APIDocumentLibraryName ="doccomparedeasymock";
//		String writeFilePath = "I:\\DATA\\Final\\CompareExperiment\\Result\\result" + APILibraryName + ".txt";
//
//		List<List<APIScore>> ALLDocumentBasedTopAPI = getFinalDocumentAPI( APILibraryName,APIDocumentLibraryName, writeFilePath);
//		System.out.println(ALLDocumentBasedTopAPI.get(0).get(0).getApi()+"   "+ALLDocumentBasedTopAPI.get(0).get(2).getScore());
//
//	}

	
	public static void getFinalDocumentAPI(String APILibraryName,String APIDocumentLibraryName ,String writeFilePathRoot)
			throws IOException, SQLException {

		List<List<DocumentBasedSimilarityResult>> resultSummary = CalculateDocumentScore(APILibraryName,APIDocumentLibraryName);
		List<List<APIScore>> ALLDocumentBasedTopKAPI = new LinkedList<List<APIScore>>();
		
		
		String  WriteFilePathDocument =  writeFilePathRoot +"\\"+ "resultDocument"+ APILibraryName+ ".txt";
		
		
		for (List<DocumentBasedSimilarityResult> DocumentBasedResultList : resultSummary) {
			List<APIScore> DocumentBasedTopKAPI = new LinkedList<APIScore>();
			for(DocumentBasedSimilarityResult resultList :DocumentBasedResultList) {
				APIScore topAPI = new APIScore();
				topAPI.setScore(resultList.getSimilarity());
				topAPI.setApi(resultList.getAPIDocMsgData().getClassName()+"#"+resultList.getAPIDocMsgData().getMethodName());
				DocumentBasedTopKAPI.add(topAPI);
			}
			
			// Divide the maximum value of data into
			// normalization.
			List<APIScore> DocumentBasedTopKAPINormalized = new LinkedList<APIScore>();
			for(int i =0; i <DocumentBasedTopKAPI.size();i++) {
				if(DocumentBasedTopKAPI.get(0).getScore()==0) {
					APIScore fianlNormalizedScore = new APIScore();
					fianlNormalizedScore.setApi(DocumentBasedTopKAPI.get(i).getApi());
					fianlNormalizedScore.setScore(0);
					DocumentBasedTopKAPINormalized.add(fianlNormalizedScore);
				}
				else {
					APIScore fianlNormalizedScore = new APIScore();
					fianlNormalizedScore.setApi(DocumentBasedTopKAPI.get(i).getApi());
					fianlNormalizedScore.setScore(DocumentBasedTopKAPI.get(i).getScore()/DocumentBasedTopKAPI.get(0).getScore());
					DocumentBasedTopKAPINormalized.add(fianlNormalizedScore);
				}

			}
			
			
			ALLDocumentBasedTopKAPI.add(DocumentBasedTopKAPINormalized);

		}

		
		System.out.println(ALLDocumentBasedTopKAPI.size());
		BufferedWriter write;
		write = new BufferedWriter(
				new OutputStreamWriter(new FileOutputStream(new File(WriteFilePathDocument), true), "UTF-8"));
		
		for (List<APIScore> APIScore : ALLDocumentBasedTopKAPI) {
			for (APIScore score : APIScore) {
				
				write.append(score.getApi()+"*"+score.getScore());
				write.append(" ");
			}
			write.append("\n");
			
		}
		write.close();
		

	}

	private static List<List<DocumentBasedSimilarityResult>> CalculateDocumentScore(String APILibraryName,
			String APIDocumentLibraryName) throws IOException, SQLException {

		List<FileData> APIDocdataList = new LinkedList<FileData>();
		DatabaseConnect dbConn = new DatabaseConnect();

		for (int i = 1; i <= 800; i++) {

			String sql = "select * from " + APILibraryName + " where ID = '  " + i + " ' ";// orginalapi

			if (dbConn.findAPIMsgData(sql).getAPI() != null) {
				APIDocdataList.add(dbConn.findAPIMsgData(sql)); // get the dataList
			}
		}
		List<List<DocumentBasedSimilarityResult>> resultDocAPI = new LinkedList<List<DocumentBasedSimilarityResult>>();
		
		for (int i = 0; i < APIDocdataList.size(); i++) { // cm.size()
			
			resultDocAPI.add(getDocumentAPIresultScore(APIDocdataList.get(i).getAnnotation(), APIDocdataList.get(i).getAPI(),
					APIDocumentLibraryName));
		}
		
		
		//System.out.println(resultDocAPI.get(0).size());
		return resultDocAPI;
	}

	private static List<DocumentBasedSimilarityResult> getDocumentAPIresultScore(String query, String correctAPI,
			String APIDocumentLibraryName) throws IOException, SQLException {
		DatabaseConnect dbconn = new DatabaseConnect();
		List<DocumentBasedSimilarityResult> resultDocument = new LinkedList<DocumentBasedSimilarityResult>();

		DocumentBasedDocumentParser dpDocument = new DocumentBasedDocumentParser();
		dpDocument.parseFiles(query, dbconn, APIDocumentLibraryName);
		dpDocument.tfIdfCalculator(); // calculates tfidf
		resultDocument = dpDocument.getCosineSimilarityCompared(query, correctAPI); // calculated cosine
																									// similarity ]

		return resultDocument;

	}

	// The following methods are used to extract and process relevant information in
	// API documents.

	// public static void main(String[] args) {
	// String DocFilePath =
	// "I:\\DATA\\Final\\CompareExperiment\\Data\\APIdoc\\ComparedDoc\\Comparedcommons-codec.xls";
	// String writeFilePath =
	// "I:\\DATA\\Final\\CompareExperiment\\Data\\APIdoc\\ComparedDoc\\ComparedMergedDoc\\ComparedMergedcommons-codec.xls";
	//
	// AddOriginalJavaDocToDatabase(DocFilePath,writeFilePath);
	// AddMyJavaDocToDatabase(DocFilePath,writeFilePath);
	//
	// AddComparedJavaDocToDatabase(DocFilePath, writeFilePath);
	// mergeSameNameMethod(DocFilePath, writeFilePath);
	// // Through the above four steps, we import the methods in the document into
	// database after pretreatment.
	//
	//
	// }
	public static void mergeSameNameMethod(String DocFilePath, String writeFilePath) {
		List<DocMsg> dmMY = new LinkedList<DocMsg>();

		HSSFWorkbook wb = null;
		InputStream is = null;

		try {
			is = new FileInputStream(DocFilePath);
			wb = new HSSFWorkbook(is);
			HSSFSheet sheet = null;
			HSSFRow row = null;

			if (wb != null) {
				sheet = wb.getSheetAt(0);
				int rownum = sheet.getPhysicalNumberOfRows();
				row = sheet.getRow(0);
				// System.out.println(rownum);
				for (int i = 0; i < rownum; i++) {

					row = sheet.getRow(i);
					if (row != null) {
						String ID = row.getCell(0).toString();
						String ClassName = row.getCell(1).toString();
						String MethodName = row.getCell(2).toString();
						String ParaInfo = row.getCell(3).toString();
						String method_description = row.getCell(4).toString();
						String modifier_type = row.getCell(5).toString();
						DocMsg dm = new DocMsg(ID, ClassName, MethodName, ParaInfo, method_description, modifier_type);
						if (!dm.getMethodDescription().equals("") && !dm.getMethodName().equals("")) {
							dmMY.add(dm);
						}

					} else {
						break;
					}

				}
			}

			List<DocMsg> dmMYfinal = new LinkedList<DocMsg>();

			for (int i = 1; i < dmMY.size(); i++) {
				if (i == 1) {
					dmMYfinal.add(dmMY.get(i));
					continue;
				}

				for (int j = 0; j < dmMYfinal.size(); j++) {
					if (dmMY.get(i).getMethodName().equals(dmMYfinal.get(j).getMethodName())) {
						break;
					}
					if (j == dmMYfinal.size() - 1) {
						dmMYfinal.add(dmMY.get(i));

					}
				}
			}
			writeExcel(dmMYfinal, writeFilePath);

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static void AddMyJavaDocToDatabase(String DocFilePath, String writeFilePath) {
		// This method is to preprocess the doc data separately according to the methods
		// mentioned in our paper .
		List<DocMsg> dmMY = new LinkedList<DocMsg>();

		HSSFWorkbook wb = null;
		InputStream is = null;

		try {
			is = new FileInputStream(DocFilePath);
			wb = new HSSFWorkbook(is);
			HSSFSheet sheet = null;
			HSSFRow row = null;

			if (wb != null) {
				sheet = wb.getSheetAt(0);
				int rownum = sheet.getPhysicalNumberOfRows();
				row = sheet.getRow(0);
				// System.out.println(rownum);
				for (int i = 0; i < rownum; i++) {

					row = sheet.getRow(i);
					if (row != null) {
						String ID = row.getCell(0).toString();
						String ClassName = row.getCell(1).toString().replaceAll(" ", "");

						String MethodName = row.getCell(2).toString().replaceAll(" ", "");// in this we dont need to
																							// deal with the method name

						String ParaInfo = row.getCell(3).toString().replaceAll(" ", " ");
						ParaInfo = ParaInfo.replace(",", ",");
						ParaInfo = ParaInfo.replace("(", "").replace(")", "").replace("...", "");

						String method_description = row.getCell(4).toString().replace(" ", "").trim();
						method_description = new DocFileDeal().replaceChinese(method_description);
						method_description = new DocFileDeal().splitMethodName(method_description);
						method_description = new participle().participleLine(method_description);
						method_description = new CustomizedAPIExtract().filterAnnotation(method_description);
						method_description = method_description.trim();

						String[] method_descriptions = method_description.split(" ");
						String method_description2 = "";

						if (method_descriptions.length != 1) {
							for (int m = 0; m < method_descriptions.length; m++) {
								if (method_descriptions[m].length() >= 3) {
									method_description2 = method_description2 + method_descriptions[m] + " ";
								}

							}
						}

						String modifier_type = row.getCell(5).toString().replaceAll(" ", "");

						DocMsg dm = new DocMsg(ID, ClassName, MethodName, ParaInfo, method_description2, modifier_type);
						if (!dm.getMethodDescription().equals("") && !dm.getMethodName().equals("")) {
							dmMY.add(dm);
						}

					} else {
						break;
					}

				}
			}

			writeExcel(dmMY, writeFilePath);

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static void AddComparedJavaDocToDatabase(String DocFilePath, String writeFilePath) {
		// This method is to preprocess the doc data separately according to the methods
		// mentioned in his paper .
		List<DocMsg> dmMY = new LinkedList<DocMsg>();

		HSSFWorkbook wb = null;
		InputStream is = null;

		try {
			is = new FileInputStream(DocFilePath);
			wb = new HSSFWorkbook(is);
			HSSFSheet sheet = null;
			HSSFRow row = null;

			if (wb != null) {
				sheet = wb.getSheetAt(0);
				int rownum = sheet.getPhysicalNumberOfRows();
				row = sheet.getRow(0);
				// System.out.println(rownum);
				for (int i = 0; i < rownum; i++) {

					row = sheet.getRow(i);
					if (row != null) {
						String ID = row.getCell(0).toString();
						String ClassName = row.getCell(1).toString().replaceAll(" ", "");
						String MethodName = row.getCell(2).toString().replaceAll(" ", "");
						String ParaInfo = row.getCell(3).toString().replaceAll(" ", " ");
						String method_description = row.getCell(4).toString().replace(" ", "").trim();
						method_description = new participle().ComparedparticipleLine(method_description);// use potter
																											// stemming

						String modifier_type = row.getCell(5).toString().replaceAll(" ", "");

						DocMsg dm = new DocMsg(ID, ClassName, MethodName, ParaInfo, method_description, modifier_type);
						if (!dm.getMethodDescription().equals("") && !dm.getMethodName().equals("")) {
							dmMY.add(dm);
						}

					} else {
						break;
					}

				}
			}

			writeExcel(dmMY, writeFilePath);

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static void AddOriginalJavaDocToDatabase(String DocFilePath, String writeFilePath) {

		// It's annoying. The data he gave has some coding problems. I have to
		// reintroduce it.

		List<DocMsg> dmMY = new LinkedList<DocMsg>();

		HSSFWorkbook wb = null;
		InputStream is = null;

		try {
			is = new FileInputStream(DocFilePath);
			wb = new HSSFWorkbook(is);
			HSSFSheet sheet = null;
			HSSFRow row = null;

			if (wb != null) {
				sheet = wb.getSheetAt(0);
				int rownum = sheet.getPhysicalNumberOfRows();
				row = sheet.getRow(0);
				// System.out.println(rownum);
				for (int i = 0; i < rownum; i++) {

					row = sheet.getRow(i);
					if (row != null) {
						String ID = row.getCell(0).toString();
						String ClassName = row.getCell(1).toString().replaceAll(" ", "");
						String MethodName = row.getCell(2).toString().replaceAll(" ", "");
						String ParaInfo = row.getCell(3).toString().replaceAll(" ", " ");
						String method_description = row.getCell(4).toString().replace(" ", "").trim();
						String modifier_type = row.getCell(5).toString().replaceAll(" ", "");

						DocMsg dm = new DocMsg(ID, ClassName, MethodName, ParaInfo, method_description, modifier_type);
						dmMY.add(dm);

					} else {
						break;
					}

				}
			}

			writeExcel(dmMY, writeFilePath);

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static void writeExcel(List<DocMsg> dmMY, String writeFilePath) {

		HSSFWorkbook wb2 = new HSSFWorkbook();
		HSSFSheet sheet2 = wb2.createSheet("sheet1");
		System.out.println(dmMY.size());

		HSSFRow row0 = sheet2.createRow(0);
		HSSFCell cell2 = row0.createCell((short) 0);
		cell2.setCellValue("ID");
		cell2 = row0.createCell((short) 1);
		cell2.setCellValue("class_name");
		cell2 = row0.createCell((short) 2);
		cell2.setCellValue("method_name");
		cell2 = row0.createCell((short) 3);
		cell2.setCellValue("para_info");
		cell2 = row0.createCell((short) 4);
		cell2.setCellValue("method_description");
		cell2 = row0.createCell((short) 5);
		cell2.setCellValue("modifier_type");

		for (int i = 0; i < dmMY.size(); i++) {
			HSSFRow row2 = sheet2.createRow(i + 1);

			cell2 = row2.createCell((short) 0);
			cell2.setCellValue(dmMY.get(i).getID());
			cell2 = row2.createCell((short) 1);
			cell2.setCellValue(dmMY.get(i).getClassName());
			cell2 = row2.createCell((short) 2);
			cell2.setCellValue(dmMY.get(i).getMethodName());
			cell2 = row2.createCell((short) 3);
			cell2.setCellValue(dmMY.get(i).getParameterInformation());
			cell2 = row2.createCell((short) 4);
			cell2.setCellValue(dmMY.get(i).getMethodDescription());
			cell2 = row2.createCell((short) 5);
			// System.out.println(dmMY.get(i).getMethodDescription());
			cell2.setCellValue(dmMY.get(i).getModifierType());

		}
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		try {
			wb2.write(os);
			byte[] content = os.toByteArray();
			File file = new File(writeFilePath);
			OutputStream fos = null;
			fos = new FileOutputStream(file);
			wb2.write(fos);
			os.close();
			fos.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
