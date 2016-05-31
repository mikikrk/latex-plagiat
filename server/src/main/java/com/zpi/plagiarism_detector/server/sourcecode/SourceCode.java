package com.zpi.plagiarism_detector.server.sourcecode;

import com.zpi.plagiarism_detector.commons.database.DocumentType;
import com.zpi.plagiarism_detector.commons.protocol.plagiarism.PlagiarismFragment;
import com.zpi.plagiarism_detector.commons.protocol.plagiarism.PlagiarismResult;
import net.sourceforge.pmd.cpd.*;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;
import java.io.*;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class SourceCode {

    private String[] supportedLanguages = new String[]{"java", "jsp", "cpp", "c", "php", "ruby", "cs", "plsql"};
    private CPDConfiguration conf;
    private CPD cPD;
    private Language language;
    private Renderer renderer;
    private int minimumTokens = 20;
    private String languageString = "java";
    private String formatString = "xml";
    private String encodingString = System.getProperty("file.encoding");
    private boolean ignoreAnnotations = true;
    private boolean ignoreIdentifiers = true;
    private boolean ignoreLiterals = true;

    public SourceCode(int minimumTokens) {

        this.conf = new CPDConfiguration();

        this.minimumTokens = minimumTokens;

    }

    public String getLanguageString() {
        return languageString;
    }

    public void setLanguageString(String languageString) {

        this.languageString = languageString;
    }

    public int getMinimumTokens() {
        return minimumTokens;
    }

    /**
     * Minimalna ilosc tokenow, ktore musza sie powtorzyc
     *
     * @param minimumTokens
     */
    public void setMinimumTokens(int minimumTokens) {
        this.minimumTokens = minimumTokens;
    }

    public boolean isIgnoreAnnotations() {
        return ignoreAnnotations;
    }

    /**
     * Ignorowanie annotacji
     *
     * @param ignoreAnnotations
     */
    public void setIgnoreAnnotations(boolean ignoreAnnotations) {
        this.ignoreAnnotations = ignoreAnnotations;
    }

    public boolean isIgnoreIdentifiers() {
        return ignoreIdentifiers;
    }

    /**
     * Ignorowanie identyfikatorow
     *
     * @param ignoreIdentifiers
     */
    public void setIgnoreIdentifiers(boolean ignoreIdentifiers) {
        this.ignoreIdentifiers = ignoreIdentifiers;
    }

    public boolean isIgnoreLiterals() {
        return ignoreLiterals;
    }

    /**
     * Ignorowanie literalow
     *
     * @param ignoreLiterals
     */
    public void setIgnoreLiterals(boolean ignoreLiterals) {
        this.ignoreLiterals = ignoreLiterals;
    }

    /**
     * Inicjalizacja konfiguracji
     */
    public void init() {
        this.language = conf.getLanguageFromString(languageString);
        conf.setMinimumTileSize(this.minimumTokens);
        conf.setLanguage(this.language);
        conf.setIgnoreAnnotations(this.ignoreAnnotations);
        conf.setIgnoreIdentifiers(this.ignoreIdentifiers);
        conf.setIgnoreLiterals(this.ignoreLiterals);
        conf.setEncoding(this.encodingString);

        CPDConfiguration.setSystemProperties(conf);
        this.cPD = new CPD(conf);

        this.renderer = new XMLRenderer(encodingString);

    }

    /**
     * Dodanie plikow do porownania
     *
     * @param path - sciezka do katalogu
     * @throws IOException
     */
    public void addRecursively(String path) throws IOException {
        this.cPD.addRecursively(path);
    }

    /**
     * Dodanie plikow do porownania
     *
     * @param file
     * @throws IOException
     */
    public void add(File file) throws IOException {

        this.cPD.add(file);
    }

    /**
     * Uruchomienie procesu porownywania plikow
     */
    public void go() {

        this.cPD.go();
    }

    /**
     * Funkcja zwraca duplikaty kodu wplikach w formacie xml
     * <pmd-cpd>
     * <duplication lines="" tokens="">
     * <file line="" path=""/>
     * <codefragment>
     * </codefragment>
     * </duplication>
     * </pmd-cpd>
     *
     * @return xml
     */
    public String getMatches() {

        return renderer.render(cPD.getMatches());
    }

    /**
     * Funkcja zwraca duplikaty kodu w plikach w formacie xml, uwzgledniajac tylko podany plik
     *
     * @param filename - nazwa pliku
     * @return xml
     */
    public String getMatches(String filename) {

        String xml = renderer.render(cPD.getMatches());
        String output = null;
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            InputSource is = new InputSource(new StringReader(xml));

            Document doc = builder.parse(is);

            XPathFactory xpf = XPathFactory.newInstance();
            XPath xpath = xpf.newXPath();
            XPathExpression expression = xpath.compile("/pmd-cpd/duplication/file[@path='" + filename + "']");
            NodeList nodeList = (NodeList) expression.evaluate(doc, XPathConstants.NODESET);

            Document newDocument = builder.newDocument();
            Element rootElement = newDocument.createElement("pmd-cpd");
            newDocument.appendChild(rootElement);

            for (int i = 0; i < nodeList.getLength(); i++) {
                Node newNode = nodeList.item(i).getParentNode().cloneNode(true);
                newDocument.adoptNode(newNode);
                newDocument.getDocumentElement().appendChild(newNode);
            }


            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();

            StringWriter writer = new StringWriter();
            transformer.transform(new DOMSource(newDocument), new StreamResult(writer));
            output = writer.getBuffer().toString();


        } catch (Exception e) {
            e.printStackTrace();
        }
        return output;

    }

    /**
     * Zwrocenie obiektu porownania
     *
     * @param xml      - xml zwrocony z porownania
     * @param filename - sciezka do pliku od uzytkownika
     * @return PlagiarismResult
     */
    public PlagiarismResult convertXml(String xml, String filename) {

        PlagiarismResult result = null;

        //mapa fragmentow
        Map<PlagiarismFragment, PlagiarismFragment> plagiarisedFragments = new HashMap<PlagiarismFragment, PlagiarismFragment>();

        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            InputSource is = new InputSource(new StringReader(xml));

            Document doc = builder.parse(is);

            XPathFactory xpf = XPathFactory.newInstance();
            XPath xpath = xpf.newXPath();
            XPathExpression expression = xpath.compile("/pmd-cpd/duplication");
            NodeList duplications = (NodeList) expression.evaluate(doc, XPathConstants.NODESET);

            for (int i = 0; i < duplications.getLength(); i++) {

                int size = Integer.parseInt(duplications.item(i).getAttributes().getNamedItem("lines").getNodeValue());
                String fragment1 = "";
                String fragment2 = "";
                int beginNew = 0;
                int countNew = 0;
                int beginExisting = 0;
                int countExisting = 0;

                NodeList childs = duplications.item(i).getChildNodes();
                for (int j = 0; j < childs.getLength(); j++) {
                    if (childs.item(j).getNodeName() == "file") {

                        Node file = childs.item(j);
                        String path = file.getAttributes().getNamedItem("path").getNodeValue();

                        //jesli to plik uzytkownika
                        if (path.compareTo(filename) == 0) {

                            int bN = Integer.parseInt(file.getAttributes().getNamedItem("line").getNodeValue());

                            //odczytanie zdublowanych linni z pliku
                            FileInputStream fs = new FileInputStream(filename);
                            BufferedReader br = new BufferedReader(new InputStreamReader(fs));


                            for (int k = 1; k < bN; ++k) {
                                String line = br.readLine();
                                beginNew += line.length() + System.getProperty("line.separator").length();
                            }
                            for (int k = 1; k <= size; ++k) {
                                String line = br.readLine();
                                countNew += line.length() + System.getProperty("line.separator").length();
                                if (line != null)
                                    fragment1 += line + System.getProperty("line.separator");
                            }
                        } else {
                            //zdalny plik
                            int bE = Integer.parseInt(file.getAttributes().getNamedItem("line").getNodeValue());

                            //odczytanie zdublowanych linni z pliku
                            FileInputStream fs = new FileInputStream(path);
                            BufferedReader br = new BufferedReader(new InputStreamReader(fs));


                            for (int k = 1; k < bE; ++k) {
                                String line = br.readLine();
                                beginExisting += line.length() + 1;
                            }
                            for (int k = 1; k <= size; ++k) {
                                String line = br.readLine();
                                countExisting += line.length() + 1;
                                if (line != null)
                                    fragment2 += line + System.getProperty("line.separator");
                            }

                        }

                    }
                }
                plagiarisedFragments.put(new PlagiarismFragment(fragment1, beginNew, beginNew + countNew - 1, countNew - 1),
                        new PlagiarismFragment(fragment2, beginExisting, beginExisting + countExisting - 1, countExisting - 1)
                );
            }


        } catch (Exception e) {
            e.printStackTrace();
        }

        if (plagiarisedFragments.size()>0){
	        result = new PlagiarismResult();
	        result.setType(DocumentType.CODE);
	        
	        result.setPlagiarisedFragments(plagiarisedFragments);
        }

        return result;
    }

    /**
     * Odczyt pliku do Stringa
     *
     * @param path     - nazwa pliku
     * @param encoding - kodowanie
     * @return file content
     */
    public String readFile(String path, Charset encoding) throws IOException {
        byte[] encoded = Files.readAllBytes(Paths.get(path));
        return new String(encoded, encoding);
    }

    /**
     * Funkcja zwraca nazwe rozpoznanego jezyka dla fragmentu kodu
     *
     * @param filename - nazwa pliku
     * @return language name
     */
    public String recognizeLanguage(String filename) throws Exception{

        ScriptEngine engine = new ScriptEngineManager().getEngineByName("nashorn");
        Invocable invocable = (Invocable) engine;
        String example;
        Object result = null;
	        InputStream resourceAsStream = getClass().getClassLoader().getResourceAsStream("highlight.pack.js");
	        InputStreamReader inputStreamReader = new InputStreamReader(resourceAsStream);
	        engine.eval(inputStreamReader);
        
//            engine.eval(new FileReader(url.getPath()));
            example = readFile(filename, Charset.defaultCharset());
            result = invocable.invokeFunction("fun1", example);
        //zwrocone jezyki
        String[] split = ((String) result).split("\n");

        String[] lang1 = ((String) split[0]).split(":");
        String[] lang2 = ((String) split[1]).split(":");

        //mapowanie jezykow
        if (lang1[0].equals("javascript")) {
            lang1[0] = "jsp";
        } else if (lang1[0].equals("sql")) {
            lang1[0] = "plsql";
        }

        return lang1[0];

    }

}



