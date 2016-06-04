package pl.edu.agh.kiro.latex_plagiat.client.model.factories;

import pl.edu.agh.kiro.latex_plagiat.client.model.core.Client;
import pl.edu.agh.kiro.latex_plagiat.client.model.io.ClientWriter;

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

        pl.edu.agh.kiro.latex_plagiat.client.model.io.ClientReader reader = new pl.edu.agh.kiro.latex_plagiat.client.model.io.ClientReader(inputStream);
        ClientWriter writer = new ClientWriter(outputStream);

        Client client = new Client(socket, reader, writer);
        return client;
    }
}
