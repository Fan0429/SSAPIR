package DataPreprocess;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class addClasspath {

	private List<String> srcs = new LinkedList<String>();
	private String classPath = "";
	private String projectPath = "";

	public void addSrc(String rootPath) throws IOException {
		List<String> fileRootList = new LinkedList<String>();
		fileRootList = getFilelist(rootPath);
		for (String fileroot : fileRootList) {

			// use method getClasspath to get .Classpath root
			String classPath = getClasspath(fileroot);

			// use method getFileLine to get .classpath's lines;
			int count = getFileLine(classPath);

			// use method getSrc to get all srcPath
			List<String> lines = new LinkedList<String>();
			getSrc(fileroot);

			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(classPath)));
			String line = "";
			int i = 0;
			while ((line = br.readLine()) != null) {
				i++;
				if (i == 3) {// we add the srcpath after line three
					for (String s : srcs) {	
						s=s.substring(rootPath.length()+1);
						s=s.substring(s.indexOf("\\")+1);
						
						lines.add("    " + "<classpathentry kind=\"src\" path=\"" + s + "\"/>");
					}
				}
				if(i != 3) {
					lines.add(line);
				}
				
			}

			BufferedWriter bw = new BufferedWriter(
					new OutputStreamWriter(new FileOutputStream(new File(classPath), false), "UTF-8"));
			for (String s : lines) {
				bw.write(s + "\n");

			}
			br.close();
			bw.close();
			srcs.clear();
		}

	}

	public void addProjectName(String rootPath) throws IOException {
		List<String> fileRootList = new LinkedList<String>();
		fileRootList = getFilelist(rootPath);
		for (String fileroot : fileRootList) {

			String ProjectName = fileroot.substring(rootPath.length() + 1);
			String projectpath = getProjectPath(fileroot);

			int count = getFileLine(projectpath);

			List<String> lines = new LinkedList<String>();

			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(projectpath)));
			String line = "";

			if (count <= 17) {// pan duan zhe ge wen jian shi wo men zi ji xie de hai shi yi jing cun zai de
				int i = 0;
				while ((line = br.readLine()) != null) {
					i++;
					if (i != 3) {// add the project name in line three
						lines.add(line);
					}
					if (i == 3) {
						lines.add("    " + "<name>" + ProjectName + "</name>");
					}
				}
				BufferedWriter bw = new BufferedWriter(
						new OutputStreamWriter(new FileOutputStream(new File(projectpath), false), "UTF-8"));
				for (String s : lines) {
					bw.write(s + "\n");
				}
				br.close();
				bw.close();
			}
		}

	}

	public String getProjectPath(String fileRoot) {
		// get the .project file's path
		File root = new File(fileRoot);
		File[] files = root.listFiles();
		for (File file : files) {
			if (file.isDirectory()) {
				this.getProjectPath(file.getAbsolutePath());
			} else

			if (file.isFile()) {
				if (file.getAbsolutePath().endsWith(".project")) {
					projectPath = file.getAbsolutePath();

				}
			}
		}
		return projectPath;
	}

	public String getClasspath(String fileRoot) {
		// get the .classpath file's path
		File root = new File(fileRoot);
		File[] files = root.listFiles();
		for (File file : files) {
			if (file.isDirectory()) {
				this.getClasspath(file.getAbsolutePath());
			} else

			if (file.isFile()) {
				if (file.getAbsolutePath().endsWith(".classpath")) {
					classPath = file.getAbsolutePath();

				}
			}
		}
		return classPath;
	}

	public int getFileLine(String fileroot) throws IOException {
		// get the file's line
		LineNumberReader lnr = new LineNumberReader(new FileReader(new File(fileroot)));
		lnr.skip(Long.MAX_VALUE);
		int count = lnr.getLineNumber() + 1;
		lnr.close();
		// System.out.println(count);
		return count;
	}

	public List<String> getFilelist(String fileRoot) {
		// get the first file list
		List<String> fileList = new LinkedList<String>();
		File root = new File(fileRoot);
		File[] files = root.listFiles();
		for (File file : files) {
			if (file.isDirectory()) {
				fileList.add(file.getAbsolutePath());
			}
		}

		return fileList;
	}

	public List<String> getSrc(String fileRoot) {
		// get the src path
		File root = new File(fileRoot);
		File[] files = root.listFiles();
		for (File file : files) {
			if (file.isDirectory()) {
				if (file.getName().equals("src")) {
					srcs.add(file.getAbsolutePath());
					// System.out.println(file.getAbsolutePath());
				}

				this.getSrc(file.getAbsolutePath());
			}
		}
		return srcs;
	}

	public void deleteFile(String fileRoot) throws IOException {
		// delete the wroth file we generate
		List<String> fileRootList = new LinkedList<String>();
		fileRootList = getFilelist(fileRoot);
		for (String fileroot : fileRootList) {
			deleteFile1(fileroot);
		}
	}

	public void deleteFile1(String fileRoot) throws IOException {

		File root = new File(fileRoot);
		File[] files = root.listFiles();
		for (File file : files) {
			if (file.isDirectory()) {
				this.deleteFile1(file.getAbsolutePath());

			} else if (file.isFile()) {
				if (file.getAbsolutePath().endsWith(".classpath") || file.getAbsolutePath().endsWith(".project")) {
					file.delete();
				}
			}
		}

	}

	public void createFile(String fileRoot) throws IOException {
		deleteFile(fileRoot);// because the file is append model ,so we delete first to make sure we
		// get the right file.

		List<String> fileRootList = new LinkedList<String>();
		fileRootList = getFilelist(fileRoot);

		for (String tempFile : fileRootList) {
			// System.out.println(tempFile);

			File ff = new File(tempFile);
			if (ff.listFiles().length > 0) {// wether the file is unll ,if not then go
				String temp = tempFile.substring(fileRoot.length() + 1);
				 
				String tempFile1 = tempFile + "\\" + ".classpath";
				String tempFile2 = tempFile + "\\" + ".project";
				File file = new File(tempFile1);
				if (!file.exists()) {
					//System.out.println(tempFile1);
					file.createNewFile();
					BufferedWriter bw = new BufferedWriter(
							new OutputStreamWriter(new FileOutputStream(file, false), "UTF-8"));
					String s = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n" + 
							"<classpath>\r\n" + 
							"	<classpathentry kind=\"src\" path=\"\"/>\r\n" + 
							"	<classpathentry kind=\"con\" path=\"org.eclipse.jdt.launching.JRE_CONTAINER\"/>\r\n" + 
							"	<classpathentry kind=\"output\" path=\"\"/>\r\n" + 
							"	<classpathentry kind=\"lib\" path=\"I:\\Jar\\android.jar\"/>\r\n" + 
							"	<classpathentry kind=\"lib\" path=\"I:\\Jar\\android-support-v4.jar\"/>\r\n" + 
							"	<classpathentry kind=\"lib\" path=\"I:\\Jar\\aws-java-sdk-1.10.38.jar\"/>\r\n" + 
							"	<classpathentry kind=\"lib\" path=\"I:\\Jar\\classmate-1.3.1.jar\"/>\r\n" + 
							"	<classpathentry kind=\"lib\" path=\"I:\\Jar\\commons-beanutils-1.7.0.jar\"/>\r\n" + 
							"	<classpathentry kind=\"lib\" path=\"I:\\Jar\\commons-collections-3.1.jar\"/>\r\n" + 
							"	<classpathentry kind=\"lib\" path=\"I:\\Jar\\commons-httpclient-3.1.jar\"/>\r\n" + 
							"	<classpathentry kind=\"lib\" path=\"I:\\Jar\\commons-io-1.3.jar\"/>\r\n" + 
							"	<classpathentry kind=\"lib\" path=\"I:\\Jar\\commons-lang-2.3.jar\"/>\r\n" + 
							"	<classpathentry kind=\"lib\" path=\"I:\\Jar\\commons-logging.jar\"/>\r\n" + 
							"	<classpathentry kind=\"lib\" path=\"I:\\Jar\\ezmorph-1.0.3.jar\"/>\r\n" + 
							"	<classpathentry kind=\"lib\" path=\"I:\\Jar\\gson-2.3.1.jar\"/>\r\n" + 
							"	<classpathentry kind=\"lib\" path=\"I:\\Jar\\gson-2.3.1-javadoc.jar\"/>\r\n" + 
							"	<classpathentry kind=\"lib\" path=\"I:\\Jar\\gson-2.3.1-sources.jar\"/>\r\n" + 
							"	<classpathentry kind=\"lib\" path=\"I:\\Jar\\guava-19.0-rc2.jar\"/>\r\n" + 
							"	<classpathentry kind=\"lib\" path=\"I:\\Jar\\hibernate-validator-4.3.0.Final.jar\"/>\r\n" + 
							"	<classpathentry kind=\"lib\" path=\"I:\\Jar\\jackson-annotations-2.8.3.jar\"/>\r\n" + 
							"	<classpathentry kind=\"lib\" path=\"I:\\Jar\\jackson-core-2.8.3.jar\"/>\r\n" + 
							"	<classpathentry kind=\"lib\" path=\"I:\\Jar\\jackson-core-asl-1.8.8.jar\"/>\r\n" + 
							"	<classpathentry kind=\"lib\" path=\"I:\\Jar\\jackson-databind-2.8.3.jar\"/>\r\n" + 
							"	<classpathentry kind=\"lib\" path=\"I:\\Jar\\jackson-mapper-asl-1.8.8.jar\"/>\r\n" + 
							"	<classpathentry kind=\"lib\" path=\"I:\\Jar\\jackson-module-jaxb-annotations-2.2.1.jar\"/>\r\n" + 
							"	<classpathentry kind=\"lib\" path=\"I:\\Jar\\jboss-logging-3.3.0.Final.jar\"/>\r\n" + 
							"	<classpathentry kind=\"lib\" path=\"I:\\Jar\\jcl-over-slf4j-1.7.21.jar\"/>\r\n" + 
							"	<classpathentry kind=\"lib\" path=\"I:\\Jar\\jetty-6.0.1.jar\"/>\r\n" + 
							"	<classpathentry kind=\"lib\" path=\"I:\\Jar\\jetty-embedded-6.0.1.jar\"/>\r\n" + 
							"	<classpathentry kind=\"lib\" path=\"I:\\Jar\\jetty-html-6.0.1.jar\"/>\r\n" + 
							"	<classpathentry kind=\"lib\" path=\"I:\\Jar\\jetty-management-6.0.2.jar\"/>\r\n" + 
							"	<classpathentry kind=\"lib\" path=\"I:\\Jar\\jetty-naming-6.0.1.jar\"/>\r\n" + 
							"	<classpathentry kind=\"lib\" path=\"I:\\Jar\\jetty-plus-6.0.1.jar\"/>\r\n" + 
							"	<classpathentry kind=\"lib\" path=\"I:\\Jar\\jetty-spring-6.0.2.jar\"/>\r\n" + 
							"	<classpathentry kind=\"lib\" path=\"I:\\Jar\\jetty-util-6.0.2.jar\"/>\r\n" + 
							"	<classpathentry kind=\"lib\" path=\"I:\\Jar\\jetty-xbean-6.0.2.jar\"/>\r\n" + 
							"	<classpathentry kind=\"lib\" path=\"I:\\Jar\\json-lib-2.1-jdk15.jar\"/>\r\n" + 
							"	<classpathentry kind=\"lib\" path=\"I:\\Jar\\jstl.jar\"/>\r\n" + 
							"	<classpathentry kind=\"lib\" path=\"I:\\Jar\\junit-4.3.jar\"/>\r\n" + 
							"	<classpathentry kind=\"lib\" path=\"I:\\Jar\\logback-classic-1.1.7.jar\"/>\r\n" + 
							"	<classpathentry kind=\"lib\" path=\"I:\\Jar\\logback-core-1.1.7.jar\"/>\r\n" + 
							"	<classpathentry kind=\"lib\" path=\"I:\\Jar\\mockito-all-1.9.5.jar\"/>\r\n" + 
							"	<classpathentry kind=\"lib\" path=\"I:\\Jar\\mssqljdbc4_2008.jar\"/>\r\n" + 
							"	<classpathentry kind=\"lib\" path=\"I:\\Jar\\mysql-connector-java-5.1.41-bin.jar\"/>\r\n" + 
							"	<classpathentry kind=\"lib\" path=\"I:\\Jar\\Oracle_11g_11.2.0.1.0_JDBC_ojdbc6.jar\"/>\r\n" + 
							"	<classpathentry kind=\"lib\" path=\"I:\\Jar\\servlet-api-2.4.jar\"/>\r\n" + 
							"	<classpathentry kind=\"lib\" path=\"I:\\Jar\\slf4j.jar\"/>\r\n" + 
							"	<classpathentry kind=\"lib\" path=\"I:\\Jar\\slf4j-api-1.5.8.jar\"/>\r\n" + 
							"	<classpathentry kind=\"lib\" path=\"I:\\Jar\\slf4j-log4j12-1.6.6.jar\"/>\r\n" + 
							"	<classpathentry kind=\"lib\" path=\"I:\\Jar\\slf4j-simple-1.6.0.jar\"/>\r\n" + 
							"	<classpathentry kind=\"lib\" path=\"I:\\Jar\\spring-aop-4.3.3.RELEASE.jar\"/>\r\n" + 
							"	<classpathentry kind=\"lib\" path=\"I:\\Jar\\spring-aspects-4.3.11.RELEASE.jar\"/>\r\n" + 
							"	<classpathentry kind=\"lib\" path=\"I:\\Jar\\spring-beans-4.3.3.RELEASE.jar\"/>\r\n" + 
							"	<classpathentry kind=\"lib\" path=\"I:\\Jar\\spring-boot-1.4.1.RELEASE.jar\"/>\r\n" + 
							"	<classpathentry kind=\"lib\" path=\"I:\\Jar\\spring-boot-autoconfigure-1.4.1.RELEASE.jar\"/>\r\n" + 
							"	<classpathentry kind=\"lib\" path=\"I:\\Jar\\spring-boot-starter-1.4.1.RELEASE.jar\"/>\r\n" + 
							"	<classpathentry kind=\"lib\" path=\"I:\\Jar\\spring-boot-starter-logging-1.4.1.RELEASE.jar\"/>\r\n" + 
							"	<classpathentry kind=\"lib\" path=\"I:\\Jar\\spring-boot-starter-tomcat-1.4.1.RELEASE.jar\"/>\r\n" + 
							"	<classpathentry kind=\"lib\" path=\"I:\\Jar\\spring-boot-starter-web-1.4.1.RELEASE.jar\"/>\r\n" + 
							"	<classpathentry kind=\"lib\" path=\"I:\\Jar\\spring-context-4.3.3.RELEASE.jar\"/>\r\n" + 
							"	<classpathentry kind=\"lib\" path=\"I:\\Jar\\spring-core-4.3.3.RELEASE.jar\"/>\r\n" + 
							"	<classpathentry kind=\"lib\" path=\"I:\\Jar\\spring-expression-4.3.3.RELEASE.jar\"/>\r\n" + 
							"	<classpathentry kind=\"lib\" path=\"I:\\Jar\\spring-instrument-4.3.11.RELEASE.jar\"/>\r\n" + 
							"	<classpathentry kind=\"lib\" path=\"I:\\Jar\\spring-instrument-tomcat-4.3.11.RELEASE.jar\"/>\r\n" + 
							"	<classpathentry kind=\"lib\" path=\"I:\\Jar\\spring-jdbc-4.3.11.RELEASE.jar\"/>\r\n" + 
							"	<classpathentry kind=\"lib\" path=\"I:\\Jar\\spring-jms-4.3.11.RELEASE.jar\"/>\r\n" + 
							"	<classpathentry kind=\"lib\" path=\"I:\\Jar\\spring-messaging-4.3.11.RELEASE.jar\"/>\r\n" + 
							"	<classpathentry kind=\"lib\" path=\"I:\\Jar\\spring-orm-4.3.11.RELEASE.jar\"/>\r\n" + 
							"	<classpathentry kind=\"lib\" path=\"I:\\Jar\\spring-oxm-4.3.11.RELEASE.jar\"/>\r\n" + 
							"	<classpathentry kind=\"lib\" path=\"I:\\Jar\\spring-test-4.3.11.RELEASE.jar\"/>\r\n" + 
							"	<classpathentry kind=\"lib\" path=\"I:\\Jar\\spring-tx-4.3.11.RELEASE.jar\"/>\r\n" + 
							"	<classpathentry kind=\"lib\" path=\"I:\\Jar\\spring-web-4.3.3.RELEASE.jar\"/>\r\n" + 
							"	<classpathentry kind=\"lib\" path=\"I:\\Jar\\spring-webmvc-4.3.3.RELEASE.jar\"/>\r\n" + 
							"	<classpathentry kind=\"lib\" path=\"I:\\Jar\\spring-webmvc-portlet-4.3.11.RELEASE.jar\"/>\r\n" + 
							"	<classpathentry kind=\"lib\" path=\"I:\\Jar\\spring-websocket-4.3.11.RELEASE.jar\"/>\r\n" + 
							"	<classpathentry kind=\"lib\" path=\"I:\\Jar\\standard.jar\"/>\r\n" + 
							"	<classpathentry kind=\"lib\" path=\"I:\\Jar\\validation-api-1.0.0.GA.jar\"/>\r\n" + 
							 "	<classpathentry kind=\"lib\" path=\"I:\\Jar\\validation-api-1.0.0.GA.jar\"/>\r\n"
								+ "	<classpathentry kind=\"lib\" path=\"I:\\Jar\\objenesis-1.1.jar\"/>\r\n"
								+ "	<classpathentry kind=\"lib\" path=\"I:\\Jar\\easymock-3.1-samples.jar\"/>\r\n"
								+ "	<classpathentry kind=\"lib\" path=\"I:\\Jar\\easymock-3.1-sources.jar\"/>\r\n"
								+ "	<classpathentry kind=\"lib\" path=\"I:\\Jar\\log4j-1.2.17.jar\"/>\r\n"
								+ "	<classpathentry kind=\"lib\" path=\"I:\\Jar\\easymock-3.1.jar\"/>\r\n"
								+ "	<classpathentry kind=\"lib\" path=\"I:\\Jar\\easymock-3.1-javadoc.jar\"/>\r\n"
								+ "	<classpathentry kind=\"lib\" path=\"I:\\Jar\\cglib_nodep2.1_3.jar\"/>" +
							"</classpath>\r\n" + 
							"";
					bw.write(s);
					bw.close();

					File file2 = new File(tempFile2);
					if (!file2.exists()) {

						file2.createNewFile();
						BufferedWriter bw2 = new BufferedWriter(
								new OutputStreamWriter(new FileOutputStream(file2, false), "UTF-8"));
						String s2 = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n" + "<projectDescription>\r\n"
								+ "	<name></name>\r\n" + "	<comment></comment>\r\n" + "	<projects>\r\n"
								+ "	</projects>\r\n" + "	<buildSpec>\r\n" + "		<buildCommand>\r\n"
								+ "			<name>org.eclipse.jdt.core.javabuilder</name>\r\n"
								+ "			<arguments>\r\n" + "			</arguments>\r\n"
								+ "		</buildCommand>\r\n" + "	</buildSpec>\r\n" + "	<natures>\r\n"
								+ "		<nature>org.eclipse.jdt.core.javanature</nature>\r\n" + "	</natures>\r\n"
								+ "</projectDescription>";
						bw2.write(s2);
						bw2.close();

					}
				}
			}
		}
	}

	public List<String> getSrcs() {
		return srcs;
	}

	public void setSrcs(List<String> srcs) {
		this.srcs = srcs;
	}

}
