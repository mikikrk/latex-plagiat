package pl.edu.agh.kiro.latex_plagiat.server.factories.handlers;

import pl.edu.agh.kiro.latex_plagiat.server.detector.ComparingAlgorithm;
import pl.edu.agh.kiro.latex_plagiat.server.articles.ArticleComparison;
import pl.edu.agh.kiro.latex_plagiat.server.articles.CompareEngine;
import pl.edu.agh.kiro.latex_plagiat.server.data.FileData;
import pl.edu.agh.kiro.latex_plagiat.server.data.ServerData;
import pl.edu.agh.kiro.latex_plagiat.server.database.Dao;
import pl.edu.agh.kiro.latex_plagiat.server.database.DaoFactory;
import pl.edu.agh.kiro.latex_plagiat.server.detector.PlagiarismDetector;
import pl.edu.agh.kiro.latex_plagiat.server.handlers.MessageDispatcher;
import pl.edu.agh.kiro.latex_plagiat.server.handlers.MessageHandler;

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
        ComparingAlgorithm comparingAlgorithm = new ComparingAlgorithm(articleComparison);

        PlagiarismDetector plagiarismDetector = new PlagiarismDetector(serverData, comparingAlgorithm);
        messageDispatcher = new MessageDispatcher(plagiarismDetector);
    }
}
