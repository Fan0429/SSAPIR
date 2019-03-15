package Frame;

public class FileData {

	private String methodName;
	private String parameterName;
	private String annotation;
	private String API;
	private int ID;

	public FileData(String methodName, String parameterName, String annotation, String api) {
		setMethodName(methodName);
		setParameterName(parameterName);
		setAnnotation(annotation);
		setAPI(api);
	}

	public FileData() {

	}

	public String getMethodName() {
		return methodName;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	public String getParameterName() {
		return parameterName;
	}

	public void setParameterName(String parameterName) {
		this.parameterName = parameterName;
	}

	public String getAnnotation() {
		return annotation;
	}

	public void setAnnotation(String annotation) {
		this.annotation = annotation;
	}

	public String getAPI() {
		return API;
	}

	public void setAPI(String aPI) {
		API = aPI;
	}

	public int getID() {
		return ID;
	}

	public void setID(int iD) {
		ID = iD;
	}
}
