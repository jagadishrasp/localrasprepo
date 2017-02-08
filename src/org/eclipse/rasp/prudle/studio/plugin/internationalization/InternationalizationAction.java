package org.eclipse.rasp.prudle.studio.plugin.internationalization;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.window.Window;
import org.eclipse.rasp.prudle.studio.plugin.configuration.ConfigurationXML;
import org.eclipse.rasp.prudle.studio.plugin.loginpopup.PasswordDialog;
import org.eclipse.rasp.prudle.studio.plugin.loginpopup.ReadWriteLoginCredentials;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IViewActionDelegate;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.console.ConsolePlugin;
import org.eclipse.ui.console.IConsole;
import org.eclipse.ui.console.IConsoleManager;
import org.eclipse.ui.console.MessageConsole;
import org.eclipse.ui.console.MessageConsoleStream;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

public class InternationalizationAction implements IViewActionDelegate {

	private File workspaceDirectory = null;
	private String projectName = null;
	private List<Multimap<Integer, String>> list = null;
	private List<LinkedHashMap<String, String>> replaceList = null;
	boolean isHardCodesListEmpty = false;
	private Shell shell;

	public InternationalizationAction() {
		// TODO Auto-generated constructor stub
		super();
	}

	@Override
	public void run(IAction arg) {

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

		Multimap<Integer, String> multimap = ArrayListMultimap.create();
		// List<HashMap<Integer, String>> list = new ArrayList<HashMap<Integer,
		// String>>();

		for (int i = 0; i < files.size(); i++) {

			try {
				multimap = getHardCodes(files.get(i));
				list.add(multimap);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		if (!(isHardCodesListEmpty)) {
			MessageDialog.openInformation(shell, "Prudle Studio", "No hard codes found!");
			return;
		}

		ExcludeSkipWords();
		writePropertiesFile(list, fileNames);

		MessageDialog.openInformation(shell, "I18", "I18 Successful");

		try {
			replaceHardCodesWithKeys(files, replaceList);
			MessageDialog.openInformation(shell, "I18", "Hardcodes are replaced with the keys successfully");
		} catch (IOException e) {
			// TODOAuto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public void selectionChanged(IAction arg0, ISelection arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void init(IViewPart arg0) {
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

		MessageConsole myConsole = findConsole("Prudle Studio");
		MessageConsoleStream out = myConsole.newMessageStream();

		Multimap<Integer, String> multimap = ArrayListMultimap.create();

		FileInputStream fstream = new FileInputStream(file);
		BufferedReader br = new BufferedReader(new InputStreamReader(fstream));

		String currentLine;
		Integer lineNumber = 0;

		// Read File Line By Line
		while ((currentLine = br.readLine()) != null) {

			if (!(currentLine.trim().startsWith("/**")) && !(currentLine.trim().startsWith("*"))
					&& !(currentLine.trim().startsWith("//")) && !(currentLine.trim().startsWith("/*"))
					&& !(currentLine.trim().startsWith("*/")) && !(currentLine.trim().contains("@Param"))) {

				lineNumber++;
				Pattern pattern = Pattern.compile("\"([^\"]*)\"");
				Matcher matcher = pattern.matcher(currentLine);

				while (matcher.find()) {

					if ((!(matcher.group(1).endsWith("_pse13n")) && !(matcher.group(1).startsWith("@Query"))
							&& !(matcher.group(1).contains("@Param")) && !(matcher.group(1).startsWith("SELECT"))
							&& !(matcher.group(1).startsWith("FROM")) && !(matcher.group(1).startsWith("UPDATE"))
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
						out.println("   Hard Code: " + matcher.group(1) + " found in (" + file.getName() + ":"
								+ lineNumber + ")");
					}

					StackTraceElement stackTraceEle = new StackTraceElement(
							file.getName().replaceFirst("[.][^.]+$", ""), "main", file.getName(), lineNumber);
					System.out.printf("%s.%s(%s:%s)%n", stackTraceEle.getClassName(), stackTraceEle.getMethodName(),
							stackTraceEle.getFileName(), stackTraceEle.getLineNumber());
				}
			}
		}
		/**
		 * // Read File Line By Line while ((currentLine = br.readLine()) !=
		 * null) {
		 * 
		 * lineNumber++; Pattern pattern = Pattern.compile("\"([^\"]*)\"");
		 * Matcher matcher = pattern.matcher(currentLine);
		 * 
		 * while (matcher.find()) { multimap.put(lineNumber, matcher.group(1));
		 * out.println(" Hard Code: " + matcher.group(1) + " found in (" +
		 * file.getName() + ":" + lineNumber + ")"); } }
		 */

		br.close();

		if (!(multimap.isEmpty()))
			isHardCodesListEmpty = true;

		return multimap;
	}

	private void writePropertiesFile(List<Multimap<Integer, String>> list, List<String> fileNames) {

		replaceList = new ArrayList<LinkedHashMap<String, String>>();

		try {

			int counter = 0;
			Properties properties = new Properties();

			for (int i = 0; i < fileNames.size(); i++) {

				Collection<String> value = null;
				Integer key = null;
				String activeFileName = fileNames.get(i);
				LinkedHashMap<String, String> propertyKeys = new LinkedHashMap<String, String>();

				Map<Integer, Collection<String>> mapp = list.get(i).asMap();

				for (Entry<Integer, Collection<String>> entry : mapp.entrySet()) {
					key = entry.getKey();
					value = list.get(i).get(key);

					for (String val : value) {
						/**
						 * LinkedHashMap<String, String> propertyKeys = new
						 * LinkedHashMap<String, String>(); String
						 * activeFileName = fileNames.get(i);
						 * LinkedHashMap<Integer, String> map = list.get(i);
						 */

						// for (Map.Entry<Integer, String> entry :
						// map.entrySet()) {

						Pattern pattern = Pattern.compile("_pse13n");
						Matcher matcher = pattern.matcher(val);
						if (!(matcher.find())) {

							properties.setProperty(
									counter + "_" + activeFileName + "_HardcodedString" + "_" + key + "_pse13n", val);
							propertyKeys.put(
									counter + "_" + activeFileName + "_HardcodedString" + "_" + key + "_pse13n", val);
							counter++;
						}

					}
				}

				replaceList.add(propertyKeys);
				// System.out.println(replaceList);
			}

			createResourceFolder();
			SimpleDateFormat formattedDate = new SimpleDateFormat("ddMMyyyyHHmmss");
			String pathName = workspaceDirectory.toString() + "\\" + projectName
					+ "\\prudleresources\\MessagesBundle_en_US" + formattedDate.format(new Date()) + ".properties";

			File file = new File(pathName);
			FileOutputStream fileOut = new FileOutputStream(file);
			properties.store(fileOut, "Properties file generated for internationalization");
			fileOut.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
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

	/**
	 * This method creates a new folder in the scanning project
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

	private void replaceHardCodesWithKeys(List<File> files, List<LinkedHashMap<String, String>> keysAndValues)
			throws IOException {

		ConfigurationXML xmlObject = new ConfigurationXML();

		for (int i = 0; i < files.size(); i++) {

			File activeFile = files.get(i);
			LinkedHashMap<String, String> map = keysAndValues.get(i);

			Path path = Paths.get(activeFile.toString());
			Charset charset = StandardCharsets.UTF_8;

			String content = new String(Files.readAllBytes(path), charset);
			// System.out.println(content);
			String[] arr = content.split("\"");

			for (Map.Entry<String, String> entry : map.entrySet()) {

				content = content.replaceAll(Pattern.quote("\"" + entry.getValue() + "\""),
						xmlObject.getValueFromCS("CustomizedKey", "MethodName") + "(\"" + entry.getKey() + "\")");
			}

			Files.write(path, content.getBytes(charset));
			// System.out.println(content);
		}

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

}
