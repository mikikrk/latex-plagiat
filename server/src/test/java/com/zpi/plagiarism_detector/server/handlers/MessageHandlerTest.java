package com.zpi.plagiarism_detector.server.handlers;

import com.zpi.plagiarism_detector.commons.protocol.Message;
import com.zpi.plagiarism_detector.commons.protocol.ProtocolCode;
import com.zpi.plagiarism_detector.commons.util.TestUtils;
import com.zpi.plagiarism_detector.server.factories.handlers.AbstractMessageHandlerFactory;
import com.zpi.plagiarism_detector.server.factories.handlers.MessageHandlerFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.lang.reflect.InvocationTargetException;

import static org.mockito.Mockito.*;

public class MessageHandlerTest {
    private MessageHandler messageHandler;
    private AbstractMessageHandlerFactory messageHandlerFactory = new MessageHandlerFactory();
    private ObjectInput in;
    private ObjectOutput out;
    private Message message;
    private MessageDispatcher messageDispatcher;

    @BeforeMethod
    public final void initMocks() throws IOException, ClassNotFoundException {
        in = mock(ObjectInput.class);
        out = mock(ObjectOutput.class);
        messageDispatcher = mock(MessageDispatcher.class);
        messageHandler = MessageHandler.create(out, in, messageDispatcher);
    }

    @Test(enabled = false) /* Problem z niedziałającym mock'iem */
    public final void tryReadMessageSuccessTest() throws IOException, ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        // given
        message = new Message(ProtocolCode.TEST, null);
        doReturn(message).when(in).readObject();

        // when
        boolean result = messageHandler.tryReadMessage();

        // then
        Assert.assertTrue(result);
    }

    @Test
    public final void tryReadMessageIOExceptionTest() throws IOException, ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        // given
        doThrow(new IOException()).when(in).readObject();

        // when
        boolean result = (boolean) TestUtils.callDeclaredMethod(messageHandler, "tryReadMessage");

        // then
        Assert.assertFalse(result);
    }

    @Test
    public final void tryReadMessageClassNotFoundTest() throws IOException, ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        // given
        doThrow(new ClassNotFoundException()).when(in).readObject();

        // when
        boolean result = (boolean) TestUtils.callDeclaredMethod(messageHandler, "tryReadMessage");

        // then
        Assert.assertFalse(result);
    }
}
