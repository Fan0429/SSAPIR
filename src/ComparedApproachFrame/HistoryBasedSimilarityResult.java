package ComparedApproachFrame;

import Frame.FileData;

public class HistoryBasedSimilarityResult {
	
	private FileData APILibraryData ;
	private  double similarity;

	public HistoryBasedSimilarityResult(FileData aPILibraryData, double similarity) {
		super();
		APILibraryData = aPILibraryData;
		this.similarity = similarity;
	}
	
	
	public FileData getAPILibraryData() {
		return APILibraryData;
	}
	public void setAPILibraryData(FileData aPILibraryData) {
		APILibraryData = aPILibraryData;
	}
	public double getSimilarity() {
		return similarity;
	}
	public void setSimilarity(double similarity) {
		this.similarity = similarity;
	}
	
	

	
}
