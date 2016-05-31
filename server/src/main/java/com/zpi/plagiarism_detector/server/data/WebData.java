package com.zpi.plagiarism_detector.server.data;

import com.zpi.plagiarism_detector.commons.protocol.DocumentData;
import com.zpi.plagiarism_detector.server.websearch.WebsiteAnalyze;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class WebData {
    private WebsiteAnalyze websiteAnalyze;

    public WebData(WebsiteAnalyze websiteAnalyze) {
        this.websiteAnalyze = websiteAnalyze;
    }

    public List<DocumentData> searchDocuments(Set<String> keywords) {
        List<DocumentData> ret = websiteAnalyze.analyze(keywords);
        return ret;
    }

}
