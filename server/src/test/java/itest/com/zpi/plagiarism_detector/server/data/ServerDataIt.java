package itest.com.zpi.plagiarism_detector.server.data;

import com.zpi.plagiarism_detector.commons.protocol.DocumentData;
import com.zpi.plagiarism_detector.server.data.FileData;
import com.zpi.plagiarism_detector.server.data.ServerData;
import com.zpi.plagiarism_detector.server.database.Dao;
import com.zpi.plagiarism_detector.server.database.DaoFactory;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.*;

public class ServerDataIt {

    private Dao dao;
    private FileData fileData;
    private ServerData serverData;

    @BeforeClass
    public void setUp() {
        dao = DaoFactory.createDao();
        fileData = new FileData();
        serverData = new ServerData(dao, fileData);
    }

    @Test
    public void savingArticleToDBTest() throws IOException {
        String title = "title";
        Set<String> keywords = new LinkedHashSet<>();
        Collections.addAll(keywords, new String[]{"a", "b", "c", "d"});
        String article = "article";
        Set<String> codes = new LinkedHashSet<>();
        Collections.addAll(codes, new String[]{"c1", "c2", "c3"});
        List<String> codesPaths = new LinkedList<>();

        DocumentData document = new DocumentData(title, keywords, article, codes);

        serverData.saveDocument(document, codesPaths);

    }

    @AfterClass
    public void teardown() {
        // Transaction rollback
    }
}
