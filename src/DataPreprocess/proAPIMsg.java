package DataPreprocess;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import Frame.ProAPIMessage;
import jxl.Workbook;
import jxl.format.Alignment;
import jxl.format.VerticalAlignment;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

import java.util.TreeMap;

public class proAPIMsg {



	public static List<String> getlist(String fileRoot) {
		// get the first file list
		List<String> fileList = new LinkedList<String>();
		File root = new File(fileRoot);
		File[] files = root.listFiles();
		for (File file : files) {
			if (file.isFile()) {
				if (file.getAbsolutePath().endsWith(".txt")) {
					fileList.add(file.getAbsolutePath());
				}
			}
		}
		return fileList;
	}

	public void generateAPIMsgFile(String fileRoot) throws IOException, RowsExceededException, WriteException {

		List<String> fileRootList = new LinkedList<String>();
		fileRootList = new proAPIMsg().getlist(fileRoot);
		int proPairs = 0;
		List<ProAPIMessage> ProsAPIMessage = new LinkedList<ProAPIMessage>();
		for (String fileroot : fileRootList) {

			String projectName = fileroot.substring(fileroot.lastIndexOf("\\") + 1).replace(".txt", "");

			BufferedReader br = new BufferedReader(new FileReader(fileroot));
			List<String> APIlists = new ArrayList<String>(); 
			String readLine = null;
			while ((readLine = br.readLine()) != null) {
				proPairs++;
				// get APIS
				String APIs = readLine.substring(readLine.indexOf("]") + 3);
				APIs = APIs.replace("]", "");
				APIs = APIs.replace("[", "[]");
				APIs = APIs.replaceAll(", ", " ");

				String[] ss = APIs.split(" ");
				for (String str : ss) {
					APIlists.add(str);
				}
			}
			br.close();

			ArrayList<Map.Entry<String, Integer>> list = getAPIFrequency(APIlists);
			for (int i = 0; i < list.size(); i++) {
				// System.out.println(list.get(i).getKey() + ": " + list.get(i).getValue());
			}

			ProAPIMessage proAPIMsg = new ProAPIMessage(projectName, proPairs, list);
			ProsAPIMessage.add(proAPIMsg);
			proPairs = 0;
		}
		
		String WriteFilePath=fileRoot+"\\"+"ProjectAPIMessage.xls";
		printExcel(ProsAPIMessage,WriteFilePath);
		
	}

	public void printExcel(List<ProAPIMessage> ProsAPIMsg, String WriteFilePath) throws IOException, RowsExceededException, WriteException {

		WritableWorkbook book = Workbook.createWorkbook(new File(WriteFilePath));
		WritableSheet sheet = book.createSheet("sheet1", 0);

	
		
		for (int i = 0; i < ProsAPIMsg.size(); i++) {//lie
			ProAPIMessage pro = ProsAPIMsg.get(i);
			for (int j = 0; j < 100; j++) {//hang
		
				if (j == 0) {
					Label label = new Label(i, j, pro.getProjetName());
					sheet.addCell(label);
	
				}
				else if (j == 1) {
					Label label = new Label(i, j, "ProjectPairCount" +" : "+ pro.getProjectPairCount());
					sheet.addCell(label);
					
				}
				else {
					Label label = new Label(i, j,pro.getProjectAPIsSortNum().get(j).getKey() + ": "
							+ pro.getProjectAPIsSortNum().get(j).getValue());
					sheet.addCell(label);
					
				}
			}
		}
		//生成格式 
		WritableCellFormat format= new WritableCellFormat();
		//垂直对齐
		format.setAlignment(Alignment.CENTRE);
		sheet.getSettings().setDefaultColumnWidth(80);
		sheet.getSettings().setDefaultRowHeight(1000);
		book.write();
		book.close();

	}

	public ArrayList<Map.Entry<String, Integer>> getAPIFrequency(List<String> APIlists) {
		Map<String, Integer> wordsCount = new TreeMap<String, Integer>(); // save message，key is word，value is word nums
		for (String li : APIlists) {
			if (wordsCount.get(li) != null ) {//&& !wordsCount.isEmpty()
				wordsCount.put(li, wordsCount.get(li) + 1);
			} else {
				wordsCount.put(li, 1);
			}
		}
		// this is the way map.entry's usage to sort the map
		ArrayList<Map.Entry<String, Integer>> list = new ArrayList<Map.Entry<String, Integer>>(wordsCount.entrySet());
		Collections.sort(list, new Comparator<Map.Entry<String, Integer>>() {
			@Override
			public int compare(Entry<String, Integer> o1, Entry<String, Integer> o2) {
				return o2.getValue() - o1.getValue(); // decrease
			}
		});
		return list;
	}

}
