package com.zpi.plagiarism_detector.server.factories.handlers;

import com.zpi.plagiarism_detector.commons.factory.AbstractFactory;
import com.zpi.plagiarism_detector.server.handlers.MessageHandler;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.net.Socket;

public abstract class AbstractMessageHandlerFactory extends AbstractFactory<MessageHandler> {
    public abstract MessageHandler createForSocket(Socket socket) throws IOException;

    public abstract MessageHandler createForStreams(ObjectOutput outputStream, ObjectInput inputStream) throws IOException;
}
