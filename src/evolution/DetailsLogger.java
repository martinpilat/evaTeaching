package evolution;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.ProcessingInstruction;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * User: Martin Pilat
 * Date: 7.10.13
 * Time: 15:55
 */
public class DetailsLogger {

    static Document doc;
    static DocumentBuilder docBuilder;
    static String fileName;
    static Element rootEle;
    static Element genEle;
    static Element mateEle;
    static Element opEle;

    public static void startNewLog(String fileName) {
        DetailsLogger.fileName = fileName;

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        try {
            docBuilder = dbf.newDocumentBuilder();
            doc = docBuilder.newDocument();
        }
        catch (ParserConfigurationException pce) {
            pce.printStackTrace();
        }

        rootEle = doc.createElement("log");
        Date now = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        rootEle.setAttribute("date", sdf.format(now));
        doc.appendChild(rootEle);
        doc.setXmlStandalone(true);
        ProcessingInstruction pi = doc.createProcessingInstruction("xml-stylesheet", "type=\"text/xsl\" href=\"eva.xsl\"");
        doc.insertBefore(pi, rootEle);

    }

    public static void logParams(Properties prop) {

        Element props = doc.createElement("properties");
        for (String name : prop.stringPropertyNames()) {
            Element e = doc.createElement("property");
            e.setAttribute("name", name);
            e.appendChild(doc.createTextNode(prop.getProperty(name)));
            props.appendChild(e);
        }

        rootEle.appendChild(props);
    }

    static void logPopulation(Population pop, Element el) {
        for (int i = 0; i < pop.getPopulationSize(); i++) {
            Element ind = doc.createElement("individual");
            Element e = doc.createElement("chromosome");
            e.appendChild(doc.createTextNode(pop.get(i).toString()));
            ind.appendChild(e);
            e = doc.createElement("fitness");
            e.appendChild(doc.createTextNode(Double.toString(pop.get(i).getFitnessValue())));
            ind.appendChild(e);
            e = doc.createElement("objective");
            e.appendChild(doc.createTextNode(Double.toString(pop.get(i).getObjectiveValue())));
            ind.appendChild(e);
            el.appendChild(ind);
        }

    }

    public static void logInitialPopulation(Population pop) {

        Element init = doc.createElement("initial-population");
        rootEle.appendChild(init);
        logPopulation(pop, init);
    }

    public static void logNewGeneration(int generationNumber) {

        genEle = doc.createElement("generation");
        genEle.setAttribute("number", Integer.toString(generationNumber));
        rootEle.appendChild(genEle);
    }

    public static void logNewMatingSelection() {
        mateEle = doc.createElement("mating-selection");
        genEle.appendChild(mateEle);
    }

    public static void logMatingPool(Population pop) {
        Element pool = doc.createElement("mating-pool");
        genEle.appendChild(pool);
        logPopulation(pop, pool);
    }

    public static void logSelectedPart(Population pop) {
        for (int i = 0; i < pop.getPopulationSize(); i++) {
            Element ind = doc.createElement("individual");
            ind.setAttribute("selected-by", pop.get(i).getLogNotes());
            Element e = doc.createElement("chromosome");
            e.appendChild(doc.createTextNode(pop.get(i).toString()));
            ind.appendChild(e);
            e = doc.createElement("fitness");
            e.appendChild(doc.createTextNode(Double.toString(pop.get(i).getFitnessValue())));
            ind.appendChild(e);
            e = doc.createElement("objective");
            e.appendChild(doc.createTextNode(Double.toString(pop.get(i).getObjectiveValue())));
            ind.appendChild(e);
            mateEle.appendChild(ind);
        }
    }

    public static void logNewOperator(String operatorName) {
        opEle = doc.createElement("operator");
        opEle.setAttribute("name", operatorName);
        genEle.appendChild(opEle);
    }

    public static void logOffspring(Population pop) {
        logPopulation(pop, opEle);
    }

    public static void logNewEnvironmentalSelection() {
        mateEle = doc.createElement("environmental-selection");
        genEle.appendChild(mateEle);
    }

    public static void writeLog() {

        try {
            Transformer tr = TransformerFactory.newInstance().newTransformer();
            tr.setOutputProperty(OutputKeys.INDENT, "yes");
            tr.setOutputProperty(OutputKeys.METHOD, "xml");
            tr.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            tr.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");

            ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(fileName + ".zip"));
            zos.putNextEntry(new ZipEntry(fileName));
            tr.transform(new DOMSource(doc), new StreamResult(zos));
            zos.close();

        } catch (TransformerException te) {
            System.out.println(te.getMessage());
        } catch (IOException ioe) {
            System.out.println(ioe.getMessage());
        }
    }

}
