package pl.edu.agh.kiro.latex_plagiat.server.articles;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import pl.edu.agh.kiro.latex_plagiat.commons.protocol.plagiarism.FragmentsPair;
import pl.edu.agh.kiro.latex_plagiat.server.ServerProperties;
import pl.edu.agh.kiro.latex_plagiat.commons.protocol.plagiarism.PlagiarismFragment;


public class TextProcessing {

    private double sentenceWordsCount;
    private double repeatedWords;
    private List<FragmentsPair> list;

	private static final String[] latexReplacements = {
			"\\\\multicolumn\\{",
			"\\\\caption\\{",
			"\\\\chapter\\{",
			"\\\\subsection\\{",
			"\\\\section\\{",
			"\\\\mbox\\{",
			"\\\\emph\\{",
			"\\\\section\\{",
			"\\\\text[^\\{]*\\{",
			"\\\\title\\{",
			"\\\\[^\\s]+"
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
    public List<FragmentsPair> compareTexts(String[] str, String pattern,int patternStart) {
    	list = new LinkedList<>();
        int textStart = 0;
        String tmpPattern = pattern;
        for(String textSentence : str){
    		repeatedWords=0;
    		String tmpText = textSentence;
        	String[] splited =  prepareText(textSentence);
        	sentenceWordsCount = splited.length-1;
			if (sentenceWordsCount > 2) {

				String[] splitedPattern = prepareText(pattern);
				for (String test : splitedPattern) {
					if (test.length() > 3 && textSentence.toLowerCase().contains(test.toLowerCase())) {
						repeatedWords++;
					}
				}
				repeatedWords -= 1;

				if (repeatedWords / sentenceWordsCount > ServerProperties.MIN_WORD_SIMILARITY_PERCENTAGE / (double) 100) {
					list.add(new FragmentsPair(new PlagiarismFragment(tmpPattern, patternStart, patternStart + tmpPattern.length(), tmpPattern.length()),
							new PlagiarismFragment(tmpText, textStart, textStart + tmpText.length(), tmpText.length())));
				}
			}
			textStart += tmpText.length() + 1;
        }
        return list;
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
		textSentence.replaceAll("[\\{\\}\\(\\)\\-\\+\\.\\^:,]","");
		textSentence = textSentence.replaceAll("\\b[\\w']{1,2}\\b", "");
		textSentence = textSentence.replaceAll("\\s{2,}", " ");
		return textSentence;
	}

}
