package GetCommentInProject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

/**
 * @author Fan ��һ������Ҫ�Ƕ��ļ����и��ֵĴ���
 */
public class FileDeal {

	public List<String> getImport(String filePath) {// �������������ȡ���еİ���

		List<String> finallyPackageSequence = new LinkedList<String>();// ���İ���+����������
		File file = new File(filePath);
		String parent = file.getParent().substring(file.getParent().lastIndexOf("\\") + 1);// �����Ǳ������У������޳��õ�
		BufferedReader reader = null;
		// System.out.println(filePath);
		while (true) {
			try {
				reader = new BufferedReader(new FileReader(filePath));
				String line = null;
				while ((line = reader.readLine()) != null) {
					// �ɹ�����һ�У�����Ҫ��import��Ȼ�󳤶Ȳ���Ϊ2���Ҳ���������Ŀ¼
					if (line.contains("import") && !(line.split(".").length == 2) && !line.contains(parent)) {
						String[] packageSequence = line.split(" ");
						if (packageSequence.length > 1) {
							// System.out.println(line);
							String packageSequence1 = packageSequence[packageSequence.length - 1];
							// �������������*��������������������Ƿ��ص������У����Բ���
							if (packageSequence1.substring(packageSequence1.lastIndexOf(".") + 1).replace(";", "")
									.equals("*")) {
								continue;
							}
							// ��Ӻ����if����Ҫ��һ�¾�OK��
							finallyPackageSequence.add(packageSequence1.replace(";", ""));
						}

					}
				}
				reader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			// System.out.println(finallyPackageSequence);
			return finallyPackageSequence;

		}

	}

	public void getAllJavaFile(String filepath, List<String> fileList) {// ����������ڱ������г��������е�java�ļ�

		File root = new File(filepath);
		File[] files = root.listFiles();
		for (File file : files) {
			// ������ļ���

			if (file.isDirectory()) {
				this.getAllJavaFile(file.getAbsolutePath(), fileList);
			}
			// ������ļ�
			if (file.isFile()) {
				// �ļ��ĺ�׺����.java
				if (file.getAbsolutePath().endsWith(".java")) {
					if (!fileList.contains(file.getAbsolutePath())) {
						fileList.add(file.getAbsolutePath());
					}
				}
			}
		}

	}

	public void fileDelete(String fileRoot) {// �����������ɾ����СΪ0�����ļ�

		File root = new File(fileRoot);
		File[] files = root.listFiles();
		for (File file : files) {
			
			// ������ļ���
			if (file.isDirectory()) {
				this.fileDelete(file.getAbsolutePath());
			}
			// ������ļ�
			if (file.isFile()) {
				// �ļ��ĺ�׺����.java�������СΪ0��ɾ��
				if (file.getAbsolutePath().endsWith(".java")) {
					if (file.length() == 0) {
						file.delete();
					} else {
						// System.out.println(file.getName());
					}
				}
			}
		}
	}

	public void writeInOneFile(String fileRoot, String wirteOneFilepath, String classNameFilepath)
			throws FileNotFoundException, UnsupportedEncodingException {

		File writeFile = new File(wirteOneFilepath);

		File root = new File(fileRoot);
		File[] files = root.listFiles();
		for (File file : files) {
			// ������ļ���
			if (file.isDirectory()) {
				this.writeInOneFile(file.getAbsolutePath(), wirteOneFilepath, classNameFilepath);
			}
			// ������ļ�
			if (file.isFile()) {
				// �ļ��ĺ�׺����.java����д���ļ���
				if (file.getAbsolutePath().endsWith(".java")) {

					BufferedReader reader = null;
					try {
						OutputStreamWriter osw = null;
						FileOutputStream fos = new FileOutputStream(wirteOneFilepath, true);// true��ʾ׷�Ӵ�,falseÿ�ζ����������д
						osw = new OutputStreamWriter(fos, "utf-8");
						BufferedWriter bw = new BufferedWriter(osw);

						reader = new BufferedReader(new FileReader(file));
						String line = null;
						while ((line = reader.readLine()) != null) {
							String[] lines = line.split(" ");
							if (lines.length >0) {
								for (int i = 1; i < lines.length; i++) {
									bw.write(lines[i] + " ");
								}
								bw.write("\n");
							}
						}

						reader.close();
						bw.close();
						fos.close();
						osw.close();

					} catch (IOException e) {
						e.printStackTrace();
					}

					BufferedReader br2 = null;

					try {
						OutputStreamWriter osw = null;
						FileOutputStream fos = new FileOutputStream(classNameFilepath, true);// true��ʾ׷�Ӵ�,falseÿ�ζ����������д
						osw = new OutputStreamWriter(fos, "utf-8");
						BufferedWriter bw = new BufferedWriter(osw);

						br2 = new BufferedReader(new FileReader(file));
						String line = null;
						while ((line = br2.readLine()) != null) {
							String[] lines = line.split(" ");
							if (lines.length >0) {
								String ss = "";
								ss=new FileDeal().splitClassName(lines[0]);
								ss=ss.trim();
								bw.write(ss);
								bw.write("\n");
							}
						}

						br2.close();
						bw.close();
						fos.close();
						osw.close();
					} catch (IOException e) {
						e.printStackTrace();
					}

				}
			}
		}
	}

	public String splitClassName(String ClassName) {//��һ���ַ��������մ�Щ�ֿ�

		StringBuffer sbf = new StringBuffer("");
		ClassName = ClassName.replace("_", " ");
		for (int i = 0; i < ClassName.length(); i++) {
			char tempChr = ClassName.charAt(i);
			if (tempChr >= 'A' && tempChr <= 'Z') {
				sbf.append(" ");// ����Ǵ�д��ĸ�������ַ�ǰ�����һ���ո�
			}
			sbf.append(tempChr);
		}

		ClassName = sbf.toString();
		return ClassName;
	}

	public int writeClassFile(String writeFilepath, List<List<String>> writeFile, String fileRoot) {
		// ���������д�ļ���һ����Ŀһ���ļ��У�����һ������һ���ļ���һ����һ�����������е�API����
		// ���ѭ��ʹ����ɸѡû��API���е��ļ��������ڲ�һ����
		int count = 0;
		String methodName = null;
		// System.out.println(fileRoot);

		String writepath = "I:\\JAVA\\TestPart\\save";
		// String writepath = "I:\\JAVA\\Data\\Datasets\\AiData\\1";

		String temp = writeFilepath.substring(fileRoot.length() + 1);
		String projectName = temp.substring(0, temp.indexOf("\\"));
		String fileName = temp.substring(temp.lastIndexOf("\\") + 1);
		String newFileName = writepath + "\\" + projectName + "\\" + fileName;
		// String newFileName = writepath + "\\"+"1"+"\\" + projectName + "\\" +
		// fileName;
		// System.out.println(newFileName);

		File file = new File(newFileName);
		if (!file.getParentFile().exists()) {
			boolean result = file.getParentFile().mkdirs();
			if (!result) {
				System.out.println("����ʧ��");
			}
		}
		OutputStreamWriter osw = null;
		try {
			FileOutputStream fos = new FileOutputStream(newFileName, true);// true��ʾ׷�Ӵ�,falseÿ�ζ����������д
			osw = new OutputStreamWriter(fos, "utf-8");
			BufferedWriter bw = new BufferedWriter(osw);

			for (int i = 0; i < writeFile.size(); i++) {
				// System.out.println(writeFile.get(i).size());
				// System.out.println(writeFile.get(i));
				if (writeFile.get(i).size() > 1) {
					for (int j = 0; j < writeFile.get(i).size(); j++) {

						bw.write(writeFile.get(i).get(j) + " ");
						count++;
						// System.out.print(newFileName);

					}
					bw.write("\n");
				}

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

		return count / 2;
	}

	public void writeSingleFile(String writeFilepath, List<List<String>> writeFile, String fileRoot) {
		// ��������ǣ�д�ļ�����д��һ������һ���ļ��У�ÿ�����������ļ����ļ�����������������õ���API���У���һ��Ԫ���Ƿ�����
		for (int k = 0; k < writeFile.size(); k++) {
			String methodName = null;
			System.out.println(writeFile.get(k).size());
			if (writeFile.get(k).size() > 1) {
				methodName = writeFile.get(k).get(0);

				String writepath = fileRoot.replace("H:", "I:");
				String temp = writeFilepath.substring(fileRoot.length() + 1);
				String projectName = temp.substring(0, temp.indexOf("\\"));
				String fileName = temp.substring(temp.lastIndexOf("\\") + 1);
				String newFileName = "I:\\JAVA\\Data\\SingleFileDataSets" + "\\" + projectName + "\\" + fileName + "\\"
						+ methodName + ".java";

				System.out.println(newFileName);
				// System.out.println(methodName);

				File file = new File(newFileName);
				if (!file.getParentFile().exists()) {
					boolean result = file.getParentFile().mkdirs();
					if (!result) {
						System.out.println("����ʧ��");
					}
				}
				OutputStreamWriter osw = null;
				try {
					FileOutputStream fos = new FileOutputStream(newFileName, false);// true��ʾ׷�Ӵ�,falseÿ�ζ����������д
					osw = new OutputStreamWriter(fos, "utf-8");
					BufferedWriter bw = new BufferedWriter(osw);

					bw.write(writeFile.get(k).toString());
					// for (int i = 0; i < writeFile.size(); i++) {
					// for (int j = 0; j < writeFile.get(i).size(); j++) {
					// bw.write(writeFile.get(i).get(j) + " ");
					// }
					// if (writeFile.get(i).size() != 0) {
					// bw.write("\n");
					// }
					// }

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
	}
}
