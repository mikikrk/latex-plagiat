package pl.edu.agh.kiro.latex_plagiat.server.handlers;

import pl.edu.agh.kiro.latex_plagiat.server.exceptions.AbortConnectionException;
import pl.edu.agh.kiro.latex_plagiat.commons.protocol.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

public class MessageHandler {
    private static final Logger log = LoggerFactory.getLogger(MessageHandler.class);
    private final MessageDispatcher messageDispatcher;
    private ObjectInput in;
    private ObjectOutput out;
    private Message message;

    private MessageHandler(ObjectOutput out, ObjectInput in, MessageDispatcher messageDispatcher) {
        this.out = out;
        this.in = in;
        this.messageDispatcher = messageDispatcher;
    }

    /**
     * Tworzy MessageHandler dla strumieni wyjścia/wejścia
     *
     * @param out
     * @param in
     * @return utworzony obiekt
     * @throws IOException
     */
    public static MessageHandler create(ObjectOutput out, ObjectInput in, MessageDispatcher messageDispatcher) {
        return new MessageHandler(out, in, messageDispatcher);
    }

    /**
     * Obsługuje przychodzące wiadomości
     *
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public void handleMessages() throws IOException, ClassNotFoundException {
        log.debug("entering handleMessages()");
        Message response = null;
        try {
            while (tryReadMessage()) {
                log.debug("dispatchingMessage: {}", message);
                response = messageDispatcher.dispatchMessage(message);
                if (response != null) {
                    sendMessage(response);
                }
            }
        } catch (AbortConnectionException e) {
            return;
        }
        log.debug("leaving handleMessages()");
    }

    public boolean tryReadMessage() {
        try {
            message = (Message) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            message = null;
        } finally {
            return message != null;
        }
    }

    /**
     * Wysyła odpowiedź
     *
     * @param message wysyłana wiadomość
     * @throws IOException
     */
    public void sendMessage(Message message) throws IOException {
        log.debug("entering sendMessage()");
        out.writeObject(message);
        log.debug("leaving sendMessage()");
    }

    /**
     * Zwalnia zasoby strumienia danych
     *
     * @throws IOException
     */
    public void freeResources() throws IOException {
        in.close();
        out.close();
    }
}
