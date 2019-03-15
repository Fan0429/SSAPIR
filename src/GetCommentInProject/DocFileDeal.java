package GetCommentInProject;

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
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.jdt.core.dom.CompilationUnit;


public class DocFileDeal {
	private List<String> filepathList = new LinkedList<String>();

	public List<String> getAllFilepath(String fileRoot) throws IOException {

		File root = new File(fileRoot);
		File[] files = root.listFiles();
		for (File file : files) {
			if (file.isDirectory()) {
				this.getAllFilepath(file.getAbsolutePath());
			}

			if (file.isFile()) {
				if (file.getAbsolutePath().endsWith(".java")) {

					filepathList.add(file.getAbsolutePath());
					// System.out.println(file.getAbsolutePath());
				}
			}
		}
		// System.out.println(filepathList.size());
		return filepathList;
	}

	public static void writeAnoFile(String fileRoot, String writeFilepath) throws IOException {

		List<String> filepathList = new LinkedList<String>();
		filepathList = new DocFileDeal().getAllFilepath(fileRoot);

		for (String filepath : filepathList) {
			List<String> finalString = new LinkedList<String>();

			// this methhod is to get the annotation
			getCommentFromOneFile gComment = new getCommentFromOneFile(filepath);

			finalString = gComment.getJavadocs();

			// System.out.println(finalString);
			String temp = filepath.substring(fileRoot.length() + 1);
			String projectName = temp.substring(0, temp.indexOf("\\"));
			String fileName = filepath.substring(filepath.lastIndexOf("\\") + 1);
			String newFileName = writeFilepath + "\\" + projectName + "\\" + fileName;

			File file = new File(newFileName);
			if (!file.getParentFile().exists()) {
				boolean result = file.getParentFile().mkdirs();
				if (!result) {
					System.out.println("??");
				}
			}
			OutputStreamWriter osw = null;
			try {
				FileOutputStream fos = new FileOutputStream(newFileName, true);// true��ʾ׷�Ӵ�,falseÿ�ζ����������д
				osw = new OutputStreamWriter(fos, "utf-8");
				BufferedWriter bw = new BufferedWriter(osw);

				for (String str : finalString) {

					bw.write(str + "\n");
				}

				bw.close();
				fos.close();
			} catch (Exception e) {
				System.out.println("writeOrderedPropertiesFile IOException:" + e.getMessage());
			} finally {
				if (osw != null) {
					try {
						osw.close();
					} catch (IOException e) {
						System.out.println("writeOrderedPropertiesFile close IOException:" + e.getMessage());
					}
				}
			}

		}

	}

	public void writeInOneFile(String filePath, String writeFilepath) throws IOException {
		// this method is to deal with the orginal API files,and write in one file
		File writeFile = new File(writeFilepath);

		File root = new File(filePath);
		File[] files = root.listFiles();
		for (File file : files) {
			if (file.isDirectory()) {
				this.writeInOneFile(file.getAbsolutePath(), writeFilepath);
			}

			if (file.isFile()) {
				if (file.getAbsolutePath().endsWith(".java")) {

					BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
					String line = "";
					String[] APIs = null;
					while ((line = br.readLine()) != null) {
						APIs = line.split(" ");
						if (APIs.length > 1) {// if only have method name ,then we break
							BufferedWriter bw = new BufferedWriter(
									new OutputStreamWriter(new FileOutputStream(writeFilepath, true), "UTF-8"));
							for (String str : APIs) {
								bw.write(str + " ");
								System.out.println(str);
							}
							bw.write("\n");
							bw.close();
						}
					}
					br.close();
				}
			}
		}
	}

	public void splitOneFile(String readFilepath, String APIClassName, String APIFile) throws IOException {

		File file = new File(readFilepath);
		BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
		String line = "";
		String[] APIs = null;
		while ((line = br.readLine()) != null) {
			APIs = line.split(" ");
			String MethodName = APIs[0];
			MethodName = splitMethodName(MethodName);

			BufferedWriter bw = new BufferedWriter(
					new OutputStreamWriter(new FileOutputStream(APIClassName, true), "UTF-8"));
			bw.write(MethodName + "\n");

			BufferedWriter bw2 = new BufferedWriter(
					new OutputStreamWriter(new FileOutputStream(APIFile, true), "UTF-8"));
			for (int i = 1; i < APIs.length; i++) {
				bw2.write(APIs[i] + " ");
			}
			bw2.write("\n");
			bw.close();
			bw2.close();
		}
	}

