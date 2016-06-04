package pl.edu.agh.kiro.latex_plagiat.commons.protocol.plagiarism;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import pl.edu.agh.kiro.latex_plagiat.commons.database.DocumentType;

public class PlagiarismResult implements Serializable{
    private String newDocument;        //to co użytkownik przesłał
    private String existingDocument;   //to do czego jest porównywany dokument użytkownika

    private Map<PlagiarismFragment, PlagiarismFragment> plagiarisedFragments;
    private DocumentType type;

    public PlagiarismResult() {

    }
    
    public PlagiarismResult(PlagiarismResult plagiarism){
    	this.newDocument = plagiarism.getNewDocument();
    	this.existingDocument = plagiarism.getExistingDocument();
    	this.plagiarisedFragments = new HashMap<PlagiarismFragment, PlagiarismFragment>();
    	plagiarisedFragments.putAll(plagiarism.getPlagiarisedFragments());
    	this.type = plagiarism.getType();
    }

    public PlagiarismResult(String newDocument,
                            String existingDocument,
                            Map<PlagiarismFragment, PlagiarismFragment> plagiarisedFragments,
                            DocumentType type) {

        this.newDocument = newDocument;
        this.existingDocument = existingDocument;
        this.plagiarisedFragments = plagiarisedFragments;
        this.type = type;
    }

    public String getNewDocument() {
        return newDocument;
    }

    public void setNewDocument(String newDocument) {
        this.newDocument = newDocument;
    }

    public String getExistingDocument() {
        return existingDocument;
    }

    public void setExistingDocument(String existingDocument) {
        this.existingDocument = existingDocument;
    }

    public Map<PlagiarismFragment, PlagiarismFragment> getPlagiarisedFragments() {
        return plagiarisedFragments;
    }

    public void setPlagiarisedFragments(
            Map<PlagiarismFragment, PlagiarismFragment> plagiarisedFragments) {
        this.plagiarisedFragments = plagiarisedFragments;
    }

    public DocumentType getType() {
        return type;
    }

    public void setType(DocumentType type) {
        this.type = type;
    }

    @Override
    public String toString() {
        // TODO: ktos kto wie jak to wyswietlic niech tutaj cos napisze :)
        return super.toString();
    }
}
