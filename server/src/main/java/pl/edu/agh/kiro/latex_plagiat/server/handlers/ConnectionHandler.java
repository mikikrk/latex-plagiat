package pl.edu.agh.kiro.latex_plagiat.server.handlers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.Socket;

/**
 * Odpowiada za odbiór danych od klientów, odpowiednie przetworzenie i wysłanie odpowiedzi.
 */
public class ConnectionHandler implements Runnable {
    private static final Logger log = LoggerFactory.getLogger(ConnectionHandler.class);
    private Socket socket;
    private MessageHandler messageHandler;

    /**
     * Tworzy obiekt uchwyt który przechowuje Socket oraz mapę id i socket handlerów.
     *
     * @param socket         socket potrzebny do odbioru/wysyłania danych.
     * @param messageHandler
     */
    public ConnectionHandler(Socket socket, MessageHandler messageHandler) {
        this.socket = socket;
        this.messageHandler = messageHandler;
    }

    @Override
    public void run() {
        log.debug("entering run()");
        try {
            messageHandler.handleMessages();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            freeHandlerResources();
        }
        log.debug("leaving run()");
    }


    private void freeHandlerResources() {
        try {
            synchronized (this) {
                if (messageHandler != null) {
                    messageHandler.freeResources();
                }
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}