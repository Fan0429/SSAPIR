package Frame;

import java.util.ArrayList;
import java.util.Map;

public class ProAPIMessage {

	private String projetName;
	private int projectPairCount;
	private ArrayList<Map.Entry<String, Integer>> projectAPIsSortNum;

	public ProAPIMessage(String projetName, int projectPairCount, ArrayList<Map.Entry<String, Integer>> projectAPIsSortNum) {
		setProjetName(projetName);
		setProjectPairCount(projectPairCount);
		setProjectAPIsSortNum(projectAPIsSortNum);
	}

	public String getProjetName() {
		return projetName;
	}

	public void setProjetName(String projetName) {
		this.projetName = projetName;
	}

	public int getProjectPairCount() {
		return projectPairCount;
	}

	public void setProjectPairCount(int projectPairCount) {
		this.projectPairCount = projectPairCount;
	}

	public ArrayList<Map.Entry<String, Integer>> getProjectAPIsSortNum() {
		return projectAPIsSortNum;
	}

	public void setProjectAPIsSortNum(ArrayList<Map.Entry<String, Integer>> projectAPIsSortNum) {
		this.projectAPIsSortNum = projectAPIsSortNum;
	}

}
