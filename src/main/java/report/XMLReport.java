package report;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;

public class XMLReport {
    private static void flattenTree(Element rootElement, Child root, Document doc){
        root.setProcessed(true);
        for(Child c:root.getChildList()){
            flattenTree(rootElement,c,doc);
        }
        if(root.hasChilds()) {
            boolean flag=false;
            for(Child c:root.getChildList()){
                if(!c.isProcessed()){
                    flag=true;
                }
            }
            if(flag==false){
                Element parent=doc.createElement(root.getType().trim());
                if(root.getType().equals("station")){
                    Attr attr=doc.createAttribute("name");
                    attr.setValue(root.getInfo().trim());
                    parent.setAttributeNode(attr);
                }
                for(Child c:root.getChildList()){
                    if(c.getObj() != null)
                        parent.appendChild((Element)c.getObj());
                }
                root.setObj(parent);
                rootElement.appendChild(parent);
            }
        }else{
            Element elem = doc.createElement(root.getType().trim());
            elem.appendChild(doc.createTextNode(root.getInfo().trim()));
            root.setObj(elem);
        }

    }


    public static void prettyPrintXML(DOMSource source, String fileName) throws TransformerException {
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
        StreamResult result = new StreamResult(fileName);
        transformer.transform(source, result);
    }

    public static void writeLine(Child root, String fileName){
        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            File file = new File("tmp.xml");
            Document doc=null;
            Element rootElement=null;
            if(file.exists()) {
                doc = docBuilder.parse(file);
                rootElement = doc.getDocumentElement();
            }else{
                doc = docBuilder.newDocument();
                rootElement=doc.createElement(root.getType().trim());
                doc.appendChild(rootElement);
            }
            flattenTree(rootElement,root,doc);
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult("tmp.xml");
            transformer.transform(source, result);

            prettyPrintXML(source, fileName);


        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (TransformerConfigurationException e) {
            e.printStackTrace();
        } catch (TransformerException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
