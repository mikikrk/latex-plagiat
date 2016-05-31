package com.zpi.plagiarism_detector.client.model.core;

import com.zpi.plagiarism_detector.client.model.io.ClientReader;
import com.zpi.plagiarism_detector.client.model.io.ClientWriter;
import com.zpi.plagiarism_detector.commons.protocol.Message;
import com.zpi.plagiarism_detector.commons.util.Observable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.Socket;
import java.util.Observer;
import java.util.concurrent.*;

public class Client extends Observable implements Observer {
    private static final Logger log = LoggerFactory.getLogger(Client.class);
    private static final long readerWriterShutDownTimeout = 2;
    private Socket socket;
    private ClientReader reader;
    private ClientWriter writer;
    private ExecutorService executorService = Executors.newFixedThreadPool(2);
    private CyclicBarrier cyclicBarrier = new CyclicBarrier(2);
    private boolean waitForResponse;

    public Client(Socket socket, ClientReader reader, ClientWriter writer) {
        this.socket = socket;
        this.reader = reader;
        this.writer = writer;
    }

    /**
     * Otwiera połączenie pomiędzy klientem a serwerem.
     */
    public void openConnection() {
        log.debug("entering openConnection()");
        reader.addObserver(this);
        executorService.execute(reader);
        executorService.execute(writer);
        log.debug("leaving openConnection()");
    }

    /**
     * Zamyka połączenie z serwerem.
     *
     * @throws InterruptedException
     */
    public synchronized void closeConnection() throws InterruptedException, IOException, BrokenBarrierException {
        log.debug("entering closeConnection()");
        if (waitForResponse) {
            waitForResponse();
        }
        closeWorkingThreads();
        closeTcpConnection();
        log.debug("leaving closeConnection()");
    }

    private void waitForResponse() throws InterruptedException {
        log.debug("entering waitForResponse()");
        try {
//            cyclicBarrier.await(10, TimeUnit.SECONDS);
            cyclicBarrier.await();
            cyclicBarrier.reset();
        } catch (BrokenBarrierException e) {
            e.printStackTrace();
        }
        log.debug("leaving waitForResponse()");
    }

    private void closeWorkingThreads() throws InterruptedException, BrokenBarrierException {
        log.debug("entering closeWorkingThreads()");
        executorService.shutdown();
        writer.close();
        writer.await();
        reader.close();
        executorService.awaitTermination(readerWriterShutDownTimeout, TimeUnit.SECONDS);
        log.debug("leaving closeWorkingThreads()");
    }

    private void closeTcpConnection() throws IOException {
        log.debug("entering closeConnection()");
        socket.close();
        log.debug("leaving closeConnection()");
    }


    /**
     * Wysyła wiadomość do serwera
     *
     * @param msg message which is sent to the server
     */
    public synchronized void sendSyncMessage(Message msg) {
        log.debug("entering sendSyncMessage()");
        try {
            waitForResponse = true;
            writer.writeMessage(msg);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        log.debug("leaving sendSyncMessage()");
    }

    @Override
    public void update(java.util.Observable o, Object arg) {
        if (waitForResponse) {
            try {
                cyclicBarrier.await();
            } catch (InterruptedException | BrokenBarrierException e) {
                e.printStackTrace();
            }
        }
        waitForResponse = false;
        notifyObservers(arg);
    }
}