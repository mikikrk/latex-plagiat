package com.zpi.plagiarism_detector.commons.protocol;

import org.testng.Assert;
import org.testng.annotations.Test;

public class MessageTest {
    private Message message;

    @Test
    public void toString_NonNullParams() {
        // given
        ProtocolCode code = ProtocolCode.CHECK_FOR_PLAGIARISM;
        String sentObject = "a";
        message = new Message(code, sentObject);

        // when
        String result = message.toString();

        //then
        Assert.assertEquals(result, "Msg code: CHECK_FOR_PLAGIARISM\tvalue: a");
    }

    @Test
    public void toString_NullCodeParam() {
        // given
        ProtocolCode code = null;
        String sentObject = "a";
        message = new Message(code, sentObject);

        // when
        String result = message.toString();

        //then
        Assert.assertEquals(result, "Msg code: null\tvalue: a");
    }

    @Test
    public void toString_NullSentObjectParam() {
        // given
        ProtocolCode code = ProtocolCode.CHECK_FOR_PLAGIARISM;
        String sentObject = null;
        message = new Message(code, sentObject);

        // when
        String result = message.toString();

        //then
        Assert.assertEquals(result, "Msg code: CHECK_FOR_PLAGIARISM\tvalue: null");
    }
}
