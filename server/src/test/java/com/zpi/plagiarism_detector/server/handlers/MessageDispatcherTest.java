package com.zpi.plagiarism_detector.server.handlers;

import com.zpi.plagiarism_detector.commons.protocol.DocumentData;
import com.zpi.plagiarism_detector.commons.protocol.Message;
import com.zpi.plagiarism_detector.commons.protocol.ProtocolCode;
import com.zpi.plagiarism_detector.commons.protocol.plagiarism.PlagiarismDetectionResult;
import com.zpi.plagiarism_detector.server.detector.PlagiarismDetector;
import com.zpi.plagiarism_detector.server.exceptions.AbortConnectionException;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.IOException;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

public class MessageDispatcherTest {
    private MessageDispatcher messageDispatcher;
    private PlagiarismDetector plagiarismDetector;
    private DocumentData document;

    @BeforeMethod
    public void init() {
        plagiarismDetector = mock(PlagiarismDetector.class);
        document = mock(DocumentData.class);
        messageDispatcher = new MessageDispatcher(plagiarismDetector);
    }

    @Test(expectedExceptions = NullPointerException.class)
    public void dispatchMessage_nullMessageFailureTest() throws AbortConnectionException {
        // when
        Message message = null;

        // given
        Message result = messageDispatcher.dispatchMessage(message);
    }

    @Test
    public void equalityTest() {
        // given
        Message m1 = new Message(ProtocolCode.CHECK_FOR_PLAGIARISM);
        Message m2 = new Message(ProtocolCode.CHECK_FOR_PLAGIARISM, null);

        Object obj = new Object();
        Message m3 = new Message(ProtocolCode.CHECK_FOR_PLAGIARISM, obj);
        Message m4 = new Message(ProtocolCode.CHECK_FOR_PLAGIARISM, obj);
        Message m5 = new Message(ProtocolCode.CHECK_FOR_PLAGIARISM, new Object());
        Message m6 = new Message(ProtocolCode.TEST, obj);

        // then
        Assert.assertEquals(m1, m2);
        Assert.assertEquals(m3, m4);
        Assert.assertNotEquals(m4, m5);
        Assert.assertNotEquals(m4, m6);
    }

    @Test
    public void dispatchMessage_messagePlagiarismCheckWithNullMessageReturnsErrorResponseTest() throws AbortConnectionException {
        // when
        Message message = new Message(ProtocolCode.CHECK_FOR_PLAGIARISM, null);

        // when
        Message result = messageDispatcher.dispatchMessage(message);

        // then
        Assert.assertEquals(result, new Message(ProtocolCode.NULL_DOCUMENT_ERROR));
    }

    @Test
    public void dispatchMessage_messagePlagiarismCheckWithAllParametersReturnCorrectResultTest() throws AbortConnectionException, IOException {
        // given
        PlagiarismDetectionResult toBeReturned = mock(PlagiarismDetectionResult.class);
        doReturn(toBeReturned).when(plagiarismDetector).checkForPlagiarism(document);
        Message message = new Message(ProtocolCode.CHECK_FOR_PLAGIARISM, document);

        // when
        Message result = messageDispatcher.dispatchMessage(message);

        // test
        Assert.assertEquals(result.getCode(), ProtocolCode.PLAGIARISM_CHECK_RESULT);
        Assert.assertEquals(result.getSentObject(), toBeReturned);
    }
}
