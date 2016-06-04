package pl.edu.agh.kiro.latex_plagiat.commons.protocol;

import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class DocumentData implements Serializable {
    private String title;
    private Set<String> keywords;
    private String article;
    private String url;
    private Set<String> codes;

    public DocumentData() {
        this.title = "n/a";
        this.keywords = new HashSet<>();
        this.keywords.add("n/a");
        this.article = "n/a";
        this.url = "n/a";
        this.codes = new HashSet<>();
        this.codes.add("n/a");
    }

    public DocumentData(String title, Set<String> keywords, String article, Set<String> codes) {
        this.title = title;
        this.keywords = keywords;
        this.article = article;
        this.codes = codes;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Set<String> getKeywords() {
        return keywords;
    }

    public void setKeywords(Set<String> keywords) {
        this.keywords = keywords;
    }

    public String getKeywordsJoined() {
        String keywordsJoined = StringUtils.join(keywords, " ");
        return keywordsJoined;
    }

    public String getArticle() {
        return article;
    }

    public void setArticle(String article) {
        this.article = article;
    }

    public Set<String> getCodes() {
        return codes;
    }

    public void setCodes(Set<String> codes) {
        this.codes = codes;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getCodesCount() {
        return codes.size();
    }

}