	public static String splitMethodName(String MethodName) {// ��һ���ַ��������մ�Щ�ֿ�

		StringBuffer sbf = new StringBuffer("");

		MethodName = MethodName.replace("_", " ");
		for (int i = 0; i < MethodName.length(); i++) {
			char tempChr = MethodName.charAt(i);
			if (tempChr >= 'A' && tempChr <= 'Z') {

				if (((i - 1) > 0) && (tempChr >= 'A' && tempChr <= 'Z')
						&& (MethodName.charAt(i - 1) >= 'A' && MethodName.charAt(i - 1) <= 'Z')) {
					sbf.append(tempChr);
					continue;
				}

				sbf.append(" ");
			}
			sbf.append(tempChr);
		}

		MethodName = sbf.toString();
		MethodName = MethodName.toLowerCase().trim();
		return MethodName;
	}

	// public static void main(String[] args) {
	// String s = "withXssMatchTuple, specifies, the, part, of, a, web, request,
	// that, you, want, AWS, WAF, ";
	// s= replaceChinese(s);
	// s= splitMethodName(s);
	// System.out.println(s);
	// }

	public static String replaceChinese(String annotation) {

		String regex = "[\u4e00-\u9fa5]";
		Pattern pat = Pattern.compile(regex);
		Matcher mat = pat.matcher(annotation);
		annotation = mat.replaceAll("");



		return annotation;
	}

	public static String getParameter(String Parameter) {
		if (Parameter.contains(",")) {
			Parameter = Parameter.substring(0, Parameter.lastIndexOf(","));
		}

		Parameter = Parameter.replace("*", " ");

		if (Parameter.contains("}#")) {
			Parameter = Parameter.replace("}#", "");
		}
		if (Parameter.contains("}#]")) {
			Parameter = Parameter.replace("}#]", "");
		}
		Parameter = Parameter.replace("%", "[").replace("&", "]");


		Parameter = Parameter.replace(","," , ");
		Parameter = Parameter.replace("<", " < ");
		Parameter = Parameter.replace(">", " > ");
		
		return Parameter;
	}

	public static String getNameAndParameter(String methodName, String Parameter) {

		Parameter = getParameter(Parameter);
		methodName = splitMethodName(methodName) + "[" + Parameter + "]";
		// System.out.println(methodName);
		methodName = methodName.replace("[", " [ ");
		methodName = methodName.replaceAll("]", " ] ");
		methodName = methodName.replace(",", " , ");
		methodName = methodName.replace("<", " < ");
		methodName = methodName.replace(">", " > ");

		return methodName;

	}

	public void splitOneFile2(String finalFilepath) throws IOException {
		// this method is to deal the API file and the anno file to replace chinese and
		// some wrong method name
		
		String finalFileName =finalFilepath.substring(finalFilepath.lastIndexOf("\\")+1);
		
		finalFilepath=finalFilepath.substring(0, finalFilepath.lastIndexOf("\\"));
		String annocationsPath = finalFilepath + "\\" + "annotation.txt";
		String APIsFilepath = finalFilepath + "\\" + "APIs.txt";
		String methodNamePath = finalFilepath + "\\" + "methodName.txt";
		String FinallyFile = finalFilepath + "\\" + finalFileName;

		File file = new File(FinallyFile);
		BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
		String line = "";

		while ((line = br.readLine()) != null) {
			String[] lines = null;
			lines = line.split(" ");
			// System.out.println(line);
			String annotation = line.substring(line.indexOf("[") + 1, line.indexOf("]"));
			String[] annotations = annotation.split(" ");

			// get method name
			String methodName = getNameAndParameter(annotations[0], annotations[1]);

			// get annotation
			String src = "";
			for (int i = 0; i < annotations.length; i++) {
				if (i != 0 && i != 1) {
					src = src + annotations[i] + " ";
				}
			}
			// deal annotation
			src = replaceChinese(src);
			src = splitMethodName(src);
			// System.out.println(src);

			// get APIS
			String APIs = line.substring(line.indexOf("]") + 3);
			APIs = APIs.replace("]", "");
			APIs = APIs.replace("[", "[]");
			APIs = APIs.replaceAll(", ", " ");
			// APIs = APIs.replace("main.java.", "");// these three lines delete some char
			// in APIs

			if (src.length() > 0 && APIs.length() > 0) {// some line don't have the annotation,so we filter
				BufferedWriter bw3 = new BufferedWriter(
						new OutputStreamWriter(new FileOutputStream(methodNamePath, true), "UTF-8"));
				bw3.write(methodName + "\n");

				BufferedWriter bw = new BufferedWriter(
						new OutputStreamWriter(new FileOutputStream(annocationsPath, true), "UTF-8"));
				bw.write(src + "\n");

				BufferedWriter bw2 = new BufferedWriter(
						new OutputStreamWriter(new FileOutputStream(APIsFilepath, true), "UTF-8"));
				bw2.write(APIs + "\n");

				bw3.close();
				bw.close();
				bw2.close();
			}

		}
	}

}
