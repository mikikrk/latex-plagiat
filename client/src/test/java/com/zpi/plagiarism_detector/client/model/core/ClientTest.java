package com.zpi.plagiarism_detector.client.model.core;

import com.zpi.plagiarism_detector.client.model.io.ClientReader;
import com.zpi.plagiarism_detector.client.model.io.ClientWriter;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.net.Socket;
import java.util.Observer;
import java.util.concurrent.CyclicBarrier;

import static org.mockito.Mockito.*;

public class ClientTest {
    private Client client;
    private Socket socket;
    private ClientReader reader;
    private ClientWriter writer;
    private CyclicBarrier cyclicBarrier;

    @BeforeMethod
    public void init() {
        socket = mock(Socket.class);
        reader = mock(ClientReader.class);
        writer = mock(ClientWriter.class);
        client = new Client(socket, reader, writer);
    }

    @Test
    public void update_UpdateRegisteredObserverAndChangeStatusToUnchangedTest() {
        // given
        java.util.Observable observable = mock(java.util.Observable.class);
        Observer observer = mock(Observer.class);
        Object arg = new Object();
        client.addObserver(observer);

        // when
        client.update(observable, arg);

        // then
        verify(observer, times(1)).update(client, arg);
    }
}
