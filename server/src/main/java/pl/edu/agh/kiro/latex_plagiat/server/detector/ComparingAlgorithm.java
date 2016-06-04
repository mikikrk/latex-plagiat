package pl.edu.agh.kiro.latex_plagiat.server.detector;

import pl.edu.agh.kiro.latex_plagiat.commons.protocol.plagiarism.PlagiarismResult;
import pl.edu.agh.kiro.latex_plagiat.server.articles.ArticleComparison;

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
