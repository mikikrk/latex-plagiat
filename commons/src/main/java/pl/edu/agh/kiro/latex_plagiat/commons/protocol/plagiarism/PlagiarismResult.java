package pl.edu.agh.kiro.latex_plagiat.commons.protocol.plagiarism;

import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import pl.edu.agh.kiro.latex_plagiat.commons.database.DocumentType;

public class PlagiarismResult implements Serializable{
    private String newDocument;        //to co użytkownik przesłał
    private String existingDocument;   //to do czego jest porównywany dokument użytkownika

    private List<FragmentsPair> plagiarisedFragments = new LinkedList<>();
    private DocumentType type;

    public PlagiarismResult() {
    }
    
    public PlagiarismResult(PlagiarismResult plagiarism){
    	this.newDocument = plagiarism.getNewDocument();
    	this.existingDocument = plagiarism.getExistingDocument();
    	this.plagiarisedFragments.addAll(plagiarism.getPlagiarisedFragments());
    	this.type = plagiarism.getType();
    }

    public PlagiarismResult(String newDocument,
                            String existingDocument,
                            List<FragmentsPair> plagiarisedFragments,
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

    public List<FragmentsPair> getPlagiarisedFragments() {
        return plagiarisedFragments;
    }

    public void setPlagiarisedFragments(
            List<FragmentsPair> plagiarisedFragments) {
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
        return super.toString();
    }
}
