package JoS.JAVAProjects;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import JoS.JAVAProjects.ApplicationParams;
public class XMLInterface {
/*	ApplicationParams gOBJ=null;//=
	XMLInterface()
	{
		gOBJ=ApplicationParams.GetObject();
	}
	 public void ReadXMLData (String sFileName) {
		 DebugInterface di=new DebugInterface();
		  try {
			  di.Debug ("Inside function ReadXMLData()..");
		 	  int j=1;
		 	  String sNodeName, sNodeValue;
		 	 Path xmlpath = Paths.get(gOBJ.getgStrXMLPath());
		 	 if (Files.exists(xmlpath)) {
		 		  // file exist
			 	 File file = new File(gOBJ.getgStrXMLPath());
				 DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			 	 DocumentBuilder db = dbf.newDocumentBuilder();	
				  Document doc = db.parse(file);
				  doc.getDocumentElement().normalize();
				  //System.out.println("Root element " + doc.getDocumentElement().getNodeName());
				  NodeList nodeLst = doc.getElementsByTagName("TestIteration");
				  //System.out.println("Number of Iterations mentioned in XML - " + nodeLst.getLength());  
				  if (nodeLst.getLength()>0) {
					  for (int s = 0; s < nodeLst.getLength(); s++) {
						  //System.out.println("XML Iteration - " + s);
						  Node node = nodeLst.item(s);
						  if (node.hasAttributes()) {							  
							  Attr attr = (Attr) node.getAttributes().getNamedItem("name");
							  if (attr != null) {
				                  String attribute= attr.getValue();                      
				                  //System.out.println("Mentioned attribute: " + attribute);
				                  //System.out.println("pCurrentTestIteration: " + pCurrentTestIteration);
				                  if (attribute.equalsIgnoreCase(gOBJ.getgCurrentTestIteration())){
				                	  System.out.println("Matched");
				                	  if (node.hasChildNodes()) {
				                		  //System.out.println("Has ChildNode" + node.hasChildNodes());
				                		  NodeList ChildNodeList = node.getChildNodes();
					                	  //System.out.println("Child node List Length - " + ChildNodeList.getLength());
					                	  for (int i = 0; i < ChildNodeList.getLength()/2; i++){	
					                		  Element eElement = (Element) node;
					                		  sNodeName = ChildNodeList.item(j).getNodeName();
					                		  //System.out.println("Node Name - " + sNodeName);
					                		  if (sNodeName == "#comment" && j < ChildNodeList.getLength()-2){
					                			  //System.out.println("inside");
					                			  j=j+2;
					                			  sNodeName = ChildNodeList.item(j).getNodeName();
					                			  //System.out.println("Node Name - " + sNodeName);	                		
					                		  }
					                		  //System.out.println(j);
					                		  if (sNodeName != "#comment"){
					                			  sNodeValue = eElement.getElementsByTagName(sNodeName).item(0).getTextContent();
					                			  Hashmap.AssignHashValueToHashKey(sNodeName, sNodeValue);
						                		  di.Debug ("~~~~~~~~~~~~~~~~~~~~~~");
						                		  di.Debug("Node Name -" + sNodeName);
						                		  di.Debug("Node Value -" + sNodeValue);
						                		  di.Debug("~~~~~~~~~~~~~~~~~~~~~~");
						                		  j=j+2;
					                		  }
					                	  }
				                	  }
				                  }
				              }
				          }		  
					  	}
			  	}
				  di.Debug ("Exiting function ReadXMLData()..");
		  }else{
			  di.Debug ("Failed::::: File path doesn't exist = " + gOBJ.getgStrXMLPath());
			  new Utils().ReportStepToResultDB("File path doesn't exist = " + gOBJ.getgStrXMLPath(), "Fail");
		  }
		  } catch (Exception e) {
		    e.printStackTrace();
		  }
		 }
	 public void ReadEnvironmentXML () {
		 DebugInterface di=new DebugInterface();
		  try {
			  di.Debug ("Inside function ReadEnvironmentXML()..");
		 	  int j=1;
		 	  String sNodeName, sNodeValue;
		 	  File file = new File(gOBJ.getgStrXMLPathEnv());
		 	  DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		 	  DocumentBuilder db = dbf.newDocumentBuilder();	
			  Document doc = db.parse(file);
			  doc.getDocumentElement().normalize();
			  //System.out.println("Root element " + doc.getDocumentElement().getNodeName());
			  NodeList nodeLst = doc.getElementsByTagName("Environment");
			  //System.out.println("Number of Iterations mentioned in XML - " + nodeLst.getLength());  
			  if (nodeLst.getLength()>0) {
				  for (int s = 0; s < nodeLst.getLength(); s++) {
					  //System.out.println("XML Iteration - " + s);
					  Node node = nodeLst.item(s);
					  if (node.hasAttributes()) {							  
						  Attr attr = (Attr) node.getAttributes().getNamedItem("name");
						  if (attr != null) {
			                  String attribute= attr.getValue();                      
			                  //System.out.println("Mentioned attribute: " + attribute);
			                  //System.out.println("pCurrentTestIteration: " + pCurrentTestIteration);
			                  if (attribute.equalsIgnoreCase(gOBJ.getgStrReleaseFolder())){
			                	  System.out.println("Matched");
			                	  if (node.hasChildNodes()) {
			                		  //System.out.println("Has ChildNode" + node.hasChildNodes());
			                		  NodeList ChildNodeList = node.getChildNodes();
				                	  //System.out.println("Child node List Length - " + ChildNodeList.getLength());
				                	  for (int i = 0; i < ChildNodeList.getLength()/2-1; i++){	
				                		  Element eElement = (Element) node;
				                		  sNodeName = ChildNodeList.item(j).getNodeName();
				                		  //System.out.println("Node Name - " + sNodeName);
				                		  if (sNodeName == "#comment" && j < ChildNodeList.getLength()-2){
				                			  //System.out.println("inside");
				                			  j=j+2;
				                			  sNodeName = ChildNodeList.item(j).getNodeName();
				                			  //System.out.println("Node Name - " + sNodeName);	                		
				                		  }
				                		  //System.out.println(j);
				                		  if (sNodeName != "#comment"){
				                			  sNodeValue = eElement.getElementsByTagName(sNodeName).item(0).getTextContent();
				                			  Hashmap.AssignHashValueToHashKey(sNodeName, sNodeValue);
					                		  di.Debug ("~~~~~~~~~~~~~~~~~~~~~~");
					                		  di.Debug("Node Name -" + sNodeName);
					                		  di.Debug("Node Value -" + sNodeValue);
					                		  di.Debug("~~~~~~~~~~~~~~~~~~~~~~");
					                		  j=j+2;
				                		  }
				                	  }
			                	  }
			                  }
			              }
			          }		  
				  	}
			  	}
			  di.Debug ("Exiting function ReadEnvironmentXML()..");
		  } catch (Exception e) {
		    e.printStackTrace();
		  }
		 }
	 public void ReadEnvironmentVariable () {
		 DebugInterface di=new DebugInterface();
		 di.Debug ("Inside function ReadEnvironmentVariable()..");
		 
		 di.Debug ("Exiting function ReadEnvironmentVariable()..");
	 }
	 public int GetNumberOfIterationsinXML() {
		 DebugInterface di=new DebugInterface();
		  try {
			  
				 di.Debug ("Inside function GetNumberOfIterationsinXML()..");
			 	 Path xmlpath = Paths.get(gOBJ.getgStrXMLPath());
			 	 if (Files.exists(xmlpath)) {
					 File file = new File(gOBJ.getgStrXMLPath());
					 DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
					 DocumentBuilder db = dbf.newDocumentBuilder();	
					 Document doc = db.parse(file);
					 doc.getDocumentElement().normalize();
					 //System.out.println("Root element " + doc.getDocumentElement().getNodeName());
					 NodeList nodeLst = doc.getElementsByTagName("TestIteration");
					 System.out.println("Number of Iterations mentioned in XML - " + nodeLst.getLength());
					 di.Debug("Number of Iterations mentioned in XML - " + nodeLst.getLength());
					 di.Debug ("Exiting function GetNumberOfIterationsinXML()..");
					 return nodeLst.getLength();
			 	 }else{
			 		di.Debug ("Failed::::: File path doesn't exist = " + gOBJ.getgStrXMLPath());
					new Utils().ReportStepToResultDB("File path does not exist = " + gOBJ.getgStrXMLPath().replace('\\', '/'), "Fail");
			 		return 0;
				  }
		  } catch (Exception e) {
			  di.Debug ("Failed:::: Exception occured:: " + e);
			  new Utils().ReportStepToResultDB("Exception occured:: " + e, "Fail");
			  e.printStackTrace();
		    return 0;
		  }		
		 }*/
}
