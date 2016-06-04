package pl.edu.agh.kiro.latex_plagiat.server.articles;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pl.edu.agh.kiro.latex_plagiat.commons.protocol.plagiarism.PlagiarismResult;

public class ArticleComparison {
	private final static Logger log = LoggerFactory.getLogger(ArticleComparison.class);

    private CompareEngine compareEngine;


    public ArticleComparison(CompareEngine compareEngine) {
        this.compareEngine = compareEngine;
    }

    public PlagiarismResult compare(String patternPath, String textPath) {
        try {
            return compareEngine.compare(patternPath, textPath);
        } catch (IOException e) {
        	log.warn(patternPath+ " or " + textPath + " does not exist");
        }
        return null;
    }
}
