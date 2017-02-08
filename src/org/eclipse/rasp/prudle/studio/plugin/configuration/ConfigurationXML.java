package org.eclipse.rasp.prudle.studio.plugin.configuration;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class ConfigurationXML {

	private List<String> xmlSkipWordTokens;
	private List<String> xmlIncludeMethodsHardTokens;
	private List<String> xmlLocaleHardTokens;
	private List<String> xmlExcludeTokens;
	private List<String> xmlIncludeMethodsListTokens;

	/**
	 * This method saves the hard codes as xml file
	 */
	public void saveAsXMLFile(Map<String, String> list, Map<String, String> includeMethodList, String key,
			Map<String, String> localeList, String keyLocale, String localeFunType,
			Map<String, String> excludeMethodList, String keyExclude, String custemizeKey) {

		try {

			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

			// root elements
			Document doc = docBuilder.newDocument();
			Element rootElement = doc.createElement("Configuration");
			doc.appendChild(rootElement);

			for (Map.Entry<String, String> entry : list.entrySet()) {

				// project elements
				if (entry.getValue().equals(""))
					continue;
				Element project = doc.createElement("HardCodeString");
				rootElement.appendChild(project);

				// set attribute to project element
				Attr attr = doc.createAttribute("name");
				attr.setValue(entry.getValue());
				project.setAttributeNode(attr);

			}

			for (Map.Entry<String, String> entry : includeMethodList.entrySet()) {

				// project elements
				if (entry.getValue().equals(""))
					continue;
				Element project = doc.createElement("HardCodeJavaString");
				rootElement.appendChild(project);

				// set attribute to project element
				Attr attr1 = doc.createAttribute("IncludeMethod");
				attr1.setValue(entry.getValue());
				project.setAttributeNode(attr1);

				// set attribute to project element
				Attr attr2 = doc.createAttribute("Key");
				attr2.setValue(key);
				project.setAttributeNode(attr2);

			}

			for (Map.Entry<String, String> entry : localeList.entrySet()) {

				// project elements
				if (entry.getValue().equals(""))
					continue;
				Element project = doc.createElement("HardCodeJavaString");
				rootElement.appendChild(project);

				// set attribute to project element
				Attr attr1 = doc.createAttribute("LocaleFunc");
				attr1.setValue(entry.getValue());
				project.setAttributeNode(attr1);

				// set attribute to project element
				Attr attr2 = doc.createAttribute("Key");
				attr2.setValue(keyLocale);
				project.setAttributeNode(attr2);

				// set attribute to project element
				Attr attr3 = doc.createAttribute("FuncType");
				attr3.setValue(localeFunType);
				project.setAttributeNode(attr3);

			}

			for (Map.Entry<String, String> entry : excludeMethodList.entrySet()) {

				// project elements
				if (entry.getValue().equals(""))
					continue;
				Element project = doc.createElement("HardCodeJavaString");
				rootElement.appendChild(project);

				// set attribute to project element
				Attr attr1 = doc.createAttribute("ExcludeMethod");
				attr1.setValue(entry.getValue());
				project.setAttributeNode(attr1);

				// set attribute to project element
				Attr attr2 = doc.createAttribute("Key");
				attr2.setValue(keyExclude);
				project.setAttributeNode(attr2);

			}
			
			// project elements				
				Element project = doc.createElement("CustomizedKey");
				rootElement.appendChild(project);
	
				// set attribute to project element
				Attr attr = doc.createAttribute("MethodName");
				attr.setValue(custemizeKey);
				project.setAttributeNode(attr);


			// write the content into xml file
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(new File("E:\\PrudleConfiguration.xml"));

			// Output to console for testing
			// StreamResult result = new StreamResult(System.out);

			transformer.transform(source, result);

			System.out.println("PrudleConfiguration saved!");

		} catch (ParserConfigurationException pce) {
			pce.printStackTrace();
		} catch (TransformerException tfe) {
			tfe.printStackTrace();
		}
	}

	public List<String> readXMLFileForSkipWords() {

		xmlSkipWordTokens = new ArrayList<String>();

		try {

			// File fXmlFile = new File("E:\\PrudleConfig.xml");
			DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document doc = builder.parse("E:\\PrudleConfiguration.xml");
			XPathFactory xPathfactory = XPathFactory.newInstance();
			XPath xpath = xPathfactory.newXPath();
			XPathExpression expr = xpath.compile("//Configuration/HardCodeString[@name]");
			NodeList nl = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);

			for (int i = 0; i < nl.getLength(); i++) {
				Node currentItem = nl.item(i);
				String key = currentItem.getAttributes().getNamedItem("name").getNodeValue();
				// System.out.println(key);
				xmlSkipWordTokens.add(key);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return xmlSkipWordTokens;

	}

	public List<String> readXMLFileForIncludeMethods() {

		xmlIncludeMethodsHardTokens = new ArrayList<String>();

		try {

			// File fXmlFile = new File("E:\\PrudleConfig.xml");
			DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document doc = builder.parse("E:\\PrudleConfiguration.xml");
			XPathFactory xPathfactory = XPathFactory.newInstance();
			XPath xpath = xPathfactory.newXPath();
			XPathExpression expr = xpath.compile("//Configuration/HardCodeJavaString[@IncludeMethod]");
			NodeList nl = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);

			for (int i = 0; i < nl.getLength(); i++) {
				Node currentItem = nl.item(i);
				String key = currentItem.getAttributes().getNamedItem("IncludeMethod").getNodeValue();
				// System.out.println(key);
				xmlIncludeMethodsHardTokens.add(key);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return xmlIncludeMethodsHardTokens;

	}

	public String readXMLFileForIncludeMethodsKey() {

		String keyStr = null;

		try {

			// File fXmlFile = new File("E:\\PrudleConfig.xml");
			DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document doc = builder.parse("E:\\PrudleConfiguration.xml");
			XPathFactory xPathfactory = XPathFactory.newInstance();
			XPath xpath = xPathfactory.newXPath();
			XPathExpression expr = xpath.compile("//Configuration/HardCodeJavaString[@Key]");
			NodeList nl = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);

			// for (int i = 0; i < nl.getLength(); i++)
			// {
			Node currentItem = nl.item(0);
			String key = currentItem.getAttributes().getNamedItem("Key").getNodeValue();
			keyStr = key;
			// System.out.println(key);
			// xmlIncludeMethodsHardTokens.add(key);
			// }
		} catch (Exception e) {
			e.printStackTrace();
		}

		return keyStr;

	}

	public List<String> readXMLFileForLocaleFunctions() {

		xmlLocaleHardTokens = new ArrayList<String>();

		try {

			// File fXmlFile = new File("E:\\PrudleConfig.xml");
			DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document doc = builder.parse("E:\\PrudleConfiguration.xml");
			XPathFactory xPathfactory = XPathFactory.newInstance();
			XPath xpath = xPathfactory.newXPath();
			XPathExpression expr = xpath.compile("//Configuration/HardCodeJavaString[@LocaleFunc]");
			NodeList nl = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);

			for (int i = 0; i < nl.getLength(); i++) {
				Node currentItem = nl.item(i);
				String key = currentItem.getAttributes().getNamedItem("LocaleFunc").getNodeValue();
				// System.out.println(key);
				xmlLocaleHardTokens.add(key);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return xmlLocaleHardTokens;

	}

	public List<String> readXMLFileForExcludeMethods() {

		xmlExcludeTokens = new ArrayList<String>();

		try {

			// File fXmlFile = new File("E:\\PrudleConfig.xml");
			DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document doc = builder.parse("E:\\PrudleConfiguration.xml");
			XPathFactory xPathfactory = XPathFactory.newInstance();
			XPath xpath = xPathfactory.newXPath();
			XPathExpression expr = xpath.compile("//Configuration/HardCodeJavaString[@ExcludeMethod]");
			NodeList nl = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);

			for (int i = 0; i < nl.getLength(); i++) {
				Node currentItem = nl.item(i);
				String key = currentItem.getAttributes().getNamedItem("ExcludeMethod").getNodeValue();
				// System.out.println(key);
				xmlExcludeTokens.add(key);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return xmlExcludeTokens;

	}

	public List<String> readXMLFileForincludeMethodsList(String headerTag, String subHeaderTag) {

		xmlIncludeMethodsListTokens = new ArrayList<String>();

		try {

			// File fXmlFile = new File("E:\\PrudleConfig.xml");
			DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document doc = builder.parse("E:\\PrudleConfiguration.xml");
			XPathFactory xPathfactory = XPathFactory.newInstance();
			XPath xpath = xPathfactory.newXPath();
			XPathExpression expr = xpath.compile("//Configuration/"+headerTag.trim()+"[@"+subHeaderTag.trim()+"]");
			NodeList nl = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);

			for (int i = 0; i < nl.getLength(); i++) {
				Node currentItem = nl.item(i);
				String key = currentItem.getAttributes().getNamedItem(subHeaderTag.trim()).getNodeValue();
				// System.out.println(key);
				xmlIncludeMethodsListTokens.add(key);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return xmlIncludeMethodsListTokens;

	}
	
	public String getValueFromCS(String headerTag, String subHeaderTag) {
		
		String value  = null;

		try {

			// File fXmlFile = new File("E:\\PrudleConfig.xml");
			DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document doc = builder.parse("E:\\PrudleConfiguration.xml");
			XPathFactory xPathfactory = XPathFactory.newInstance();
			XPath xpath = xPathfactory.newXPath();
			XPathExpression expr = xpath.compile("//Configuration/"+headerTag.trim()+"[@"+subHeaderTag.trim()+"]");
			NodeList nl = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);

				Node currentItem = nl.item(0);
				value = currentItem.getAttributes().getNamedItem(subHeaderTag.trim()).getNodeValue();
				
		} catch (Exception e) {
			e.printStackTrace();
		}

		return value.trim();

	}

}
