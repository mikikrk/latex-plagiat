package pl.edu.agh.kiro.latex_plagiat.server.articles;



import java.io.IOException;
import java.util.HashMap;

import pl.edu.agh.kiro.latex_plagiat.commons.protocol.plagiarism.PlagiarismResult;
import pl.edu.agh.kiro.latex_plagiat.commons.database.DocumentType;
import pl.edu.agh.kiro.latex_plagiat.commons.protocol.plagiarism.PlagiarismFragment;


public class CompareEngine {

    private String pattern = "";
    private String text = "";
    private String[] patternTab;
    private String[] textTab;
    private PlagiarismResult comparisonResult;
    private FileLoader fL;
    private TextProcessing tP = new TextProcessing();

    public CompareEngine() {
    }

    /**
     * Dzielenie tesktow na zdania
     */
    private void splitStrings() {

        patternTab = pattern.split("\\.");
        textTab = text.split("\\.");
    }


    /**
     * Porownanie dwoch stringow podanych w parametrach
     *
     * @param _pattern
     * @param _text
     */
    public PlagiarismResult compare(String _pattern, String _text) throws IOException {
    	comparisonResult = new PlagiarismResult();
    	comparisonResult.setType(DocumentType.TEXT);
    	comparisonResult.setPlagiarisedFragments(new HashMap<PlagiarismFragment, PlagiarismFragment>());
        fL = new FileLoader(_pattern, _text);
        fL.loadFiles();
        pattern = fL.getPattern();
        comparisonResult.setNewDocument(fL.getPattern());
        comparisonResult.setExistingDocument(fL.getText());
        text = fL.getText();
        this.splitStrings();
        int indexStart = 0;
        for (String patternSentence : patternTab) {
            comparisonResult.getPlagiarisedFragments().putAll(tP.compareTexts(textTab, patternSentence, indexStart));
            indexStart+=patternSentence.length() + 1;
        }

        return comparisonResult;
    }


}