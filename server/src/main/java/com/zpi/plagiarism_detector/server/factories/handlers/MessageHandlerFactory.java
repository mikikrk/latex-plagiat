package com.zpi.plagiarism_detector.server.factories.handlers;

import com.zpi.plagiarism_detector.server.articles.ArticleComparison;
import com.zpi.plagiarism_detector.server.articles.CompareEngine;
import com.zpi.plagiarism_detector.server.data.FileData;
import com.zpi.plagiarism_detector.server.data.ServerData;
import com.zpi.plagiarism_detector.server.database.Dao;
import com.zpi.plagiarism_detector.server.database.DaoFactory;
import com.zpi.plagiarism_detector.server.detector.ComparingAlgorithm;
import com.zpi.plagiarism_detector.server.detector.PlagiarismDetector;
import com.zpi.plagiarism_detector.server.handlers.MessageDispatcher;
import com.zpi.plagiarism_detector.server.handlers.MessageHandler;
import com.zpi.plagiarism_detector.server.sourcecode.SourceCodeComparison;

import java.io.*;
import java.net.Socket;

public class MessageHandlerFactory extends AbstractMessageHandlerFactory {

    private MessageDispatcher messageDispatcher;

    public MessageHandler createForSocket(Socket socket) throws IOException {
        initFields();

        ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
        ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());

        return MessageHandler.create(outputStream, inputStream, messageDispatcher);
    }

    public MessageHandler createForStreams(ObjectOutput outputStream, ObjectInput inputStream) throws IOException {
        initFields();

        return MessageHandler.create(outputStream, inputStream, messageDispatcher);
    }

    private void initFields() {
        Dao dao = DaoFactory.createDao();
        FileData fileData = new FileData();
        ServerData serverData = new ServerData(dao, fileData);
        CompareEngine compareEngine = new CompareEngine();
        ArticleComparison articleComparison = new ArticleComparison(compareEngine);
        SourceCodeComparison sourceCodeComparison = new SourceCodeComparison();
        ComparingAlgorithm comparingAlgorithm = new ComparingAlgorithm(articleComparison, sourceCodeComparison);

        PlagiarismDetector plagiarismDetector = new PlagiarismDetector(serverData, comparingAlgorithm);
        messageDispatcher = new MessageDispatcher(plagiarismDetector);
    }
}
