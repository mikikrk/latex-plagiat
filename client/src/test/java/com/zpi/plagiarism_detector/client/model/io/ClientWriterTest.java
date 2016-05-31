package com.zpi.plagiarism_detector.client.model.io;

import com.zpi.plagiarism_detector.commons.protocol.Message;
import com.zpi.plagiarism_detector.commons.protocol.ProtocolCode;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.IOException;
import java.io.ObjectOutput;
import java.util.concurrent.BlockingQueue;

import static org.mockito.Mockito.*;

public class ClientWriterTest {
    private ClientWriter clientWriter;
    private ObjectOutput out;
    private BlockingQueue<Message> sendQueue;

    @BeforeMethod
    public void init() {
        sendQueue = mock(BlockingQueue.class);
        out = mock(ObjectOutput.class);
        clientWriter = new ClientWriter(out);
        clientWriter.sendQueue = sendQueue;
    }

    @Test
    public void run_OneMessageInQueueSuccessTest() throws IOException, InterruptedException {
        // given
        final Message message = new Message(ProtocolCode.TEST);
        when(sendQueue.take()).thenReturn(message, Message.POISON_PILL);

        // when
        Thread thread = new Thread(clientWriter);
        thread.start();
        thread.join();

        // then
        verify(sendQueue, times(2)).take();
        verify(out, times(1)).writeObject(message);
        verify(out, times(1)).close();
    }

    @Test
    public void run_NoMessagesInQueueSuccessTest() throws InterruptedException, IOException {
        // given
        when(sendQueue.take()).thenReturn(Message.POISON_PILL);

        // when
        Thread thread = new Thread(clientWriter);
        thread.start();
        thread.join();

        // then
        verify(sendQueue, times(1)).take();
        verify(out, times(1)).close();
    }

    @Test(expectedExceptions = NullPointerException.class)
    public void writeMessage_nullMessageThrowsNullPointerException() throws InterruptedException {
        // given
        Message message = null;
        doThrow(NullPointerException.class).when(sendQueue).put(any(Message.class));

        // when
        clientWriter.writeMessage(message);
    }

    @Test
    public void writeMessage_succesWithNonNullMessage() throws InterruptedException {
        // given
        Message message = new Message(ProtocolCode.TEST);

        // when
        clientWriter.writeMessage(message);

        //
        return;
    }
}
