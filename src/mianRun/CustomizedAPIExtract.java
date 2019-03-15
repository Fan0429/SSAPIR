package mianRun;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import DataPreprocess.participle;
import DataPreprocess.proAPIMsg;
import Frame.FileData;
import Frame.ProAPIMessage;
import GetCommentInProject.DocFileDeal;
import jxl.Workbook;
import jxl.format.Alignment;
import jxl.write.Label;
import jxl.write.Number;
import jxl.write.WritableCellFormat;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

public class CustomizedAPIExtract {

	private static List<String> oneJarAPI = new LinkedList<String>();

	public void APIExtract() throws RowsExceededException, WriteException, IOException {
		String fileroot = "I:\\DATA\\Final\\CRAPI\\data\\File\\jreAPI\\jrefianlly.txt";
		String fileroot2 = "I:\\DATA\\Final\\CompareExperiment\\Data\\File\\ComparedjreAPI.txt";
		String fielroot3 = "I:\\DATA\\Final\\CompareExperiment\\Data\\File\\10LibrariesData\\slf4j\\Comparedslf4j.txt";

		String test = "I:\\DATA\\Final\\CRAPI\\data\\File\\thirdPartyAPI\\10LibrariesData\\test.txt";

		// cutOneJarAPI(fileroot,"springframework");
		// countAPINum(fileroot,"servlet");

		 //cutFileTodatabase(fileroot, "jreAPI");
		 //cutFileTodatabaseCompared(fileroot, "ComparedjreAPI");
		
		//cutFileTodatabase2(fileroot2,"Comparedslf4j");
		//countAPINum2(fileroot2,"Comparedslf4j");
		
		//cutFileTodatabase3(fileroot2,fielroot3, "FilteredComparedslf4j");

		
	}

