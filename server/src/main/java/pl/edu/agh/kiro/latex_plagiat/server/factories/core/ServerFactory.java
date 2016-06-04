package pl.edu.agh.kiro.latex_plagiat.server.factories.core;

import pl.edu.agh.kiro.latex_plagiat.server.core.Server;
import pl.edu.agh.kiro.latex_plagiat.server.factories.handlers.MessageHandlerFactory;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerFactory extends AbstractServerFactory {

    public Server create(int threadPoolSize, int portNumber) throws IOException {
        ServerSocket serverSocket = new ServerSocket(portNumber);
        ExecutorService pool = Executors.newFixedThreadPool(threadPoolSize);

        return new Server(serverSocket, pool, new MessageHandlerFactory());
    }

}
