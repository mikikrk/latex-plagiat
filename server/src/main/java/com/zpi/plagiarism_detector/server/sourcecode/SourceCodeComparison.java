package com.zpi.plagiarism_detector.server.sourcecode;

import java.io.File;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zpi.plagiarism_detector.commons.protocol.plagiarism.PlagiarismResult;
import com.zpi.plagiarism_detector.server.articles.FileLoader;

public class SourceCodeComparison {
	
	private final static Logger log = LoggerFactory.getLogger(SourceCodeComparison.class);
	
    private String language;
    private SourceCode sc;

    public SourceCodeComparison() {
        this.language = null;
    }

    public void getOriginalLanguage(String path)  throws Exception{

        this.sc = new SourceCode(20);
        this.language = sc.recognizeLanguage(path);
        this.sc.setLanguageString(this.language);
    }

    /**
     * Metoda por�wnuj�ca dwa teksty
     *
     * @param pathNew   - sciezka do pliku od uzytkownika
     * @param pathExist - sciezka do istniejacego pliku
     * @return PlagiarismResult w przypadku sukcesu(puste PlagiarismFragment gdy nic nie znalazlo)
     * null jesli podany plik jest w innym jezyku
     * @throws IOException
     */
    public PlagiarismResult compareFiles(String pathNew, String pathExist) throws IOException {

    	PlagiarismResult result = null;
    	try{
	        getOriginalLanguage(pathNew);
	        this.sc.init();
	
	        File userFile = new File(pathNew);
	        File foundFile = new File(pathExist);
	        if (userFile.exists() && foundFile.exists()){
		        //dodanie pliku uzytkownika jako pierwszego
		        this.sc.add(userFile);
		        try {
		            //dodanie pliku do porownania
		            this.sc.add(foundFile);
		        } catch (net.sourceforge.pmd.lang.ast.TokenMgrError e) {
		            //inny kod zrodlowy programu
		            return null;
		        }
		        //uruchomienie porownania
		        this.sc.go();
		        
		        result = sc.convertXml(sc.getMatches(), pathNew);
		        if (result != null){
			        FileLoader fl = new FileLoader(pathNew, pathExist);
			        fl.loadFiles();
			        result.setNewDocument(fl.getPattern());
			        result.setExistingDocument(fl.getText());
		        }
	        }else{
	        	log.warn(pathNew + " or " + pathExist + " does not exist");
	        }
    	}catch(Exception e){
    		log.warn("Error while detecting language of " + pathNew + " and " + pathExist);
    	}

        return result;
    }
}
