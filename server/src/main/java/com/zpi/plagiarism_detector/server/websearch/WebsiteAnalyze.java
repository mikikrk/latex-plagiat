package com.zpi.plagiarism_detector.server.websearch;

import com.google.gson.Gson;
import com.snowtide.PDF;
import com.snowtide.pdf.OutputTarget;
import com.zpi.plagiarism_detector.commons.protocol.DocumentData;
import com.zpi.plagiarism_detector.server.ServerProperties;
import com.zpi.plagiarism_detector.server.database.Dao;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class WebsiteAnalyze implements WebsiteAnalyzeInterface {
    private Dao dao;
    
    @Override
    public List<DocumentData> analyze(Set<String> keywords) {
        List<DocumentData> ret = new ArrayList<>();
        try {
            ret = webAnalyze(keywords);
        } catch (Exception ex) {}
        return ret;
    }
    
    public WebsiteAnalyze(Dao dao) {
        this.dao = dao;
    }

    private List<DocumentData> webAnalyze(Set<String> keywords) throws IOException {
        
        String keyword = StringUtils.join(keywords, " ");
        String[] linksArray = googleSearch(keyword);
        List<DocumentData> sr = new ArrayList<>();
        
        for (int i = 0; i < linksArray.length; i++) {

            /**
             * Sprawdzanie czy dany link jest juz w bazie.
             */
            if (dao.containsUri(linksArray[i])) {
                dao.addKeywordsToUri(linksArray[i], keywords); //dodanie slowa kluczowego jesli dany url jest ju� w bazie, ale szukany keyword si� tam nie znajduje.
            }else{

                String fileName = null;
                String dirName = ServerProperties.DOCS_PATH;
                URL url = new URL(linksArray[i]);
                URLConnection connection = url.openConnection();
                connection.connect();
                String contentType = "n/a";
                if (connection.getContentType() != null) {
                    contentType = connection.getContentType();
                }

                /**
                 * Jesli link jest pdfem to zwracany jest wraz 
                 * z innymi parametrami z uzyciem klasy SearchResult.
                 */
                if (contentType.contains("application/pdf")) {
                    DocumentData serres = new DocumentData();
                    Set<String> codesArray = new HashSet<>();
                    (new File(dirName)).mkdirs(); //tworzenie folderu jesli jeszcze go nie ma
                    fileName = "Article from PDF (keyword - " + keyword + "; url nr - " + i + ").pdf";
                    String full = dirName + "\\" + fileName;
                    saveFileFromUrl(full, linksArray[i]); //tymczasowy zapis pliku pdf na dysk

                    com.snowtide.pdf.Document pdf = PDF.open(full);
                    StringBuilder text = new StringBuilder(1024);
                    pdf.pipe(new OutputTarget(text)); //wyciaganie tekstu z pliku pdf
                    String pdfTitle;
                    if (pdf.getAttribute("ATTR_TITLE") == null) {
                        pdfTitle = linksArray[i];
                    } else pdfTitle = pdf.getAttribute("ATTR_TITLE").toString();
                    pdf.close();
                    String text2 = text.toString();
                    File file = new File(dirName + "\\" + fileName);
                    file.delete(); //usuniecie pliku pdf
                    serres.setArticle(text2);
                    serres.setUrl(linksArray[i]);
                    serres.setTitle(pdfTitle);
                    serres.setKeywords(keywords);
                    codesArray.add("n/a");
                    serres.setCodes(codesArray);
                    sr.add(serres);
                }

                /**
                 * Jesli wyszukana strona ma artykul (np. jako abstract) jest on 
                 * zwracany z uzyciem klasy DocumentData wraz z pozostalymi parametrami.
                 */
                else if (contentType.contains("text/html")) {
                    DocumentData serres = new DocumentData();
                    Set<String> codesArray = new HashSet<>();
                    String siteTitle;
                    org.jsoup.Connection con = Jsoup.connect(linksArray[i]).ignoreHttpErrors(true).userAgent("Mozilla"); //przygotowanie pliku html do analizy
                    //org.jsoup.Connection.Response resp = con.execute();
                    org.jsoup.nodes.Document doc = null;
                    if (con.execute().statusCode() == 200) {
                        doc = con.get();
                        siteTitle = doc.title();
                        String articleText = null;
                        boolean check2 = false;

                        /**
                         * Tu wpisujemy wybrane tagi lub klasy CSS.
                         * Klasy zaczynaja sie ".", id "#", a tagi to sama nazwa. 
                         */
                        Elements texts = doc.select("p, div");

                        for (org.jsoup.nodes.Element text : texts) {
                            articleText += text.text();

                            /**
                             * Sprawdzanie czy artykul jest wystarczajaco dlugi.
                             */
                            if (articleText.length() > 500) {
                                check2 = true;    
                            }
                        }
                        
                        String codeText = null;
                        Elements codes = doc.select("pre, code, prettyprint, brush"); //tu wpisujemy tagi lub klasy CSS zawierajace w htmlu kod 
                        for (org.jsoup.nodes.Element code : codes) {
                            codeText = code.text();

                            /**
                             * Sprawdzanie czy kod ma minimum dwie linijki i czy jest
                             * wystarczajaco dlugi.
                             */
                            if (codeText.contains("\n") && codeText.length() > 150) {
                                codesArray.add(codeText);
                            }
                        }
                        if (articleText != null && check2) {
                                serres.setArticle(articleText);
                                serres.setUrl(linksArray[i]);
                                serres.setTitle(siteTitle);
                                serres.setKeywords(keywords);
                                serres.setCodes(codesArray);
                                sr.add(serres);
                        }
                    }
                }
            }
        }
        return sr;
    }

    public static void saveFileFromUrl(String fileName, String fileUrl) throws MalformedURLException, IOException {
        FileUtils.copyURLToFile(new URL(fileUrl), new File(fileName));
    }

    public String[] googleSearch(String keywords) throws IOException {
        
        int numberOfResults = ServerProperties.NUMBER_OF_RESULTS;
        String[] urls = new String[numberOfResults];
        String cx = "003774498586208713767:hnxd8xpprx4"; //klucz cx google przypisany do mojego konta
        String key = "AIzaSyCT9MHJyBqmOuplt9-Rfzz2y0vDB-1xnHI"; //klucz api google - max 100 wyszukan dziennie
        String charset = "UTF-8";

        /**
         * Uzycie petli poniewaz za pomoca Google Custom Search uzyskujemy
         * max 10 wynikow na kazde wywolanie. 
         */
        for (int i = 0; i < numberOfResults; i = i + 10) {

            int ii = i + 1;
            String queryArguments = key + "&cx=" + cx + "&q=" + keywords + "&alt=json" + "&start=" + ii; //tworzenie adresu Google Custom Search

            /**
             * Zamiana blednych znakow powstalych w koncowej czesci adresu
             * przy kodowaniu URL. 
             */
            String addition = URLEncoder.encode(queryArguments, "UTF-8")
                    .replaceAll("\\%28", "(")
                    .replaceAll("\\%29", ")")
                    .replaceAll("\\+", "%20")
                    .replaceAll("\\%27", "'")
                    .replaceAll("\\%21", "!")
                    .replaceAll("\\%7E", "~")
                    .replaceAll("\\%26", "&")
                    .replaceAll("\\%3D", "=")
                    .replaceAll("\\%3A", ":");

            String url = "https://www.googleapis.com/customsearch/v1?key=";
            String total = url + addition;
            URL url2 = new URL(total);

            Reader reader = new InputStreamReader(url2.openStream(), charset);
            GoogleResults results = new Gson().fromJson(reader, GoogleResults.class); //konwersja Json do Javy

            if (results.getThing(0) != null) {

                /**
                 * Zmniejszanie ilosci wynikow jesli zadana liczba nie jest wielokrotnoscia 10
                 */
                if ((numberOfResults - 10) < i && numberOfResults % 10 != 0) {
                    int i2 = numberOfResults % 10;
                    for (int m = 0; m < i2; m++) {
                        String urlFound = results.getThing(m).link;
                        urls[i + m] = results.getThing(m).link;
                    }
                } else {
                    for (int m = 0; m < 10; m++) {
                        String urlFound = results.getThing(m).link;
                        urls[i + m] = results.getThing(m).link;
                    }
                }

            } else
                System.out.println("No results");
        }
        return urls;
    }    

}