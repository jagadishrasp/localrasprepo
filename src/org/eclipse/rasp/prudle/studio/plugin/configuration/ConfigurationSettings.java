package org.eclipse.rasp.prudle.studio.plugin.configuration;

import java.util.HashMap;
import java.util.List;

import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Link;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;

/**
 * Configuration Settings dialog area configures the rules for scanning the
 * project files
 * 
 * @author RASP
 *
 */
public class ConfigurationSettings extends TitleAreaDialog {

	private Label skipWordTextToExcludeLabel, setKeyLable, resourceFunctionLabel, languageLabel, textToIncludeLabel,
			languageLocaleLabel, languageExcludeLabel, externalizeLabel;
	private Text skipWordsTextBox, skipWordTextArea, setKeyText, resourceFunctionText, textToIncludeText,
			textToIncludeLocaleText, includeMethodTextArea, localeArea;
	private Link skipWord, includeMethods, includeLocaleFunction, excludeMethods, addResourceFunctions,
			defineCustomizedKey, externalizeOptions;

	private Combo languageIncludeText, languageLocaleText, functionTypeText, languageExcludeText;

	private Label textToIncludeLocaleLabel, functionTypeLabel, textToIncludeExcludeLabel;

	private Text textToIncludeExcludeText, excludeArea, externalizeOptionsArea;

	private Composite configurationDialog, leftComposite, rightComposite, rightBottomComposite;

	private SashForm rootLayout;

	private Group descriptionsTab;

	private ScrolledComposite scrolledComp;

	private Button addButtonForSkipWords, addButtonForIncludeMethod, removeButtonForIncludeMethod,
			addButtonForIncludeLocaleFunction, removeButtonForIncludeLocaleFunction, addButtonForExcludeMethods,
			removeButtonForExcludeMethods, removeButtonForAddResourceFunction, removeButtonForDefineCustomizedKey,
			removeButtonForSkipWords, replaceStringCheckBox;

	private StringBuilder includeMethodLangSelection = new StringBuilder();
	private StringBuilder localeLangSelection = new StringBuilder();
	private StringBuilder localeFunctionTypeSelection = new StringBuilder();
	private StringBuilder excludeMethodLangSelection = new StringBuilder();

	List<String> skipWordArrayList;
	private HashMap<String, String> skipWordList = new HashMap<String, String>();
	private HashMap<String, String> includeMethodList = new HashMap<String, String>();
	private HashMap<String, String> localeList = new HashMap<String, String>();
	private HashMap<String, String> excludeMethodList = new HashMap<String, String>();

	private StringBuilder skipWordsTextAreaContent = new StringBuilder();
	private StringBuilder includeMethodsTextAreaContent = new StringBuilder();
	private StringBuilder includeLocaleFunTextAreaContent = new StringBuilder();
	private StringBuilder excludeMethodsTextAreaContent = new StringBuilder();
	private StringBuilder customizedKeyContent = new StringBuilder();

	private ConfigurationXML xmlObject = new ConfigurationXML();

