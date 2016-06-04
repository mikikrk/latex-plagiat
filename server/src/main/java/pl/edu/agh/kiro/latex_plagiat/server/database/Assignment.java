package pl.edu.agh.kiro.latex_plagiat.server.database;

import java.io.Serializable;

class Assignment implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = -6426851346568245591L;
    private Long articleId;
    private Long keywordId;

    public Long getArticleId() {
        return articleId;
    }

    public void setArticleId(Long articleId) {
        this.articleId = articleId;
    }

    public Long getKeywordId() {
        return keywordId;
    }

    public void setKeywordId(Long keywordId) {
        this.keywordId = keywordId;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof Assignment)) {
            return false;
        }
        Assignment as = (Assignment) other;
        return articleId.equals(as.articleId) && keywordId.equals(as.keywordId);

    }

    @Override
    public int hashCode() {
        int result = 17;
        int c = (int) (keywordId ^ (keywordId >>> 32));
        result = 31 * result + c;
        c = (int) (articleId ^ (articleId >>> 32));
        result = 31 * result + c;
        return result;
    }

}
