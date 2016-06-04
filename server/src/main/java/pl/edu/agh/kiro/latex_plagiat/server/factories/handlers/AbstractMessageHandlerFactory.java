package pl.edu.agh.kiro.latex_plagiat.server.factories.handlers;

import pl.edu.agh.kiro.latex_plagiat.commons.factory.AbstractFactory;
import pl.edu.agh.kiro.latex_plagiat.server.handlers.MessageHandler;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.net.Socket;

public abstract class AbstractMessageHandlerFactory extends AbstractFactory<MessageHandler> {
    public abstract MessageHandler createForSocket(Socket socket) throws IOException;

    public abstract MessageHandler createForStreams(ObjectOutput outputStream, ObjectInput inputStream) throws IOException;
}
