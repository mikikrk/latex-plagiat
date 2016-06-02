package com.zpi.plagiarism_detector.server.detector;

import com.zpi.plagiarism_detector.commons.protocol.DocumentData;
import com.zpi.plagiarism_detector.commons.protocol.plagiarism.PlagiarismDetectionResult;
import com.zpi.plagiarism_detector.commons.protocol.plagiarism.PlagiarismResult;
import com.zpi.plagiarism_detector.server.data.ServerData;
import com.zpi.plagiarism_detector.server.websearch.WebsiteAnalyze;
import org.apache.commons.lang3.StringUtils;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.LinkedList;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

public class PlagiarismDetectorTest {
    private PlagiarismDetector plagiarismDetector;
    private WebsiteAnalyze websiteAnalyze;

    private ServerData serverData;
    private ComparingAlgorithm comparingAlgorithm;

    private DocumentData document;

    @BeforeMethod
    public void init() {
        document = mock(DocumentData.class);


        plagiarismDetector = new PlagiarismDetector(serverData, webData, comparingAlgorithm);
    }

    @Test(expectedExceptions = NullPointerException.class)
    public void checkForPlagiarism_nullDocumentThrowsNullPointerExceptionTest() throws IOException {
        // given
        document = null;

        // when
        PlagiarismDetectionResult result = plagiarismDetector.checkForPlagiarism(document);
    }

    @Test(enabled = false)
    public void checkForPlagiarism_documentIsPlagiarismResultTrueTest() throws IOException {
        // given
        String title = "title";
        String[] keywords = new String[]{"a", "b", "c", "d", "e"};
        String keywordsJoined = StringUtils.join(keywords, " ");

        LinkedList<PlagiarismResult> expectedResults = new LinkedList<>();
        PlagiarismDetectionResult expectedResult = new PlagiarismDetectionResult(expectedResults);

        doReturn(title).when(document).getTitle();
        doReturn(keywordsJoined).when(document).getKeywordsJoined();

        // when
        PlagiarismDetectionResult result = plagiarismDetector.checkForPlagiarism(document);

        // then
        Assert.assertEquals(result, expectedResult);
    }
}
