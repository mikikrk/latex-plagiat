package com.zpi.plagiarism_detector.server.core;

import com.zpi.plagiarism_detector.server.factories.handlers.MessageHandlerFactory;
import com.zpi.plagiarism_detector.server.handlers.ConnectionHandler;
import org.mockito.Mockito;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;

import static org.mockito.Mockito.*;

public class ServerTest {
    private Server server;

    private ServerSocket serverSocket;
    private ExecutorService pool;
    private Socket clientSocket;

    @BeforeMethod
    public void createMocks() {
        clientSocket = Mockito.mock(Socket.class);
        serverSocket = Mockito.mock(ServerSocket.class);
        pool = Mockito.mock(ExecutorService.class);
        server = new Server(serverSocket, pool, new MessageHandlerFactory());
    }

    @Test(timeOut = 1000)
    public void handleConnectionsInterruptTest() throws IOException, InterruptedException {
        // given
        doReturn(clientSocket).when(serverSocket).accept();
        doNothing().when(pool).execute(Mockito.any(ConnectionHandler.class));

        // when
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                server.handleConnections();
            }
        });

        t.interrupt();
        t.join();
    }


    @Test
    public void handleConnectionsException() throws IOException {
        // given
        doThrow(new IOException()).when(serverSocket).accept();
        doNothing().when(pool).execute(Mockito.any(ConnectionHandler.class));

        // when
        server.handleConnections();

        // then
        verify(serverSocket, times(1)).accept();
    }

}
