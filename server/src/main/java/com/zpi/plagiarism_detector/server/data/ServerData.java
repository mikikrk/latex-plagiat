package com.zpi.plagiarism_detector.server.data;

import com.zpi.plagiarism_detector.commons.database.DocumentType;
import com.zpi.plagiarism_detector.commons.protocol.DocumentData;
import com.zpi.plagiarism_detector.server.ServerProperties;
import com.zpi.plagiarism_detector.server.database.Article;
import com.zpi.plagiarism_detector.server.database.Dao;

import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.util.List;
import java.util.Set;

public class ServerData {
    private Dao dao;
    private FileData fileData;

    private String path;

    private String title;
    private String articleText;
    private Set<String> keywords;
    private Set<String> codes;

    public ServerData(Dao dao, FileData fileData) {
        this.dao = dao;
        this.fileData = fileData;
    }

    /**
     * Zapisuje dokumenty w odpowiednie miejsca w drzewie katalogów
     *
     * @param docs dokumenty do zapisania
     */
    public void saveDocuments(List<DocumentData> docs, List<String> codesPaths) throws IOException {
        for (DocumentData document : docs) {
            saveDocument(document, codesPaths);
        }
    }

    /**
     * Zapisuje dokument w odpowiednim miejscu w drzewie katalogów
     *
     * @param document   dokument do zapisania
     * @param codesPaths
     */
    public String saveDocument(DocumentData document, List<String> codesPaths) throws IOException {
        String articlePath = null;
        extractFields(document);
        createArticleDirectory();
        if (articleText != null) {
            articlePath = saveArticle();
        }
        if (codes != null && !codes.isEmpty()) {
            saveArticleCodes(codesPaths);
        }
        return articlePath;
    }

    private void extractFields(DocumentData document) {
        title = document.getTitle();
        keywords = document.getKeywords();
        articleText = document.getArticle();
        codes = document.getCodes();
        path = getDirectoryPath();
    }

    private String getDirectoryPath() {
        return ServerProperties.DOCS_PATH + "/" + title.replaceAll("\\s", "");
    }

    private void createArticleDirectory() throws IOException {
        String dirPath = path;
        int postfix = 1;
        boolean created = false;

        do {
            try {
                fileData.createDir(dirPath);
                created = true;
            } catch (FileAlreadyExistsException e) {
                dirPath = path + postfix;
                ++postfix;
            }
        } while (created == false);

        path = dirPath;
    }

    private String saveArticle() throws IOException {
        String articlePath = getArticlePath();
        saveDocumentReferenceInDatabase(articlePath, DocumentType.TEXT);
        saveDocumentFile(articlePath, articleText);
        return articlePath;
    }

    private String getArticlePath() {
        return path + "/article";
    }

    private void saveDocumentReferenceInDatabase(String documentPath, DocumentType documentType) {
        Article newDocument = new Article(documentPath, documentType);
        newDocument.setTitle(title);
        dao.addArticle(newDocument, keywords);
    }

    private void saveDocumentFile(String articlePath, String articleText) throws IOException {
        fileData.writeToFile(articlePath, articleText);
    }

    private void saveArticleCodes(List<String> codesPaths) throws IOException {
        int i = 1;
        for (String code : codes) {
            String codePath = getCodePath(i);
            codesPaths.add(codePath);
            saveDocumentReferenceInDatabase(codePath, DocumentType.CODE);
            saveDocumentFile(codePath, code);
            ++i;
        }
    }

    private String getCodePath(int i) {
        return path + "/code" + i;
    }

    public Set<String> getCommonKeywordDocumentsPaths(Set<String> keywords, DocumentType documentType) {
        Set<String> commonArticlesPaths = dao.findArticlesWithAtLeastOne(keywords, documentType);
        return commonArticlesPaths;
    }

   
}
