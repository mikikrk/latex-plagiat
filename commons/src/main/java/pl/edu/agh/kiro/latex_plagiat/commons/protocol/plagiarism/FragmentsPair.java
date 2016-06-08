package pl.edu.agh.kiro.latex_plagiat.commons.protocol.plagiarism;

import java.io.Serializable;

public class FragmentsPair implements Serializable {
    private PlagiarismFragment insertedFragment;
    private PlagiarismFragment databaseFragment;

    public FragmentsPair(PlagiarismFragment insertedFragment, PlagiarismFragment databaseFragment) {
        this.insertedFragment = insertedFragment;
        this.databaseFragment = databaseFragment;
    }

    public PlagiarismFragment getInsertedFragment() {
        return insertedFragment;
    }

    public PlagiarismFragment getDatabaseFragment() {
        return databaseFragment;
    }
}
