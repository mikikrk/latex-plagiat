package com.zpi.plagiarism_detector.server.websearch;

import java.util.List;

/**
 * Klasa uzywana do konwersji Json'a do Javy
 */
public class GoogleResults {

    public String link;
    public List<GoogleResults> items;
    public String dc;

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public List<GoogleResults> getItems() {
        return items;
    }

    public void setGroups(List<GoogleResults> items) {
        this.items = items;
    }

    public GoogleResults getThing(int i) {
        //System.out.println(items.get(i));
        return items.get(i);
    }

    @Override
    public String toString() {
        return String.format("%s", link);
    }

}