	public ConfigurationSettings(Shell parentShell) {
		super(parentShell);
	}

	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);

		newShell.setText("Configuration Settings");
	}

	@Override
	protected Control createDialogArea(Composite parent) {

		// skipWordsTextAreaContent.append(xmlObject.readXMLFile());
		// addToSkipWordTextArea(skipWordsTextAreaContent.toString());

		configurationDialog = (Composite) super.createDialogArea(parent);
		// Vertical sash
		rootLayout = new SashForm(configurationDialog, SWT.HORIZONTAL);
		rootLayout.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		// First part, scrollable
		scrolledComp = new ScrolledComposite(rootLayout, SWT.V_SCROLL);

		leftComposite = new Composite(scrolledComp, SWT.BORDER);
		leftComposite.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));
		leftComposite.setLayout(new GridLayout());

		// Set scroll size - may need to adjust this
		Point size = leftComposite.computeSize(200, 350);
		scrolledComp.setMinHeight(size.y);
		scrolledComp.setMinWidth(size.x);
		scrolledComp.setExpandVertical(true);
		scrolledComp.setExpandHorizontal(true);

		scrolledComp.setContent(leftComposite);

		// Second part
		rightComposite = new Composite(rootLayout, SWT.BORDER);
		GridLayout rightLayout = new GridLayout(4, false);
		rightComposite.setLayout(rightLayout);

		/**
		 * final Tree tree = new Tree(leftComposite, SWT.VIRTUAL | SWT.BORDER);
		 * tree.setItemCount(1); tree.addListener(SWT.SetData, new Listener() {
		 * public void handleEvent(Event event) { TreeItem item =
		 * (TreeItem)event.item; TreeItem parentItem = item.getParentItem();
		 * String text = null; if (parentItem == null) { text = "node " +
		 * tree.indexOf(item); } else { text = parentItem.getText() + " - " +
		 * parentItem.indexOf(item); } item.setText(text);
		 * System.out.println(text); item.setItemCount(1); } });
		 */

		List<String> skipWordTemp = xmlObject.readXMLFileForSkipWords();
		for (String token : skipWordTemp)
			skipWordsTextAreaContent.append(token + "\n");

		includeMethodLangSelection.append(xmlObject.readXMLFileForIncludeMethodsKey());

		List<String> includeMethodTemp = xmlObject.readXMLFileForIncludeMethods();
		for (String token : includeMethodTemp)
			includeMethodsTextAreaContent.append(token + "\n");

		localeLangSelection.append("Java");

		List<String> localeTemp = xmlObject.readXMLFileForLocaleFunctions();
		for (String token : localeTemp)
			includeLocaleFunTextAreaContent.append(token + "\n");

		localeFunctionTypeSelection.append("Date Time");

		List<String> excludeTemp = xmlObject.readXMLFileForExcludeMethods();
		for (String token : excludeTemp)
			excludeMethodsTextAreaContent.append(token + "\n");
		excludeMethodLangSelection.append("Java");
		
		customizedKeyContent.append(xmlObject.getValueFromCS("CustomizedKey", "MethodName"));

		createSkipWordTextToExcludeLabel(rightComposite);
		createTextToInclude(rightComposite);
		createAddButtonForSkipWords(rightComposite);
		createRemoveButtonForSkipWords(rightComposite);
		createTextArea(rightComposite);
		createReplaceStringCheckBox(rightComposite);
		createDescriptionTab(rightComposite);

		rootLayout.setWeights(new int[] { 20, 75 });

		createSkipWord(leftComposite);
		createIncludeMethods(leftComposite);
		createIncludeLocaleFunction(leftComposite);
		createExcludeMethods(leftComposite);
		createAddResourceFunctions(leftComposite);
		createDefineCustomizedKey(leftComposite);
		createExternalizeOptions(leftComposite);
		// createHyperlinkLink(leftComposite);
		// createViewer(leftComposite);

		// xmlObject.readXMLFile();

		// Third Part
		rightBottomComposite = new Composite(scrolledComp, SWT.BORDER);
		GridLayout rightBottomLayout = new GridLayout(1, false);
		// rightBottomLayout.marginRight=150;
		rightBottomComposite.setLayout(rightBottomLayout);

		createSkipWord(rightBottomComposite);

		return configurationDialog;

	}

	private void createSkipWord(Composite container) {

		GridData dataSkipWordButton = new GridData(SWT.FILL, SWT.CENTER, true, false);
		dataSkipWordButton.horizontalIndent = 10;
		dataSkipWordButton.verticalIndent = 5;
		dataSkipWordButton.grabExcessHorizontalSpace = true;
		dataSkipWordButton.horizontalAlignment = GridData.FILL;

		// skipWord = new Button(container, SWT.PUSH);
		// skipWord.setText("Skip Word");
		skipWord = new Link(container, SWT.NONE);
		skipWord.setText("<A>Skip Word</A>");
		skipWord.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));
		// skipWord.setSize(400, 400);

		skipWord.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {

				for (Control kid : rightComposite.getChildren()) {
					kid.dispose();
				}

				// skipWord.redraw();
				createSkipWordTextToExcludeLabel(rightComposite);
				createTextToInclude(rightComposite);
				createAddButtonForSkipWords(rightComposite);
				createRemoveButtonForSkipWords(rightComposite);
				createTextArea(rightComposite);
				skipWordTextArea.setText(skipWordsTextAreaContent.toString());
				createReplaceStringCheckBox(rightComposite);
				createDescriptionTab(rightComposite);
				rightComposite.layout();
				rightComposite.pack();
			}
		});

		skipWord.setLayoutData(dataSkipWordButton);
	}

	private void createIncludeMethods(Composite container) {

		GridData dataIncludeMethods = new GridData();
		dataIncludeMethods.grabExcessHorizontalSpace = true;
		dataIncludeMethods.horizontalAlignment = GridData.FILL;
		dataIncludeMethods.horizontalIndent = 10;
		dataIncludeMethods.verticalIndent = 5;
		includeMethods = new Link(container, SWT.NONE);
		includeMethods.setText("<A>Include Methods</A>");
		includeMethods.setSize(400, 400);
		includeMethods.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));
		includeMethods.setLayoutData(dataIncludeMethods);

		includeMethods.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {

				for (Control kid : rightComposite.getChildren()) {
					kid.dispose();
				}

				createLanguageText(rightComposite);
				createTextToIncludeLabel(rightComposite);
				createTextToIncludeText(rightComposite);
				createAddButtonForIncludeMethods(rightComposite);
				createRemoveButtonForIncludeMethods(rightComposite);
				createIncludeMethodTestArea(rightComposite);
				includeMethodTextArea.setText(includeMethodsTextAreaContent.toString());
				createReplaceStringCheckBox(rightComposite);
				createDescriptionTab(rightComposite);
				rightComposite.layout();
				rightComposite.pack();
			}
		});
	}

	private void createIncludeLocaleFunction(Composite container) {

		GridData dataIncludeLocaleFunctionButton = new GridData();
		dataIncludeLocaleFunctionButton.grabExcessHorizontalSpace = true;
		dataIncludeLocaleFunctionButton.horizontalAlignment = GridData.FILL;
		dataIncludeLocaleFunctionButton.horizontalIndent = 10;
		dataIncludeLocaleFunctionButton.verticalIndent = 5;
		includeLocaleFunction = new Link(container, SWT.NONE);
		includeLocaleFunction.setText("<A>Include Locale Functions</A>");
		includeLocaleFunction.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));
		includeLocaleFunction.setSize(400, 400);

		includeLocaleFunction.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {

				// textToInclude.setText("You clicked" +
				// externalizeOptions.getText());
				for (Control kid : rightComposite.getChildren()) {
					kid.dispose();
				}

				createLanguageLabelForLocaleFunction(rightComposite);
				createLanguageLocaleText(rightComposite);
				createTextToIncludeLocaleLabel(rightComposite);
				createTextToIncludeLocaleText(rightComposite);
				createAddButtonForIncludeLocaleFunction(rightComposite);
				createRemoveButtonForIncludeLocaleFunction(rightComposite);
				createFunctionTypeLabel(rightComposite);
				createFunctionTypeText(rightComposite);
				createLocaleArea(rightComposite);
				localeArea.setText(includeLocaleFunTextAreaContent.toString());
				createReplaceStringCheckBox(rightComposite);
				createDescriptionTab(rightComposite);
				rightComposite.layout();
				rightComposite.pack();
			}
		});

		includeLocaleFunction.setLayoutData(dataIncludeLocaleFunctionButton);
	}

	private void createExcludeMethods(Composite container) {

		GridData dataExcludeMethodsButton = new GridData();
		dataExcludeMethodsButton.grabExcessHorizontalSpace = true;
		dataExcludeMethodsButton.horizontalAlignment = GridData.FILL;
		dataExcludeMethodsButton.horizontalIndent = 10;
		dataExcludeMethodsButton.verticalIndent = 5;
		excludeMethods = new Link(container, SWT.NONE);
		excludeMethods.setText("<A>Exclude Methods</A>");
		excludeMethods.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));
		excludeMethods.setSize(400, 400);

		excludeMethods.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {

				for (Control kid : rightComposite.getChildren())
					kid.dispose();

				createLanguageExcludeLabel(rightComposite);
				createLanguageExcludeText(rightComposite);
				createTextToIncludeExcludeLabel(rightComposite);
				createTextToIncludeExcludeText(rightComposite);
				createAddButtonForExcludeMethod(rightComposite);
				createRemoveButtonForExcludeMethod(rightComposite);
				createExcludeArea(rightComposite);
				excludeArea.setText(excludeMethodsTextAreaContent.toString());
				createReplaceStringCheckBox(rightComposite);
				createDescriptionTab(rightComposite);
				rightComposite.layout();
				rightComposite.pack();
			}
		});

		excludeMethods.setLayoutData(dataExcludeMethodsButton);
	}

	private void createAddResourceFunctions(Composite container) {

		GridData dataAddResourceFunctionsButton = new GridData();
		dataAddResourceFunctionsButton.grabExcessHorizontalSpace = true;
		dataAddResourceFunctionsButton.horizontalAlignment = GridData.FILL;
		dataAddResourceFunctionsButton.horizontalIndent = 10;
		dataAddResourceFunctionsButton.verticalIndent = 5;
		addResourceFunctions = new Link(container, SWT.NONE);
		addResourceFunctions.setText("<A>Add Resource Functions</A>");
		addResourceFunctions.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));
		addResourceFunctions.setSize(400, 400);

		addResourceFunctions.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {

				for (Control kid : rightComposite.getChildren()) {
					kid.dispose();
				}

				createResourceFunctionLabel(rightComposite);
				createResourceFunctionText(rightComposite);
				createRemoveButtonForAddResourceFunction(rightComposite);
				createReplaceStringCheckBox(rightComposite);
				createDescriptionTab(rightComposite);
				rightComposite.layout();
				rightComposite.pack();
			}
		});

		addResourceFunctions.setLayoutData(dataAddResourceFunctionsButton);
	}

	private void createDefineCustomizedKey(Composite container) {

		GridData dataDefineCustomizedKeyButton = new GridData();
		dataDefineCustomizedKeyButton.grabExcessHorizontalSpace = true;
		dataDefineCustomizedKeyButton.horizontalAlignment = GridData.FILL;
		dataDefineCustomizedKeyButton.horizontalIndent = 10;
		dataDefineCustomizedKeyButton.verticalIndent = 5;
		defineCustomizedKey = new Link(container, SWT.NONE);
		defineCustomizedKey.setText("<A>Define Customized Key</A>");
		defineCustomizedKey.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));
		defineCustomizedKey.setSize(400, 400);

		defineCustomizedKey.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {

				for (Control kid : rightComposite.getChildren()) {
					kid.dispose();
				}

				createSetKeyLabel(rightComposite);
				createSetKeyText(rightComposite);
				createRemoveButtonForDefineCustomizedKey(rightComposite);
				createReplaceStringCheckBox(rightComposite);
				createDescriptionTab(rightComposite);
				rightComposite.layout();
				rightComposite.pack();
			}
		});

		defineCustomizedKey.setLayoutData(dataDefineCustomizedKeyButton);
	}

	private void createExternalizeOptions(Composite container) {

		GridData dataExternalizeOptionsButton = new GridData();
		dataExternalizeOptionsButton.grabExcessHorizontalSpace = true;
		dataExternalizeOptionsButton.horizontalAlignment = GridData.FILL;
		dataExternalizeOptionsButton.horizontalIndent = 10;
		dataExternalizeOptionsButton.verticalIndent = 5;
		externalizeOptions = new Link(container, SWT.NONE);
		externalizeOptions.setText("<A>Externalize Options</A>");
		externalizeOptions.setSize(100, 100);
		externalizeOptions.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));
		externalizeOptions.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {

				for (Control kid : rightComposite.getChildren()) {
					kid.dispose();
				}

				// createExternalizeOptionsArea(rightComposite);
				createExternalizeLabel(rightComposite);
				createTable(rightComposite);
				createReplaceStringCheckBox(rightComposite);
				createDescriptionTab(rightComposite);
				rightComposite.layout();
				rightComposite.pack();
			}
		});
		externalizeOptions.setLayoutData(dataExternalizeOptionsButton);
	}

	private void createAddButtonForSkipWords(Composite container) {

		GridData dataAddButton = new GridData();
		dataAddButton.widthHint = 10;
		// dataAddButton.heightHint = 40;
		dataAddButton.grabExcessHorizontalSpace = true;
		dataAddButton.horizontalAlignment = GridData.FILL;
		addButtonForSkipWords = new Button(container, SWT.PUSH);
		addButtonForSkipWords.setText("Add");
		addButtonForSkipWords.setLayoutData(dataAddButton);

		addButtonForSkipWords.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {

				StringBuilder getTokens = addToSkipWordTextArea(skipWordsTextBox.getText());
				skipWordTextArea.setText(getTokens.toString());
			}
		});
	}
	
	private void createRemoveButtonForSkipWords(Composite container) {

		GridData dataRemoveButton = new GridData();
		dataRemoveButton.widthHint = 10;
		dataRemoveButton.grabExcessHorizontalSpace = true;
		dataRemoveButton.horizontalAlignment = GridData.FILL;
		removeButtonForSkipWords = new Button(container, SWT.PUSH);
		removeButtonForSkipWords.setText("Remove");
		removeButtonForSkipWords.setLayoutData(dataRemoveButton);

		removeButtonForSkipWords.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {

				StringBuilder getTokens = removeFromSkipWordTextArea(skipWordsTextBox.getText());
				skipWordTextArea.setText(getTokens.toString());
			}
		});
	}

	/**
	 * Create Add button for include methods
	 * 
	 * @param container
	 */
	private void createAddButtonForIncludeMethods(Composite container) {

		GridData dataAddButtonForIncludeMethod = new GridData();
		dataAddButtonForIncludeMethod.widthHint = 80;
		// dataAddButton.heightHint = 40;
		dataAddButtonForIncludeMethod.grabExcessHorizontalSpace = true;
		dataAddButtonForIncludeMethod.horizontalAlignment = GridData.FILL;
		addButtonForIncludeMethod = new Button(container, SWT.PUSH);
		addButtonForIncludeMethod.setText("Add");
		addButtonForIncludeMethod.setLayoutData(dataAddButtonForIncludeMethod);

		addButtonForIncludeMethod.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {

				StringBuilder getTokens = addToIncludeMetodsTextArea(textToIncludeText.getText());
				includeMethodTextArea.setText(getTokens.toString());
			}
		});

	}

	/**
	 * Remove button for include methods
	 * 
	 * @param container
	 */

	private void createRemoveButtonForIncludeMethods(Composite container) {

		GridData dataAddButtonForIncludeMethod = new GridData();
		dataAddButtonForIncludeMethod.widthHint = 100;
		dataAddButtonForIncludeMethod.grabExcessHorizontalSpace = true;
		dataAddButtonForIncludeMethod.horizontalAlignment = GridData.FILL;
		removeButtonForIncludeMethod = new Button(container, SWT.PUSH);
		removeButtonForIncludeMethod.setText("Remove");
		removeButtonForIncludeMethod.setLayoutData(dataAddButtonForIncludeMethod);
		
		removeButtonForIncludeMethod.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				
				StringBuilder getTokens = removeFromIncludeMetodsTextArea(textToIncludeText.getText());

				includeMethodTextArea.setText(getTokens.toString());
			}
		});

	}

	private void createAddButtonForIncludeLocaleFunction(Composite container) {

		GridData dataAddButton = new GridData();
		dataAddButton.grabExcessHorizontalSpace = true;
		dataAddButton.horizontalAlignment = GridData.FILL;
		addButtonForIncludeLocaleFunction = new Button(container, SWT.PUSH);
		addButtonForIncludeLocaleFunction.setText("Add");
		addButtonForIncludeLocaleFunction.setLayoutData(dataAddButton);

		addButtonForIncludeLocaleFunction.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {

				StringBuilder getTokens = addToIncludeLocaleFuncTextArea(textToIncludeLocaleText.getText());

				localeArea.setText(getTokens.toString());

			}
		});
	}

	private void createRemoveButtonForIncludeLocaleFunction(Composite container) {

		GridData dataAddButton = new GridData();
		dataAddButton.grabExcessHorizontalSpace = true;
		dataAddButton.horizontalAlignment = GridData.FILL;
		removeButtonForIncludeLocaleFunction = new Button(container, SWT.PUSH);
		removeButtonForIncludeLocaleFunction.setText("Remove");
		removeButtonForIncludeLocaleFunction.setLayoutData(dataAddButton);

		removeButtonForIncludeLocaleFunction.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {

				StringBuilder getTokens = removeFromIncludeLocaleFuncTextArea(textToIncludeLocaleText.getText());

				localeArea.setText(getTokens.toString());
			}
		});
	}

	private void createAddButtonForExcludeMethod(Composite container) {

		GridData dataAddButton = new GridData();
		dataAddButton.grabExcessHorizontalSpace = true;
		dataAddButton.horizontalAlignment = GridData.FILL;
		addButtonForExcludeMethods = new Button(container, SWT.PUSH);
		addButtonForExcludeMethods.setText("Add");
		addButtonForExcludeMethods.setLayoutData(dataAddButton);

		addButtonForExcludeMethods.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {

				StringBuilder getTokens = addToExcludeMethodsTextArea(textToIncludeExcludeText.getText());
				excludeArea.setText(getTokens.toString());
			}
		});
	}

	private void createRemoveButtonForExcludeMethod(Composite container) {

		GridData dataAddButton = new GridData();
		dataAddButton.grabExcessHorizontalSpace = true;
		dataAddButton.horizontalAlignment = GridData.FILL;
		removeButtonForExcludeMethods = new Button(container, SWT.PUSH);
		removeButtonForExcludeMethods.setText("Remove");
		removeButtonForExcludeMethods.setLayoutData(dataAddButton);

		removeButtonForExcludeMethods.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {

				StringBuilder getTokens = removeFromExcludeMethodsTextArea(textToIncludeExcludeText.getText());
				excludeArea.setText(getTokens.toString());
			}
		});
	}

	/*
	 * Create remove button for add resource function component
	 */
	private void createRemoveButtonForAddResourceFunction(Composite container) {

		GridData dataAddButton = new GridData();
		dataAddButton.horizontalSpan = 1;
		dataAddButton.grabExcessHorizontalSpace = true;
		dataAddButton.horizontalAlignment = GridData.FILL;
		removeButtonForAddResourceFunction = new Button(container, SWT.PUSH);
		removeButtonForAddResourceFunction.setText("Remove");
		removeButtonForAddResourceFunction.setLayoutData(dataAddButton);

		removeButtonForAddResourceFunction.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {

				StringBuilder getTokens = addToExcludeMethodsTextArea(textToIncludeExcludeText.getText());

				excludeArea.setText(getTokens.toString());

			}
		});
	}

	private void createRemoveButtonForDefineCustomizedKey(Composite container) {

		GridData dataRemoveButton = new GridData();
		dataRemoveButton.widthHint = 80;
		dataRemoveButton.grabExcessHorizontalSpace = true;
		dataRemoveButton.horizontalAlignment = GridData.FILL;
		removeButtonForDefineCustomizedKey = new Button(container, SWT.PUSH);
		removeButtonForDefineCustomizedKey.setText("Add");
		removeButtonForDefineCustomizedKey.setLayoutData(dataRemoveButton);

		removeButtonForDefineCustomizedKey.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				customizedKeyContent = new StringBuilder();
				customizedKeyContent.append(setKeyText.getText());
			}
		});
	}

	private void createSkipWordTextToExcludeLabel(Composite container) {

		GridData dataTextToIncludeLabel = new GridData();
		dataTextToIncludeLabel.horizontalSpan = 4;
		dataTextToIncludeLabel.verticalIndent = 5;
		dataTextToIncludeLabel.grabExcessHorizontalSpace = true;
		dataTextToIncludeLabel.horizontalAlignment = GridData.FILL;

		skipWordTextToExcludeLabel = new Label(container, SWT.NONE);
		skipWordTextToExcludeLabel.setText("Text to exclude");
		skipWordTextToExcludeLabel.setLayoutData(dataTextToIncludeLabel);
	}

	private void createTextToInclude(Composite container) {

		GridData dataTextToIncludeButton = new GridData();
		dataTextToIncludeButton.widthHint = 200;
		dataTextToIncludeButton.grabExcessHorizontalSpace = true;
		dataTextToIncludeButton.horizontalAlignment = GridData.FILL;

		skipWordsTextBox = new Text(container, SWT.BORDER);
		skipWordsTextBox.setLayoutData(dataTextToIncludeButton);
	}

	private void createSetKeyLabel(Composite container) {
		setKeyLable = new Label(container, SWT.NONE);
		setKeyLable.setText("Set Key");

		GridData dataSetKeyLabel = new GridData();
		dataSetKeyLabel.widthHint = 620;
		dataSetKeyLabel.horizontalSpan = 4;
		dataSetKeyLabel.verticalIndent = 5;
		dataSetKeyLabel.grabExcessHorizontalSpace = true;
		dataSetKeyLabel.horizontalAlignment = GridData.FILL;

		setKeyLable.setLayoutData(dataSetKeyLabel);
	}

	private void createSetKeyText(Composite container) {

		GridData dataSetKeyTextButton = new GridData();
		dataSetKeyTextButton.widthHint = 450;
		dataSetKeyTextButton.horizontalSpan = 3;
		dataSetKeyTextButton.grabExcessHorizontalSpace = true;
		dataSetKeyTextButton.horizontalAlignment = GridData.FILL;
		
		setKeyText = new Text(container, SWT.BORDER);
		setKeyText.setText(customizedKeyContent.toString());
		setKeyText.setLayoutData(dataSetKeyTextButton);
		
	}

	private void createResourceFunctionLabel(Composite container) {

		resourceFunctionLabel = new Label(container, SWT.NONE);
		resourceFunctionLabel.setText("Resource");

		GridData dataResourceFunctionLabel = new GridData();
		dataResourceFunctionLabel.widthHint = 620;
		dataResourceFunctionLabel.horizontalSpan = 4;
		dataResourceFunctionLabel.verticalIndent = 5;
		dataResourceFunctionLabel.grabExcessHorizontalSpace = true;
		dataResourceFunctionLabel.horizontalAlignment = GridData.FILL;

		resourceFunctionLabel.setLayoutData(dataResourceFunctionLabel);
	}

	private void createResourceFunctionText(Composite container) {

		GridData dataResourceFunctionButton = new GridData();
		dataResourceFunctionButton.widthHint = 450;
		dataResourceFunctionButton.horizontalSpan = 3;
		dataResourceFunctionButton.grabExcessHorizontalSpace = true;
		dataResourceFunctionButton.horizontalAlignment = GridData.FILL;

		resourceFunctionText = new Text(container, SWT.BORDER);
		resourceFunctionText.setLayoutData(dataResourceFunctionButton);
	}

	private void createLanguageText(Composite container) {

		languageLabel = new Label(container, SWT.NONE);
		languageLabel.setText("Language");

		GridData dataLanguageTextButton = new GridData();
		dataLanguageTextButton.horizontalSpan = 4;
		dataLanguageTextButton.verticalIndent = 5;
		dataLanguageTextButton.grabExcessHorizontalSpace = true;
		dataLanguageTextButton.horizontalAlignment = GridData.FILL;

		languageIncludeText = new Combo(container, SWT.NULL);
		languageIncludeText.setItems(new String[] { "Java", "C#", "C", "C++", "Scala" });
		languageIncludeText.setText(includeMethodLangSelection.toString());
		// languageIncludeText.select(0);
		languageIncludeText.setLayoutData(dataLanguageTextButton);

		languageIncludeText.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {

				if (languageIncludeText.getText().equals("Java")) {
					includeMethodTextArea.setText(includeMethodsTextAreaContent.toString());
				} else if (languageIncludeText.getText().equals("C#")) {
					String newItems[] = { "A", "B", "C" };
					includeMethodTextArea.setText("C#");
				} else if (languageIncludeText.getText().equals("C")) {
					String newItems[] = { "A", "B", "C" };
					includeMethodTextArea.setText("C");
				} else if (languageIncludeText.getText().equals("C++")) {
					String newItems[] = { "A", "B", "C" };
					includeMethodTextArea.setText("C++");
				} else if (languageIncludeText.getText().equals("Scala")) {
					String newItems[] = { "A", "B", "C" };
					includeMethodTextArea.setText("Scala");
				}

				includeMethodLangSelection = new StringBuilder();
				includeMethodLangSelection.append(languageIncludeText.getText());
				System.out.println(includeMethodLangSelection);
			}
		});
	}

	/*
	 * Create Language Label for Locale Function Component
	 */

	private void createLanguageLabelForLocaleFunction(Composite container) {

		languageLocaleLabel = new Label(container, SWT.NONE);
		languageLocaleLabel.setText("Language");

		GridData datacreateLanguageLocaleLabel = new GridData();
		datacreateLanguageLocaleLabel.grabExcessHorizontalSpace = true;
		datacreateLanguageLocaleLabel.horizontalSpan = 4;
		datacreateLanguageLocaleLabel.verticalIndent = 5;
		datacreateLanguageLocaleLabel.horizontalAlignment = GridData.FILL;

		languageLocaleLabel.setLayoutData(datacreateLanguageLocaleLabel);
	}

	private void createLanguageLocaleText(Composite container) {

		GridData datacreateLanguageLocaleTextButton = new GridData();
		datacreateLanguageLocaleTextButton.horizontalSpan = 4;
		datacreateLanguageLocaleTextButton.verticalIndent = 5;
		datacreateLanguageLocaleTextButton.grabExcessHorizontalSpace = true;
		datacreateLanguageLocaleTextButton.horizontalAlignment = GridData.FILL;

		languageLocaleText = new Combo(container, SWT.NULL);
		languageLocaleText.setItems(new String[] { "Java", "C#", "C", "C++", "Scala" });
		languageLocaleText.setText(includeMethodLangSelection.toString());
		// languageLocaleText.select(0);
		languageLocaleText.setLayoutData(datacreateLanguageLocaleTextButton);

		languageLocaleText.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {

				if (languageLocaleText.getText().equals("Java")) {
					localeArea.setText(includeLocaleFunTextAreaContent.toString());
				} else if (languageLocaleText.getText().equals("C#")) {
					String newItems[] = { "A", "B", "C" };
					localeArea.setText("C#");
				} else if (languageLocaleText.getText().equals("C")) {
					String newItems[] = { "A", "B", "C" };
					localeArea.setText("C");
				} else if (languageLocaleText.getText().equals("C++")) {
					String newItems[] = { "A", "B", "C" };
					localeArea.setText("C++");
				} else if (languageLocaleText.getText().equals("Scala")) {
					String newItems[] = { "A", "B", "C" };
					localeArea.setText("Scala");
				}
				localeLangSelection = new StringBuilder();
				localeLangSelection.append(languageLocaleText.getText());
			}
		});
	}

	private void createLanguageExcludeLabel(Composite container) {

		languageExcludeLabel = new Label(container, SWT.NONE);
		languageExcludeLabel.setText("Language");

		GridData datacreateLanguageExcludeLabel = new GridData();
		datacreateLanguageExcludeLabel.horizontalSpan = 4;
		datacreateLanguageExcludeLabel.verticalIndent = 5;
		datacreateLanguageExcludeLabel.grabExcessHorizontalSpace = true;
		datacreateLanguageExcludeLabel.horizontalAlignment = GridData.FILL;

		languageExcludeLabel.setLayoutData(datacreateLanguageExcludeLabel);
	}

	private void createLanguageExcludeText(Composite container) {

		GridData datacreateLanguageExcludeTextButton = new GridData();
		datacreateLanguageExcludeTextButton.horizontalSpan = 4;
		datacreateLanguageExcludeTextButton.grabExcessHorizontalSpace = true;
		datacreateLanguageExcludeTextButton.horizontalAlignment = GridData.FILL;

		languageExcludeText = new Combo(container, SWT.NULL);
		languageExcludeText.setItems(new String[] { "Java", "C#", "C", "C++", "Scala" });
		languageExcludeText.setText(includeMethodLangSelection.toString());
		// languageExcludeText.select(0);
		languageExcludeText.setLayoutData(datacreateLanguageExcludeTextButton);

		languageExcludeText.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {

				if (languageExcludeText.getText().equals("Java")) {
					excludeArea.setText(excludeMethodsTextAreaContent.toString());
				} else if (languageExcludeText.getText().equals("C#")) {
					String newItems[] = { "A", "B", "C" };
					excludeArea.setText("C#");
				} else if (languageExcludeText.getText().equals("C")) {
					String newItems[] = { "A", "B", "C" };
					excludeArea.setText("C");
				} else if (languageExcludeText.getText().equals("C++")) {
					String newItems[] = { "A", "B", "C" };
					excludeArea.setText("C++");
				} else if (languageExcludeText.getText().equals("Scala")) {
					String newItems[] = { "A", "B", "C" };
					excludeArea.setText("Scala");
				}

				excludeMethodLangSelection = new StringBuilder();
				excludeMethodLangSelection.append(languageExcludeText.getText());
			}
		});
	}

	private void createTextToIncludeLabel(Composite container) {

		textToIncludeLabel = new Label(container, SWT.NONE);
		textToIncludeLabel.setText("Text to include");

		GridData datacreateTextToIncludeTextLabel = new GridData();
		datacreateTextToIncludeTextLabel.horizontalSpan = 4;
		datacreateTextToIncludeTextLabel.grabExcessHorizontalSpace = true;
		datacreateTextToIncludeTextLabel.horizontalAlignment = GridData.FILL;

		textToIncludeLabel.setLayoutData(datacreateTextToIncludeTextLabel);
	}

	private void createTextToIncludeText(Composite container) {

		GridData datacreateTextToIncludeTextButton = new GridData();
		datacreateTextToIncludeTextButton.horizontalSpan = 2;
		// datacreateTextToIncludeTextButton.widthHint=100;
		datacreateTextToIncludeTextButton.grabExcessHorizontalSpace = true;
		datacreateTextToIncludeTextButton.horizontalAlignment = GridData.FILL;

		textToIncludeText = new Text(container, SWT.BORDER);
		textToIncludeText.setLayoutData(datacreateTextToIncludeTextButton);
	}

	private void createTextToIncludeLocaleLabel(Composite container) {

		textToIncludeLocaleLabel = new Label(container, SWT.NONE);
		textToIncludeLocaleLabel.setText("Text to include");

		GridData datacreateTextToIncludeLocaleLabel = new GridData();
		datacreateTextToIncludeLocaleLabel.horizontalSpan = 4;
		datacreateTextToIncludeLocaleLabel.grabExcessHorizontalSpace = true;
		datacreateTextToIncludeLocaleLabel.horizontalAlignment = GridData.FILL;

		textToIncludeLocaleLabel.setLayoutData(datacreateTextToIncludeLocaleLabel);
	}

	private void createTextToIncludeLocaleText(Composite container) {

		GridData datacreateTextToIncludeLocaleText = new GridData();
		datacreateTextToIncludeLocaleText.horizontalSpan = 2;
		datacreateTextToIncludeLocaleText.grabExcessHorizontalSpace = true;
		datacreateTextToIncludeLocaleText.horizontalAlignment = GridData.FILL;

		textToIncludeLocaleText = new Text(container, SWT.BORDER);
		textToIncludeLocaleText.setLayoutData(datacreateTextToIncludeLocaleText);
	}

	/*
	 * Function Type Label for Include Locale Function component
	 */

	private void createFunctionTypeLabel(Composite container) {

		functionTypeLabel = new Label(container, SWT.NONE);
		functionTypeLabel.setText("Function type");

		GridData datacreateFunctionTypeLabel = new GridData();
		datacreateFunctionTypeLabel.horizontalSpan = 4;
		datacreateFunctionTypeLabel.grabExcessHorizontalSpace = true;
		datacreateFunctionTypeLabel.horizontalAlignment = GridData.FILL;

		functionTypeLabel.setLayoutData(datacreateFunctionTypeLabel);

	}

	private void createFunctionTypeText(Composite container) {

		GridData datacreateFunctionTypeText = new GridData();
		datacreateFunctionTypeText.horizontalSpan = 4;
		datacreateFunctionTypeText.grabExcessHorizontalSpace = true;
		datacreateFunctionTypeText.horizontalAlignment = GridData.FILL;

		functionTypeText = new Combo(container, SWT.NULL);
		functionTypeText.setItems(new String[] { "Date Time", "Currency", "Number" });
		functionTypeText.select(0);
		functionTypeText.setLayoutData(datacreateFunctionTypeText);

		functionTypeText.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {

				if (functionTypeText.getText().equals("Date Time")) {
				} else if (functionTypeText.getText().equals("Currency")) {
				} else if (functionTypeText.getText().equals("Number")) {
				}

				localeFunctionTypeSelection = new StringBuilder();
				localeFunctionTypeSelection.append(functionTypeText.getText());
			}
		});

	}

	/*
	 * Text to include label for exclude method components
	 */

	private void createTextToIncludeExcludeLabel(Composite container) {

		textToIncludeExcludeLabel = new Label(container, SWT.NONE);
		textToIncludeExcludeLabel.setText("Text to include");

		GridData datacreateTextToIncludeExcludeLabel = new GridData();
		datacreateTextToIncludeExcludeLabel.horizontalSpan = 4;
		datacreateTextToIncludeExcludeLabel.grabExcessHorizontalSpace = true;
		datacreateTextToIncludeExcludeLabel.horizontalAlignment = GridData.FILL;

		textToIncludeExcludeLabel.setLayoutData(datacreateTextToIncludeExcludeLabel);
	}

	private void createTextToIncludeExcludeText(Composite container) {

		GridData datacreateTextToIncludeExcludeText = new GridData();
		datacreateTextToIncludeExcludeText.horizontalSpan = 2;
		datacreateTextToIncludeExcludeText.grabExcessHorizontalSpace = true;
		datacreateTextToIncludeExcludeText.horizontalAlignment = GridData.FILL;

		textToIncludeExcludeText = new Text(container, SWT.BORDER);
		textToIncludeExcludeText.setLayoutData(datacreateTextToIncludeExcludeText);
	}

	private void createTextArea(Composite container) {

		skipWordTextArea = new Text(container, SWT.MULTI | SWT.BORDER | SWT.WRAP | SWT.V_SCROLL);
		skipWordTextArea.setEditable(false);
		skipWordTextArea.setText(skipWordsTextAreaContent.toString());
		skipWordTextArea.setToolTipText("Skip words");

		GridData dataTextAreaButton = new GridData();
		dataTextAreaButton.grabExcessHorizontalSpace = true;
		dataTextAreaButton.horizontalAlignment = GridData.FILL;
		dataTextAreaButton.verticalAlignment = GridData.FILL;
		dataTextAreaButton.grabExcessVerticalSpace = true;
		dataTextAreaButton.horizontalSpan = 4;
		dataTextAreaButton.heightHint = 350;
		dataTextAreaButton.widthHint = 600;

		skipWordTextArea.setLayoutData(dataTextAreaButton);
	}

	private void createIncludeMethodTestArea(Composite container) {

		includeMethodTextArea = new Text(container, SWT.MULTI | SWT.BORDER | SWT.WRAP | SWT.V_SCROLL);
		includeMethodTextArea.setText(includeLocaleFunTextAreaContent.toString());
		includeMethodTextArea.setEditable(false);
		includeMethodTextArea.setToolTipText("Include methods");

		GridData dataTextAreaButton = new GridData();
		dataTextAreaButton.horizontalSpan = 4;
		dataTextAreaButton.grabExcessHorizontalSpace = true;
		dataTextAreaButton.horizontalAlignment = GridData.FILL;
		dataTextAreaButton.verticalAlignment = GridData.FILL;
		dataTextAreaButton.grabExcessVerticalSpace = true;
		dataTextAreaButton.heightHint = 300;
		dataTextAreaButton.widthHint = 600;

		includeMethodTextArea.setLayoutData(dataTextAreaButton);
	}

	private void createLocaleArea(Composite container) {

		localeArea = new Text(container, SWT.MULTI | SWT.BORDER | SWT.WRAP | SWT.V_SCROLL);
		localeArea.setEditable(false);
		localeArea.setText(includeLocaleFunTextAreaContent.toString());
		localeArea.setToolTipText("Locale function");

		GridData dataTextAreaButton = new GridData();
		dataTextAreaButton.grabExcessHorizontalSpace = true;
		dataTextAreaButton.horizontalSpan = 4;
		dataTextAreaButton.horizontalAlignment = GridData.FILL;
		dataTextAreaButton.verticalAlignment = GridData.FILL;
		dataTextAreaButton.grabExcessVerticalSpace = true;
		dataTextAreaButton.heightHint = 250;
		dataTextAreaButton.widthHint = 600;

		// textArea = new Text(container, SWT.MULTI | SWT.BORDER);
		// textArea.setBounds(100, 75, 100, 20);

		localeArea.setLayoutData(dataTextAreaButton);
	}

	private void createExcludeArea(Composite container) {

		excludeArea = new Text(container, SWT.MULTI | SWT.BORDER | SWT.WRAP | SWT.V_SCROLL);
		excludeArea.setEditable(false);
		excludeArea.setText(excludeMethodsTextAreaContent.toString());
		excludeArea.setToolTipText("Exclude tokens");

		GridData dataTextAreaButton = new GridData();
		dataTextAreaButton.horizontalSpan = 4;
		dataTextAreaButton.grabExcessHorizontalSpace = true;
		dataTextAreaButton.horizontalAlignment = GridData.FILL;
		dataTextAreaButton.verticalAlignment = GridData.FILL;
		dataTextAreaButton.grabExcessVerticalSpace = true;
		dataTextAreaButton.heightHint = 300;
		dataTextAreaButton.widthHint = 600;

		excludeArea.setLayoutData(dataTextAreaButton);
	}

	private void createExternalizeOptionsArea(Composite container) {

		externalizeOptionsArea = new Text(container, SWT.MULTI | SWT.BORDER | SWT.WRAP | SWT.V_SCROLL);
		externalizeOptionsArea.setToolTipText("Externalize Options");

		GridData dataExternalizeOptionsArea = new GridData();
		dataExternalizeOptionsArea.horizontalSpan = 4;
		dataExternalizeOptionsArea.grabExcessHorizontalSpace = true;
		dataExternalizeOptionsArea.horizontalAlignment = GridData.FILL;
		dataExternalizeOptionsArea.verticalAlignment = GridData.FILL;
		dataExternalizeOptionsArea.grabExcessVerticalSpace = true;
		dataExternalizeOptionsArea.heightHint = 300;
		dataExternalizeOptionsArea.widthHint = 600;

		externalizeOptionsArea.setLayoutData(dataExternalizeOptionsArea);
	}

	private void createReplaceStringCheckBox(Composite container) {

		GridData dataCheckBoxButton = new GridData();
		dataCheckBoxButton.horizontalSpan = 4;
		dataCheckBoxButton.grabExcessHorizontalSpace = true;
		dataCheckBoxButton.horizontalAlignment = GridData.FILL;

		replaceStringCheckBox = new Button(container, SWT.CHECK);
		replaceStringCheckBox.setText("Replace string values in source code");
		replaceStringCheckBox.setLayoutData(dataCheckBoxButton);
	}

	private void createDescriptionTab(Composite composite) {

		GridData dataDescriptionTab = new GridData();
		dataDescriptionTab.horizontalSpan = 4;
		dataDescriptionTab.grabExcessHorizontalSpace = true;
		dataDescriptionTab.horizontalAlignment = GridData.FILL;

		descriptionsTab = new Group(composite, SWT.NULL);
		descriptionsTab.setText("Description");
		Label label = new Label(descriptionsTab, SWT.NULL);
		label.setAlignment(SWT.LEFT);
		label.setText("Your tab options here");
		descriptionsTab.setLayoutData(dataDescriptionTab);

	}

	private void createExternalizeLabel(Composite container) {

		externalizeLabel = new Label(container, SWT.NONE);
		externalizeLabel.setText("Externalize");

		GridData datacreateExternalizeLabel = new GridData();
		datacreateExternalizeLabel.horizontalSpan = 4;
		datacreateExternalizeLabel.widthHint = 620;
		datacreateExternalizeLabel.grabExcessHorizontalSpace = true;
		datacreateExternalizeLabel.horizontalAlignment = GridData.FILL;

		externalizeLabel.setLayoutData(datacreateExternalizeLabel);

	}

	private void createTable(Composite composite) {

		GridData dataTable = new GridData();
		dataTable.horizontalSpan = 4;
		dataTable.widthHint = 620;
		dataTable.grabExcessHorizontalSpace = true;
		dataTable.horizontalAlignment = GridData.FILL;

		Table table = new Table(composite, SWT.CHECK | SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL);
		table.setHeaderVisible(true);

		String[] titles = { "Col 1", "Col 2", "Col 3", "Col 4" };

		for (int loopIndex = 0; loopIndex < titles.length; loopIndex++) {
			TableColumn column = new TableColumn(table, SWT.NULL);
			column.setText(titles[loopIndex]);
		}

		for (int loopIndex = 0; loopIndex < 5; loopIndex++) {
			TableItem item = new TableItem(table, SWT.NULL);
			item.setText("Item " + loopIndex);
			item.setText(0, "Item " + loopIndex);
			item.setText(1, "Yes");
			item.setText(2, "No");
			item.setText(3, "A table item");
		}

		for (int loopIndex = 0; loopIndex < titles.length; loopIndex++) {
			table.getColumn(loopIndex).pack();
		}

		table.setBounds(25, 25, 220, 200);

	}

	@Override
	protected boolean isResizable() {
		return true;
	}

	@Override
	protected void okPressed() {
		ConfigurationXML con = new ConfigurationXML();
		saveFieldState();
		System.out.println(customizedKeyContent.toString().trim());
		con.saveAsXMLFile(skipWordList, includeMethodList, includeMethodLangSelection.toString(), localeList,
				localeLangSelection.toString(), localeFunctionTypeSelection.toString(), excludeMethodList,
				excludeMethodLangSelection.toString(), customizedKeyContent.toString().trim());
		System.out.println("Done");
		super.okPressed();
	}

	/**
	 * This method splits the comma separated strings into an array of string
	 * token
	 */
	public void saveFieldState() {

		int i = 1;
		String[] skipWordTokens = skipWordsTextAreaContent.toString().split("\n", -1);
		String[] includeMethodsTokens = includeMethodsTextAreaContent.toString().split("\n", -1);
		String[] localeMethodsTokens = includeLocaleFunTextAreaContent.toString().split("\n", -1);
		String[] excludeMethodsTokens = excludeMethodsTextAreaContent.toString().split("\n", -1);

		for (String token : skipWordTokens)
			skipWordList.put("name" + i++, token);
		for (String token : includeMethodsTokens)
			includeMethodList.put("IncludeMethod" + i++, token);
		for (String token : localeMethodsTokens)
			localeList.put("LocaleFunc" + i++, token);
		for (String token : excludeMethodsTokens)
			excludeMethodList.put("ExcludeMethod" + i++, token);

	}

	public StringBuilder addToSkipWordTextArea(String str) {

		String[] tokens = str.split(",", -1);
		for (String token : tokens) {
			skipWordsTextAreaContent.append(token + "\n");
		}
		return skipWordsTextAreaContent;
	}
	
	public StringBuilder removeFromSkipWordTextArea(String str) {

		String[] tokens = str.split(",", -1);
		for (String token : tokens) {
			
		    int i = skipWordsTextAreaContent.indexOf(token);
		    if (i != -1) {
		    	skipWordsTextAreaContent.delete(i, i + token.length()+1);
		    }
		}
		return skipWordsTextAreaContent;		
	}

	public StringBuilder addToIncludeMetodsTextArea(String str) {

		String[] tokens = str.split(",", -1);
		for (String token : tokens) {
			includeMethodsTextAreaContent.append(token + "\n");
		}
		return includeMethodsTextAreaContent;
	}
	
	public StringBuilder removeFromIncludeMetodsTextArea(String str) {

		String[] tokens = str.split(",", -1);
		for (String token : tokens) {
			
		    int i = includeMethodsTextAreaContent.indexOf(token);
		    if (i != -1) {
		    	includeMethodsTextAreaContent.delete(i, i + token.length()+1);
		    }
		}
		return includeMethodsTextAreaContent;		
	}

	/**
	 * Returns
	 * 
	 * @param str
	 * @return
	 */
	public StringBuilder addToIncludeLocaleFuncTextArea(String str) {

		String[] tokens = str.split(",", -1);
		for (String token : tokens) {
			includeLocaleFunTextAreaContent.append(token + "\n");
		}
		return includeLocaleFunTextAreaContent;
	}
	
	public StringBuilder removeFromIncludeLocaleFuncTextArea(String str) {

		String[] tokens = str.split(",", -1);
		for (String token : tokens) {
			
		    int i = includeLocaleFunTextAreaContent.indexOf(token);
		    if (i != -1) {
		    	includeLocaleFunTextAreaContent.delete(i, i + token.length()+1);
		    }
		}
		return includeLocaleFunTextAreaContent;		
	}

	/***
	 * This method returns an array of strings
	 * 
	 * @param str
	 * @return
	 */
	public StringBuilder addToExcludeMethodsTextArea(String str) {

		String[] tokens = str.split(",", -1);
		for (String token : tokens) {
			excludeMethodsTextAreaContent.append(token + "\n");
		}

		excludeMethodLangSelection = new StringBuilder();
		excludeMethodLangSelection.append(languageExcludeText.getText());

		return excludeMethodsTextAreaContent;
	}
	
	public StringBuilder removeFromExcludeMethodsTextArea(String str) {

		String[] tokens = str.split(",", -1);
		for (String token : tokens) {
			
		    int i = excludeMethodsTextAreaContent.indexOf(token);
		    if (i != -1) {
		    	excludeMethodsTextAreaContent.delete(i, i + token.length()+1);
		    }
		}
		return excludeMethodsTextAreaContent;		
	}

}
