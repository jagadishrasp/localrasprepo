package org.eclipse.rasp.prudle.studio.plugin.scan;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.window.Window;
import org.eclipse.rasp.prudle.studio.plugin.configuration.ConfigurationXML;
import org.eclipse.rasp.prudle.studio.plugin.configuration.ShowNotoficationDialogHandler;
import org.eclipse.rasp.prudle.studio.plugin.loginpopup.HttpURLLoginConnection;
import org.eclipse.rasp.prudle.studio.plugin.loginpopup.PasswordDialog;
import org.eclipse.rasp.prudle.studio.plugin.loginpopup.ReadWriteLoginCredentials;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IViewActionDelegate;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.console.ConsolePlugin;
import org.eclipse.ui.console.IConsole;
import org.eclipse.ui.console.IConsoleManager;
import org.eclipse.ui.console.MessageConsole;
import org.eclipse.ui.console.MessageConsoleStream;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

/**
 * Scan dialog, scans the project and generates xml reports for hardcodes in a
 * project files
 * 
 * @author RASP
 *
 */
public class ScanAction implements IViewActionDelegate {

	private File workspaceDirectory = null;
	private String projectName;
	private String scanType = null;
	private List<Multimap<Integer, String>> list = null;
	List<LinkedHashMap<Integer, String>> numberOfMethods = new ArrayList<LinkedHashMap<Integer, String>>();
	List<LinkedHashMap<Integer, String>> methodsList = new ArrayList<LinkedHashMap<Integer, String>>();
	List<String> methodNamesFromConSettings = null;
	List<LinkedHashMap<Integer, String>> localeMethodsList = new ArrayList<LinkedHashMap<Integer, String>>();
	List<String> localeMethodNamesFromConSettings = null;
	ConfigurationXML configurationXML = null;
	boolean isHardCodesListEmpty = false;
	private Shell shell;

	public ScanAction() {
		super();
	}

	public void setActivePart(IAction action, IWorkbenchPart targetPart) {
		shell = targetPart.getSite().getShell();
	}

