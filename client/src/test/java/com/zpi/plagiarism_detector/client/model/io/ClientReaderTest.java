package com.zpi.plagiarism_detector.client.model.io;

import com.zpi.plagiarism_detector.commons.protocol.Message;
import com.zpi.plagiarism_detector.commons.protocol.ProtocolCode;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.IOException;
import java.io.ObjectInput;

import static org.mockito.Mockito.*;

public class ClientReaderTest {
    private ClientReader clientReader;
    private ObjectInput in;

    @BeforeMethod
    public void init() {
        in = mock(ObjectInput.class);
        clientReader = new ClientReader(in);
    }

    @Test
    public void receiveMessages_OneMessageReceivedSuccessTest() throws IOException, ClassNotFoundException {
        // given
        Message message = new Message(ProtocolCode.TEST);
        when(in.readObject()).thenReturn(message, null);

        // when
        clientReader.receiveMessages();

        // then
        verify(in, times(2)).readObject();
    }

    @Test
    public void receiveMessages_ZeroMessagesReceivedSuccessTest() throws IOException, ClassNotFoundException {
        // given
        when(in.readObject()).thenReturn(null);

        // when
        clientReader.receiveMessages();

        // then
        verify(in).readObject();
    }

    @Test(expectedExceptions = IOException.class)
    public void receiveMessages_IOExceptionThrownTest() throws IOException, ClassNotFoundException {
        // given
        doThrow(new IOException()).when(in).readObject();

        // when
        clientReader.receiveMessages();
    }

    @Test(expectedExceptions = ClassNotFoundException.class)
    public void receiveMessages_ClassNotFoundExceptionThrownTest() throws ClassNotFoundException, IOException {
        // given
        doThrow(new ClassNotFoundException()).when(in).readObject();

        // when
        clientReader.receiveMessages();
    }

    @Test
    public void closeSuccessTest() throws IOException {
        // given
        doNothing().when(in).close();

        // when
        clientReader.close();

        // then
        return;
    }

    @Test
    public void close_IOExceptionSuccessTest() throws IOException {
        // given
        doThrow(new IOException()).when(in).close();

        // when
        clientReader.close();

        // then
        return;
    }
}
