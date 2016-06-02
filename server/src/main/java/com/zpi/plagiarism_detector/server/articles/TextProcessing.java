package com.zpi.plagiarism_detector.server.articles;

import java.util.LinkedHashMap;
import java.util.Map;

import com.zpi.plagiarism_detector.commons.database.DocumentType;
import com.zpi.plagiarism_detector.commons.protocol.plagiarism.PlagiarismFragment;
import com.zpi.plagiarism_detector.commons.protocol.plagiarism.PlagiarismResult;
import com.zpi.plagiarism_detector.server.ServerProperties;


public class TextProcessing {

    private double sentenceWordsCount;
    private double repeatedWords;
    private Map<PlagiarismFragment, PlagiarismFragment> map;

	private static final String[] latexReplacements = {
			"\\\\multicolumn{",
			"\\\\caption{",
			"\\\\chapter{",
			"\\\\subsection{",
			"\\\\section{",
			"\\\\mbox{",
			"\\\\emph{",
			"\\\\section{",
			"\\\\text[^{]*{",
			"\\\\title{",
			"\\\\[^ \\n]+"
	};
    
    public TextProcessing() {
    }

    /**
     * W�asciwe por�wnanie
     *
     * @param str
     * @param pattern
     * @param patternStart
     */
    public Map<PlagiarismFragment, PlagiarismFragment> compareTexts(String[] str, String pattern,int patternStart) {
    	map  = new LinkedHashMap<>();
        int textStart = 0;
        String tmpPattern = pattern;
        for(String textSentence : str){
    		repeatedWords=0;
    		String tmpText = textSentence;
        	String[] splited =  prepareText(textSentence);
        	sentenceWordsCount=splited.length-1;
			if (sentenceWordsCount > 3) {

				String[] splitedPattern = prepareText(pattern);
				for (String test : splitedPattern) {
					if (textSentence.toLowerCase().contains(test.toLowerCase())) {
						repeatedWords++;
					}
				}
				repeatedWords -= 1;

				if (repeatedWords / sentenceWordsCount > ServerProperties.MIN_WORD_SIMILARITY_PERCENTAGE / (double) 100) {
					map.put(new PlagiarismFragment(tmpPattern, patternStart, patternStart + tmpPattern.length(), tmpPattern.length()),
							new PlagiarismFragment(tmpText, textStart, textStart + tmpText.length(), tmpText.length()));
				}
			}
			textStart += tmpText.length();
        }
        return map;
	}

	private String[] prepareText(String textSentence) {
		String result;
		result = filterLatex(textSentence);
		result = clearText(result);
		return result.split("\\s+");
	}

	private String filterLatex(String result) {
		for (String regex: latexReplacements) {
			result = result.replaceAll(regex, "");
		}
		return result;
	}

	private String clearText(String textSentence) {
		textSentence.replaceAll("[\\{}\\(\\)\\-\\+\\.\\^:,]","");
		textSentence = textSentence.replaceAll("\\b[\\w']{1,2}\\b", "");
		textSentence = textSentence.replaceAll("\\s{2,}", " ");
		return textSentence;
	}

}
