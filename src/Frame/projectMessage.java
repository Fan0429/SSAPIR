package Frame;

public class projectMessage {

	private String projetName;
	private long projectLines;
	private int fileNumbers;
	private int pairNumbers;

	projectMessage(String projetName, long projectLines, int fileNumbers, int pairNumber) {
		
		setProjetName(projetName);
		setProjectLines(projectLines);
		setFileNumbers(fileNumbers);
		setPairNumber(pairNumber);
		
	}	

	public String getProjetName() {
		return projetName;
	}

	public void setProjetName(String projetName) {
		this.projetName = projetName;
	}

	public long getProjectLines() {
		return projectLines;
	}

	public void setProjectLines(long projectLines) {
		this.projectLines = projectLines;
	}

	public int getFileNumbers() {
		return fileNumbers;
	}

	public void setFileNumbers(int fileNumbers) {
		this.fileNumbers = fileNumbers;
	}

	public int getPairNumber() {
		return pairNumbers;
	}

	public void setPairNumber(int pairNumbers) {
		this.pairNumbers = pairNumbers;
	}


}
