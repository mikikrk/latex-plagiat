package pl.edu.agh.kiro.latex_plagiat.server.database;

import pl.edu.agh.kiro.latex_plagiat.commons.database.DocumentType;

import java.util.Set;

public class DaoImp implements Dao {
	private final static String pathToConfigurationFile = "META-INF/hibernate.cfg.xml";
	HibernateAccess hibernateAccess;

	public DaoImp() {
		createHibernateAccess();
	}

	void createHibernateAccess() {
		hibernateAccess = new HibernateAccess(pathToConfigurationFile);
	}

	public Article getArticle(String path) {
		return hibernateAccess.getArticle(path);
	}

	@Override
	public boolean containsUri(String uri) {
		return hibernateAccess.containsUri(uri);
	}

	@Override
	public int addArticle(Article article, Set<String> keywords) {

		return hibernateAccess.addArticle(article, keywords);
	}

	@Override
	public int replaceKeywords(String path, Set<String> keywords) {
		return hibernateAccess.setKeywords("path", path, keywords, false);
	}

	@Override
	public int replaceKeywordsToUri(String uri, Set<String> keywords) {
		return hibernateAccess.setKeywords("uri", uri, keywords, false);
	}

	@Override
	public int addKeywords(String path, Set<String> keywords) {
		return hibernateAccess.setKeywords("path", path, keywords, true);
	}

	@Override
	public int addKeywordsToUri(String uri, Set<String> keywords) {
		return hibernateAccess.setKeywords("uri", uri, keywords, true);
	}

	@Override
	public Set<String> getKeywords(String path) {
		return hibernateAccess.getKeywords(path);
	}

	@Override
	public Set<String> findArticlesWithAtLeastOne(Set<String> set,
			DocumentType type) {
		return hibernateAccess.findArticles(set, type, false);
	}

	@Override
	public Set<String> findArticlesWithAll(Set<String> set, DocumentType type) {
		return hibernateAccess.findArticles(set, type, true);
	}

	@Override
	public int removeAll() {
		return hibernateAccess.removeAll();
	}

	@Override
	public int changePath(String currentPath, String newPath) {
		return hibernateAccess.setPath(currentPath, newPath);

	}

	@Override
	public int removeArticle(String path) {
		return hibernateAccess.removeArticle(path);

	}

	

}
