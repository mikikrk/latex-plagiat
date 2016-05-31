package com.zpi.plagiarism_detector.client.model;

import com.zpi.plagiarism_detector.client.model.core.Client;
import com.zpi.plagiarism_detector.client.model.exceptions.CannotConnectToTheServerException;
import com.zpi.plagiarism_detector.client.model.factories.AbstractClientFactory;
import com.zpi.plagiarism_detector.commons.protocol.ApplicationProperties;
import com.zpi.plagiarism_detector.commons.protocol.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.BrokenBarrierException;

public class ClientModel extends com.zpi.plagiarism_detector.commons.util.Observable implements Model, Observer {
    private Logger log = LoggerFactory.getLogger(ClientModel.class);
    private AbstractClientFactory clientFactory;
    private Client client;

    public ClientModel(AbstractClientFactory clientFactory) {
        this.clientFactory = clientFactory;
    }

    public void openConnection() throws CannotConnectToTheServerException {
        try {
            client = clientFactory.create(ApplicationProperties.HOSTNAME, ApplicationProperties.PORT);
            client.addObserver(this);
            client.openConnection();
        } catch (IOException e) {
            throw new CannotConnectToTheServerException(e);
        }
    }
    public void openConnection(String hostname, int port) throws CannotConnectToTheServerException {
        try {
            client = clientFactory.create(hostname, port);
            client.addObserver(this);
            client.openConnection();
        } catch (IOException e) {
            throw new CannotConnectToTheServerException(e);
        }
    }

    public void sendMessage(Message message) {
        client.sendSyncMessage(message);
    }

    public void closeConnection() {
        try {
            client.closeConnection();
        } catch (InterruptedException | IOException | BrokenBarrierException e) {
            e.printStackTrace();
        } finally {
            log.debug("Connection closed");
        }
    }

    @Override
    public void update(Observable o, Object arg) {
        notifyObservers(arg);
    }
}
