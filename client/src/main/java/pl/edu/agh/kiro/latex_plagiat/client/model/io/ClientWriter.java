package pl.edu.agh.kiro.latex_plagiat.client.model.io;

import pl.edu.agh.kiro.latex_plagiat.commons.protocol.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.ObjectOutput;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingQueue;

public class ClientWriter implements Runnable {
    private static final Logger log = LoggerFactory.getLogger(ClientWriter.class);
    BlockingQueue<Message> sendQueue = new LinkedBlockingQueue<>();
    private ObjectOutput out;
    private boolean stop = false;
    private CountDownLatch countDownLatch = new CountDownLatch(1);

    public ClientWriter(ObjectOutput out) {
        this.out = out;
    }

    @Override
    public void run() {
        log.debug("entering run()");
        try {
            sendQueuedMessages();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        } finally {
            releaseResources();
            log.debug("leaving run()");
        }
    }

    private void sendQueuedMessages() throws InterruptedException, IOException {
        log.debug("entering sendQueuedMessages()");
        while (isInRunningState()) {
            sendMessage();
        }
        log.debug("leaving sendQueuedMessages()");
    }

    private synchronized boolean isInRunningState() {
        return !stop;
    }

    private synchronized void sendMessage() throws InterruptedException, IOException {
        log.debug("entering sendMessage()");
        Message message = sendQueue.take();
        log.debug("processing next message from sendQueue: [{}]", message);
        if (message == Message.POISON_PILL) {
            stopWriter();
            return;
        }
        out.writeObject(message);
        log.debug("leaving sendMessage()");
    }

    private synchronized void stopWriter() {
        stop = true;
    }

    private synchronized void releaseResources() {
        try {
            countDownLatch.countDown();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Dodaje wysyłaną wiadomość do kolejki
     *
     * @param message
     * @throws InterruptedException
     */
    public void writeMessage(Message message) throws InterruptedException {
        sendQueue.put(message);
    }

    /**
     * Powoduje zamknięcie obiektu wysyłającego wiadomości
     *
     * @throws InterruptedException
     */
    public void close() throws InterruptedException {
        writeMessage(Message.POISON_PILL);
    }

    public void await() throws BrokenBarrierException, InterruptedException {
        countDownLatch.await();
    }
}
