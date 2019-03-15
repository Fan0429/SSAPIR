package ComparedApproachFrame;

public class APIDocMsg {

	
	private int ID ;
	private String className;
	private String methodName;
	private String parameterInfo;
	private String methodDescription;
	private String modifierType;
	
	public APIDocMsg() {};
	
	public APIDocMsg(int iD, String className, String methodName, String parameterInfo, String methodDescription,
			String modifierType) {
		super();
		ID = iD;
		this.className = className;
		this.methodName = methodName;
		this.parameterInfo = parameterInfo;
		this.methodDescription = methodDescription;
		this.modifierType = modifierType;
	}
	
	
	public int getID() {
		return ID;
	}
	public void setID(int iD) {
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
	public String getParameterInfo() {
		return parameterInfo;
	}
	public void setParameterInfo(String parameterInfo) {
		this.parameterInfo = parameterInfo;
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
