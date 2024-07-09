package it.unipd.dei.webapp.utils;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.message.StringFormatterMessageFactory;

/**
 * Load queries from the query.xml file.
 */
public class QueryLoader {

    protected static final Logger LOGGER = LogManager.getLogger(QueryLoader.class,
            StringFormatterMessageFactory.INSTANCE);

    /**
     * Build and access the user data access page.
     * 
     * @param queryName
     *                  the string representing the name of the query to be loaded.
     * 
     * @throws IOException
     *                     if any error occurs while loading the queries.
     */
    public String loadQuery(String queryName) {

        try {

            // Get the base path of the application
            /// usr/share/tomcat10 /

            String filePath = findQueryXMLPath();

            // add servletContext
            // String filePath = servletContext.getRealPath("/WEB-INF/classes/queries.xml");

            File inputFile = new File(filePath);

            // LOGGER.info("path: %s", inputFile.getAbsolutePath());

            // Check if the file exists
            if (!inputFile.exists()) {
                return "Error: queries.xml file not found at specified location";
            }

            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(inputFile);
            doc.getDocumentElement().normalize();
            NodeList nodeList = doc.getElementsByTagName("query");
            for (int i = 0; i < nodeList.getLength(); i++) {
                Element element = (Element) nodeList.item(i);
                String name = element.getAttribute("name");
                if (name.equals(queryName)) {
                    return element.getElementsByTagName("statement").item(0).getTextContent();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Find the path to the xml file containing the queries (queries.xml).
     *
     */

    private String findQueryXMLPath() {
        String path = System.getProperty("catalina.base") + "/webapps/";
        path = path.replace('/', File.separatorChar);
        File webappsDir = new File(path);

        String[] directories = webappsDir.list();
        for (String dir : directories) {
            // LOGGER.info("\n\n"+dir+"\n\n");
            if (new File(path + dir).isDirectory() && dir.startsWith("dinner-dilemma")) {
                path = path + dir + "/WEB-INF/classes/queries.xml";
                break;
            }
        }

        return path.replace('/', File.separatorChar);
    }
}
