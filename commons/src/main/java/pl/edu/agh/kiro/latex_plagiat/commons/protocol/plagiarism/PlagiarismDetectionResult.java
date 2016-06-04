package pl.edu.agh.kiro.latex_plagiat.commons.protocol.plagiarism;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PlagiarismDetectionResult implements Serializable {
    private List<PlagiarismResult> plagiarisms;

    public PlagiarismDetectionResult() {
        this.plagiarisms = new ArrayList<>();
    }

    public PlagiarismDetectionResult(List<PlagiarismResult> plagiarisms) {
        this.plagiarisms = plagiarisms;
    }

    public void addPlagiarismResult(PlagiarismResult plagiarismResult) {
        plagiarisms.add(plagiarismResult);
    }

    public boolean isPlagiarism() {
        return !plagiarisms.isEmpty();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        int i = 1;
        for (PlagiarismResult plagiarism : plagiarisms) {
            sb.append(i);
            sb.append(". ");
            sb.append(plagiarism);
            sb.append("\n");
            ++i;
        }
        return sb.toString();
    }
    public List<PlagiarismResult> getAllResults() {
        return plagiarisms;
    }
}
