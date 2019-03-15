package Frame;

public class DocMsg {

	private String ID;
	private String className;
	private String methodName;
	private String parameterInformation;
	private String methodDescription;
	private String modifierType;
	
	
	
	public DocMsg(){};
	
	
	public DocMsg(String iD, String className, String methodName, String parameterInformation, String methodDescription,
			String modifierType) {
		this.ID = iD;
		this.className = className;
		this.methodName = methodName;
		this.parameterInformation = parameterInformation;
		this.methodDescription = methodDescription;
		this.modifierType = modifierType;
	}
	
	
	
	public String getID() {
		return ID;
	}
	public void setID(String iD) {
		ID = iD;
	}
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	public String getMethodName() {
		return methodName;
	}
	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}
	public String getParameterInformation() {
		return parameterInformation;
	}
	public void setParameterInformation(String parameterInformation) {
		this.parameterInformation = parameterInformation;
	}
	public String getMethodDescription() {
		return methodDescription;
	}
	public void setMethodDescription(String methodDescription) {
		this.methodDescription = methodDescription;
	}
	public String getModifierType() {
		return modifierType;
	}
	public void setModifierType(String modifierType) {
		this.modifierType = modifierType;
	}
	
	
	
	
	
	
	
	
}
