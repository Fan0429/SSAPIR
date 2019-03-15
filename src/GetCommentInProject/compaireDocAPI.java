package GetCommentInProject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import DataPreprocess.addClasspath;



public class compaireDocAPI {

	private Map map = new HashMap();

	public static List<String> getlist(String fileRoot) {
		// get the first file list
		List<String> fileList = new LinkedList<String>();
		File root = new File(fileRoot);
		File[] files = root.listFiles();
		for (File file : files) {
			if (file.isFile()) {
				if (file.getAbsolutePath().endsWith(".java")) {
					fileList.add(file.getAbsolutePath());
				}
			}
		}
		return fileList;
	}

	public static File createFile(String filePath) throws IOException {
		File file = new File(filePath);
		File fileParent = file.getParentFile();
		if (!fileParent.exists()) {
			fileParent.mkdirs();
		}
		file.createNewFile();
		return file;
	}

	public Map compaireTwo1(String APIroot, String DocRoot, String WriteFilePath, String oneProAPIPath)
			throws IOException {

		List<String> APIpathList = new LinkedList<String>();
		List<String> DocpathList = new LinkedList<String>();

		APIpathList = new addClasspath().getFilelist(APIroot);// 不说了，这里改一下，路径重新写吧哥，以前的路径写得不好
		DocpathList = new addClasspath().getFilelist(DocRoot);

		for (String Docpath : DocpathList) {
			for (String APIpath : APIpathList) {

				List<String> DocFilepathList = new LinkedList<String>();
				List<String> APIFilepathList = new LinkedList<String>();
				DocFilepathList = getlist(Docpath);
				APIFilepathList = getlist(APIpath);

				String DocprojectName = Docpath.substring(Docpath.lastIndexOf("\\") + 1);
				String APIprojectName = APIpath.substring(APIpath.lastIndexOf("\\") + 1);
				int pairCount = 0;
				List<String> oneProAPI = new LinkedList<String>();
				if (DocprojectName.equals(APIprojectName)) {

					for (String DocFileName : DocFilepathList) {
						for (String APIFileName : APIFilepathList) {
							String DocName = DocFileName.substring(DocFileName.lastIndexOf("\\") + 1);
							String APIName = APIFileName.substring(APIFileName.lastIndexOf("\\") + 1);
							if (DocName.equals(APIName)) {

								BufferedReader brDoc = new BufferedReader(
										new InputStreamReader(new FileInputStream(DocFileName)));
								BufferedReader brAPI = new BufferedReader(
										new InputStreamReader(new FileInputStream(APIFileName)));

								String lineDoc = "";
								String lineAPI = "";

								List<List<String>> APIlist = new LinkedList<List<String>>();
								List<List<String>> Doclist = new LinkedList<List<String>>();

								while ((lineAPI = brAPI.readLine()) != null) {// 文件读完一次，光标就不会再回去了
									String[] APIs = null;
									APIs = lineAPI.split(" ");
									List<String> temp = new LinkedList<String>();
									for (String str : APIs)
										temp.add(str);
									APIlist.add(temp);

								}

								while ((lineDoc = brDoc.readLine()) != null) {
									lineDoc = lineDoc.replace("{", " ");
									String[] Docs = null;
									Docs = lineDoc.split(" ");

									Docs[0] = Docs[0].replace("#", "");
									List<String> temp = new LinkedList<String>();
									for (String str : Docs)
										temp.add(str);
									Doclist.add(temp);
								}

								for (List<String> API1 : APIlist) {
									for (List<String> Doc1 : Doclist) {
										if (API1.get(0).equals(Doc1.get(0)) && API1.size() > 1) {
											// System.out.println(Docpath.substring(Docpath.lastIndexOf("\\") + 1));
											// System.out.println(Doc1);
											pairCount++;

											File fileJre = createFile(
													WriteFilePath + "\\" + "jreAPI" + "\\" + "jrefianlly.txt");
											File filethirdpart = createFile(WriteFilePath + "\\" + "thirdpartAPI" + "\\"
													+ "thirdpartfianlly.txt");

											BufferedWriter writerJer = new BufferedWriter(new OutputStreamWriter(
													new FileOutputStream(fileJre, true), "UTF-8"));
											BufferedWriter writerThirdPart = new BufferedWriter(new OutputStreamWriter(
													new FileOutputStream(filethirdpart, true), "UTF-8"));

											String jreAPI = "";
											String thirdPartAPI = "";

											for (int i = 1; i < API1.size(); i++) {
												if (API1.get(i).contains("main.")||API1.get(i).contains("test.")) {
													thirdPartAPI = thirdPartAPI + API1.get(i) + " ";
												} else {
													jreAPI = jreAPI + API1.get(i) + " ";
												}
											}
											jreAPI = "[" + jreAPI + "]";
											thirdPartAPI = "[" + thirdPartAPI + "]";
											writerJer.append(Doc1 + " " + jreAPI);
											writerJer.append("\n");
											writerThirdPart.append(Doc1 + " " + thirdPartAPI);
											writerThirdPart.append("\n");
											writerJer.close();
											writerThirdPart.close();

											File writerFile = createFile(WriteFilePath + "\\" + "fianlly.txt");
											BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
													new FileOutputStream(writerFile, true), "UTF-8"));
											API1.remove(0);
											writer.append(Doc1 + " " + API1);
											writer.append("\n");
											
											
											File s1 = createFile(WriteFilePath + "\\" + "jreAPI" + "\\" +"ProMsg"+"\\"+DocprojectName + ".txt");
											BufferedWriter writerOneProJer = new BufferedWriter(new OutputStreamWriter(
													new FileOutputStream(s1, true), "UTF-8"));
											writerOneProJer.append(Doc1 + " " + jreAPI);
											writerOneProJer.append("\n");
											
											File s2 =createFile(WriteFilePath + "\\" + "thirdpartAPI" + "\\" +"ProMsg"+"\\"+DocprojectName + ".txt");
											BufferedWriter writerOneProthirdpart = new BufferedWriter(new OutputStreamWriter(
													new FileOutputStream(s2, true), "UTF-8"));
											writerOneProthirdpart.append(Doc1 + " " + thirdPartAPI);
											writerOneProthirdpart.append("\n");
											
											writerOneProJer.close();
											writerOneProthirdpart.close();
											
											File writeOneProFile = createFile(
													oneProAPIPath + "\\" + DocprojectName + ".txt");
											BufferedWriter writeOnePro = new BufferedWriter(new OutputStreamWriter(
													new FileOutputStream(writeOneProFile, true), "UTF-8"));
											writeOnePro.append(Doc1 + " " + API1);
											writeOnePro.append("\n");

											writer.close();
											writeOnePro.close();
											break;
										}
									}
								}

								brAPI.close();
								brDoc.close();
							}
						}
					}
					map.put(DocprojectName, pairCount);
				}
				pairCount = 0;
			}
		}
		System.out.println(map);
		return map;

	}

}
