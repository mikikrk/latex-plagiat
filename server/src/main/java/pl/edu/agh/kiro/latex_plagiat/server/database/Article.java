package pl.edu.agh.kiro.latex_plagiat.server.database;

import pl.edu.agh.kiro.latex_plagiat.commons.database.DocumentType;

public class Article {
    private Long id;
    private String path;

    private DocumentType type;
    private String title;
    private String uri;

    public Article() {
    }

    public Article(String path, DocumentType type) {
        this.path = path;
        this.type = type;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String source) {
        this.uri = source;
    }

    Long getId() {
        return id;
    }

    void setId(Long id) {
        this.id = id;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public DocumentType getType() {
        return type;
    }

    public void setType(DocumentType type) {
        this.type = type;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Article) {
            Article article = (Article) obj;
            boolean arePathsEqual = path == article.getPath() || path.equals(article.getPath());
            boolean areTypesEqual = type == article.getType();
            return arePathsEqual && areTypesEqual;
        }
        return false;
    }
}
