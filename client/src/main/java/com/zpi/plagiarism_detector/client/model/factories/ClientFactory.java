package com.zpi.plagiarism_detector.client.model.factories;

import com.zpi.plagiarism_detector.client.model.core.Client;
import com.zpi.plagiarism_detector.client.model.io.ClientReader;
import com.zpi.plagiarism_detector.client.model.io.ClientWriter;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClientFactory extends AbstractClientFactory {
    @Override
    public Client create(String hostName, int port) throws IOException {
        Socket socket = new Socket(hostName, port);

        ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
        ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());

        ClientReader reader = new ClientReader(inputStream);
        ClientWriter writer = new ClientWriter(outputStream);

        Client client = new Client(socket, reader, writer);
        return client;
    }
}
