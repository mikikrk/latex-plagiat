package com.zpi.plagiarism_detector.server.data;

import com.zpi.plagiarism_detector.commons.database.DocumentType;
import com.zpi.plagiarism_detector.commons.protocol.DocumentData;
import com.zpi.plagiarism_detector.server.ServerProperties;
import com.zpi.plagiarism_detector.server.database.Article;
import com.zpi.plagiarism_detector.server.database.Dao;
import org.powermock.modules.testng.PowerMockTestCase;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.util.*;

import static org.mockito.Mockito.*;

public class ServerDataTest extends PowerMockTestCase {
    private ServerData serverData;
    private Dao dao;
    private FileData fileData;
    private String articleText;
    private String title;
    private Set<String> codes;
    private Set<String> keywords;
    private DocumentData document;
    private List<String> codesPaths;

    @BeforeMethod
    public void init() {
        this.dao = mock(Dao.class);
        this.fileData = mock(FileData.class);
        this.serverData = new ServerData(dao, fileData);
    }

    @Test(expectedExceptions = NullPointerException.class)
    public void saveDocument_nullArgExceptionTest() throws IOException {
        // given
        DocumentData document = null;

        // when
        serverData.saveDocument(document, null);
    }

    @Test
    public void saveDocument_NonNullDocWithoutCodesSuccessTest() throws IOException {
        // given
        keywords = new HashSet<>();
        keywords.add("keyword");
        codes = new HashSet<>();
        codesPaths = new LinkedList<>();

        title = "title";
        articleText = "article";
        DocumentData document = new DocumentData(title, keywords, articleText, codes);

        // when
        String articlePath = serverData.saveDocument(document, codesPaths);

        // then
        String expectedDirPath = getDirPathWithPostfix("");
        String expectedArtPath = expectedDirPath + "/article";

        Assert.assertEquals(articlePath, expectedArtPath);
        Assert.assertTrue(codesPaths.isEmpty());
        verify(dao).addArticle(any(Article.class), eq(keywords));
        verify(fileData).createDir(expectedDirPath);
        verify(fileData).writeToFile(expectedArtPath, articleText);
    }

    @Test
    public void saveDocument_withDocumentAndCodesSuccessTest() throws IOException {
        // given
        keywords = new LinkedHashSet<>();
        keywords.add("keyword");
        codes = new LinkedHashSet<>();
        codes.add("a");
        codes.add("b");
        codes.add("c");
        codesPaths = new LinkedList<>();

        title = "title";
        articleText = "article";
        DocumentData document = new DocumentData(title, keywords, articleText, codes);

        // when
        serverData.saveDocument(document, codesPaths);

        // then
        String expectedDirPath = getDirPathWithPostfix("");
        String expectedCodeAPath = expectedDirPath + "/code1";
        String expectedCodeBPath = expectedDirPath + "/code2";
        String expectedCodeCPath = expectedDirPath + "/code3";

        Assert.assertEquals(codesPaths.size(), 3);
        verify(fileData).writeToFile(expectedCodeAPath, "a");
        verify(fileData).writeToFile(expectedCodeBPath, "b");
        verify(fileData).writeToFile(expectedCodeCPath, "c");
    }

    @Test
    public void saveDocument_withNullArticleAndCodesSuccessTest() throws IOException {
        // given
        keywords = new LinkedHashSet<>();
        keywords.add("keyword");
        codes = new LinkedHashSet<>();
        codes.add("a");
        codes.add("b");
        codes.add("c");
        codesPaths = new LinkedList<>();

        title = "title";
        articleText = null;
        document = new DocumentData(title, keywords, articleText, codes);

        // when
        serverData.saveDocument(document, codesPaths);

        // then
        String expectedDirPath = getDirPathWithPostfix("");
        String unexpectedArtPath = expectedDirPath + "/article";
        String expectedCodeAPath = expectedDirPath + "/code1";
        String expectedCodeBPath = expectedDirPath + "/code2";
        String expectedCodeCPath = expectedDirPath + "/code3";

        verify(dao, times(0)).addArticle(new Article(unexpectedArtPath, DocumentType.TEXT), keywords);
        verify(fileData).createDir(expectedDirPath);
        verify(fileData, times(0)).writeToFile(unexpectedArtPath, articleText);

        verify(fileData).writeToFile(expectedCodeAPath, "a");
        verify(fileData).writeToFile(expectedCodeBPath, "b");
        verify(fileData).writeToFile(expectedCodeCPath, "c");
    }

    private String getDirPathWithPostfix(String postFix) {
        return ServerProperties.DOCS_PATH + "/" + title + postFix;
    }

    @Test
    public void saveDocument_articleWithNameThatExists_AddPostfixSuccessTest() throws IOException {
        // given
        keywords = new LinkedHashSet<>();
        codes = new LinkedHashSet<>();

        title = "title";
        articleText = "articleText";
        document = new DocumentData(title, keywords, articleText, codes);

        final String defaultDirPath = getDirPathWithPostfix("");

        FileAlreadyExistsException ex = new FileAlreadyExistsException(defaultDirPath);
        ArrayList<String> strings = new ArrayList<String>() {{
            add(defaultDirPath);
            add(defaultDirPath + "1");
        }};
        doThrow(ex).doThrow(ex).when(fileData).createDir(any(String.class));
        doNothing().when(fileData).createDir(defaultDirPath + "2");

        // when
        serverData.saveDocument(document, null);

        // then
        String expectedDirPath = getDirPathWithPostfix("2");
        String expectedArtPath = expectedDirPath + "/article";
        verify(fileData).createDir(expectedDirPath);
        verify(fileData).writeToFile(expectedArtPath, articleText);
        verify(dao).addArticle(new Article(expectedArtPath, DocumentType.TEXT), keywords);
    }

    @Test
    public void getCommonKeywordDocuments_NonEmptyKeywordSet() {
        // given
        keywords = new HashSet<>();
        Collections.addAll(keywords, new String[]{"a", "b", "c"});
        doReturn(new HashSet<>()).when(dao).findArticlesWithAtLeastOne(keywords, DocumentType.TEXT);

        // when
        Set<String> commonKeywordDocuments = serverData.getCommonKeywordDocumentsPaths(keywords, DocumentType.TEXT);

        // then
        Assert.assertNotNull(commonKeywordDocuments);
    }
}
