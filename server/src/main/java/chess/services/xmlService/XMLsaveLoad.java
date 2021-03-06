package chess.services.xmlService;

import chess.Constants;
import chess.ServerMain;
import chess.model.Player;
import chess.model.Status;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * <code>XMLsaveLoad</code> class saves players to the file
 * periodically (after game is over) and restores players
 * from the file (when server launches).
 */
public class XMLsaveLoad {
    private static File filePlayers = new File(System.getProperty("user.dir"), "savedPlayers.xml");
    private static File filebannedIP = new File(System.getProperty("user.dir"), "bannedIP.xml");
    private static File filesettings = new File(System.getProperty("user.dir"), "adminsettings.xml");
    private static final DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
    private final static Logger logger = Logger.getLogger(XMLsaveLoad.class);

    public static void savePlayers() throws ParserConfigurationException, TransformerException {

        DocumentBuilder db = dbf.newDocumentBuilder();
        Document doc = db.newDocument();
        Element root = doc.createElement("savedPlayers");
        doc.appendChild(root);
        for (Player player : ServerMain.allPlayers) {
            Element el = doc.createElement("player");
            el.setAttribute("id", String.valueOf(player.getId()));
            root.appendChild(el);
            Element login = doc.createElement("login");
            login.appendChild(doc.createTextNode(player.getLogin()));
            el.appendChild(login);
            Element password = doc.createElement("password");
            password.appendChild(doc.createTextNode(player.getPassword()));
            el.appendChild(password);
            Element rank = doc.createElement("rank");
            rank.appendChild(doc.createTextNode(String.valueOf(player.getRank())));
            el.appendChild(rank);
            Element status = doc.createElement("status");
            status.appendChild(doc.createTextNode(String.valueOf(player.getStatus())));
            el.appendChild(status);
            Element ipadress = doc.createElement("ipadress");
            ipadress.appendChild(doc.createTextNode(player.getIpadress()));
            el.appendChild(ipadress);
        }

        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        DOMSource source = new DOMSource(doc);
        StreamResult result = new StreamResult(filePlayers);
        transformer.transform(source, result);
    }

    public static void loadPlayers() throws ParserConfigurationException, IOException, SAXException, TransformerException {
        if(!filePlayers.exists()) {
            logger.warn("file with saved players not found. New empty file created");
            savePlayers();
        }
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document doc = db.parse(filePlayers);
        doc.getDocumentElement().normalize();
        Element element = doc.getDocumentElement();
        NodeList nodes = element.getChildNodes();

        for (int i = 0; i < nodes.getLength(); i++) {
            if ("player".equals(nodes.item(i).getNodeName())) {
                Element el = (Element) nodes.item(i);
                int id = Integer.parseInt(el.getAttribute("id"));
                String login = el.getElementsByTagName("login").item(0).getTextContent();
                String password = el.getElementsByTagName("password").item(0).getTextContent();
                int rank = Integer.parseInt(el.getElementsByTagName("rank").item(0).getTextContent());
                Status status = Status.valueOf(el.getElementsByTagName("status").item(0).getTextContent());
                String ipadress = el.getElementsByTagName("ipadress").item(0).getTextContent();
                Player player = new Player(login, password, status, ipadress);
                player.setRank(rank);
                player.setId(id);
                ServerMain.allPlayers.add(player);
            }
        }

        if (ServerMain.allPlayers.size() > 0) {
            ServerMain.setAllPlayers(ServerMain.allPlayers);
        } else {
            logger.info("No players read from file");
        }
    }

    public static void saveBanned() throws ParserConfigurationException, FileNotFoundException, TransformerException {
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document doc = db.newDocument();
        Element root = doc.createElement("bannedIP");
        doc.appendChild(root);
        for (String s : ServerMain.bannedIP) {
            Element el = doc.createElement("address");
            el.setAttribute("ip", s);
            root.appendChild(el);
        }

        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        DOMSource source = new DOMSource(doc);
        StreamResult result = new StreamResult(filebannedIP);
        transformer.transform(source, result);
    }

    public static void loadBanned() throws ParserConfigurationException, IOException, SAXException, TransformerException {
        if(!filebannedIP.exists()) {
            saveBanned();
        }
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document doc = db.parse(filebannedIP);
        doc.getDocumentElement().normalize();
        Element element = doc.getDocumentElement();
        NodeList nodes = element.getChildNodes();

        for (int i = 0; i < nodes.getLength(); i++) {
            if ("address".equals(nodes.item(i).getNodeName())) {
                Element el = (Element) nodes.item(i);
                String ip = el.getAttribute("ip");
                ServerMain.bannedIP.add(ip);
            }
        }

        if (ServerMain.allPlayers.size() > 0) {
            ServerMain.setAllPlayers(ServerMain.allPlayers);
        } else {
            logger.info("No banned ip read from file");
        }
    }
    public static void loadSettings() throws ParserConfigurationException, IOException, SAXException, TransformerException {
        if(!filesettings.exists()) {
            logger.warn("file with settings not found. New empty file created");
            saveSettings();
        }
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document doc = db.parse(filesettings);
        doc.getDocumentElement().normalize();
        Element element = doc.getDocumentElement();
        NodeList nodes = element.getChildNodes();
        ServerMain.loginAdmin = element.getElementsByTagName("login").item(0).getTextContent();
        ServerMain.passwordAdmin = element.getElementsByTagName("password").item(0).getTextContent();
        ServerMain.serverPort = Integer.parseInt(element.getElementsByTagName("port").item(0).getTextContent());
    }
    public static void saveSettings() throws ParserConfigurationException, FileNotFoundException, TransformerException {
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document doc = db.newDocument();
        Element root = doc.createElement("root");
        Element login = doc.createElement("login");
        login.appendChild(doc.createTextNode(Constants.ADMIN_NAME));
        root.appendChild(login);
        Element password = doc.createElement("password");
        password.appendChild(doc.createTextNode(Constants.ADMIN_PASS));
        root.appendChild(password);
        Element port = doc.createElement("port");
        port.appendChild(doc.createTextNode(String.valueOf(Constants.PORT)));
        root.appendChild(port);
        doc.appendChild(root);
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        DOMSource source = new DOMSource(doc);
        StreamResult result = new StreamResult(filesettings);
        transformer.transform(source, result);
    }

}
