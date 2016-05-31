package com.zpi.plagiarism_detector.server.database;

import com.zpi.plagiarism_detector.commons.database.DocumentType;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class ArticleTest {
    private Article article;
    private DocumentType type;
    private String path;

    @BeforeMethod
    public void init() {
        path = "path";
        type = DocumentType.TEXT;
        article = new Article(path, type);
    }

    @Test
    public void equalityTest_otherReferencesButEqual() {
        // given
        Article a2 = new Article(path, type);

        // when
        boolean isEqual = article.equals(a2);

        // then
        Assert.assertTrue(isEqual);
    }

    @Test
    public void equalityTest_otherReferencesNotEqual() {
        // given
        Article a2 = new Article("asd", DocumentType.TEXT);

        // when
        boolean isEqual = article.equals(a2);

        // then
        Assert.assertFalse(isEqual);
    }

    @Test
    public void equalityTest_sameReferenceEqual() {
        // when
        boolean isEqual = article.equals(article);

        // then
        Assert.assertTrue(isEqual);
    }

    @Test
    public void equalityTest_nullNotEqual() {
        // given
        Article a2 = null;

        // when
        boolean isEqual = article.equals(a2);

        // then
        Assert.assertFalse(isEqual);
    }

    @Test
    public void equalityTest_objectNotEqual() {
        // given
        Object obj = new Object();

        // when
        boolean isEqual = article.equals(obj);

        // then
        Assert.assertFalse(isEqual);
    }
}
