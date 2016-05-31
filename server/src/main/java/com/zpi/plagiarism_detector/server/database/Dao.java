package com.zpi.plagiarism_detector.server.database;

import com.zpi.plagiarism_detector.commons.database.DocumentType;

import java.util.Set;

/**
 * Interfejs umozliwiajacy komunikacje z baza danych.
 */
public interface Dao {

	/**
	 * zwraca artykul dla podanej sciezki (gdy sciezka nie istnieje null)
	 */
	public Article getArticle(String path);

	/**
	 * sprawdza czy istnieje artykul o zadanym uri
	 */
	public boolean containsUri(String uri);

	/**
	 * dodanie artyku�u do bazy danych
	 *
	 * @param article
	 *            zawiera info o artykle
	 * @param keywords
	 *            zbior slow kluczowych
	 */
	public int addArticle(Article article, Set<String> keywords);

	/**
	 * ustawia slowa kluczowe dla artykulu o podanej sciezce (poprzednie zostaja
	 * usuniete)
	 *
	 * @return 1 -udane, 0-nieudane
	 */
	public int replaceKeywords(String path, Set<String> keywords);

	/**
	 * dodaje slowa kluczowe dla artykulu o podanej sciezce (poprzednie zostaja
	 * zachowane)
	 *
	 * @return 1 -udane, 0-nieudane
	 */
	public int addKeywords(String path, Set<String> keywords);

	/**
	 * ustawia slowa kluczowe dla artykulu o podanym uri (poprzednie zostaja
	 * usuniete)
	 *
	 * @return 1 -udane, 0-nieudane
	 */
	public int replaceKeywordsToUri(String uri, Set<String> keywords);

	/**
	 * dodaje slowa kluczowe dla artykulu o podanym uri (poprzednie zostaja
	 * zachowane)
	 *
	 * @return 1 -udane, 0-nieudane
	 */
	public int addKeywordsToUri(String uri, Set<String> keywords);

	/**
	 * zwraca slowa kluczowe dla artykulu o podanej sciezce (poprzednie zostaja
	 * zachowane)
	 *
	 * @return 1 -udane, 0-nieudane
	 */
	public Set<String> getKeywords(String path);

	/**
	 * zwraca liste sciezek do artykulow o podanym typie z conajmniej jednym
	 * slowem kluczowym
	 */
	public Set<String> findArticlesWithAtLeastOne(Set<String> set,
			DocumentType type);

	/**
	 * zwraca liste sciezek do artykulow o podanym typie ze wszystkimi slowami
	 * kluczowymi
	 */
	public Set<String> findArticlesWithAll(Set<String> set, DocumentType type);

	/**
	 * usuwa wszystko z bazy danych
	 *
	 * @return 1 -udane, 0-nieudane
	 */
	public int removeAll();

	/**
	 * zmienia sciezke do artykulu
	 *
	 * @return 1 -udane, 0-nieudane
	 */
	public int changePath(String currentPath, String newPath);

	/**
	 * usuwa z bazy artykul o podanej sciezce
	 *
	 * @return 1 -udane, 0-nieudane
	 */
	public int removeArticle(String path);

}