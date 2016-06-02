package com.zpi.plagiarism_detector.server.detector;

import com.zpi.plagiarism_detector.commons.protocol.plagiarism.PlagiarismResult;
import com.zpi.plagiarism_detector.server.articles.ArticleComparison;
import com.zpi.plagiarism_detector.server.sourcecode.SourceCodeComparison;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class ComparingAlgorithm {
    private ArticleComparison articleComparison;

    public ComparingAlgorithm(ArticleComparison articleComparison) {
        this.articleComparison = articleComparison;
    }

    public List<PlagiarismResult> determineArticlePlagiarism(String articlePath, Set<String> matchingArticlesPaths) {
        List<PlagiarismResult> overalResults = new LinkedList<>();
        for (String matchingCodePath : matchingArticlesPaths) {
            PlagiarismResult plagiarismResult = articleComparison.compare(articlePath, matchingCodePath);
            if (plagiarismResult != null){
            	overalResults.add(plagiarismResult);
            }
        }
        return overalResults;
    }

}