	@Override
	public void run(IAction action) {

		boolean flag = false;

		/**
		 * if (selection2 instanceof IStructuredSelection) {
		 * IStructuredSelection ssel = (IStructuredSelection) selection2; Object
		 * obj = ssel.getFirstElement(); IFile file = (IFile)
		 * Platform.getAdapterManager().getAdapter(obj, IFile.class); if (file
		 * == null) { if (obj instanceof IAdaptable) { file = (IFile)
		 * ((IAdaptable) obj).getAdapter(IFile.class); } } if (file != null) {
		 * // do something System.out.println(file); } }
		 */

		ReadWriteLoginCredentials obj = new ReadWriteLoginCredentials();
		String userId = obj.readEncryptedText().trim();

		if (userId.equals(null)) {

			PasswordDialog dialog = new PasswordDialog(shell);
			dialog.create();
			if (dialog.open() == Window.OK) {
			}
		}

		list = new ArrayList<Multimap<Integer, String>>();

		/**
		 * get workbench to assign menu to the navigator and package explorer
		 */
		IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
		/**
		 * Prudle studio menu to resource navigator view
		 */
		IViewPart viewPart = page.findView("org.eclipse.ui.views.ResourceNavigator");
		ISelectionProvider selProvider = viewPart.getSite().getSelectionProvider();
		IStructuredSelection iStr = (IStructuredSelection) selProvider.getSelection();

		/**
		 * Prudle studio menu to package explorer view
		 */
		/**
		 * IViewPart viewPart2 =
		 * page.findView("org.eclipse.jdt.ui.PackageExplorer");
		 * ISelectionProvider selProvider2 =
		 * viewPart2.getSite().getSelectionProvider(); IStructuredSelection
		 * iStr2 = (IStructuredSelection) selProvider2.getSelection();
		 */

		/**
		 * get object which represents the workspace get location of workspace
		 * (java.io.File)
		 */

		/**
		 * IPath path = null; IWorkbenchWindow window =
		 * PlatformUI.getWorkbench().getActiveWorkbenchWindow(); if (window !=
		 * null) { IStructuredSelection selection = (IStructuredSelection)
		 * window.getSelectionService().getSelection(); Object firstElement =
		 * selection.getFirstElement(); if (firstElement instanceof IAdaptable)
		 * { IProject project = (IProject) ((IAdaptable)
		 * firstElement).getAdapter(IProject.class); path =
		 * project.getFullPath(); // System.out.println(path); } }
		 */

		/**
		 * get the current workspace path
		 */
		workspaceDirectory = ResourcesPlugin.getWorkspace().getRoot().getLocation().toFile();

		/**
		 * get the active selected path from menu
		 */

		ISelection selection = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getSelectionService()
				.getSelection();
		Object[] elements = ((IStructuredSelection) selection).toArray();

		List<File> proPackFiles = new ArrayList<File>();

		for (Object ob : elements) {
			proPackFiles.add(new File(workspaceDirectory.toString().concat(ob.toString().substring(1))));
		}

		/**
		 * String activeProjectName = path.toString(); String activeProject =
		 * workspaceDir.concat(activeProjectName); projectName =
		 * activeProjectName.split("/"); System.out.println(projectName); File
		 * projectPath = new File(activeProject); currentWorkspaceDir =
		 * projectPath.toString(); System.out.println(currentWorkspaceDir);
		 */

		/**
		 * file extensions to get the type of files from the current workspace
		 */
		List<File> files = new ArrayList<File>();

		for (File identify : proPackFiles) {

			flag = identify.isDirectory();

			if (flag) {
				String[] extensions = new String[] { "java" };
				files.addAll((List<File>) FileUtils.listFiles(identify, extensions, true));
			} else {
				files.add(identify);
			}
		}

		List<String> fileNames = new ArrayList<String>();

		for (File name : files) {
			fileNames.add(name.getName());
		}

		String[] levelOfScan = elements[0].toString().substring(2).split("/");

		projectName = (levelOfScan[0]);

		if (!flag) {
			scanType = "File";
		} else if (levelOfScan.length == 1) {
			scanType = "Project";
		} else {
			scanType = "Package";
		}

		Multimap<Integer, String> multimap = ArrayListMultimap.create();

		for (int i = 0; i < files.size(); i++) {
			try {
				multimap = getHardCodes(files.get(i));
				list.add(multimap);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		configurationXML = new ConfigurationXML();

		methodNamesFromConSettings = new ArrayList<String>();
		methodNamesFromConSettings
				.addAll(configurationXML.readXMLFileForincludeMethodsList("HardCodeJavaString", "IncludeMethod"));
		methodsList.addAll(includeMethodsfound(numberOfMethods, methodNamesFromConSettings));

		localeMethodNamesFromConSettings = new ArrayList<String>();
		localeMethodNamesFromConSettings
				.addAll(configurationXML.readXMLFileForincludeMethodsList("HardCodeJavaString", "LocaleFunc"));
		localeMethodsList.addAll(includeMethodsfound(numberOfMethods, localeMethodNamesFromConSettings));
		System.out.println(localeMethodsList);
		// List<Multimap<Integer,String>> skiped = new
		// ArrayList<Multimap<Integer,String>>();

		if (!(isHardCodesListEmpty)) {
			MessageDialog.openInformation(shell, "Prudle Studio", "No hard codes found!");
			return;
		}

		ExcludeSkipWords();
		saveAsXMLFile(list, fileNames, methodsList, localeMethodsList);
		// writePropertiesFile(list, fileNames);
		ShowNotoficationDialogHandler sn = new ShowNotoficationDialogHandler();
		sn.execute(shell, "Scan completed successfully");

		String extName = FilenameUtils
				.getExtension("E:\\Prudle Studio Dev\\test\\MessagesBundle_en_US05012017144449.properties");
		if (!(extName.equals("properties"))) {
			MessageDialog.openInformation(shell, "File Upload", "Please provide properties file");
		} else {

			HttpURLLoginConnection conn = new HttpURLLoginConnection();
			try {
				// conn.sendPost();
				MessageDialog.openInformation(shell, "File Upload", "File uploaded successfully");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		/**
		 * MessageConsole console = new MessageConsole("CONSOLE_NAME", null);
		 * console.activate();
		 * ConsolePlugin.getDefault().getConsoleManager().addConsoles(new
		 * IConsole[] { myConsole }); IPath path1 =
		 * Path.fromOSString(files.get(0).toString()); IFile file =
		 * ResourcesPlugin.getWorkspace().getRoot().getFileForLocation(path1);
		 * FileLink fileLink = new FileLink((IFile) file, null, -1, -1, -1); try
		 * { myConsole.addHyperlink(fileLink, 10, 5); } catch
		 * (BadLocationException e) { // TODO Auto-generated catch block
		 * e.printStackTrace(); }
		 */

		// out.println("(HelloPrudleStudio.java:20)");

		/***
		 * StackTraceElement stackTraceEle = new StackTraceElement("strLine",
		 * "strLine", "HelloPrudleStudio.java", 8);
		 * System.out.printf("%s.%s(%s:%s)%n", stackTraceEle.getClassName() ,
		 * stackTraceEle.getMethodName(), stackTraceEle.getFileName(),
		 * stackTraceEle.getLineNumber());
		 */

		// MessageDialog.openInformation(shell, "Plugin", "No. of Hard Code :" +
		// "" + hardCodes.size());
	}

	@Override
	public void selectionChanged(IAction action, ISelection select) {
		// TODO Auto-generated method stub
	}

	@Override
	public void init(IViewPart viewer) {
		// TODO Auto-generated method stub
	}

	/**
	 * This method returns hard codes from the file
	 * 
	 * @param fie
	 * @return
	 * @throws IOException
	 */
	public Multimap<Integer, String> getHardCodes(File file) throws IOException {

		LinkedHashMap<Integer, String> lineNumAndMethods = new LinkedHashMap<Integer, String>();
		Multimap<Integer, String> multimap = ArrayListMultimap.create();

		MessageConsole myConsole = findConsole("Prudle Studio");
		MessageConsoleStream out = myConsole.newMessageStream();

		FileInputStream fstream = new FileInputStream(file);
		BufferedReader br = new BufferedReader(new InputStreamReader(fstream));

		String currentLine;
		Integer lineNumber = 0;

		// Read File Line By Line
		while ((currentLine = br.readLine()) != null) {
			// Print the content on the console

			if (!(currentLine.trim().startsWith("/**")) && !(currentLine.trim().startsWith("*"))
					&& !(currentLine.trim().startsWith("//")) && !(currentLine.trim().startsWith("/*"))
					&& !(currentLine.trim().startsWith("*/")) && !(currentLine.trim().contains("@Param"))) {

				lineNumber++;
				Pattern pattern = Pattern.compile("\"([^\"]*)\"");
				Matcher matcher = pattern.matcher(currentLine);

				if (!(currentLine.trim().contains("@Param"))) {

					while (matcher.find()) {

						if ((!(matcher.group(1).endsWith("_pse13n")) 
								&& !(matcher.group(1).startsWith("@Query"))
								&& !(matcher.group(1).contains("@Param")) 
								&& !(matcher.group(1).startsWith("SELECT"))
								&& !(matcher.group(1).startsWith("FROM")) 
								&& !(matcher.group(1).startsWith("UPDATE"))
								&& !(matcher.group(1).startsWith("DELETE FROM"))
								&& !(matcher.group(1).startsWith("INSERT INTO"))
								&& !(matcher.group(1).startsWith("SELECT count"))
								&& !(matcher.group(1).contains(".createQuery(\"update )"))
								&& !(matcher.group(1).contains(".createQuery(\"delete )"))
								&& !(matcher.group(1).contains(".createQuery(\"insert into )"))
								&& !(matcher.group(1).startsWith("@NamedQueries"))
								&& !(matcher.group(1).startsWith("@NamedQuery"))
								&& !(matcher.group(1).contains(".getNamedQuery"))
								&& !(matcher.group(1).contains("createNativeQuery"))
								&& !(matcher.group(1).startsWith("@NamedNativeQueries"))
								&& !(matcher.group(1).startsWith("@NamedNativeQuery"))
								&& !(matcher.group(1).contains("SELECT * FROM"))
								&& !(matcher.group(1).contains("SELECT DISTINCT"))
								&& !(matcher.group(1).contains("DELETE FROM"))
								&& !(matcher.group(1).contains("SELECT TOP"))
								&& !(matcher.group(1).contains("createQuery"))
								&& !(matcher.group(1).startsWith("ALTER TABLE"))
								&& !(matcher.group(1).startsWith("CREATE DATABASE"))
								&& !(matcher.group(1).startsWith("CREATE TABLE"))
								&& !(matcher.group(1).startsWith("CREATE INDEX"))
								&& !(matcher.group(1).startsWith("CREATE UNIQUE INDEX"))
								&& !(matcher.group(1).startsWith("CREATE VIEW"))
								&& !(matcher.group(1).startsWith("DELETE FROM"))
								&& !(matcher.group(1).startsWith("DROP DATABASE"))
								&& !(matcher.group(1).startsWith("DROP INDEX"))
								&& !(matcher.group(1).startsWith("ALTER TABLE"))
								&& !(matcher.group(1).startsWith("TRUNCATE TABLE")))) {
							multimap.put(lineNumber, matcher.group(1));
							out.println("Hard Code: " + matcher.group(1) + " found in (" + file.getName() + ":"
									+ lineNumber + ")");
						}
					}
				}
				Pattern pattern3 = Pattern.compile("\\w+ *\\([^\\)]*\\) *");
				Matcher matcher3 = pattern3.matcher(currentLine);

				while (matcher3.find()) {
					lineNumAndMethods.put(lineNumber, matcher3.group());
				}
			}
		}
		numberOfMethods.add(lineNumAndMethods);
		// Close the input stream
		br.close();

		if (!(multimap.isEmpty()))
			isHardCodesListEmpty = true;

		return multimap;
	}

	/**
	 * This method saves the hard codes as xml file
	 */
	public void saveAsXMLFile(List<Multimap<Integer, String>> list, List<String> fileNames,
			List<LinkedHashMap<Integer, String>> methodNameList,
			List<LinkedHashMap<Integer, String>> localeMethodNameList) {

		int fileNameCounter = 0;
		int currenMethodNameListCounter = 0;
		int currenLocaleMethodNameListCounter = 0;

		try {

			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

			// root elements
			Document doc = docBuilder.newDocument();
			Element rootElement = doc.createElement("PrudleStudio");
			doc.appendChild(rootElement);

			// project elements
			Element project = doc.createElement("Project");
			rootElement.appendChild(project);

			// set attribute to project element
			Attr attr = doc.createAttribute("Name");
			attr.setValue(projectName);
			project.setAttributeNode(attr);

			for (Multimap<Integer, String> map : list) {

				Element file = doc.createElement("File");
				project.appendChild(file);

				Attr attr2 = doc.createAttribute("Name");
				attr2.setValue(fileNames.get(fileNameCounter));
				file.setAttributeNode(attr2);

				Collection<String> value = null;
				Integer key = null;

				Map<Integer, Collection<String>> mapp = map.asMap();

				for (Entry<Integer, Collection<String>> entry : mapp.entrySet()) {
					key = entry.getKey();
					value = map.get(key);
					System.out.println(key + ":" + value);

					for (String val : value) {

						Element hardCodedStringHead = doc.createElement("HardCodeString");
						// hardCodedStringHead.appendChild(doc.createTextNode(entry.getKey()));
						file.appendChild(hardCodedStringHead);

						// lastname elements
						Element lineNumber = doc.createElement("LineNumber");
						lineNumber.appendChild(doc.createTextNode(key.toString()));
						hardCodedStringHead.appendChild(lineNumber);

						// lastname elements
						Element hardCodeString = doc.createElement("HardCodedString");
						hardCodeString.appendChild(doc.createTextNode(val));
						hardCodedStringHead.appendChild(hardCodeString);

					}
				}

				for (Map.Entry<Integer, String> entry : methodNameList.get(currenMethodNameListCounter).entrySet()) {

					System.out.println(methodNameList);

					System.out.println(entry);

					Element hardCodedStringHead = doc.createElement("Function");
					// hardCodedStringHead.appendChild(doc.createTextNode(entry.getKey()));
					file.appendChild(hardCodedStringHead);

					// lastname elements
					Element lineNumber = doc.createElement("LineNumber");
					lineNumber.appendChild(doc.createTextNode(Integer.toString(entry.getKey())));
					hardCodedStringHead.appendChild(lineNumber);

					// lastname elements
					Element hardCodeString = doc.createElement("FunctionName");
					hardCodeString.appendChild(doc.createTextNode(entry.getValue()));
					hardCodedStringHead.appendChild(hardCodeString);

				}

				for (Map.Entry<Integer, String> entry : localeMethodNameList.get(currenLocaleMethodNameListCounter)
						.entrySet()) {

					System.out.println(entry);

					Element hardCodedStringHead = doc.createElement("LocaleFunction");
					// hardCodedStringHead.appendChild(doc.createTextNode(entry.getKey()));
					file.appendChild(hardCodedStringHead);

					// lastname elements
					Element lineNumber = doc.createElement("LineNumber");
					lineNumber.appendChild(doc.createTextNode(Integer.toString(entry.getKey())));
					hardCodedStringHead.appendChild(lineNumber);

					// lastname elements
					Element hardCodeString = doc.createElement("LocaleFunctionName");
					hardCodeString.appendChild(doc.createTextNode(entry.getValue()));
					hardCodedStringHead.appendChild(hardCodeString);

				}

				fileNameCounter++;
				currenMethodNameListCounter++;
				currenLocaleMethodNameListCounter++;

			}

			// write the content into xml file
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);

			createResourceFolder();

			SimpleDateFormat formattedDate = new SimpleDateFormat("ddMMyyyyHHmmss");
			String pathName = workspaceDirectory.toString() + "\\" + projectName + "\\prudleresources\\_" + scanType
					+ "_" + projectName + "___G11n" + formattedDate.format(new Date()) + ".xml";
			StreamResult result = new StreamResult(new File(pathName));
			transformer.transform(source, result);

			System.out.println("saved!");

		} catch (ParserConfigurationException pce) {
			pce.printStackTrace();
		} catch (TransformerException tfe) {
			tfe.printStackTrace();
		}

	}

	private MessageConsole findConsole(String name) {

		ConsolePlugin plugin = ConsolePlugin.getDefault();
		IConsoleManager conMan = plugin.getConsoleManager();
		org.eclipse.ui.console.IConsole[] existing = conMan.getConsoles();
		for (int i = 0; i < existing.length; i++)
			if (name.equals(existing[i].getName()))
				return (MessageConsole) existing[i];
		// no console found, so create a new one
		MessageConsole myConsole = new MessageConsole(name, null);
		conMan.addConsoles((org.eclipse.ui.console.IConsole[]) new IConsole[] { (IConsole) myConsole });
		return myConsole;
	}

	private void ExcludeSkipWords() {

		// List<Multimap<Integer,String>> skippedTokens = new
		// ArrayList<Multimap<Integer,String>>();
		// skippedTokens.addAll(list);
		StringBuilder strBuild = new StringBuilder();
		List<String> skipWords = new ArrayList<String>();
		ConfigurationXML readSkipWords = new ConfigurationXML();
		skipWords.addAll(readSkipWords.readXMLFileForSkipWords());

		for (Multimap<Integer, String> multimap : list) {

			Map<Integer, Collection<String>> map = multimap.asMap();

			for (Entry<Integer, Collection<String>> entry : map.entrySet()) {
				Integer key = entry.getKey();
				Collection<String> value = multimap.get(key);
				System.out.println(key + ":" + value);
				for (String token : value)
					strBuild.append(token + " ");
			}
			/**
			 * for (Map.Entry<Integer, String> entry : map.entrySet()) {
			 * strBuild.append(entry.getValue() + " "); }
			 */
		}

		for (String pattern : skipWords) {

			if (strBuild.toString().toLowerCase().contains(pattern.toLowerCase())) {

				for (Multimap<Integer, String> multimap : list) {

					Map<Integer, Collection<String>> map = multimap.asMap();

					for (Entry<Integer, Collection<String>> entry : map.entrySet()) {
						Integer key = entry.getKey();
						Collection<String> value = multimap.get(key);
						Iterator it = value.iterator();
						while (it.hasNext()) {
							if (it.next().equals(pattern))
								map.remove(pattern);
						}
					}
				}
			}

		}
	}

	/**
	 * This method creates a new folder in the scanning project named
	 * prudleresources
	 */
	private void createResourceFolder() {

		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		IWorkspaceRoot root = workspace.getRoot();
		IProject project = root.getProject(projectName);
		IFolder folder = project.getFolder("prudleresources");
		// at this point, no resources have been created
		try {
			if (!project.exists())
				project.create(null);
			if (!project.isOpen())
				project.open(null);
			if (!folder.exists())
				folder.create(IResource.NONE, true, null);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	/**
	 * private void writePropertiesFile(List<HashMap<Integer, String>> list,
	 * List<String> fileNames){
	 * 
	 * try {
	 * 
	 * int counter = 0; Properties properties = new Properties();
	 * 
	 * for (HashMap<Integer, String> map : list ) {
	 * 
	 * for (Map.Entry<Integer, String> entry : map.entrySet()) {
	 * 
	 * properties.setProperty(counter+"_"+fileNames.get(0)+"HardcodedString"+"_"+entry.getKey()+"_pse13n",
	 * entry.getValue()); counter++; } }
	 * 
	 * SimpleDateFormat formattedDate = new SimpleDateFormat("ddMMyyyyHHmmss");
	 * String pathName =
	 * currentWorkspaceDir+"\\prudleresources\\MessagesBundle_en_US"+formattedDate.format(new
	 * Date())+".properties";
	 * 
	 * File file = new File(pathName); FileOutputStream fileOut = new
	 * FileOutputStream(file); properties.store(fileOut, "Properties file
	 * generated for internationalization"); fileOut.close(); } catch
	 * (FileNotFoundException e) { e.printStackTrace(); } catch (IOException e)
	 * { e.printStackTrace(); } System.out.println("Inside
	 * writePropertiesFile()"); }
	 */

	private List<LinkedHashMap<Integer, String>> includeMethodsfound(List<LinkedHashMap<Integer, String>> methodsFound,
			List<String> methodNamesFromCS) {

		List<LinkedHashMap<Integer, String>> mapList = new ArrayList<LinkedHashMap<Integer, String>>();

		for (LinkedHashMap<Integer, String> methods : methodsFound) {

			LinkedHashMap<Integer, String> temp = new LinkedHashMap<Integer, String>();

			for (Map.Entry<Integer, String> entry : methods.entrySet()) {
				for (String arg : methodNamesFromCS) {
					if ((entry.getValue().replaceAll("\\(.*\\)", "").trim().equals(arg)))
						temp.put(entry.getKey(), entry.getValue().replaceAll("\\(.*\\)", "").trim());
				}
			}
			mapList.add(temp);
		}
		return mapList;
	}

	private void displayProgressBar() {
		final Display display = new Display();
		Shell shell = new Shell(display);
		final ProgressBar bar = new ProgressBar(shell, SWT.SMOOTH);
		Rectangle clientArea = shell.getClientArea();
		bar.setBounds(clientArea.x, clientArea.y, 200, 32);
		shell.open();

		display.timerExec(100, new Runnable() {
			int i = 0;

			@Override
			public void run() {
				if (bar.isDisposed())
					return;
				bar.setSelection(i++);
				if (i <= bar.getMaximum())
					display.timerExec(100, this);
			}
		});

		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		display.dispose();
	}

	public IResource extractSelection(ISelection sel) {
		if (!(sel instanceof IStructuredSelection))
			return null;
		IStructuredSelection ss = (IStructuredSelection) sel;
		Object element = ss.getFirstElement();
		if (element instanceof IResource)
			return (IResource) element;
		if (!(element instanceof IAdaptable))
			return null;
		IAdaptable adaptable = (IAdaptable) element;
		Object adapter = adaptable.getAdapter(IResource.class);
		return (IResource) adapter;
	}

}
