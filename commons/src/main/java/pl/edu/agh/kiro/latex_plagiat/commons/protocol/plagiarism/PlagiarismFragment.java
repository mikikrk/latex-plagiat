package pl.edu.agh.kiro.latex_plagiat.commons.protocol.plagiarism;

import java.io.Serializable;

public class PlagiarismFragment implements Serializable {
    private String fragment;
    private int begin;      //miejsce chara rozpoczynajacego dany fragment
    private int end;
    private int size;

    public PlagiarismFragment(String fragment, int begin, int end,
                              int size) {
        this.fragment = fragment;
        this.begin = begin;
        this.end = end;
        this.size = size;
    }

    public PlagiarismFragment() {
        super();
    }

    public String getFragment() {
        return fragment;
    }

    public void setFragment(String fragment) {
        this.fragment = fragment;
    }

    public int getBegin() {
        return begin;
    }

    public void setBegin(int begin) {
        this.begin = begin;
    }

    public int getEnd() {
        return end;
    }

    public void setEnd(int end) {
        this.end = end;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

}
