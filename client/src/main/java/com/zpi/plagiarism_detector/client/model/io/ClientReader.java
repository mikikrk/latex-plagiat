package com.zpi.plagiarism_detector.client.model.io;

import com.zpi.plagiarism_detector.commons.protocol.Message;
import com.zpi.plagiarism_detector.commons.util.Observable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.ObjectInput;

public class ClientReader extends Observable implements Runnable {
    private static final Logger log = LoggerFactory.getLogger(ClientReader.class);
    private ObjectInput in;
    private Message message;

    public ClientReader(ObjectInput in) {
        this.in = in;
    }

    @Override
    public void run() {
        log.debug("entering run()");
        try {
            receiveMessages();
        } catch (IOException e) {
            log.debug("ClientReader stopped receiving messages");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            close();
            log.debug("leaving run()");
        }
    }

    /**
     * Odbiera wiadomości przychodzące z serwera
     *
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public void receiveMessages() throws IOException, ClassNotFoundException {
        log.debug("entering receiveMessages()");
        while (tryReadMessage()) {
            processMessage();
        }
        log.debug("leaving receiveMessages()");
    }

    /**
     * próbuje odczytać wiadomość z strumienia
     *
     * @return wartość logiczna określająca czy udało się odebrać wiadomość
     * @throws ClassNotFoundException
     * @throws IOException
     */
    public boolean tryReadMessage() throws ClassNotFoundException, IOException {
        log.debug("entering tryReadMessage()");
        message = (Message) in.readObject();
        log.debug("leaving tryReadMessage()");
        return message != null;
    }

    private void processMessage() {
        log.debug("entering processMessage()");
        notifyObservers(message);
        log.debug("response received:\n{}", message);
        log.debug("leaving processMessage()");
    }


    /**
     * Zamkyka strumień danych
     */
    public void close() {
        log.debug("entering close()");
        try {
            in.close();
        } catch (IOException e) {
            log.debug("closing with an exception clientReader thread: {}", e);
        } finally {
            log.debug("leaving close()");
        }
    }
}