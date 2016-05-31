package com.zpi.plagiarism_detector.server.handlers;

import static com.zpi.plagiarism_detector.commons.protocol.ProtocolCode.TEST;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zpi.plagiarism_detector.commons.protocol.DocumentData;
import com.zpi.plagiarism_detector.commons.protocol.Message;
import com.zpi.plagiarism_detector.commons.protocol.ProtocolCode;
import com.zpi.plagiarism_detector.commons.protocol.plagiarism.PlagiarismDetectionResult;
import com.zpi.plagiarism_detector.commons.protocol.plagiarism.PlagiarismResult;
import com.zpi.plagiarism_detector.server.detector.PlagiarismDetector;
import com.zpi.plagiarism_detector.server.exceptions.AbortConnectionException;

public class MessageDispatcher {
    private static final Logger log = LoggerFactory.getLogger(MessageDispatcher.class);
    private Message response;
    private PlagiarismDetector plagiarismDetector;

    public MessageDispatcher(PlagiarismDetector plagiarismDetector) {
        this.plagiarismDetector = plagiarismDetector;
    }

    public Message dispatchMessage(Message message) throws AbortConnectionException {
        log.debug("entering dispatchMessage()");
        ProtocolCode command = message.getCode();
        Object sentObject = message.getSentObject();

        response = null;
        if (command == ProtocolCode.POISON_PILL) {
            throw new AbortConnectionException();
        }
        if (command == TEST) {
            handleTestMessage();
        } else if (command == ProtocolCode.CHECK_FOR_PLAGIARISM) {
            handlePlagiarismChecking(sentObject);
        }
        log.debug("leaving dispatchMessage()");
        return response;
    }

    private void handleTestMessage() {
        response = new Message(TEST);
    }

    private void handlePlagiarismChecking(Object sentObject) {
        if (sentObject == null) {
            response = new Message(ProtocolCode.NULL_DOCUMENT_ERROR);
        } else {
            checkForPlagiarism((DocumentData) sentObject);
        }
    }

    private void checkForPlagiarism(DocumentData sentObject) {
        try {
            DocumentData document = (DocumentData) sentObject;
            PlagiarismDetectionResult result = plagiarismDetector.checkForPlagiarism(document);
            response = new Message(ProtocolCode.PLAGIARISM_CHECK_RESULT, result);
        } catch (IOException e) {
            response = new Message(ProtocolCode.PLAGIARISM_CHECK_ERROR, e.getCause());
        }
    }
}
