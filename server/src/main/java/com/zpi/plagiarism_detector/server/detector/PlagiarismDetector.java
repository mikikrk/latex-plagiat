package com.zpi.plagiarism_detector.server.detector;

import com.zpi.plagiarism_detector.commons.database.DocumentType;
import com.zpi.plagiarism_detector.commons.protocol.DocumentData;
import com.zpi.plagiarism_detector.commons.protocol.plagiarism.PlagiarismDetectionResult;
import com.zpi.plagiarism_detector.commons.protocol.plagiarism.PlagiarismResult;
import com.zpi.plagiarism_detector.server.data.ServerData;

import java.io.IOException;
import java.util.*;

public class PlagiarismDetector {
    private ServerData serverData;
    private ComparingAlgorithm comparingAlgorithm;

    private DocumentData analyzedDocument;
    private Set<String> keywords;
    private String articlePath;
    private ArrayList<String> codesPaths;

    public PlagiarismDetector(ServerData serverData, ComparingAlgorithm comparingAlgorithm) {
        this.serverData = serverData;
        this.comparingAlgorithm = comparingAlgorithm;
    }

    public PlagiarismDetectionResult checkForPlagiarism(DocumentData document) throws IOException {
        extractData(document);
        saveCheckedDocument();

        List<PlagiarismResult> results = new ArrayList<>();
        if (articlePath != null) {
            List<PlagiarismResult> articleResults = checkArticle();
            results.addAll(articleResults);
        }

        PlagiarismDetectionResult result = new PlagiarismDetectionResult(results);
        return result;
    }

    private void extractData(DocumentData document) {
        this.analyzedDocument = document;
        this.keywords = document.getKeywords();
    }

    private void saveCheckedDocument() throws IOException {
        int codesCount = analyzedDocument.getCodesCount();
        codesPaths = new ArrayList<>(codesCount);
        articlePath = serverData.saveDocument(analyzedDocument, codesPaths);
    }

    private List<PlagiarismResult> checkArticle() {
        Set<String> matchingDocsPaths = getSimilarDocumentsPaths();
        List<PlagiarismResult> plagiarisms = determineArticlePlagiarism(matchingDocsPaths);
        return plagiarisms;
    }

    private Set<String> getSimilarDocumentsPaths() {
        Set<String> commonKeywordDocumentsPaths = getCommonKeywordDocumentsPaths(keywords, DocumentType.TEXT);
        commonKeywordDocumentsPaths.remove(articlePath);
        return commonKeywordDocumentsPaths;
    }

    private Set<String> getCommonKeywordDocumentsPaths(Set<String> kw, DocumentType type) {
        return serverData.getCommonKeywordDocumentsPaths(kw, type);
    }

    private List<PlagiarismResult> determineArticlePlagiarism(Set<String> matchingDocsPath) {
        return comparingAlgorithm.determineArticlePlagiarism(articlePath, matchingDocsPath);
    }

}
