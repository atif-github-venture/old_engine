package JoS.JAVAProjects;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class SeleniumIDE_HTML_toExcel {
	/*
	 * This class deal with reading the html file input and converting the same to excel file
	 * excel file contains the 3 sheets for flow, task and object repository respectively
	 * HTML file - the input to this class; recorder by Selenium IDE
	 */
	private String htmlFilePath;
	public String getHtmlFilePath() {
		return htmlFilePath;
	}
	public void setHtmlFilePath(String htmlFilePath) {
		this.htmlFilePath = htmlFilePath;
	}

	SeleniumIDE_HTML_toExcel(String htmlFilepath){
		this.htmlFilePath = htmlFilepath;
	}
	SeleniumIDE_HTML_toExcel(){
		this.htmlFilePath = null;
	}

	public static void main(String[] args) 
	{     
		SeleniumIDE_HTML_toExcel abc= new SeleniumIDE_HTML_toExcel();
		abc.setHtmlFilePath("C:\\Users\\ahm9997\\Desktop\\def.html");
		Document doc = abc.readXMLContent(abc.getHtmlFilePath());
		doc.getDocumentElement().normalize();
		System.out.println ("Root element of the doc is " + doc.getDocumentElement().getNodeName());
		String xpath = "html/body/table/tbody/tr/td";
		List<String> xpathList = abc.getXpathList(doc, xpath);
		System.out.println("List Obtained:" + Arrays.toString(xpathList.toArray()));
	}
	public Document readXMLContent(String xmlPath){
		Path xmlpath = Paths.get(htmlFilePath);
		Document doc = null;
		if (Files.exists(xmlpath)) {
			System.out.println("file exists: " + htmlFilePath);
			File file = new File(htmlFilePath);
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = null;
			try {
				db = dbf.newDocumentBuilder();
			} catch (ParserConfigurationException e) {
				System.out.println("Exception" + e.getMessage());
				e.printStackTrace();
			}	

			try {
				doc = db.parse(file);
				return doc;
			}catch (SAXException | IOException e) {
				System.out.println("Exception" + e.getMessage());
				e.printStackTrace();
			}
		}
		return doc;
	}

	public List<String> getXpathList(Document doc, String xpath) {
		List<String> list = new ArrayList<>();
		// Create XPathFactory object
		XPathFactory xpathFactory = XPathFactory.newInstance();

		// Create XPath object
		XPath xpat = xpathFactory.newXPath();
		try {
			//create XPathExpression object
			XPathExpression expr =	xpat.compile("/"+ xpath +"/text()");
			//evaluate expression result on XML document
			String nodeValue = null;
			NodeList nodes = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
			for (int i = 0; i < nodes.getLength(); i++){
				nodeValue = nodes.item(i).getNodeValue();
				if (nodeValue == null || nodeValue.isEmpty()){
					nodeValue = "NULL";
				}
				list.add(nodeValue);
			}			
		} catch (XPathExpressionException e) {
			System.out.println("Exception" + e.getMessage());
		}
		return list;
	}
}