	public static void cutFileTodatabase3(String originalFilePath, String FilePath2,String mode)
			throws RowsExceededException, WriteException, IOException {
		//In this method, we need to count the number of APIs that occur, and we ignore the number of APIs that occur 1. Just like the article in our comparison.
		List<String> OnceAppearedAPI = new LinkedList<String>();
		OnceAppearedAPI = countAPINum3(FilePath2);

		String fileRoot = originalFilePath.substring(0, originalFilePath.lastIndexOf("\\"));
		List<FileData> FileDataList = new LinkedList<FileData>();

		BufferedWriter writeFile = new BufferedWriter(
				new OutputStreamWriter(new FileOutputStream(new File(fileRoot + "\\" + mode + ".txt"), true), "UTF-8"));

		File file = new File(originalFilePath);
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
			String line = "";

			while ((line = br.readLine()) != null) {
				String[] lines = null;
				lines = line.split("&");
				if (lines.length == 4) {
					String methodName = lines[0];
					methodName = methodName.trim();
					String parameter = lines[1];

					String annotation = lines[2];
					annotation = annotation.trim();
					String[] annotations = annotation.split(" ");

					String annotation2 = "";
					if (annotations.length != 1) {
						for (int i = 0; i < annotations.length; i++) {
							if (annotations[i].length() >= 3) {
								annotation2 = annotation2 + annotations[i] + " ";
							}

						}
					}

					String APIs = lines[3];

					if (annotation2.length() > 0 && APIs.length() > 0 && methodName.length() > 0) {// some line don't
						// filter
						if (APIs.contains("slf4j")) {
							String[] API = APIs.split(" ");
							String tempAPIs = "";
							for (String str : API) {
								
									if (str.contains("slf4j")&&!OnceAppearedAPI.contains(str)) {
										//System.out.println(str);
											tempAPIs = tempAPIs + str + " ";
								}
							}
							if(tempAPIs!="") {
								String[] APIsLengthFilter = tempAPIs.split(" ");
								if (APIsLengthFilter.length > 0 && APIsLengthFilter.length <= 4) {
									FileData fileData = new FileData(methodName, parameter, annotation2, tempAPIs);
									FileDataList.add(fileData);

									// System.out.println(methodName+"|"+parameter+"|"+annotation+"|"+APIs);
									writeFile.append(
											methodName + " & " + parameter + " & " + annotation2 + " & " + tempAPIs);
									writeFile.append("\n");
								}

							}
						
						}

					}
				}

			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		writeFile.close();
		printExcel(fileRoot, FileDataList, mode);

	}

	public static void cutFileTodatabase2(String originalFilePath, String mode)
			throws RowsExceededException, WriteException, IOException {
		String fileRoot = originalFilePath.substring(0, originalFilePath.lastIndexOf("\\"));
		List<FileData> FileDataList = new LinkedList<FileData>();

		BufferedWriter writeFile = new BufferedWriter(
				new OutputStreamWriter(new FileOutputStream(new File(fileRoot + "\\" + mode + ".txt"), true), "UTF-8"));

		File file = new File(originalFilePath);
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
			String line = "";

			while ((line = br.readLine()) != null) {
				String[] lines = null;
				lines = line.split("&");
				if (lines.length == 4) {
					String methodName = lines[0];
					methodName = methodName.trim();
					String parameter = lines[1];

					String annotation = lines[2];
					annotation = annotation.trim();
					String[] annotations = annotation.split(" ");

					String annotation2 = "";
					if (annotations.length != 1) {
						for (int i = 0; i < annotations.length; i++) {
							if (annotations[i].length() >= 3) {
								annotation2 = annotation2 + annotations[i] + " ";
							}

						}
					}

					String APIs = lines[3];

					if (annotation2.length() > 0 && APIs.length() > 0 && methodName.length() > 0) {// some line don't
						// filter
						if (APIs.contains("slf4j")) {
							String[] API = APIs.split(" ");
							String tempAPIs = "";
							for (String str : API) {

								if (str.contains("slf4j")) {
									tempAPIs = tempAPIs + str + " ";

								}
							}

							String[] APIsLengthFilter = tempAPIs.split(" ");
							if (APIsLengthFilter.length > 0 && APIsLengthFilter.length <= 4) {
								FileData fileData = new FileData(methodName, parameter, annotation2, tempAPIs);
								FileDataList.add(fileData);

								// System.out.println(methodName+"|"+parameter+"|"+annotation+"|"+APIs);
								writeFile.append(
										methodName + " & " + parameter + " & " + annotation2 + " & " + tempAPIs);
								writeFile.append("\n");
							}

						}

					}
				}

			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		writeFile.close();
		printExcel(fileRoot, FileDataList, mode);

	}

	public static void cutFileTodatabaseCompared(String originalFilePath, String mode)
			throws RowsExceededException, WriteException, IOException {
		String fileRoot = originalFilePath.substring(0, originalFilePath.lastIndexOf("\\"));
		List<FileData> FileDataList = new LinkedList<FileData>();

		BufferedWriter writeFile = new BufferedWriter(
				new OutputStreamWriter(new FileOutputStream(new File(fileRoot + "\\" + mode + ".txt"), true), "UTF-8"));
		int count = 0;
		File file = new File(originalFilePath);
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
			String line = "";

			while ((line = br.readLine()) != null) {
				String[] lines = null;
				lines = line.split(" ");

				String tempAnnotation = line.substring(line.indexOf("[") + 1, line.indexOf("]"));
				String[] annotations = tempAnnotation.split(" ");

				// get method name 这里要改，把方法名和参数分开

				String methodName = getNameAndParameter(annotations[0]);
				methodName = new participle().participleLine(methodName);
				methodName = methodName.trim();
				String parameter = new DocFileDeal().getParameter(annotations[1]);

				// get annotation
				String annotation = "";

				for (int i = 0; i < annotations.length; i++) {

					if (i != 0 && i != 1) {
						annotation = annotation + annotations[i] + " ";
					}
				}
				// deal annotation
				annotation = new participle().ComparedparticipleLine(annotation);

				// get APIS
				String APIs = line.substring(line.indexOf("]") + 3);
				APIs = APIs.replace("]", "");
				APIs = APIs.replace("[", "[]");
				APIs = APIs.replaceAll(", ", " ");
				// System.out.println(APIs);
				// in APIs
				String[] tempAPIs = APIs.split(" ");
				String tempAPI = "";
				for (String str : tempAPIs) {
					if (str.contains("(")) {
						str = str.substring(0, str.indexOf("("));
						tempAPI = tempAPI + str + " ";
					}

				}

				if (annotation.length() > 0 && tempAPI.length() > 0 && methodName.length() > 0) {// some line don't have
																									// the annotation,so
																									// we filter

					FileData fileData = new FileData(methodName, parameter, annotation, tempAPI);
					FileDataList.add(fileData);

					// System.out.println(methodName+"|"+parameter+"|"+annotation+"|"+APIs);
					writeFile.append(methodName + " & " + parameter + " & " + annotation + " & " + tempAPI);
					writeFile.append("\n");

				}
				count++;
				System.out.println(count);
			}
			writeFile.close();
			br.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		printExcel(fileRoot, FileDataList, mode);
	}

	public static void cutFileTodatabase(String originalFilePath, String mode)
			throws IOException, RowsExceededException, WriteException {

		String fileRoot = originalFilePath.substring(0, originalFilePath.lastIndexOf("\\"));
		List<FileData> FileDataList = new LinkedList<FileData>();

		BufferedWriter writeFile = new BufferedWriter(
				new OutputStreamWriter(new FileOutputStream(new File(fileRoot + "\\" + mode + ".txt"), true), "UTF-8"));
		int count = 0;
		File file = new File(originalFilePath);
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
			String line = "";

			while ((line = br.readLine()) != null) {
				String[] lines = null;
				lines = line.split(" ");

				String tempAnnotation = line.substring(line.indexOf("[") + 1, line.indexOf("]"));
				String[] annotations = tempAnnotation.split(" ");

				// get method name 这里要改，把方法名和参数分开

				String methodName = getNameAndParameter(annotations[0]);
				methodName = new participle().participleLine(methodName);
				methodName = methodName.trim();
				String parameter = new DocFileDeal().getParameter(annotations[1]);

				// get annotation
				String annotation = "";

				for (int i = 0; i < annotations.length; i++) {

					if (i != 0 && i != 1) {
						annotation = annotation + annotations[i] + " ";
					}
				}
				// deal annotation
				annotation = new DocFileDeal().replaceChinese(annotation);
				annotation = new DocFileDeal().splitMethodName(annotation);
				annotation = new participle().participleLine(annotation);
				annotation = filterAnnotation(annotation);
				annotation = annotation.trim();

				// get APIS
				String APIs = line.substring(line.indexOf("]") + 3);
				APIs = APIs.replace("]", "");
				APIs = APIs.replace("[", "[]");
				APIs = APIs.replaceAll(", ", " ");
				// System.out.println(APIs);
				// in APIs
				String[] tempAPIs = APIs.split(" ");
				String tempAPI = "";
				for (String str : tempAPIs) {
					if (str.contains("(")) {
						str = str.substring(0, str.indexOf("("));
						tempAPI = tempAPI + str + " ";
					}

				}

				if (annotation.length() > 0 && tempAPI.length() > 0 && methodName.length() > 0) {// some line don't have
																									// the annotation,so
																									// we filter

					FileData fileData = new FileData(methodName, parameter, annotation, tempAPI);
					FileDataList.add(fileData);

					// System.out.println(methodName+"|"+parameter+"|"+annotation+"|"+APIs);
					writeFile.append(methodName + " & " + parameter + " & " + annotation + " & " + tempAPI);
					writeFile.append("\n");

				}
				count++;
				System.out.println(count);
			}
			writeFile.close();
			br.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		printExcel(fileRoot, FileDataList, mode);

	}

	public static List<String> countAPINum3(String originalFilePath)
			throws IOException, RowsExceededException, WriteException {

		BufferedReader br = new BufferedReader(new FileReader(originalFilePath));
		List<String> APIlists = new ArrayList<String>();
		String readLine = null;
		while ((readLine = br.readLine()) != null) {

			// get APIS
			String APIs = readLine.substring(readLine.lastIndexOf("&") + 2);
			String[] ss = APIs.split(" ");
			for (String str : ss) {

				APIlists.add(str);

			}
		}
		br.close();

		ArrayList<Map.Entry<String, Integer>> list = new proAPIMsg().getAPIFrequency(APIlists);
		List<String> OnceAppearedAPIList = new LinkedList<String>();

		for (int j = 0; j < list.size(); j++) {
			if (list.get(j).getValue() == 1) {
				//System.out.println(list.get(j).getValue()+"     "+list.get(j).getKey());
				OnceAppearedAPIList.add(list.get(j).getKey());
			}
		}


		return OnceAppearedAPIList;
	}

	public static void countAPINum2(String originalFilePath, String mode)
			throws IOException, RowsExceededException, WriteException {

		String fileRoot = originalFilePath.substring(0, originalFilePath.lastIndexOf("\\"));
		String WriteFilePath = fileRoot + "\\" + mode + ".xls";

		BufferedReader br = new BufferedReader(new FileReader(fileRoot + "\\" + mode + ".txt"));
		List<String> APIlists = new ArrayList<String>();
		String readLine = null;
		while ((readLine = br.readLine()) != null) {

			// get APIS
			String APIs = readLine.substring(readLine.lastIndexOf("&") + 2);
			String[] ss = APIs.split(" ");
			for (String str : ss) {

				APIlists.add(str);

			}
		}
		br.close();

		ArrayList<Map.Entry<String, Integer>> list = new proAPIMsg().getAPIFrequency(APIlists);

		WritableWorkbook book = Workbook.createWorkbook(new File(WriteFilePath));
		WritableSheet sheet = book.createSheet("sheet1", 0);
		for (int i = 0; i < list.size(); i++) {// irow,1lie
			Label label = new Label(0, i, list.get(i).getKey());
			sheet.addCell(label);

			Number nb = new Number(1, i, list.get(i).getValue());
			sheet.addCell(nb);
		}
		// 生成格式
		WritableCellFormat format = new WritableCellFormat();
		// 垂直对齐
		format.setAlignment(Alignment.CENTRE);
		sheet.getSettings().setDefaultColumnWidth(20);
		book.write();
		book.close();

	}

	public static void printExcel(String fileRoot, List<FileData> FileDataList, String mode)
			throws IOException, RowsExceededException, WriteException {
		for (int j = 0; j < 6; j++) {
			String WriteFilePath = fileRoot + "\\" + mode + "data" + j + ".xls";
			WritableWorkbook book;

			book = Workbook.createWorkbook(new File(WriteFilePath));
			WritableSheet sheet = book.createSheet("sheet1", 0);

			Label label = new Label(0, 0, "ID");
			sheet.addCell(label);

			label = new Label(1, 0, "MethodName");
			sheet.addCell(label);

			label = new Label(2, 0, "ParameterName");
			sheet.addCell(label);

			label = new Label(3, 0, "Annotation");
			sheet.addCell(label);

			label = new Label(4, 0, "API");
			sheet.addCell(label);

			for (int i = 1; i <= 60000; i++) {// irow,1lie

				if (j * 60000 + i < FileDataList.size()) {
					Number nb = new Number(0, i + 1, j * 60000 + i);
					sheet.addCell(nb);

					label = new Label(1, i + 1, FileDataList.get(j * 60000 + i).getMethodName());
					sheet.addCell(label);

					label = new Label(2, i + 1, FileDataList.get(j * 60000 + i).getParameterName());
					sheet.addCell(label);

					label = new Label(3, i + 1, FileDataList.get(j * 60000 + i).getAnnotation());
					sheet.addCell(label);

					label = new Label(4, i + 1, FileDataList.get(j * 60000 + i).getAPI());
					sheet.addCell(label);
				} else {
					break;
				}

			}
			// 生成格式
			WritableCellFormat format = new WritableCellFormat();
			// 垂直对齐
			format.setAlignment(Alignment.CENTRE);
			sheet.getSettings().setDefaultColumnWidth(20);
			book.write();
			book.close();
		}

	}

	public static void countAPINum(String filepath, String jarName) {

		String fileRoot = filepath.substring(0, filepath.lastIndexOf("\\"));
		filepath = fileRoot + "\\" + jarName + ".txt";

		BufferedReader br;
		try {
			br = new BufferedReader(new InputStreamReader(new FileInputStream(new File(filepath))));
			String line = "";
			while ((line = br.readLine()) != null) {
				String[] temp = line.split(" ");
				for (String str : temp) {
					if (str.contains("(")) {
						str = str.substring(0, str.indexOf("("));
						oneJarAPI.add(str);
					}

				}
			}

			ArrayList<Map.Entry<String, Integer>> list = new proAPIMsg().getAPIFrequency(oneJarAPI);
			String WriteFilePath = fileRoot + "\\" + jarName + ".xls";
			WritableWorkbook book = Workbook.createWorkbook(new File(WriteFilePath));
			WritableSheet sheet = book.createSheet("sheet1", 0);
			System.out.println(list.size());
			for (int i = 0; i < list.size(); i++) {// irow,1lie
				Label label = new Label(0, i, list.get(i).getKey() + ": " + list.get(i).getValue());
				sheet.addCell(label);
			}
			// 生成格式
			WritableCellFormat format = new WritableCellFormat();
			// 垂直对齐
			format.setAlignment(Alignment.CENTRE);
			sheet.getSettings().setDefaultColumnWidth(20);
			book.write();
			book.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RowsExceededException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (WriteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static void cutOneJarAPI(String fileroot, String jarName) {
		try {
			String temp = fileroot.substring(0, fileroot.lastIndexOf("\\"));
			String filethirdpart = temp + "\\" + jarName + ".txt";

			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(new File(fileroot))));
			BufferedWriter writerThirdPart = new BufferedWriter(
					new OutputStreamWriter(new FileOutputStream(filethirdpart, true), "UTF-8"));
			String line = "";

			while ((line = br.readLine()) != null) {
				String[] APIS = line.split(" ");
				String thirdPart = "";

				for (String str : APIS) {
					if (str.contains(jarName)) {
						thirdPart = thirdPart + str + " ";
					}
				}

				if (!thirdPart.equals("")) {
					writerThirdPart.append(thirdPart);
					writerThirdPart.append("\n");
				}

			}
			writerThirdPart.close();
			br.close();
		} catch (FileNotFoundException | UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static String filterAnnotation(String annotation) {
		annotation = annotation.replace("，", "");
		annotation = annotation.replace("。", "");
		annotation = annotation.replace("；", "");
		annotation = annotation.replace("、", "");
		annotation = annotation.replace("-", "");
		annotation = annotation.replace("”", "");
		annotation = annotation.replace("“", "");
		annotation = annotation.replace("//", "");
		annotation = annotation.replace("（", "");
		annotation = annotation.replace("）", "");
		annotation = annotation.replace("：", "");
		annotation = annotation.replace("=", "");
		annotation = annotation.replace("》", "");
		annotation = annotation.replace("《", "");
		annotation = annotation.replace("<", "");
		annotation = annotation.replace(">", "");
		annotation = annotation.replace("\"", "");
		annotation = annotation.replace("$", "");
		annotation = annotation.replace("￥", "");
		annotation = annotation.replace("%", "");
		annotation = annotation.replace("#", "");
		annotation = annotation.replace("'", "");
		annotation = annotation.replace(".", "");
		annotation = annotation.replace(":", "");
		annotation = annotation.replace("/", "");
		annotation = annotation.replace(")", " ");
		annotation = annotation.replace("(", " ");
		annotation = annotation.replace(".", " ");
		annotation = annotation.replace("?", " ");
		annotation = annotation.replace(",", " ");
		annotation = annotation.replace(",", " ");
		annotation = annotation.replace("`", " ");
		annotation = annotation.replace(",", "");
		annotation = annotation.replace("arn", "");
		annotation = annotation.replace("rrb", "");
		annotation = annotation.replace("lrb", "");
		annotation = annotation.replace("lsb", "");
		annotation = annotation.replace("rsb", "");
		annotation = annotation.replace("	，", "");
		annotation = annotation.replace(";", "");

		return annotation;
	}

	private static String getNameAndParameter(String methodName) {

		methodName = new DocFileDeal().splitMethodName(methodName);
		// System.out.println(methodName);
		methodName = methodName.replace("[", " [ ");
		methodName = methodName.replaceAll("]", " ] ");
		methodName = methodName.replace(",", "");
		methodName = methodName.replace("<", " < ");
		methodName = methodName.replace(">", " > ");

		return methodName;

	}
}
