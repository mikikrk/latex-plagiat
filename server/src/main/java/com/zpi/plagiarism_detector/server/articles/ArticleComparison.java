package com.zpi.plagiarism_detector.server.articles;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zpi.plagiarism_detector.commons.protocol.plagiarism.PlagiarismResult;
import com.zpi.plagiarism_detector.server.sourcecode.SourceCodeComparison;

public class ArticleComparison {
	private final static Logger log = LoggerFactory.getLogger(SourceCodeComparison.class);

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
